/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

public final class UserMethods {
    public final Runnable initHandler;
    public final LambdaRequestHandler requestHandler;

    public UserMethods(Runnable initHandler, LambdaRequestHandler requestHandler) {
        this.initHandler = initHandler;
        this.requestHandler = requestHandler;
    }
}
