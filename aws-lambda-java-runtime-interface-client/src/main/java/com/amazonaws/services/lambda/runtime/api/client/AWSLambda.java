//
//  AWSLambda.java
//
//  Copyright (c) 2013 Amazon. All rights reserved.
//
package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.api.client.LambdaRequestHandler.UserFaultHandler;
import com.amazonaws.services.lambda.runtime.api.client.logging.FramedTelemetryLogSink;
import com.amazonaws.services.lambda.runtime.api.client.logging.LambdaContextLogger;
import com.amazonaws.services.lambda.runtime.api.client.logging.LogSink;
import com.amazonaws.services.lambda.runtime.api.client.logging.StdOutLogSink;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.InvocationRequest;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.LambdaRuntimeClient;
import com.amazonaws.services.lambda.runtime.api.client.util.LambdaOutputStream;
import com.amazonaws.services.lambda.runtime.api.client.util.UnsafeUtil;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.factories.GsonFactory;
import com.amazonaws.services.lambda.runtime.serialization.factories.JacksonFactory;
import com.amazonaws.services.lambda.runtime.serialization.util.ReflectUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.security.Security;
import java.util.Properties;

/**
 * The entrypoint of this class is {@link AWSLambda#startRuntime}. It performs two main tasks:
 *
 * <p>
 * 1. loads the user's handler.
 * <br/>
 * 2. enters the Lambda runtime loop which handles function invocations as defined in the Lambda Custom Runtime API.
 *
 * <p>
 * Once initialized, {@link AWSLambda#startRuntime} will halt only if an irrecoverable error occurs.
 */
public class AWSLambda {

    private static final String TRUST_STORE_PROPERTY = "javax.net.ssl.trustStore";

    private static final String JAVA_SECURITY_PROPERTIES = "java.security.properties";

    private static final String NETWORKADDRESS_CACHE_NEGATIVE_TTL_ENV_VAR = "AWS_LAMBDA_JAVA_NETWORKADDRESS_CACHE_NEGATIVE_TTL";

    private static final String NETWORKADDRESS_CACHE_NEGATIVE_TTL_PROPERTY = "networkaddress.cache.negative.ttl";

    private static final String DEFAULT_NEGATIVE_CACHE_TTL = "1";

    // System property for Lambda tracing, see aws-xray-sdk-java/LambdaSegmentContext
    // https://github.com/aws/aws-xray-sdk-java/blob/2f467e50db61abb2ed2bd630efc21bddeabd64d9/aws-xray-recorder-sdk-core/src/main/java/com/amazonaws/xray/contexts/LambdaSegmentContext.java#L39-L40
    private static final String LAMBDA_TRACE_HEADER_PROP = "com.amazonaws.xray.traceHeader";

    static {
        // Override the disabledAlgorithms setting to match configuration for openjdk8-u181.
        // This is to keep DES ciphers around while we deploying security updates.
        Security.setProperty(
                "jdk.tls.disabledAlgorithms",
                "SSLv3, RC4, MD5withRSA, DH keySize < 1024, EC keySize < 224, DES40_CBC, RC4_40, 3DES_EDE_CBC"
        );
        // Override the location of the trusted certificate authorities to be provided by the system.
        // The ca-certificates package provides /etc/pki/java/cacerts which becomes the symlink destination
        // of $java_home/lib/security/cacerts when java is installed in the chroot. Given that java is provided
        // in /var/lang as opposed to installed in the chroot, this brings it closer.
        if (System.getProperty(TRUST_STORE_PROPERTY) == null) {
            final File systemCacerts = new File("/etc/pki/java/cacerts");
            if (systemCacerts.exists() && systemCacerts.isFile()) {
                System.setProperty(TRUST_STORE_PROPERTY, systemCacerts.getPath());
            }
        }

        if (isNegativeCacheOverridable()) {
            String ttlFromEnv = System.getenv(NETWORKADDRESS_CACHE_NEGATIVE_TTL_ENV_VAR);
            String negativeCacheTtl = ttlFromEnv == null ? DEFAULT_NEGATIVE_CACHE_TTL : ttlFromEnv;
            Security.setProperty(NETWORKADDRESS_CACHE_NEGATIVE_TTL_PROPERTY, negativeCacheTtl);
        }
    }

    private static boolean isNegativeCacheOverridable() {
        String securityPropertiesPath = System.getProperty(JAVA_SECURITY_PROPERTIES);
        if (securityPropertiesPath == null) {
            return true;
        }
        try (FileInputStream inputStream = new FileInputStream(securityPropertiesPath)) {
            Properties secProps = new Properties();
            secProps.load(inputStream);
            return !secProps.containsKey(NETWORKADDRESS_CACHE_NEGATIVE_TTL_PROPERTY);
        } catch (IOException e) {
            return true;
        }
    }

    private static LambdaRequestHandler findRequestHandler(final String handlerString, ClassLoader customerClassLoader) {
        final HandlerInfo handlerInfo;
        try {
            handlerInfo = HandlerInfo.fromString(handlerString, customerClassLoader);
        } catch (HandlerInfo.InvalidHandlerException e) {
            UserFault userFault = UserFault.makeUserFault("Invalid handler: `" + handlerString + "'");
            return new UserFaultHandler(userFault);
        } catch (ClassNotFoundException e) {
            return LambdaRequestHandler.classNotFound(e, HandlerInfo.className(handlerString));
        } catch (NoClassDefFoundError e) {
            return LambdaRequestHandler.initErrorHandler(e, HandlerInfo.className(handlerString));
        } catch (Throwable t) {
            throw UserFault.makeInitErrorUserFault(t, HandlerInfo.className(handlerString));
        }

        final LambdaRequestHandler requestHandler = EventHandlerLoader.loadEventHandler(handlerInfo);
        // if loading the handler failed and the failure is fatal (for e.g. the constructor threw an exception)
        // we want to report this as an init error rather than deferring to the first invoke.
        if (requestHandler instanceof UserFaultHandler) {
            UserFault userFault = ((UserFaultHandler) requestHandler).fault;
            if (userFault.fatal) {
                throw userFault;
            }
        }
        return requestHandler;
    }

    public static void setupRuntimeLogger(LambdaLogger lambdaLogger)
            throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
        ReflectUtil.setStaticField(
                Class.forName("com.amazonaws.services.lambda.runtime.LambdaRuntime"),
                "logger",
                true,
                lambdaLogger
        );
    }

    public static String getEnvOrExit(String envVariableName) {
        String value = System.getenv(envVariableName);
        if (value == null) {
            System.err.println("Could not get environment variable " + envVariableName);
            System.exit(-1);
        }
        return value;
    }

    protected static URLClassLoader customerClassLoader;

    /**
     * convert an integer into a FileDescriptor object using reflection to access private members.
     */
    private static FileDescriptor intToFd(int fd) throws RuntimeException {
        try {
            Class<FileDescriptor> clazz = FileDescriptor.class;
            Constructor<FileDescriptor> c = clazz.getDeclaredConstructor(new Class<?>[]{Integer.TYPE});
            c.setAccessible(true);
            return c.newInstance(new Integer(fd));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static LogSink createLogSink() {
        final String fdStr = System.getenv("_LAMBDA_TELEMETRY_LOG_FD");
        if (fdStr == null) {
            return new StdOutLogSink();
        }

        try {
            int fdInt = Integer.parseInt(fdStr);
            FileDescriptor fd = intToFd(fdInt);
            return new FramedTelemetryLogSink(fd);
        } catch (Exception e) {
            return new StdOutLogSink();
        }
    }

    public static void main(String[] args) {
        // TODO validate arguments, show usage
        startRuntime(args[0]);
    }

    private static void startRuntime(String handler) {
        try (LogSink logSink = createLogSink()) {
            startRuntime(handler, new LambdaContextLogger(logSink));
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    private static void startRuntime(String handler, LambdaLogger lambdaLogger) throws Throwable {
        UnsafeUtil.disableIllegalAccessWarning();

        System.setOut(new PrintStream(new LambdaOutputStream(System.out), false, "UTF-8"));
        System.setErr(new PrintStream(new LambdaOutputStream(System.err), false, "UTF-8"));
        setupRuntimeLogger(lambdaLogger);

        String runtimeApi = getEnvOrExit(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_RUNTIME_API);
        LambdaRuntimeClient runtimeClient = new LambdaRuntimeClient(runtimeApi);

        String taskRoot = System.getProperty("user.dir");
        String libRoot = "/opt/java";
        // Make system classloader the customer classloader's parent to ensure any aws-lambda-java-core classes
        // are loaded from the system classloader.
        customerClassLoader = new CustomerClassLoader(taskRoot, libRoot, ClassLoader.getSystemClassLoader());
        Thread.currentThread().setContextClassLoader(customerClassLoader);

        // Load the user's handler
        LambdaRequestHandler requestHandler;
        try {
            requestHandler = findRequestHandler(handler, customerClassLoader);
        } catch (UserFault userFault) {
            lambdaLogger.log(userFault.reportableError());
            ByteArrayOutputStream payload = new ByteArrayOutputStream(1024);
            Failure failure = new Failure(userFault);
            GsonFactory.getInstance().getSerializer(Failure.class).toJson(failure, payload);
            runtimeClient.postInitError(payload.toByteArray(), failure.getErrorType());
            System.exit(1);
            return;
        }

        boolean shouldExit = false;
        while (!shouldExit) {
            UserFault userFault = null;
            InvocationRequest request = runtimeClient.waitForNextInvocation();
            if (request.getXrayTraceId() != null) {
                System.setProperty(LAMBDA_TRACE_HEADER_PROP, request.getXrayTraceId());
            } else {
                System.clearProperty(LAMBDA_TRACE_HEADER_PROP);
            }

            ByteArrayOutputStream payload;
            try {
                payload = requestHandler.call(request);
                // TODO calling payload.toByteArray() creates a new copy of the underlying buffer
                runtimeClient.postInvocationResponse(request.getId(), payload.toByteArray());
            } catch (UserFault f) {
                userFault = f;
                UserFault.filterStackTrace(f);
                payload = new ByteArrayOutputStream(1024);
                Failure failure = new Failure(f);
                GsonFactory.getInstance().getSerializer(Failure.class).toJson(failure, payload);
                shouldExit = f.fatal;
                runtimeClient.postInvocationError(request.getId(), payload.toByteArray(), failure.getErrorType());
            } catch (Throwable t) {
                UserFault.filterStackTrace(t);
                userFault = UserFault.makeUserFault(t);
                payload = new ByteArrayOutputStream(1024);
                Failure failure = new Failure(t);
                GsonFactory.getInstance().getSerializer(Failure.class).toJson(failure, payload);
                // These two categories of errors are considered fatal.
                shouldExit = Failure.isInvokeFailureFatal(t);
                runtimeClient.postInvocationError(request.getId(), payload.toByteArray(), failure.getErrorType(),
                        serializeAsXRayJson(t));
            } finally {
                if (userFault != null) {
                    lambdaLogger.log(userFault.reportableError());
                }
            }
        }
    }

    private static PojoSerializer<XRayErrorCause> xRayErrorCauseSerializer;

    /**
     * @param throwable throwable to convert
     * @return json as string expected by XRay's web console. On conversion failure, returns null.
     */
    private static String serializeAsXRayJson(Throwable throwable) {
        try {
            final OutputStream outputStream = new ByteArrayOutputStream();
            final XRayErrorCause cause = new XRayErrorCause(throwable);
            if (xRayErrorCauseSerializer == null) {
                xRayErrorCauseSerializer = JacksonFactory.getInstance().getSerializer(XRayErrorCause.class);
            }
            xRayErrorCauseSerializer.toJson(cause, outputStream);
            return outputStream.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
