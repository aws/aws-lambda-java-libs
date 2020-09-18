/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.InvocationRequest;

import java.io.ByteArrayOutputStream;

public interface LambdaRequestHandler {
    ByteArrayOutputStream call(InvocationRequest request) throws Error, Exception;

    class UserFaultHandler implements LambdaRequestHandler {
        public final UserFault fault;

        public UserFaultHandler(UserFault fault) {
            this.fault = fault;
        }

        public ByteArrayOutputStream call(InvocationRequest request) {
            throw fault;
        }
    }

    static LambdaRequestHandler initErrorHandler(final Throwable e, String className) {
        return new UserFaultHandler(UserFault.makeInitErrorUserFault(e, className));
    }

    static LambdaRequestHandler classNotFound(final Throwable e, String className) {
        return new UserFaultHandler(UserFault.makeClassNotFoundUserFault(e, className));
    }
}
