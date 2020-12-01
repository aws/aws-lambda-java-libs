/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LambdaContextLogger implements LambdaLogger {
    // If a null string is passed in, replace it with "null",
    // replicating the behavior of System.out.println(null);
    private static final byte[] NULL_BYTES_VALUE = "null".getBytes(UTF_8);

    private final transient LogSink sink;

    public LambdaContextLogger(LogSink sink) {
        this.sink = sink;
    }

    public void log(byte[] message) {
        if (message == null) {
            message = NULL_BYTES_VALUE;
        }
        sink.log(message);
    }

    public void log(String message) {
        if (message == null) {
            this.log(NULL_BYTES_VALUE);
        } else {
            this.log(message.getBytes(UTF_8));
        }
    }
}
