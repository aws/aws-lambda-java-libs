/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.api.client.util.EnvReader;

import static java.lang.Integer.parseInt;

public class LambdaEnvironment {
    public static final EnvReader ENV_READER = new EnvReader();
    public static final int MEMORY_LIMIT = parseInt(ENV_READER.getEnvOrDefault(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_FUNCTION_MEMORY_SIZE, "128"));
    public static final String LOG_GROUP_NAME = ENV_READER.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_LOG_GROUP_NAME);
    public static final String LOG_STREAM_NAME = ENV_READER.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_LOG_STREAM_NAME);
    public static final String FUNCTION_NAME = ENV_READER.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_FUNCTION_NAME);
    public static final String FUNCTION_VERSION = ENV_READER.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_FUNCTION_VERSION);
}
