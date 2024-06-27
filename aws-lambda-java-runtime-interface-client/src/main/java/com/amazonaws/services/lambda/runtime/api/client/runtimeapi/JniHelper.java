/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class JniHelper {
    
    private static final String NATIVE_LIB_PATH = "/tmp/.libaws-lambda-jni.so";
    private static final String NATIVE_CLIENT_JNI_PROPERTY = "com.amazonaws.services.lambda.runtime.api.client.runtimeapi.NativeClient.JNI";

    /**
     * Unpacks JNI library from the JAR to a temporary location and tries to load it using System.load()
     * Implementation based on AWS CRT
     * (ref. <a href="https://github.com/awslabs/aws-crt-java/blob/0e9c3db8b07258b57c2503cfc47c787ccef10670/src/main/java/software/amazon/awssdk/crt/CRT.java#L106-L134">...</a>)
     *
     * @param libsToTry - array of native libraries to try
     */
    public static void load() {
        String jniLib = System.getProperty(NATIVE_CLIENT_JNI_PROPERTY);
        if (jniLib != null) {
            System.load(jniLib);
        } else {
            String[] libsToTry = new String[]{
                "libaws-lambda-jni.linux-x86_64.so",
                "libaws-lambda-jni.linux-aarch_64.so",
                "libaws-lambda-jni.linux_musl-x86_64.so",
                "libaws-lambda-jni.linux_musl-aarch_64.so"
            };
            unpackAndLoad(libsToTry, NativeClient.class);
        }
    }

    private static void unpackAndLoad(String[] libsToTry, Class clazz) {
        List<String> errorMessages = new ArrayList<>();
        for (String libToTry : libsToTry) {
            try (InputStream inputStream = clazz.getResourceAsStream(
                    Paths.get("/jni", libToTry).toString())) {
                if (inputStream == null) {
                    throw new FileNotFoundException("Specified file not in the JAR: " + libToTry);
                }
                Files.copy(inputStream, Paths.get(NATIVE_LIB_PATH), StandardCopyOption.REPLACE_EXISTING);
                System.load(NATIVE_LIB_PATH);
                return;
            } catch (UnsatisfiedLinkError | Exception e) {
                errorMessages.add(e.getMessage());
            }
        }

        for (int i = 0; i < libsToTry.length; ++i) {
            System.err.println("Failed to load the native runtime interface client library " 
                + libsToTry[i]
                + ". Exception: " 
                + errorMessages.get(i));
        }
        System.exit(-1);
    } 
}
