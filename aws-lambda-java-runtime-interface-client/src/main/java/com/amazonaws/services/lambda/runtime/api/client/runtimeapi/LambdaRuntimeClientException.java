/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

public class LambdaRuntimeClientException extends RuntimeException {
    public LambdaRuntimeClientException(String message, int responseCode) {
        super(message + " Response code: '" + responseCode + "'.");
    }
}
