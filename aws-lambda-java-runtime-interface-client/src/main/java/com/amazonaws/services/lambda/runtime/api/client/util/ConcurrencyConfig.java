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
    private static final int AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY_LIMIT = 1000;
    private final int numberOfPlatformThreads;
    private final boolean isConcurrencyEnabled;

    public ConcurrencyConfig(LambdaContextLogger logger) {
        this(logger, new EnvReader());
    }

    public ConcurrencyConfig(LambdaContextLogger logger, EnvReader envReader) {
        this.isConcurrencyEnabled = Boolean.parseBoolean(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_ENABLE_MULTICONCURRENT_RIC));
        int platformThreads = this.isConcurrencyEnabled ? Runtime.getRuntime().availableProcessors() : 0;
        
        if (this.isConcurrencyEnabled) {
            try {
                int readNumOfPlatformThreads = Integer.parseUnsignedInt(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY));
                if (readNumOfPlatformThreads > 0 && readNumOfPlatformThreads <= AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY_LIMIT) {
                    platformThreads = readNumOfPlatformThreads;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                String message = String.format("User configured %s is not valid. Please make sure it is a positive number more than zero and less than or equal %d\n%s\nDefaulting to number of cores as number of platform threads.", ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY, AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY_LIMIT, UserFault.trace(e));
                logger.log(message, logger.getLogFormat() == LogFormat.JSON ? LogLevel.WARN : LogLevel.UNDEFINED);
            }
        }

        this.numberOfPlatformThreads = platformThreads;
    }

    public String getConcurrencyConfigMessage() {
        return String.format("Starting %d concurrent function handler threads.", this.numberOfPlatformThreads);
    }

    public boolean isMultiConcurrent() {
        return this.isConcurrencyEnabled && this.numberOfPlatformThreads >= 2;
    }

    public int getNumberOfPlatformThreads() {
        return numberOfPlatformThreads;
    }
}
