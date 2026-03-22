/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import com.amazonaws.services.lambda.runtime.api.client.logging.LambdaContextLogger;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.InvocationRequest;
import java.io.IOException;

/**
 * Java interface for 
 * <a href="https://docs.aws.amazon.com/lambda/latest/dg/runtimes-api.html">Lambda Runtime API</a>
 */
public interface LambdaRuntimeApiClient {

    /**
     * Report Init error
     * @param error error to report
     */
    void reportInitError(LambdaError error) throws IOException;

    /**
     * Get next invocation
     */
    InvocationRequest nextInvocation() throws IOException;

    /**
     * Get next invocation with exponential backoff
     */
    InvocationRequest nextInvocationWithExponentialBackoff(LambdaContextLogger lambdaLogger) throws Exception;

    /**
     * Report invocation success
     * @param requestId request id
     * @param response byte array representing response
     */
    void reportInvocationSuccess(String requestId, byte[] response) throws IOException;

    /**
     * Report invocation error
     * @param requestId request id
     * @param error error to report
     */
    void reportInvocationError(String requestId, LambdaError error) throws IOException;

    /**
     * SnapStart endpoint to report that beforeCheckoint hooks were executed
     */
    void restoreNext() throws IOException;

    /**
     * SnapStart endpoint to report errors during afterRestore hooks execution
     * @param error error to report
     */
    void reportRestoreError(LambdaError error) throws IOException;
}
