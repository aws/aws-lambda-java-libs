/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.LambdaRuntimeInternal;

import com.amazonaws.services.lambda.runtime.api.client.api.LambdaClientContext;
import com.amazonaws.services.lambda.runtime.api.client.api.LambdaCognitoIdentity;
import com.amazonaws.services.lambda.runtime.api.client.api.LambdaContext;
import com.amazonaws.services.lambda.runtime.api.client.util.UnsafeUtil;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.events.LambdaEventSerializers;
import com.amazonaws.services.lambda.runtime.serialization.factories.GsonFactory;
import com.amazonaws.services.lambda.runtime.serialization.factories.JacksonFactory;
import com.amazonaws.services.lambda.runtime.serialization.util.Functions;
import com.amazonaws.services.lambda.runtime.serialization.util.ReflectUtil;
import com.amazonaws.services.lambda.runtime.api.client.LambdaRequestHandler.UserFaultHandler;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.InvocationRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

import static com.amazonaws.services.lambda.runtime.api.client.UserFault.filterStackTrace;
import static com.amazonaws.services.lambda.runtime.api.client.UserFault.makeUserFault;
import static com.amazonaws.services.lambda.runtime.api.client.UserFault.trace;

public final class EventHandlerLoader {
    private static final byte[] _JsonNull = new byte[]{'n', 'u', 'l', 'l'};

    private enum Platform {
        ANDROID,
        IOS,
        UNKNOWN
    }

    private static final EnumMap<Platform, Map<Type, PojoSerializer<Object>>> typeCache = new EnumMap<>(Platform.class);

    private EventHandlerLoader() { }

    /**
     * returns the appropriate serializer for the class based on platform and whether the class is a supported event
     * @param platform enum platform
     * @param type Type of object used
     * @return PojoSerializer
     * @see Platform for which platforms are used
     * @see LambdaEventSerializers for how mixins and modules are added to the serializer
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static PojoSerializer<Object> getSerializer(Platform platform, Type type) {
        // if serializing a Class that is a Lambda supported event, use Jackson with customizations
        if (type instanceof Class) {
            Class<Object> clazz = ((Class)type);
            if (LambdaEventSerializers.isLambdaSupportedEvent(clazz.getName())) {
                return LambdaEventSerializers.serializerFor(clazz, AWSLambda.customerClassLoader);
            }
        }
        // else platform dependent (Android uses GSON but all other platforms use Jackson)
        switch (platform) {
            case ANDROID:
                return GsonFactory.getInstance().getSerializer(type);
            default:
                return JacksonFactory.getInstance().getSerializer(type);
        }
    }

    private static PojoSerializer<Object> getSerializerCached(Platform platform, Type type) {
        Map<Type, PojoSerializer<Object>> cache = typeCache.get(platform);
        if (cache == null) {
            cache = new HashMap<>();
            typeCache.put(platform, cache);
        }

        PojoSerializer<Object> serializer = cache.get(type);
        if (serializer == null) {
            serializer = getSerializer(platform, type);
            cache.put(type, serializer);
        }

        return serializer;
    }

    private static volatile PojoSerializer<LambdaClientContext> contextSerializer;
    private static volatile PojoSerializer<LambdaCognitoIdentity> cognitoSerializer;

    private static PojoSerializer<LambdaClientContext> getContextSerializer() {
        if (contextSerializer == null) {
            contextSerializer = GsonFactory.getInstance().getSerializer(LambdaClientContext.class);
        }
        return contextSerializer;
    }

    private static PojoSerializer<LambdaCognitoIdentity> getCognitoSerializer() {
        if (cognitoSerializer == null) {
            cognitoSerializer = GsonFactory.getInstance().getSerializer(LambdaCognitoIdentity.class);
        }
        return cognitoSerializer;
    }


    private static Platform getPlatform(Context context) {
        ClientContext cc = context.getClientContext();
        if (cc == null) {
            return Platform.UNKNOWN;
        }

        Map<String, String> env = cc.getEnvironment();
        if (env == null) {
            return Platform.UNKNOWN;
        }

        String platform = env.get("platform");
        if (platform == null) {
            return Platform.UNKNOWN;
        }

        if ("Android".equalsIgnoreCase(platform)) {
            return Platform.ANDROID;
        } else if ("iPhoneOS".equalsIgnoreCase(platform)) {
            return Platform.IOS;
        } else {
            return Platform.UNKNOWN;
        }
    }

    private static boolean isVoid(Type type) {
        return Void.TYPE.equals(type) || (type instanceof Class) && Void.class.isAssignableFrom((Class<?>)type);
    }

    /**
     * Wraps a RequestHandler as a lower level stream handler using supplied types.
     * Optional types mean that the input and/or output should be ignored respectiveley
     */
    @SuppressWarnings("rawtypes")
    private static final class PojoHandlerAsStreamHandler implements RequestStreamHandler {

        public RequestHandler innerHandler;
        public final Optional<Type> inputType;
        public final Optional<Type> outputType;

        public PojoHandlerAsStreamHandler(
                RequestHandler innerHandler,
                Optional<Type> inputType,
                Optional<Type> outputType
        ) {
            this.innerHandler = innerHandler;
            this.inputType = inputType;
            this.outputType = outputType;


            if (inputType.isPresent()) {
                getSerializerCached(Platform.UNKNOWN, inputType.get());
            }

            if (outputType.isPresent()) {
                getSerializerCached(Platform.UNKNOWN, outputType.get());
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
                throws IOException {
            final Object input;
            final Platform platform = getPlatform(context);
            try {
                if (inputType.isPresent()) {
                    input = getSerializerCached(platform, inputType.get()).fromJson(inputStream);
                } else {
                    input = null;
                }
            } catch (Throwable t) {
                throw new RuntimeException("An error occurred during JSON parsing", filterStackTrace(t));
            }

            final Object output;
            try {
                output = innerHandler.handleRequest(input, context);
            } catch (Throwable t) {
                throw UnsafeUtil.throwException(filterStackTrace(t));
            }

            try {
                if (outputType.isPresent()) {
                    PojoSerializer<Object> serializer = getSerializerCached(platform, outputType.get());
                    serializer.toJson(output, outputStream);
                } else {
                    outputStream.write(_JsonNull);
                }
            } catch (Throwable t) {
                throw new RuntimeException("An error occurred during JSON serialization of response", t);
            }
        }
    }

    /**
     * Wraps a java.lang.reflect.Method as a POJO RequestHandler
     */
    private static final class PojoMethodRequestHandler implements RequestHandler<Object, Object> {
        public final Method m;
        public final Type pType;
        public final Object instance;
        public final boolean needsContext;
        public final int argSize;

        public PojoMethodRequestHandler(Method m, Type pType, Type rType, Object instance, boolean needsContext) {
            this.m = m;
            this.pType = pType;
            this.instance = instance;
            this.needsContext = needsContext;
            this.argSize = (needsContext ? 1 : 0) + (pType != null ? 1 : 0);
        }

        public static PojoMethodRequestHandler fromMethod(
                Class<?> clazz,
                Method m,
                Type pType,
                Type rType,
                boolean needsContext
        ) throws Exception {
            final Object instance;
            if (Modifier.isStatic(m.getModifiers())) {
                instance = null;
            } else {
                instance = newInstance(getConstructor(clazz));
            }

            return new PojoMethodRequestHandler(m, pType, rType, instance, needsContext);
        }

        public static LambdaRequestHandler makeRequestHandler(
                Class<?> clazz,
                Method m,
                Type pType,
                Type rType,
                boolean needsContext
        ) {
            try {
                return wrapPojoHandler(fromMethod(clazz, m, pType, rType, needsContext), pType, rType);
            } catch (UserFault f) {
                return new UserFaultHandler(f);
            } catch (Throwable t) {
                return new UserFaultHandler(makeUserFault(t));
            }
        }

        @Override
        public Object handleRequest(Object input, Context context) {
            final Object[] args = new Object[argSize];
            int idx = 0;

            if (pType != null) {
                args[idx++] = input;
            }

            if (this.needsContext) {
                args[idx++] = context;
            }

            try {
                return m.invoke(this.instance, args);
            } catch (InvocationTargetException e) {
                if (e.getCause() != null) {
                    throw UnsafeUtil.throwException(filterStackTrace(e.getCause()));
                } else {
                    throw UnsafeUtil.throwException(filterStackTrace(e));
                }
            } catch (Throwable t) {
                throw UnsafeUtil.throwException(filterStackTrace(t));
            }
        }
    }

    /**
     * Wraps a java.lang.reflect.Method object as a RequestStreamHandler
     */
    private static final class StreamMethodRequestHandler implements RequestStreamHandler {
        public final Method m;
        public final Object instance;
        public final boolean needsInput;
        public final boolean needsOutput;
        public final boolean needsContext;
        public final int argSize;

        public StreamMethodRequestHandler(
                Method m,
                Object instance,
                boolean needsInput,
                boolean needsOutput,
                boolean needsContext
        ) {
            this.m = m;
            this.instance = instance;
            this.needsInput = needsInput;
            this.needsOutput = needsOutput;
            this.needsContext = needsContext;
            this.argSize = (needsInput ? 1 : 0) + (needsOutput ? 1 : 0) + (needsContext ? 1 : 0);
        }

        public static StreamMethodRequestHandler fromMethod(
                Class<?> clazz,
                Method m,
                boolean needsInput,
                boolean needsOutput,
                boolean needsContext
        ) throws Exception {
            if (!isVoid(m.getReturnType())) {
                System.err.println("Will ignore return type " + m.getReturnType() + " on byte stream handler");
            }
            final Object instance = Modifier.isStatic(m.getModifiers())
                    ? null
                    : newInstance(getConstructor(clazz));

            return new StreamMethodRequestHandler(m, instance, needsInput, needsOutput, needsContext);
        }

        public static LambdaRequestHandler makeRequestHandler(
                Class<?> clazz,
                Method m,
                boolean needsInput,
                boolean needsOutput,
                boolean needsContext
        ) {
            try {
                return wrapRequestStreamHandler(fromMethod(clazz, m, needsInput, needsOutput, needsContext));
            } catch (UserFault f) {
                return new UserFaultHandler(f);
            } catch (Throwable t) {
                return new UserFaultHandler(makeUserFault(t));
            }
        }

        @Override
        public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
                throws IOException {
            final Object[] args = new Object[argSize];
            int idx = 0;

            if (needsInput) {
                args[idx++] = inputStream;
            } else {
                inputStream.close();
            }

            if (needsOutput) {
                args[idx++] = outputStream;
            }

            if (needsContext) {
                args[idx++] = context;
            }

            try {
                m.invoke(this.instance, args);
                if (!needsOutput) {
                    outputStream.write(_JsonNull);
                }
            } catch (InvocationTargetException e) {
                if (e.getCause() != null) {
                    throw UnsafeUtil.throwException(filterStackTrace(e.getCause()));
                } else {
                    throw UnsafeUtil.throwException(filterStackTrace(e));
                }
            } catch (Throwable t) {
                throw UnsafeUtil.throwException(filterStackTrace(t));
            }
        }
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz) throws Exception {
        final Constructor<T> constructor;
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            if (clazz.getEnclosingClass() != null && !Modifier.isStatic(clazz.getModifiers())) {
                throw new Exception("Class "
                        + clazz.getName()
                        + " cannot be instantiated because it is a non-static inner class");
            } else {
                throw new Exception("Class " + clazz.getName() + " has no public zero-argument constructor", e);
            }
        }
        return constructor;
    }

    public static <T> T newInstance(Constructor<? extends T> constructor) {
        try {
            return constructor.newInstance();
        } catch (UserFault e) {
            throw e;
        } catch (InvocationTargetException e) {
            throw makeUserFault(e.getCause() == null ? e : e.getCause(), true);
        } catch (InstantiationException e) {
            throw UnsafeUtil.throwException(e.getCause() == null ? e : e.getCause());
        } catch (IllegalAccessException e) {
            throw UnsafeUtil.throwException(e);
        }
    }

    private static final class ClassContext {
        public final Class<?> clazz;
        public final Type[] actualTypeArguments;

        @SuppressWarnings({"rawtypes"})
        private TypeVariable[] typeParameters;

        public ClassContext(Class<?> clazz, Type[] actualTypeArguments) {
            this.clazz = clazz;
            this.actualTypeArguments = actualTypeArguments;
        }

        @SuppressWarnings({"rawtypes"})
        public ClassContext(Class<?> clazz, ClassContext curContext) {
            this.typeParameters = clazz.getTypeParameters();
            if (typeParameters.length == 0 || curContext.actualTypeArguments == null) {
                this.clazz = clazz;
                this.actualTypeArguments = null;
            } else {
                Type[] types = new Type[typeParameters.length];
                for (int i = 0; i < types.length; i++) {
                    types[i] = curContext.resolveTypeVariable(typeParameters[i]);
                }

                this.clazz = clazz;
                this.actualTypeArguments = types;
            }
        }

        @SuppressWarnings({"rawtypes"})
        public ClassContext(ParameterizedType type, ClassContext curContext) {
            Type[] types = type.getActualTypeArguments();
            for (int i = 0; i < types.length; i++) {
                Type t = types[i];
                if (t instanceof TypeVariable) {
                    types[i] = curContext.resolveTypeVariable((TypeVariable)t);
                }
            }

            Type t = type.getRawType();
            if (t instanceof Class) {
                this.clazz = (Class)t;
            } else if (t instanceof TypeVariable) {
                this.clazz = (Class)((TypeVariable)t).getGenericDeclaration();
            } else {
                throw new RuntimeException("Type " + t + " is of unexpected type " + t.getClass());
            }
            this.actualTypeArguments = types;
        }

        @SuppressWarnings({"rawtypes"})
        public Type resolveTypeVariable(TypeVariable t) {
            TypeVariable[] variables = getTypeParameters();
            for (int i = 0; i < variables.length; i++) {
                if (t.getName().equals(variables[i].getName())) {
                    return actualTypeArguments == null ? variables[i] : actualTypeArguments[i];
                }
            }

            return t;
        }

        @SuppressWarnings({"rawtypes"})
        private TypeVariable[] getTypeParameters() {
            if (typeParameters == null) {
                typeParameters = clazz.getTypeParameters();
            }
            return typeParameters;
        }
    }

    /**
     * perform a breadth-first search for the first parameterized type for iface
     *
     * @return null of no type found. Otherwise the type found.
     */
    public static Type[] findInterfaceParameters(Class<?> clazz, Class<?> iface) {
        LinkedList<ClassContext> clazzes = new LinkedList<>();
        clazzes.addFirst(new ClassContext(clazz, (Type[])null));
        while (!clazzes.isEmpty()) {
            final ClassContext curContext = clazzes.removeLast();
            Type[] interfaces = curContext.clazz.getGenericInterfaces();

            for (Type type : interfaces) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType candidate = (ParameterizedType)type;
                    Type rawType = candidate.getRawType();
                    if (!(rawType instanceof Class)) {
                        //should be impossible
                        System.err.println("raw type is not a class: " + rawType);
                        continue;
                    }
                    Class<?> rawClass = (Class<?>)rawType;
                    if (iface.isAssignableFrom(rawClass)) {
                        return new ClassContext(candidate, curContext).actualTypeArguments;
                    } else {
                        clazzes.addFirst(new ClassContext(candidate, curContext));
                    }
                } else if (type instanceof Class) {
                    clazzes.addFirst(new ClassContext((Class<?>)type, curContext));
                } else {
                    //should never happen?
                    System.err.println("Unexpected type class " + type.getClass().getName());
                }
            }

            final Type superClass = curContext.clazz.getGenericSuperclass();
            if (superClass instanceof ParameterizedType) {
                clazzes.addFirst(new ClassContext((ParameterizedType)superClass, curContext));
            } else if (superClass != null) {
                clazzes.addFirst(new ClassContext((Class<?>)superClass, curContext));
            }
        }
        return null;
    }


    @SuppressWarnings({"rawtypes"})
    public static LambdaRequestHandler wrapRequestHandlerClass(final Class<? extends RequestHandler> clazz) {
        Type[] ptypes = findInterfaceParameters(clazz, RequestHandler.class);
        if (ptypes == null) {
            return new UserFaultHandler(makeUserFault("Class "
                    + clazz.getName()
                    + " does not implement RequestHandler with concrete type parameters"));
        }
        if (ptypes.length != 2) {
            return new UserFaultHandler(makeUserFault(
                    "Invalid class signature for RequestHandler. Expected two generic types, got " + ptypes.length));
        }

        for (Type t : ptypes) {
            if (t instanceof TypeVariable) {
                Type[] bounds = ((TypeVariable)t).getBounds();
                boolean foundBound = false;
                if (bounds != null) {
                    for (Type bound : bounds) {
                        if (!Object.class.equals(bound)) {
                            foundBound = true;
                            break;
                        }
                    }
                }

                if (!foundBound) {
                    return new UserFaultHandler(makeUserFault("Class " + clazz.getName()
                            + " does not implement RequestHandler with concrete type parameters: parameter "
                            + t + " has no upper bound."));
                }
            }
        }

        final Type pType = ptypes[0];
        final Type rType = ptypes[1];

        final Constructor<? extends RequestHandler> constructor;
        try {
            constructor = getConstructor(clazz);
            return wrapPojoHandler(newInstance(constructor), pType, rType);
        } catch (UserFault f) {
            return new UserFaultHandler(f);
        } catch (Throwable e) {
            return new UserFaultHandler(makeUserFault(e));
        }
    }

    public static LambdaRequestHandler wrapRequestStreamHandlerClass(final Class<? extends RequestStreamHandler> clazz) {
        final Constructor<? extends RequestStreamHandler> constructor;
        try {
            constructor = getConstructor(clazz);
            return wrapRequestStreamHandler(newInstance(constructor));
        } catch (UserFault f) {
            return new UserFaultHandler(f);
        } catch (Throwable e) {
            return new UserFaultHandler(makeUserFault(e));
        }
    }

    public static LambdaRequestHandler loadStreamingRequestHandler(Class<?> clazz) {
        if (RequestStreamHandler.class.isAssignableFrom(clazz)) {
            return wrapRequestStreamHandlerClass(clazz.asSubclass(RequestStreamHandler.class));
        } else if (RequestHandler.class.isAssignableFrom(clazz)) {
            return wrapRequestHandlerClass(clazz.asSubclass(RequestHandler.class));
        } else {
            return new UserFaultHandler(makeUserFault("Class does not implement an appropriate handler interface: "
                    + clazz.getName()));
        }
    }

    public static LambdaRequestHandler loadEventHandler(HandlerInfo handlerInfo) {
        if (handlerInfo.methodName == null) {
            return loadStreamingRequestHandler(handlerInfo.clazz);
        } else {
            return loadEventPojoHandler(handlerInfo);
        }
    }

    private static Optional<LambdaRequestHandler> getOneLengthHandler(
            Class<?> clazz,
            Method m,
            Type pType,
            Type rType
    ) {
        if (InputStream.class.equals(pType)) {
            return Optional.of(StreamMethodRequestHandler.makeRequestHandler(clazz, m, true, false, false));
        } else if (OutputStream.class.equals(pType)) {
            return Optional.of(StreamMethodRequestHandler.makeRequestHandler(clazz, m, false, true, false));
        } else if (isContext(pType)) {
            return Optional.of(PojoMethodRequestHandler.makeRequestHandler(clazz, m, null, rType, true));
        } else {
            return Optional.of(PojoMethodRequestHandler.makeRequestHandler(clazz, m, pType, rType, false));
        }
    }

    private static Optional<LambdaRequestHandler> getTwoLengthHandler(
            Class<?> clazz,
            Method m,
            Type pType1,
            Type pType2,
            Type rType
    ) {
        if (OutputStream.class.equals(pType1)) {
            if (isContext(pType2)) {
                return Optional.of(StreamMethodRequestHandler.makeRequestHandler(clazz, m, false, true, true));
            } else {
                System.err.println(
                        "Ignoring two-argument overload because first argument type is OutputStream and second argument type is not Context");
                return Optional.empty();
            }
        } else if (isContext(pType1)) {
            System.err.println("Ignoring two-argument overload because first argument type is Context");
            return Optional.empty();
        } else if (InputStream.class.equals(pType1)) {
            if (OutputStream.class.equals(pType2)) {
                return Optional.of(StreamMethodRequestHandler.makeRequestHandler(clazz, m, true, true, false));
            } else if (isContext(pType2)) {
                return Optional.of(StreamMethodRequestHandler.makeRequestHandler(clazz, m, true, false, true));
            } else {
                System.err.println("Ignoring two-argument overload because second parameter type, "
                        + ReflectUtil.getRawClass(pType2).getName()
                        + ", is not OutputStream.");
                return Optional.empty();
            }
        } else if (isContext(pType2)) {
            return Optional.of(PojoMethodRequestHandler.makeRequestHandler(clazz, m, pType1, rType, true));
        } else {
            System.err.println("Ignoring two-argument overload because second parameter type is not Context");
            return Optional.empty();
        }
    }

    private static Optional<LambdaRequestHandler> getThreeLengthHandler(
            Class<?> clazz,
            Method m,
            Type pType1,
            Type pType2,
            Type pType3,
            Type rType
    ) {
        if (InputStream.class.equals(pType1) && OutputStream.class.equals(pType2) && isContext(pType3)) {
            return Optional.of(StreamMethodRequestHandler.makeRequestHandler(clazz, m, true, true, true));
        } else {
            System.err.println(
                    "Ignoring three-argument overload because argument signature is not (InputStream, OutputStream, Context");
            return Optional.empty();
        }
    }

    private static Optional<LambdaRequestHandler> getHandlerFromOverload(Class<?> clazz, Method m) {
        final Type rType = m.getGenericReturnType();
        final Type[] pTypes = m.getGenericParameterTypes();

        if (pTypes.length == 0) {
            return Optional.of(PojoMethodRequestHandler.makeRequestHandler(clazz, m, null, rType, false));
        } else if (pTypes.length == 1) {
            return getOneLengthHandler(clazz, m, pTypes[0], rType);
        } else if (pTypes.length == 2) {
            return getTwoLengthHandler(clazz, m, pTypes[0], pTypes[1], rType);
        } else if (pTypes.length == 3) {
            return getThreeLengthHandler(clazz, m, pTypes[0], pTypes[1], pTypes[2], rType);
        } else {
            System.err.println("Ignoring an overload of method "
                    + m.getName()
                    + " because it has too many parameters: Expected at most 3, got "
                    + pTypes.length);
            return Optional.empty();
        }
    }

    private static final boolean isContext(Type t) {
        return Context.class.equals(t);
    }

    /**
     * Returns true if the last type in params is a lambda context object interface (Context).
     */
    private static final boolean lastParameterIsContext(Class<?>[] params) {
        return params.length != 0 && isContext(params[params.length - 1]);
    }

    /**
     * Implement a comparator for Methods. We sort overloaded handler methods using this comparator, and then pick the
     * lowest sorted method.
     */
    private static final Comparator<Method> methodPriority = new Comparator<Method>() {
        public int compare(Method lhs, Method rhs) {

            //1. Non bridge methods are preferred over bridge methods.
            if (! lhs.isBridge() && rhs.isBridge()) {
                return -1;
            }
            else if (!rhs.isBridge() && lhs.isBridge()) {
                return 1;
            }

            //2. We prefer longer signatures to shorter signatures. Except we count a method whose last argument is
            //Context as having 1 more argument than it really does. This is a stupid thing to do, but we
            //need to keep it for back compat reasons.
            Class<?>[] lParams = lhs.getParameterTypes();
            Class<?>[] rParams = rhs.getParameterTypes();

            int lParamCompareLength = lParams.length;
            int rParamCompareLength = rParams.length;

            if (lastParameterIsContext(lParams)) {
                ++lParamCompareLength;
            }

            if (lastParameterIsContext(rParams)) {
                ++rParamCompareLength;
            }

            return -Integer.compare(lParamCompareLength, rParamCompareLength);
        }
    };

    private static LambdaRequestHandler loadEventPojoHandler(HandlerInfo handlerInfo) {
        Method[] methods;
        try {
            methods = handlerInfo.clazz.getMethods();
        } catch (NoClassDefFoundError e) {
            return new LambdaRequestHandler.UserFaultHandler(new UserFault(
                    "Error loading method " + handlerInfo.methodName + " on class " + handlerInfo.clazz.getName(),
                    e.getClass().getName(),
                    trace(e)
            ));
        }
        if (methods.length == 0) {
            final String msg = "Class "
                    + handlerInfo.getClass().getName()
                    + " has no public method named "
                    + handlerInfo.methodName;
            return new UserFaultHandler(makeUserFault(msg));
        }

        /*
         * We support the following signatures
         * Anything (InputStream, OutputStream, Context)
         * Anything (InputStream, OutputStream)
         * Anything (OutputStream, Context)
         * Anything (InputStream, Context)
         * Anything (InputStream)
         * Anything (OutputStream)
         * Anything (Context)
         * Anything (AlmostAnything, Context)
         * Anything (AlmostAnything)
         * Anything ()
         *
         * where AlmostAnything is any type except InputStream, OutputStream, Context
         * Anything represents any type (primitive, void, or Object)
         *
         * prefer methods with longer signatures, add extra weight to those ending with a Context object
         *
         */

        int slide = 0;

        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            methods[i - slide] = m;
            if (!m.getName().equals(handlerInfo.methodName)) {
                slide++;
                continue;
            }
        }

        final int end = methods.length - slide;
        Arrays.sort(methods, 0, end, methodPriority);

        for (int i = 0; i < end; i++) {
            Method m = methods[i];
            Optional<LambdaRequestHandler> result = getHandlerFromOverload(handlerInfo.clazz, m);
            if (result.isPresent()) {
                return result.get();
            } else {
                continue;
            }
        }

        return new UserFaultHandler(makeUserFault("No public method named "
                + handlerInfo.methodName
                + " with appropriate method signature found on class "
                + handlerInfo.clazz.getName()));
    }

    @SuppressWarnings({"rawtypes"})
    public static LambdaRequestHandler wrapPojoHandler(RequestHandler instance, Type pType, Type rType) {
        return wrapRequestStreamHandler(new PojoHandlerAsStreamHandler(instance, Optional.ofNullable(pType),
                isVoid(rType) ? Optional.<Type>empty() : Optional.of(rType)
        ));
    }

    public static String exceptionToString(Throwable t) {
        StringWriter writer = new StringWriter(65536);
        try (PrintWriter wrapped = new PrintWriter(writer)) {
            t.printStackTrace(wrapped);
        }
        StringBuffer buffer = writer.getBuffer();
        if (buffer.length() > 262144) {
            final String extra = " Truncated by Lambda";
            buffer.delete(262144, buffer.length());
            buffer.append(extra);
        }

        return buffer.toString();
    }

    public static LambdaRequestHandler wrapRequestStreamHandler(final RequestStreamHandler handler) {
        return new LambdaRequestHandler() {
            private final ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
            private Functions.V2<String, String> log4jContextPutMethod = null;

            private void safeAddRequestIdToLog4j(String log4jContextClassName,
                                                 InvocationRequest request, Class contextMapValueClass) {
                try {
                    Class<?> log4jContextClass = ReflectUtil.loadClass(AWSLambda.customerClassLoader, log4jContextClassName);
                    log4jContextPutMethod = ReflectUtil.loadStaticV2(log4jContextClass, "put", false, String.class, contextMapValueClass);
                    log4jContextPutMethod.call("AWSRequestId", request.getId());
                } catch (Exception e) {}
            }

            public ByteArrayOutputStream call(InvocationRequest request) throws Error, Exception {
                output.reset();

                LambdaCognitoIdentity cognitoIdentity = null;
                if(request.getCognitoIdentity() != null && !request.getCognitoIdentity().isEmpty()) {
                    cognitoIdentity = getCognitoSerializer().fromJson(request.getCognitoIdentity());
                }

                LambdaClientContext clientContext = null;
                if (request.getClientContext() != null && !request.getClientContext().isEmpty()) {
                    //Use GSON here because it handles immutable types without requiring annotations
                    clientContext = getContextSerializer().fromJson(request.getClientContext());
                }

                LambdaContext context = new LambdaContext(
                        LambdaEnvironment.MEMORY_LIMIT,
                        request.getDeadlineTimeInMs(),
                        request.getId(),
                        LambdaEnvironment.LOG_GROUP_NAME,
                        LambdaEnvironment.LOG_STREAM_NAME,
                        LambdaEnvironment.FUNCTION_NAME,
                        cognitoIdentity,
                        LambdaEnvironment.FUNCTION_VERSION,
                        request.getInvokedFunctionArn(),
                        clientContext
                );

                if (LambdaRuntimeInternal.getUseLog4jAppender()) {
                    safeAddRequestIdToLog4j("org.apache.log4j.MDC", request, Object.class);
                    safeAddRequestIdToLog4j("org.apache.logging.log4j.ThreadContext", request, String.class);
                    // if put method not assigned in either call to safeAddRequestIdtoLog4j then log4jContextPutMethod = null
                    if (log4jContextPutMethod == null) {
                        System.err.println("Customer using log4j appender but unable to load either " +
                                "org.apache.log4j.MDC or org.apache.logging.log4j.ThreadContext. " +
                                "Customer cannot see RequestId in log4j log lines.");
                    }
                }

                handler.handleRequest(request.getContentAsStream(), output, context);
                return output;
            }
        };
    }
}
