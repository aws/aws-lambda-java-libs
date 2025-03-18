/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.logging.LogFormat;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.Closeable;
import java.io.IOException;

public class LambdaContextLogger extends AbstractLambdaLogger implements Closeable{
    // If a null string is passed in, replace it with "null",
    // replicating the behavior of System.out.println(null);
    private static final byte[] NULL_BYTES_VALUE = "null".getBytes(UTF_8);

    private final transient LogSink sink;

    public LambdaContextLogger(LogSink sink, LogLevel logLevel, LogFormat logFormat) {
        super(logLevel, logFormat);
        this.sink = sink;
    }

    @Override
    protected void logMessage(byte[] message, LogLevel logLevel) {
        if (message == null) {
            sink.log(logLevel, this.logFormat, NULL_BYTES_VALUE);
        } else {
            sink.log(logLevel, this.logFormat, message);
        }
    }

    @Override
    public void close() throws IOException {
        sink.close();
        
    }
}
