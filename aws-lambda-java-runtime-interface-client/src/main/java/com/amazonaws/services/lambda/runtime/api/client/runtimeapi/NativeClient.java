/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * This module defines the native Runtime Interface Client which is responsible for all HTTP
 * interactions with the Runtime API.
 */
class NativeClient {
    private static final String nativeLibPath = "/tmp/.aws-lambda-runtime-interface-client";
    private static final String[] libsToTry = {
            "/aws-lambda-runtime-interface-client.glibc.so",
            "/aws-lambda-runtime-interface-client.musl.so",
    };
    private static final Throwable[] exceptions = new Throwable[libsToTry.length];
    static {
            boolean loaded = false;
            for (int i = 0; !loaded && i < libsToTry.length; ++i) {
                try (InputStream lib = NativeClient.class.getResourceAsStream(libsToTry[i])) {
                    Files.copy(lib, Paths.get(nativeLibPath), StandardCopyOption.REPLACE_EXISTING);
                    System.load(nativeLibPath);
                    loaded = true;
                } catch (UnsatisfiedLinkError e) {
                    exceptions[i] = e;
                } catch (Exception e) {
                    exceptions[i] = e;
                }
            }
            if (!loaded) {
                for (int i = 0; i < libsToTry.length; ++i) {
                    System.err.printf("Failed to load the native runtime interface client library %s. Exception: %s\n", libsToTry[i], exceptions[i].getMessage());
                }
                System.exit(-1);
            }
            String userAgent = String.format(
                    "aws-lambda-java/%s-%s" ,
                    System.getProperty("java.vendor.version"),
                    NativeClient.class.getPackage().getImplementationVersion());
            initializeClient(userAgent.getBytes());
    }

    static native void initializeClient(byte[] userAgent);

    static native InvocationRequest next();

    static native void postInvocationResponse(byte[] requestId, byte[] response);

}
