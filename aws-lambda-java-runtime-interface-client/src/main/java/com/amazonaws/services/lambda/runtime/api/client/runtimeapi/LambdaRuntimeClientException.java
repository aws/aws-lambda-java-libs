package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

/**
 * Copyright (c) 2019 Amazon. All rights reserved.
 */
public class LambdaRuntimeClientException extends RuntimeException {
    public LambdaRuntimeClientException(String message, int responseCode) {
        super(message + "Response code: '" + responseCode + "'.");
    }

}
