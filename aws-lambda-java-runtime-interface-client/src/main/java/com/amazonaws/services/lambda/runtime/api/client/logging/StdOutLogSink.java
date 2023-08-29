/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import java.io.IOException;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.amazonaws.services.lambda.runtime.logging.LogFormat;

public class StdOutLogSink implements LogSink {
    @Override
    public void log(byte[] message) {
        log(LogLevel.UNDEFINED, LogFormat.TEXT, message);
    }

    public void log(LogLevel logLevel, LogFormat logFormat, byte[] message) {
        try {
            System.out.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
    }
}
