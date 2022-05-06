package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

/**
 * Copyright (c) 2022 Amazon. All rights reserved.
 */
public class UnknownPlatformException extends RuntimeException {

    public UnknownPlatformException(String message) {
        super(message);
    }
}
