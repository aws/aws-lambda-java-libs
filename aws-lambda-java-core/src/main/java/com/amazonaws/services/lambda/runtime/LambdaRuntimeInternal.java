/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime;

/**
 * This class is used internally by Lambda Runtime.
 */
public final class LambdaRuntimeInternal {
    private LambdaRuntimeInternal() {}

    private static boolean useLog4jAppender;

    public static void setUseLog4jAppender(boolean useLog4j) {
        useLog4jAppender = useLog4j;
    }

    public static boolean getUseLog4jAppender() {
        return useLog4jAppender;
    }
}
