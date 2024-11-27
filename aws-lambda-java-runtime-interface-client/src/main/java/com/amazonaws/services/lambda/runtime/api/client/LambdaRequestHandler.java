/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.InvocationRequest;
import java.io.ByteArrayOutputStream;

public interface LambdaRequestHandler {
    ByteArrayOutputStream call(InvocationRequest request) throws Error, Exception;

    static LambdaRequestHandler initErrorHandler(final Throwable e, String className) {
        return new UserFaultHandler(UserFault.makeInitErrorUserFault(e, className));
    }

    static LambdaRequestHandler classNotFound(final Throwable e, String className) {
        return new UserFaultHandler(UserFault.makeClassNotFoundUserFault(e, className));
    }

    class UserFaultHandler implements LambdaRequestHandler {
        public final UserFault fault;

        public UserFaultHandler(UserFault fault) {
            this.fault = fault;
        }

        public ByteArrayOutputStream call(InvocationRequest request) {
            throw fault;
        }
    }
}
