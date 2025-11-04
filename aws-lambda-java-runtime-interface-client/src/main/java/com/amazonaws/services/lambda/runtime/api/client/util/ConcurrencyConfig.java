/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.util;

import com.amazonaws.services.lambda.runtime.api.client.ReservedRuntimeEnvironmentVariables;
import com.amazonaws.services.lambda.runtime.api.client.UserFault;
import com.amazonaws.services.lambda.runtime.api.client.logging.LambdaContextLogger;
import com.amazonaws.services.lambda.runtime.logging.LogFormat;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;

public class ConcurrencyConfig {
    private static final int AWS_LAMBDA_MAX_CONCURRENCY_LIMIT = 1000;
    private final int numberOfPlatformThreads;
    private final String INVALID_CONFIG_MESSAGE_PREFIX = String.format("User configured %s is invalid. Please make sure it is a positive number more than zero and less than or equal %d", 
                                                                        ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_MAX_CONCURRENCY, AWS_LAMBDA_MAX_CONCURRENCY_LIMIT);

    public ConcurrencyConfig(LambdaContextLogger logger) {
        this(logger, new EnvReader());
    }

    public ConcurrencyConfig(LambdaContextLogger logger, EnvReader envReader) {
        int readNumOfPlatformThreads = 0;
        try {
            String readLambdaMaxConcurrencyEnvVar = envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_MAX_CONCURRENCY);

            if (readLambdaMaxConcurrencyEnvVar != null) {
                readNumOfPlatformThreads = Integer.parseInt(readLambdaMaxConcurrencyEnvVar);
            }
        } catch (Exception e) {
            String message = String.format("%s\n%s", INVALID_CONFIG_MESSAGE_PREFIX, UserFault.trace(e));
            logger.log(message, logger.getLogFormat() == LogFormat.JSON ? LogLevel.ERROR : LogLevel.UNDEFINED);
            throw e;
        }

        this.numberOfPlatformThreads = readNumOfPlatformThreads;
    }

    public String getConcurrencyConfigMessage() {
        return String.format("Starting %d concurrent function handler threads.", this.numberOfPlatformThreads);
    }

    public boolean isMultiConcurrent() {
        return this.numberOfPlatformThreads >= 1;
    }

    public int getNumberOfPlatformThreads() {
        return numberOfPlatformThreads;
    }
}
