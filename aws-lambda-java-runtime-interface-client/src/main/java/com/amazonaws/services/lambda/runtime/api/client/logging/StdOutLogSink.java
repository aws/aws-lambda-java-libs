/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.logging.LogFormat;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import java.io.IOException;

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
