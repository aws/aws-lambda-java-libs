/* Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

public class TooManyServiceProvidersFoundException extends RuntimeException {
    public TooManyServiceProvidersFoundException() {
    }

    public TooManyServiceProvidersFoundException(String errorMessage) {
        super(errorMessage);
    }

    public TooManyServiceProvidersFoundException(Throwable cause) {
        super(cause);
    }

    public TooManyServiceProvidersFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}