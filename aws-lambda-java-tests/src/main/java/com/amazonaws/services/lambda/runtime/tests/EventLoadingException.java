/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

public class EventLoadingException extends RuntimeException {

    private static final long serialVersionUID = 5766526909472206270L;

    public EventLoadingException() {
    }

    public EventLoadingException(String message) {
        super(message);
    }

    public EventLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
