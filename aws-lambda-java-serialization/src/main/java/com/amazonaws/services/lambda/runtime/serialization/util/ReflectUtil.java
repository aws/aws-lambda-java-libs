/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;

import com.amazonaws.services.lambda.runtime.serialization.util.Functions.R0;
import com.amazonaws.services.lambda.runtime.serialization.util.Functions.R1;
import com.amazonaws.services.lambda.runtime.serialization.util.Functions.R2;
import com.amazonaws.services.lambda.runtime.serialization.util.Functions.R3;
import com.amazonaws.services.lambda.runtime.serialization.util.Functions.R4;
import com.amazonaws.services.lambda.runtime.serialization.util.Functions.R5;
import com.amazonaws.services.lambda.runtime.serialization.util.Functions.R9;
import com.amazonaws.services.lambda.runtime.serialization.util.Functions.V1;
import com.amazonaws.services.lambda.runtime.serialization.util.Functions.V2;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * Class with reflection utilities
 */
public final class ReflectUtil {

    private ReflectUtil() {
    }

    /**
     * Copy a class from one class loader to another. Was previously used in AwsJackson class to do some crazy
     * passing of classes from class loader to class loader. When that class was removed due to excessive complexity,
     * this method was retained for potential future use.
     * @param clazz class to copy
     * @param cl class loader to copy class to
     * @return Class inside new classloader
     * @throws UncheckedIOException if class cannot be copied
     * @throws ReflectException if class cannot be read after copying
     */
    @SuppressWarnings({"unchecked"})
    public static Class<?> copyClass(Class<?> clazz, ClassLoader cl) {
        // if class exists in target class loader then just load that class and return
        try {
            return cl.loadClass(clazz.getName());
        } catch (ClassNotFoundException e) {}
        // copy class to target class loader
        LambdaByteArrayOutputStream stream;
        // 1 kb
        final int chunkSize = 1024;
        final String resourceName = clazz.getName().replace('.', '/') + ".class";
        try(InputStream input = clazz.getClassLoader().getResourceAsStream(resourceName)) {
            int initial = Math.max(chunkSize, input.available());
            stream = new LambdaByteArrayOutputStream(initial);
            stream.readAll(input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        // load class from target class loader
        try {
            Functions.R5<Class<?>, ClassLoader, String, byte[], Integer, Integer> defineClassMethod =
                    ReflectUtil.loadInstanceR4(ClassLoader.class, "defineClass", true,
                            (Class<Class<?>>)(Class)Class.class, String.class, byte[].class, int.class, int.class);
            Class<?> result = defineClassMethod.call(cl, clazz.getName(), stream.getRawBuf(), 0, stream.getValidByteCount());
            V2<ClassLoader, Class<?>> resolveClass =
                    ReflectUtil.loadInstanceV1(ClassLoader.class, "resolveClass", true, Class.class);
            resolveClass.call(cl, result);
            return result;
        } catch (ClassFormatError | SecurityException e) {
            throw new ReflectException(e);
        }
    }

    public static Class<?> loadClass(ClassLoader cl, String name) {
        try {
            return Class.forName(name, true, cl);
        } catch(ClassNotFoundException | LinkageError e) {
            throw new ReflectException(e);
        }
    }

    public static class ReflectException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public ReflectException() {
            super();
            // TODO Auto-generated constructor stub
        }

        public ReflectException(String message, Throwable cause,
                boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        public ReflectException(String message, Throwable cause) {
            super(message, cause);
        }

        public ReflectException(String message) {
            super(message);
        }

        public ReflectException(Throwable cause) {
            super(cause);
        }

    }

    private static <T> T newInstance(Constructor<? extends T> constructor, Object... params) {
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ReflectException(e);
        }
    }

    
    public static Class<?> getRawClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType)type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Class<?> componentRaw = getRawClass(((GenericArrayType)type).getGenericComponentType());
            return Array.newInstance(componentRaw, 0).getClass();
        } else if (type instanceof TypeVariable) {
            throw new ReflectException("type variables not supported");
        } else {
            throw new ReflectException("unsupport type: " + type.getClass().getName());
        }
    }

    public static R1<Object, Object> makeCaster(Type type) {
        return makeCaster(getRawClass(type));
    }

    private static <T> R1<T, Object> boxCaster(final Class<? extends T> clazz) {
        return new R1<T, Object>() {
            public T call(Object o) {
                return clazz.cast(o);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> R1<T, Object> makeCaster(Class<? extends T> clazz) {
        if(long.class.equals(clazz)) {
            return (R1)boxCaster(Long.class);
        } else if (double.class.equals(clazz)) {
            return (R1)boxCaster(Double.class);
        } else if (float.class.equals(clazz)) {
            return (R1)boxCaster(Float.class);
        } else if (int.class.equals(clazz)) {
            return (R1)boxCaster(Integer.class);
        } else if (short.class.equals(clazz)) {
            return (R1)boxCaster(Short.class);
        } else if (char.class.equals(clazz)) {
            return (R1)boxCaster(Character.class);
        } else if (byte.class.equals(clazz)) {
            return (R1)boxCaster(Byte.class);
        } else if (boolean.class.equals(clazz)) {
            return (R1)boxCaster(Boolean.class);
        } else {
            return boxCaster(clazz);
        }
    }

    private static <T> T invoke(Method method, Object instance, Class<? extends T> rType, Object... params) {
        final R1<T, Object> caster = makeCaster(rType);
        try {
            Object result = method.invoke(instance, params);
            if(rType.equals(Void.TYPE)) {
                return null;
            } else {
                return caster.call(result);
            }
        } catch(InvocationTargetException | ExceptionInInitializerError | IllegalAccessException e) {
            throw new ReflectException(e);
        }
    }

    private static Method lookupMethod(Class<?> clazz, String name, Class<?>... pTypes) {
        try {
            try {
                return clazz.getDeclaredMethod(name, pTypes);
            } catch (NoSuchMethodException e) {
                return clazz.getMethod(name, pTypes);
            }
        } catch (NoSuchMethodException | SecurityException e) {
            throw new ReflectException(e);
        }
    }

    private static Method getDeclaredMethod(Class<?> clazz, String name, boolean isStatic, 
            boolean setAccessible, Class<?> rType, 
            Class<?>... pTypes) {
        final Method method = lookupMethod(clazz, name, pTypes);

        if (!rType.equals(Void.TYPE) && !rType.isAssignableFrom(method.getReturnType())) {
            throw new ReflectException("Class=" + clazz.getName() + " method="
                    + name + " type " + method.getReturnType().getName() + " not assignment-compatible with "
                    + rType.getName());
        }

        int mods = method.getModifiers();
        if (Modifier.isStatic(mods) != isStatic) {
            throw new ReflectException("Class=" + clazz.getName() + " method="
                    + name + " expected isStatic=" + isStatic);
        }

        if (setAccessible) {
            method.setAccessible(true);
        }
        return method;
    }

    private static <T> Constructor<? extends T> getDeclaredConstructor(Class<? extends T> clazz, boolean setAccessible, Class<?>... pTypes) {
        final Constructor<? extends T> constructor;
        try {
            constructor = clazz.getDeclaredConstructor(pTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new ReflectException(e);
        }

        if (setAccessible) {
            constructor.setAccessible(true);
        }
        return constructor;
    }

    /**
     * load instance method that takes no parameters and returns type R
     * @param clazz Class of instance
     * @param name name of method
     * @param setAccessible whether method is accessible (public vs private)
     * @param rType class of return type
     * @param <C> instance class type
     * @param <R> return type
     * @return function handle
     */
    public static <C, R> R1<R, C> loadInstanceR0(Class<? super C> clazz, String name,
                                                 boolean setAccessible, final Class<? extends R> rType) {
        final Method method = getDeclaredMethod(clazz, name, false, setAccessible, rType);
        return new R1<R, C>() {
            public R call(C instance) {
                return invoke(method, instance, rType);
            }
        };
    }

    /**
     * load instance method that takes 4 parameters and return type R
     * @param clazz class of instance
     * @param name name of method
     * @param setAccessible whether method is accessible (public vs private)
     * @param rType class of return type
     * @param a1Type argument 1 class
     * @param a2Type argument 2 class
     * @param a3Type argument 3 class
     * @param a4Type argument 4 class
     * @param <A1> argument 1 type
     * @param <A2> argument 2 type
     * @param <A3> argument 3 type
     * @param <A4> argument 4 type
     * @param <C> instance class type
     * @param <R> return type
     * @return function handle
     */
    public static <A1, A2, A3, A4, C, R> R5<R, C, A1, A2, A3, A4> loadInstanceR4(Class<? super C> clazz,
                                                                                 String name,
                                                                                 boolean setAccessible,
                                                                                 final Class<? extends R> rType,
                                                                                 Class<? super A1> a1Type,
                                                                                 Class<? super A2> a2Type,
                                                                                 Class<? super A3> a3Type,
                                                                                 Class<? super A4> a4Type) {
        final Method method = getDeclaredMethod(clazz, name, false, setAccessible, rType, a1Type, a2Type, a3Type, a4Type);
        return new R5<R, C, A1, A2, A3, A4>() {
            public R call(C instance, A1 a1, A2 a2, A3 a3, A4 a4) {
                return invoke(method, instance, rType, a1, a2, a3, a4);
            }
        };
    }

    /**
     * load an instance method that take 1 parameter and does not return anything
     * @param clazz class of instance
     * @param name name of method
     * @param setAccessible whether method is accessible (public vs private)
     * @param a1Type argument 1 class
     * @param <C> instance class type
     * @param <A1> argument 1 type
     * @return function handle
     */
    public static <A1, C> V2<C, A1> loadInstanceV1(final Class<? super C> clazz, String name, boolean setAccessible,
                                                   final Class<? super A1> a1Type) {
        final Method method = getDeclaredMethod(clazz, name, false, setAccessible, Void.TYPE, a1Type);
        return new V2<C, A1>() {
            public void call(C instance, A1 a1) {
                invoke(method, instance, Void.TYPE, a1);
            }
        };
    }

    /**
     * load an instance method that takes no parameters and return type R
     * @param instance instance to load method from
     * @param name method name
     * @param setAccessible whether method is accessible (public vs private)
     * @param rType class of return type
     * @param <C> instance type
     * @param <R> return type
     * @return function handle
     */
    public static <C, R> R0<R> bindInstanceR0(final C instance, String name, boolean setAccessible,
            final Class<? extends R> rType) {
        final Method method = getDeclaredMethod(instance.getClass(), name, false, setAccessible, rType);
        return new R0<R>() {
            public R call() {
                return invoke(method, instance, rType);
            }
        };
    }

    /**
     * load an instance method that takes 1 parameter and returns type R
     * @param instance instance to load method from
     * @param name method name
     * @param setAccessible whether method is accessible (public vs private)
     * @param rType class of return type
     * @param a1Type class of argument 1
     * @param <C> instance type
     * @param <A1> argument 1 type
     * @param <R> return type
     * @return function handle
     */
    public static <A1, C, R> R1<R, A1> bindInstanceR1(final C instance, String name, boolean setAccessible,
                                                      final Class<? extends R> rType, Class<? super A1> a1Type) {
        final Method method = getDeclaredMethod(instance.getClass(), name, false, setAccessible, rType, a1Type);
        return new R1<R, A1>() {
            public R call(A1 a1) {
                return invoke(method, instance, rType, a1);
            }
        };
    }

    /**
     * * load an instance method that takes 1 parameter and returns nothing
     * @param instance instance to load method from
     * @param name method name
     * @param setAccessible whether method is accessible (public vs private)
     * @param a1Type class of argument 1
     * @param <C> instance type
     * @param <A1> argument 1 type
     * @return function handle
     */
    public static <A1, C> V1<A1> bindInstanceV1(final C instance, String name, boolean setAccessible,
    final Class<? super A1> a1Type) {
        final Method method = getDeclaredMethod(instance.getClass(), name, false, setAccessible, Void.TYPE, a1Type);
        return new V1<A1>() {
            public void call(A1 a1) {
                invoke(method, instance, Void.TYPE, a1);
            }
        };
    }

    /**
     * load an instance method that takes 2 parameter and returns nothing
     * @param instance instance to load method from
     * @param name method name
     * @param setAccessible whether method is accessible (public vs private)
     * @param a1Type class of argument 1
     * @param a2Type class of argument 2
     * @param <C> instance type
     * @param <A1> argument 1 type
     * @param <A2> argument 2 type
     * @return function handle
     */
    public static <A1, A2, C> V2<A1, A2> bindInstanceV2(final C instance, String name, boolean setAccessible,
    final Class<? super A1> a1Type, final Class<? super A2> a2Type) {
        final Method method = getDeclaredMethod(instance.getClass(), name, false, setAccessible, Void.TYPE, a1Type, a2Type);
        return new V2<A1, A2>() {
            public void call(A1 a1, A2 a2) {
                invoke(method, instance, Void.TYPE, a1, a2);
            }
        };
    }

    /**
     * load static method that takes no parameters and returns type R
     * @param clazz class to load static method from
     * @param name method name
     * @param setAccessible whether method is accessible (public vs private)
     * @param rType class of return type
     * @param <R> return type
     * @return function handle
     */
    public static <R> R0<R> loadStaticR0(Class<?> clazz, String name, boolean setAccessible,
    final Class<? extends R> rType) {
        final Method method = getDeclaredMethod(clazz, name, true, setAccessible, rType);
        return new R0<R>() {
            public R call() {
                return invoke(method, null, rType);
            }
        };
    }

    /**
     * load static method that takes one parameter and returns type R
     * @param clazz class to load static method from
     * @param name method name
     * @param setAccessible whether method is accessible (public vs private)
     * @param rType class of return type
     * @param a1Type argument 1 class
     * @param <R> return type
     * @param <A1> argument 1 type
     * @return function handle
     */
    public static <A1, R> R1<R, A1> loadStaticR1(Class<?> clazz, String name, boolean setAccessible,
    final Class<? extends R> rType, Class<? super A1> a1Type) {
        final Method method = getDeclaredMethod(clazz, name, true, setAccessible, rType, a1Type);
        return new R1<R, A1>() {
            public R call(A1 a1) {
                return invoke(method, null, rType, a1);
            }
        };
    }

    /**
     * load static method that takes two parameters and return nothing
     * @param clazz class to load static method from
     * @param name method name
     * @param setAccessible whether method is accessible (public vs private)
     * @param a1Type argument 1 class
     * @param a2Type argument 2 class
     * @param <A1> argument 1 type
     * @param <A2> argument 2 type
     * @return function handle
     */
    public static <A1, A2> V2<A1, A2> loadStaticV2(Class<?> clazz, String name, boolean setAccessible,
                                                   final Class<? super A1> a1Type, final Class<? super A2> a2Type) {
        final Method method = getDeclaredMethod(clazz, name, true, setAccessible, Void.TYPE, a1Type, a2Type);
        return new V2<A1, A2>() {
            public void call(A1 a1, A2 a2) {
                invoke(method, null, Void.TYPE, a1, a2);
            }
        };
    }

    /**
     * load default constructor
     * @param clazz Class to load constructor for
     * @param setAccessible whether method is accessible (public vs private)
     * @param <C> Class type
     * @return function handle
     */
    public static <C> R0<C> loadConstructor0(final Class<? extends C> clazz, boolean setAccessible) {
        final Constructor<? extends C> constructor = getDeclaredConstructor(clazz, setAccessible);
        return new R0<C>() {
            public C call() {
                return newInstance(constructor);
            }
        };
    }

    /**
     * load constructor that takes 1 parameter
     * @param clazz Class to load constructor for
     * @param setAccessible whether method is accessible (public vs private)
     * @param a1Type argument 1 class
     * @param <C> Class type
     * @param <A1> argument 1 type
     * @return function handle
     */
    public static <A1, C> R1<C, A1> loadConstructor1(final Class<? extends C> clazz, boolean setAccessible,
                                                     Class<? super A1> a1Type) {
        final Constructor<? extends C> constructor = getDeclaredConstructor(clazz, setAccessible, a1Type);
        return new R1<C, A1>() {
            public C call(A1 a1) {
                return newInstance(constructor, a1);
            }
        };
    }

    /**
     * load constructor that takes 2 parameters
     * @param clazz Class to load constructor for
     * @param setAccessible whether method is accessible (public vs private)
     * @param a1Type argument 1 class
     * @param a2Type argument 2 class
     * @param <C> Class type
     * @param <A1> argument 1 type
     * @param <A2> argument 2 type
     * @return function handle
     */
    public static <A1, A2, C> R2<C, A1, A2> loadConstructor2(final Class<? extends C> clazz, boolean setAccessible,
                                                             Class<? super A1> a1Type, Class<? super A2> a2Type) {
        final Constructor<? extends C> constructor = getDeclaredConstructor(clazz, setAccessible, a1Type, a2Type);
        return new R2<C, A1, A2>() {
            public C call(A1 a1, A2 a2) {
                return newInstance(constructor, a1, a2);
            }
        };
    }

    /**
     * load constuctor that takes 3 parameters
     * @param clazz class to load constructor for
     * @param setAccessible whether method is accessible (public vs private)
     * @param a1Type argument 1 class
     * @param a2Type argument 2 class
     * @param a3Type argument 3 class
     * @param <C> Class type
     * @param <A1> argument 1 type
     * @param <A2> argument 2 type
     * @param <A3> argument 3 type
     * @return function handle
     */
    public static <C, A1, A2, A3> R3<C, A1, A2, A3> loadConstuctor3(final Class<? extends C> clazz, boolean setAccessible,
                                                                    Class<? super A1> a1Type, Class<? super A2> a2Type,
                                                                    Class<? super A3> a3Type) {
        final Constructor<? extends C> constructor = getDeclaredConstructor(clazz, setAccessible, a1Type, a2Type, a3Type);
        return new R3<C, A1, A2, A3>() {
            public C call(A1 a1, A2 a2, A3 a3) {
                return newInstance(constructor, a1, a2, a3);
            }
        };
    }

    /**
     * loads constructor that takes 4 parameters
     * @param clazz class to load constructor for
     * @param setAccessible whether method is accessible (public vs private)
     * @param a1Type argument 1 class
     * @param a2Type argument 2 class
     * @param a3Type argument 3 class
     * @param a4Type argument 4 class
     * @param <C> Class type
     * @param <A1> argument 1 type
     * @param <A2> argument 2 type
     * @param <A3> argument 3 type
     * @param <A4> argument 4 type
     * @return function handle
     */
    public static <C, A1, A2, A3, A4> R4<C, A1, A2, A3, A4> loadConstuctor4(final Class<? extends C> clazz,
                                                                            boolean setAccessible,
                                                                            Class<? super A1> a1Type,
                                                                            Class<? super A2> a2Type,
                                                                            Class<? super A3> a3Type,
                                                                            Class<? super A4> a4Type) {
        final Constructor<? extends C> constructor = getDeclaredConstructor(clazz, setAccessible, a1Type, a2Type, a3Type, a4Type);
        return new R4<C, A1, A2, A3, A4>() {
            public C call(A1 a1, A2 a2, A3 a3, A4 a4) {
                return newInstance(constructor, a1, a2, a3, a4);
            }
        };
    }

    /**
     * loads constructor that takes 5 paramters
     * @param clazz class to load constructor for
     * @param setAccessible whether method is accessible (public vs private)
     * @param a1Type argument 1 class
     * @param a2Type argument 2 class
     * @param a3Type argument 3 class
     * @param a4Type argument 4 class
     * @param a5Type argument 5 class
     * @param <C> Class type
     * @param <A1> argument 1 type
     * @param <A2> argument 2 type
     * @param <A3> argument 3 type
     * @param <A4> argument 4 type
     * @param <A5> argument 5 type
     * @return function handle
     */
    public static <C, A1, A2, A3, A4, A5> R5<C, A1, A2, A3, A4, A5> loadConstuctor5(final Class<? extends C> clazz,
                                                                                    boolean setAccessible,
                                                                                    Class<? super A1> a1Type,
                                                                                    Class<? super A2> a2Type,
                                                                                    Class<? super A3> a3Type,
                                                                                    Class<? super A4> a4Type,
                                                                                    Class<? super A5> a5Type) {
        final Constructor<? extends C> constructor = getDeclaredConstructor(clazz, setAccessible, a1Type, a2Type, a3Type, a4Type, a5Type);
        return new R5<C, A1, A2, A3, A4, A5>() {
            public C call(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5) {
                return newInstance(constructor, a1, a2, a3, a4, a5);
            }
        };
    }

    /**
     * load constuctor that takes 9 parameters
     * @param a1Type argument 1 class
     * @param a2Type argument 2 class
     * @param a3Type argument 3 class
     * @param a4Type argument 4 class
     * @param a5Type argument 5 class
     * @param a6Type argument 6 class
     * @param a7Type argument 7 class
     * @param a8Type argument 8 class
     * @param a9type argument 9 class
     * @param <C> Class type
     * @param <A1> argument 1 type
     * @param <A2> argument 2 type
     * @param <A3> argument 3 type
     * @param <A4> argument 4 type
     * @param <A5> argument 5 type
     * @param <A6> argument 6 type
     * @param <A7> argument 7 type
     * @param <A8> argument 8 type
     * @param <A9> argument 9 type
     * @return function handle
     */
    public static <C, A1, A2, A3, A4, A5, A6, A7, A8, A9> R9<C, A1, A2, A3, A4, A5, A6, A7, A8, A9> loadConstuctor9(
                                                                                    final Class<? extends C> clazz,
                                                                                    boolean setAccessible,
                                                                                    Class<? super A1> a1Type,
                                                                                    Class<? super A2> a2Type,
                                                                                    Class<? super A3> a3Type,
                                                                                    Class<? super A4> a4Type,
                                                                                    Class<? super A5> a5Type,
                                                                                    Class<? super A6> a6Type,
                                                                                    Class<? super A7> a7Type,
                                                                                    Class<? super A8> a8Type,
                                                                                    Class<? super A9> a9type) {
        final Constructor<?extends C> constructor=
            getDeclaredConstructor(clazz, setAccessible, a1Type, a2Type, a3Type, a4Type, a5Type, a6Type, a7Type, a8Type, a9type);
        return new R9<C, A1, A2, A3, A4, A5, A6, A7, A8, A9>(){
            public C call(A1 a1,A2 a2,A3 a3,A4 a4,A5 a5,A6 a6,A7 a7,A8 a8,A9 a9){
                return newInstance(constructor,a1,a2,a3,a4,a5,a6,a7,a8,a9);
            }
        };
    }

    public static <T> T getStaticField(Class<?> clazz, String name, Class<? extends T> type) {
        R1<T, Object> caster = makeCaster(type);
        try {
            return caster.call(clazz.getField(name).get(null));
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            throw new ReflectException(e);
        }
    }

    public static void setStaticField(Class<?> clazz, String name, boolean setAccessible, final Object value) {
        try {
            Field field = clazz.getDeclaredField(name);
            if (setAccessible) {
                field.setAccessible(true);
            }
            field.set(null, value);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            throw new ReflectException(e);
        }
    }
}
