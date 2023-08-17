/* Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.logging;


public enum LogFormat {
    JSON,
    TEXT;

    public static LogFormat fromString(String logFormat) {
        try {
            return LogFormat.valueOf(logFormat.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid log format: '" + logFormat + "' expected one of [JSON, TEXT]");
        }
    }
}
