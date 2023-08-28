/* Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.api.client.api.LambdaContext;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;

public interface LogFormatter {
    String format(String message, LogLevel logLevel);

    default void setLambdaContext(LambdaContext context) {
    };
}
