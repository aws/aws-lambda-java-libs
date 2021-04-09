/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amazonaws.services.lambda.runtime.api.client;

/**
 * Lambda runtimes set several environment variables during initialization.
 * Most of the environment variables provide information about the function or runtime.
 * The keys for these environment variables are reserved and cannot be set in your function configuration.
 * @see <a href="https://docs.aws.amazon.com/lambda/latest/dg/configuration-envvars.html#configuration-envvars-runtime">Using AWS Lambda Environment Variables</a>
 *
 * NOTICE: This class is forked from io.micronaut.function.aws.runtime.ReservedRuntimeEnvironments found at https://github.com/micronaut-projects/micronaut-aws
 *
 */
public interface ReservedRuntimeEnvironmentVariables {

    /**
     * The handler location configured on the function.
     */
    String HANDLER = "_HANDLER";

    /**
     * The AWS Region where the Lambda function is executed.
     */
    String AWS_REGION = "AWS_REGION";

    /**
     * The runtime identifier, prefixed by AWS_Lambda_—for example, AWS_Lambda_java8.
     */
    String AWS_EXECUTION_ENV = "AWS_EXECUTION_ENV";

    /**
     * The name of the function.
     */
    String AWS_LAMBDA_FUNCTION_NAME = "AWS_LAMBDA_FUNCTION_NAME";

    /**
     * The amount of memory available to the function in MB.
     */
    String AWS_LAMBDA_FUNCTION_MEMORY_SIZE = "AWS_LAMBDA_FUNCTION_MEMORY_SIZE";

    /**
     * The version of the function being executed.
     */
    String AWS_LAMBDA_FUNCTION_VERSION = "AWS_LAMBDA_FUNCTION_VERSION";

    /**
     * The name of the Amazon CloudWatch Logs group for the function.
     */
    String AWS_LAMBDA_LOG_GROUP_NAME = "AWS_LAMBDA_LOG_GROUP_NAME";

    /**
     * The name of the Amazon CloudWatch stream for the function.
     */
    String AWS_LAMBDA_LOG_STREAM_NAME = "AWS_LAMBDA_LOG_STREAM_NAME";

    /**
     * Access key id obtained from the function's execution role.
     */
    String AWS_ACCESS_KEY_ID = "AWS_ACCESS_KEY_ID";

    /**
     * secret access key obtained from the function's execution role.
     */
    String AWS_SECRET_ACCESS_KEY = "AWS_SECRET_ACCESS_KEY";

    /**
     *
     * The access keys obtained from the function's execution role.
     */
    String AWS_SESSION_TOKEN = "AWS_SESSION_TOKEN";

    /**
     * (Custom runtime) The host and port of the runtime API.
     */
    String AWS_LAMBDA_RUNTIME_API = "AWS_LAMBDA_RUNTIME_API";

    /**
     * The path to your Lambda function code.
     */
    String LAMBDA_TASK_ROOT = "LAMBDA_TASK_ROOT";

    /**
     * The path to runtime libraries.
     */
    String LAMBDA_RUNTIME_DIR = "LAMBDA_RUNTIME_DIR";

    /**
     * The environment's time zone (UTC). The execution environment uses NTP to synchronize the system clock.
     */
    String TZ = "TZ";
}
