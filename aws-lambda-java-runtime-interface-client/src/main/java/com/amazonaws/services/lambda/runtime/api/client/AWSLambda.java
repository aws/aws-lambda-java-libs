/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.crac.Core;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.api.client.LambdaRequestHandler.UserFaultHandler;
import com.amazonaws.services.lambda.runtime.api.client.logging.FramedTelemetryLogSink;
import com.amazonaws.services.lambda.runtime.api.client.logging.LambdaContextLogger;
import com.amazonaws.services.lambda.runtime.api.client.logging.LogSink;
import com.amazonaws.services.lambda.runtime.api.client.logging.StdOutLogSink;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.LambdaError;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.LambdaRuntimeApiClient;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.LambdaRuntimeApiClientImpl;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.RapidErrorType;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.converters.LambdaErrorConverter;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.converters.XRayErrorCauseConverter;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.InvocationRequest;
import com.amazonaws.services.lambda.runtime.api.client.util.ConcurrencyConfig;
import com.amazonaws.services.lambda.runtime.api.client.util.LambdaOutputStream;
import com.amazonaws.services.lambda.runtime.api.client.util.UnsafeUtil;
import com.amazonaws.services.lambda.runtime.logging.LogFormat;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.amazonaws.services.lambda.runtime.serialization.util.ReflectUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.security.Security;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static URLClassLoader customerClassLoader;
    
    private static final String TRUST_STORE_PROPERTY = "javax.net.ssl.trustStore";

    private static final String JAVA_SECURITY_PROPERTIES = "java.security.properties";

    private static final String NETWORKADDRESS_CACHE_NEGATIVE_TTL_ENV_VAR = "AWS_LAMBDA_JAVA_NETWORKADDRESS_CACHE_NEGATIVE_TTL";

    private static final String NETWORKADDRESS_CACHE_NEGATIVE_TTL_PROPERTY = "networkaddress.cache.negative.ttl";

    private static final String DEFAULT_NEGATIVE_CACHE_TTL = "1";

    // System property for Lambda tracing, see aws-xray-sdk-java/LambdaSegmentContext
    // https://github.com/aws/aws-xray-sdk-java/blob/2f467e50db61abb2ed2bd630efc21bddeabd64d9/aws-xray-recorder-sdk-core/src/main/java/com/amazonaws/xray/contexts/LambdaSegmentContext.java#L39-L40
    private static final String LAMBDA_TRACE_HEADER_PROP = "com.amazonaws.xray.traceHeader";

    private static final String INIT_TYPE_SNAP_START = "snap-start";

    private static final String AWS_LAMBDA_INITIALIZATION_TYPE = System.getenv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_INITIALIZATION_TYPE);
    
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

    private static LambdaRequestHandler getLambdaRequestHandlerObject(String handler, LambdaContextLogger lambdaLogger, LambdaRuntimeApiClient runtimeClient) throws ClassNotFoundException, IOException {
        UnsafeUtil.disableIllegalAccessWarning();

        System.setOut(new PrintStream(new LambdaOutputStream(System.out), false, "UTF-8"));
        System.setErr(new PrintStream(new LambdaOutputStream(System.err), false, "UTF-8"));
        setupRuntimeLogger(lambdaLogger);

        String taskRoot = System.getProperty("user.dir");
        String libRoot = "/opt/java";
        // Make system classloader the customer classloader's parent to ensure any aws-lambda-java-core classes
        // are loaded from the system classloader.
        customerClassLoader = new CustomerClassLoader(taskRoot, libRoot, ClassLoader.getSystemClassLoader());
        Thread.currentThread().setContextClassLoader(customerClassLoader);

        // Load the user's handler
        LambdaRequestHandler requestHandler = null;
        try {
            requestHandler = findRequestHandler(handler, customerClassLoader);
        } catch (UserFault userFault) {
            lambdaLogger.log(userFault.reportableError(), lambdaLogger.getLogFormat() == LogFormat.JSON ? LogLevel.ERROR : LogLevel.UNDEFINED);
            LambdaError error = new LambdaError(
                    LambdaErrorConverter.fromUserFault(userFault),
                    RapidErrorType.BadFunctionCode);
            runtimeClient.reportInitError(error);
            System.exit(1);
        }

        if (INIT_TYPE_SNAP_START.equals(AWS_LAMBDA_INITIALIZATION_TYPE)) {
            onInitComplete(lambdaLogger, runtimeClient);
        }

        return requestHandler;
    }

    private static void setupRuntimeLogger(LambdaLogger lambdaLogger)
            throws ClassNotFoundException {
        ReflectUtil.setStaticField(
                Class.forName("com.amazonaws.services.lambda.runtime.LambdaRuntime"),
                "logger",
                true,
                lambdaLogger
        );
    }

    /**
     * convert an integer into a FileDescriptor object using reflection to access private members.
     */
    private static FileDescriptor intToFd(int fd) throws RuntimeException {
        try {
            Class<FileDescriptor> clazz = FileDescriptor.class;
            Constructor<FileDescriptor> c = clazz.getDeclaredConstructor(Integer.TYPE);
            c.setAccessible(true);
            return c.newInstance(fd);
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

    public static void main(String[] args) throws Throwable {
        try (LambdaContextLogger lambdaLogger = initLogger()) {
            LambdaRuntimeApiClient runtimeClient = new LambdaRuntimeApiClientImpl(LambdaEnvironment.RUNTIME_API);
            LambdaRequestHandler lambdaRequestHandler = getLambdaRequestHandlerObject(args[0], lambdaLogger, runtimeClient);
            ConcurrencyConfig concurrencyConfig = new ConcurrencyConfig(lambdaLogger);
            startRuntimeLoops(lambdaRequestHandler, lambdaLogger, concurrencyConfig, runtimeClient);
        } catch (IOException | ClassNotFoundException t) {
            throw new Error(t);
        }
    }

    private static LambdaContextLogger initLogger() {
        LogSink logSink = createLogSink();
        LambdaContextLogger logger = new LambdaContextLogger(
                logSink,
                LogLevel.fromString(LambdaEnvironment.LAMBDA_LOG_LEVEL),
                LogFormat.fromString(LambdaEnvironment.LAMBDA_LOG_FORMAT));

        return logger;
    }

    private static void startRuntimeLoopWithExecutor(LambdaRequestHandler lambdaRequestHandler, LambdaContextLogger lambdaLogger, ExecutorService executorService, LambdaRuntimeApiClient runtimeClient) {
        executorService.submit(() -> { 
            try {
                startRuntimeLoop(lambdaRequestHandler, lambdaLogger, runtimeClient);
            } catch (Exception e) {
                lambdaLogger.log(String.format("Runtime Loop on Thread ID: %s Failed.\n%s", Thread.currentThread().getName(), UserFault.trace(e)), lambdaLogger.getLogFormat() == LogFormat.JSON ? LogLevel.ERROR : LogLevel.UNDEFINED);
            }
        });
    }

    protected static void startRuntimeLoops(LambdaRequestHandler lambdaRequestHandler, LambdaContextLogger lambdaLogger, ConcurrencyConfig concurrencyConfig, LambdaRuntimeApiClient runtimeClient) throws Exception {
        if (concurrencyConfig.isMultiConcurrent()) {
            lambdaLogger.log(concurrencyConfig.getConcurrencyConfigMessage(), lambdaLogger.getLogFormat() == LogFormat.JSON ? LogLevel.INFO : LogLevel.UNDEFINED);
            ExecutorService platformThreadExecutor = Executors.newFixedThreadPool(concurrencyConfig.getNumberOfPlatformThreads());
            try {
                for (int i = 0; i < concurrencyConfig.getNumberOfPlatformThreads(); i++) {
                    startRuntimeLoopWithExecutor(lambdaRequestHandler, lambdaLogger, platformThreadExecutor, runtimeClient);
                }
            } finally {
                platformThreadExecutor.shutdown();
                while (true) {
                    if (platformThreadExecutor.isTerminated()) {
                        break;
                    }
                }
            }
        } else {
            startRuntimeLoop(lambdaRequestHandler, lambdaLogger, runtimeClient);
        }
    }

    private static void startRuntimeLoop(LambdaRequestHandler lambdaRequestHandler, LambdaContextLogger lambdaLogger, LambdaRuntimeApiClient runtimeClient) throws Exception {
        boolean shouldExit = false;
        while (!shouldExit) {
            UserFault userFault = null;
            InvocationRequest request = runtimeClient.nextInvocation();
            if (request.getXrayTraceId() != null) {
                System.setProperty(LAMBDA_TRACE_HEADER_PROP, request.getXrayTraceId());
            } else {
                System.clearProperty(LAMBDA_TRACE_HEADER_PROP);
            }

            ByteArrayOutputStream payload;
            try {
                payload = lambdaRequestHandler.call(request);
                runtimeClient.reportInvocationSuccess(request.getId(), payload.toByteArray());
                // clear interrupted flag in case if it was set by user's code
                Thread.interrupted();
            } catch (UserFault f) {
                shouldExit = f.fatal;
                userFault = f;
                UserFault.filterStackTrace(f);
                LambdaError error = new LambdaError(
                        LambdaErrorConverter.fromUserFault(f),
                        RapidErrorType.BadFunctionCode);
                runtimeClient.reportInvocationError(request.getId(), error);
            } catch (Throwable t) {
                shouldExit = t instanceof VirtualMachineError || t instanceof IOError;
                UserFault.filterStackTrace(t);
                userFault = UserFault.makeUserFault(t);

                LambdaError error = new LambdaError(
                        LambdaErrorConverter.fromThrowable(t),
                        XRayErrorCauseConverter.fromThrowable(t),
                        RapidErrorType.UserException);
                runtimeClient.reportInvocationError(request.getId(), error);
            } finally {
                if (userFault != null) {
                    lambdaLogger.log(userFault.reportableError(), lambdaLogger.getLogFormat() == LogFormat.JSON ? LogLevel.ERROR : LogLevel.UNDEFINED);
                }
            }
        }
    }

    private static void onInitComplete(final LambdaContextLogger lambdaLogger, LambdaRuntimeApiClient runtimeClient) throws IOException {
        try {
            Core.getGlobalContext().beforeCheckpoint(null);
            runtimeClient.restoreNext();
        } catch (Exception e1) {
            logExceptionCloudWatch(lambdaLogger, e1);
            runtimeClient.reportInitError(new LambdaError(
                    LambdaErrorConverter.fromThrowable(e1),
                    RapidErrorType.BeforeCheckpointError));
            System.exit(64);
        }

        try {
            Core.getGlobalContext().afterRestore(null);
        } catch (Exception restoreExc) {
            logExceptionCloudWatch(lambdaLogger, restoreExc);
            runtimeClient.reportRestoreError(new LambdaError(
                    LambdaErrorConverter.fromThrowable(restoreExc),
                    RapidErrorType.AfterRestoreError));
            System.exit(64);
        }
    }

    private static void logExceptionCloudWatch(LambdaContextLogger lambdaLogger, Exception exc) {
        UserFault.filterStackTrace(exc);
        UserFault userFault = UserFault.makeUserFault(exc, true);
        lambdaLogger.log(userFault.reportableError(), lambdaLogger.getLogFormat() == LogFormat.JSON ? LogLevel.ERROR : LogLevel.UNDEFINED);
    }

    protected static URLClassLoader getCustomerClassLoader() {
        return customerClassLoader;
    }
}
