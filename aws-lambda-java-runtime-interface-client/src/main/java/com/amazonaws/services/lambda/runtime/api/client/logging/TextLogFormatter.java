/* Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.api.client.api.LambdaContext;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;

import java.util.HashMap;

public class TextLogFormatter implements LogFormatter {
    private static final HashMap<LogLevel, String> logLevelMapper = new HashMap<LogLevel, String>() {{
        for (LogLevel logLevel: LogLevel.values()) {
            put(logLevel, "[" + logLevel.toString() + "] ");
        }
    }};

    @Override
    public String format(String message, LogLevel logLevel) {
        if (logLevel == LogLevel.UNDEFINED) {
            return message;
        }

        return new StringBuilder()
            .append(logLevelMapper.get(logLevel))
            .append(message)
            .toString();
    }
}
