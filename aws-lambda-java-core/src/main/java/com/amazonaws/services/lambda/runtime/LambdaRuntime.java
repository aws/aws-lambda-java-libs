/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime;

import java.io.IOException;

public final class LambdaRuntime {
    private LambdaRuntime() {}

    private static volatile LambdaLogger logger = new LambdaLogger() {

        public void log(String message) {
            System.out.print(message);
        }

        public void log(byte[] message) {
            try {
                System.out.write(message);
            } catch (IOException e) {
                // NOTE: When actually running on AWS Lambda, an IOException would never happen
                e.printStackTrace();
            }
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
