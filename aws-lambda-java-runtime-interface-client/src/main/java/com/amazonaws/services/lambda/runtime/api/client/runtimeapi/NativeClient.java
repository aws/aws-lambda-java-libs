/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.InvocationRequest;
import static com.amazonaws.services.lambda.runtime.api.client.runtimeapi.LambdaRuntimeApiClientImpl.USER_AGENT;

/**
 * This module defines the native Runtime Interface Client which is responsible for HTTP
 * interactions with the Runtime API.
 */
class NativeClient {
    static void init(String awsLambdaRuntimeApi) {
        JniHelper.load();
        initializeClient(USER_AGENT.getBytes(), awsLambdaRuntimeApi.getBytes());
    }
    
    static native void initializeClient(byte[] userAgent, byte[] awsLambdaRuntimeApi);

    static native InvocationRequest next();

    static native void postInvocationResponse(byte[] requestId, byte[] response);

}
