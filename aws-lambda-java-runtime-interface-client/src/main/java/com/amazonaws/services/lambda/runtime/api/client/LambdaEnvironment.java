/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.api.client.util.EnvReader;
import static com.amazonaws.services.lambda.runtime.api.client.ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_FUNCTION_MEMORY_SIZE;
import static com.amazonaws.services.lambda.runtime.api.client.ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_FUNCTION_NAME;
import static com.amazonaws.services.lambda.runtime.api.client.ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_FUNCTION_VERSION;
import static com.amazonaws.services.lambda.runtime.api.client.ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_LOG_FORMAT;
import static com.amazonaws.services.lambda.runtime.api.client.ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_LOG_GROUP_NAME;
import static com.amazonaws.services.lambda.runtime.api.client.ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_LOG_LEVEL;
import static com.amazonaws.services.lambda.runtime.api.client.ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_LOG_STREAM_NAME;
import static com.amazonaws.services.lambda.runtime.api.client.ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_RUNTIME_API;
import static java.lang.Integer.parseInt;

public class LambdaEnvironment {
    public static final EnvReader ENV_READER = new EnvReader();
    public static final int MEMORY_LIMIT = parseInt(ENV_READER.getEnvOrDefault(AWS_LAMBDA_FUNCTION_MEMORY_SIZE, "128"));
    public static final String LOG_GROUP_NAME = ENV_READER.getEnv(AWS_LAMBDA_LOG_GROUP_NAME);
    public static final String LOG_STREAM_NAME = ENV_READER.getEnv(AWS_LAMBDA_LOG_STREAM_NAME);
    public static final String LAMBDA_LOG_LEVEL = ENV_READER.getEnvOrDefault(AWS_LAMBDA_LOG_LEVEL, "UNDEFINED");
    public static final String LAMBDA_LOG_FORMAT = ENV_READER.getEnvOrDefault(AWS_LAMBDA_LOG_FORMAT, "TEXT");
    public static final String FUNCTION_NAME = ENV_READER.getEnv(AWS_LAMBDA_FUNCTION_NAME);
    public static final String FUNCTION_VERSION = ENV_READER.getEnv(AWS_LAMBDA_FUNCTION_VERSION);
    public static final String RUNTIME_API = ENV_READER.getEnv(AWS_LAMBDA_RUNTIME_API);
}
