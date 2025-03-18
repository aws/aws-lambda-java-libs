// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
package com.amazonaws.services.lambda.extension;

public class Logger {

    private static final boolean IS_DEBUG_ENABLED = initializeDebugFlag();
    private static final String PREFIX = "[PROFILER] ";

    private static boolean initializeDebugFlag() {
        String envValue = System.getenv("AWS_LAMBDA_PROFILER_DEBUG");
        return "true".equalsIgnoreCase(envValue) || "1".equals(envValue);
    }

    public static void debug(String message) {
        if(IS_DEBUG_ENABLED) {
            System.out.println(PREFIX + message);
        }
    }

    public static void error(String message) {
        System.out.println(PREFIX + message);
    }
    
}