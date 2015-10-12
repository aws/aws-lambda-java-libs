/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime;

public final class LambdaRuntime {
    private LambdaRuntime() {}

    private static volatile LambdaLogger logger = new LambdaLogger() {
        public void log(String string) {
            System.out.print(string);
        }
    };

    /**
     * Returns the global lambda logger instance
     *
     */
	public static LambdaLogger getLogger() {
        return logger;
    }
}
