/* Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.amazonaws.services.lambda.runtime.api.client.api.LambdaContext;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.logging.LogFormat;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;

/**
 * Provides default implementation of the convenience logger functions.
 * When extending AbstractLambdaLogger, only one function has to be overridden:
 * void logMessage(byte[] message, LogLevel logLevel);
 */
public abstract class AbstractLambdaLogger implements LambdaLogger {
    private final LogFiltering logFiltering;
    private final LogFormatter logFormatter;
    protected final LogFormat logFormat;

    public AbstractLambdaLogger(LogLevel logLevel, LogFormat logFormat) {
        this.logFiltering = new LogFiltering(logLevel);

        this.logFormat = logFormat;
        if (logFormat == LogFormat.JSON) {
            logFormatter = new JsonLogFormatter();
        } else {
            logFormatter = new TextLogFormatter();
        }
    }

    protected abstract void logMessage(byte[] message, LogLevel logLevel);

    protected void logMessage(String message, LogLevel logLevel) {
        byte[] messageBytes = message == null ? null : message.getBytes(UTF_8);
        logMessage(messageBytes, logLevel);
    }

    @Override
    public void log(String message, LogLevel logLevel) {
        if (logFiltering.isEnabled(logLevel)) {
            this.logMessage(logFormatter.format(message, logLevel), logLevel);
        }
    }

    @Override
    public void log(byte[] message, LogLevel logLevel) {
        if (logFiltering.isEnabled(logLevel)) {
            // there is no formatting for byte[] messages
            this.logMessage(message, logLevel);
        }
    }

    @Override
    public void log(String message) {
        this.log(message, LogLevel.UNDEFINED);
    }

    @Override
    public void log(byte[] message) {
        this.log(message, LogLevel.UNDEFINED);
    }

    public void setLambdaContext(LambdaContext lambdaContext) {
        this.logFormatter.setLambdaContext(lambdaContext);
    }
}
