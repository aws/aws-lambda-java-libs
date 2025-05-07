/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

/**
 * Lambda runtimes set several environment variables during initialization.
 * Most of the environment variables provide information about the function or runtime.
 * The keys for these environment variables are reserved and cannot be set in your function configuration.
 *
 * @see <a href="https://docs.aws.amazon.com/lambda/latest/dg/configuration-envvars.html#configuration-envvars-runtime">Using AWS Lambda Environment Variables</a>
 * <p>
 * NOTICE: This class is forked from io.micronaut.function.aws.runtime.ReservedRuntimeEnvironments found at https://github.com/micronaut-projects/micronaut-aws
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
     * The logging level set for the function.
     */
    String AWS_LAMBDA_LOG_LEVEL = "AWS_LAMBDA_LOG_LEVEL";

    /**
     * The logging format set for the function.
     */
    String AWS_LAMBDA_LOG_FORMAT = "AWS_LAMBDA_LOG_FORMAT";

    /**
     * Access key id obtained from the function's execution role.
     */
    String AWS_ACCESS_KEY_ID = "AWS_ACCESS_KEY_ID";

    /**
     * secret access key obtained from the function's execution role.
     */
    String AWS_SECRET_ACCESS_KEY = "AWS_SECRET_ACCESS_KEY";

    /**
     * The access keys obtained from the function's execution role.
     */
    String AWS_SESSION_TOKEN = "AWS_SESSION_TOKEN";

    /**
     * (Custom runtime) The host and port of the runtime API.
     */
    String AWS_LAMBDA_RUNTIME_API = "AWS_LAMBDA_RUNTIME_API";


    /**
     * Initialization type
     */
    String AWS_LAMBDA_INITIALIZATION_TYPE = "AWS_LAMBDA_INITIALIZATION_TYPE";

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

    /**
     * Boolean determining whether the RIC should run multi-concurrent runtime loops. Default value is "false".
     * In case it is set to "true", AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY can be used to set the required number of concurrent runtime loops.
     */
    String AWS_LAMBDA_ENABLE_MULTICONCURRENT_RIC = "AWS_LAMBDA_ENABLE_MULTICONCURRENT_RIC";

    /*
     * Used to set the required number of concurrent runtime loops, 
     * Only used if AWS_LAMBDA_ENABLE_MULTICONCURRENT_RIC is set to "true", 
     * If AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY is not set, the default number of concurrent runtime loops is the number of cores.
     */
    String AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY = "AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY";
}
