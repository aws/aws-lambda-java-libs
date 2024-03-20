/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.converters;

import com.amazonaws.services.lambda.runtime.api.client.UserFault;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.LambdaError;

public class LambdaErrorConverter {
    private LambdaErrorConverter() {
    }

    public static LambdaError fromUserFault(UserFault userFault) {
        // Not setting stacktrace for compatibility with legacy/native runtime
        return new LambdaError(userFault.msg, userFault.exception, null);
    }

    public static LambdaError fromThrowable(Throwable throwable) {
        String errorMessage = throwable.getLocalizedMessage() == null ?
                throwable.getClass().getName() : throwable.getLocalizedMessage();
        String errorType = throwable.getClass().getName();

        StackTraceElement[] trace = throwable.getStackTrace();
        String[] stackTrace = new String[trace.length];
        for (int i = 0; i < trace.length; i++) {
            stackTrace[i] = trace[i].toString();
        }
        return new LambdaError(errorMessage, errorType, stackTrace);
    }
}
