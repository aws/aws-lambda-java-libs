/* Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.logging;


public enum LogLevel {
    // UNDEFINED log level is used when the legacy LambdaLogger::log(String) function is called
    // where the loglevel is not defined. In this case we're not filtering the message in the runtime
    UNDEFINED,
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL;

    public static LogLevel fromString(String logLevel) {
        try {
            return LogLevel.valueOf(logLevel.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid log level: '" + logLevel + "' expected one of [TRACE, DEBUG, INFO, WARN, ERROR, FATAL]");
        }
    }
}
