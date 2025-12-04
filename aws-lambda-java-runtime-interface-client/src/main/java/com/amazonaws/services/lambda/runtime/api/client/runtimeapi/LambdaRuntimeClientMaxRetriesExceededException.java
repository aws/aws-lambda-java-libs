/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

public class LambdaRuntimeClientMaxRetriesExceededException extends LambdaRuntimeClientException {
    // 429 is possible; however, that is more appropriate when a server is responding to a spamming client that it wants to rate limit. 
    // In Our case, however, the RIC is a client that is not able to get a response from an upstream server, so 500 is more appropriate.
    public LambdaRuntimeClientMaxRetriesExceededException(String operationName) {
        super("Maximum Number of retries have been exceed" + (operationName.equals(null)
                ? String.format(" for the %s operation.", operationName)
                : "."), 500);
    }
}
