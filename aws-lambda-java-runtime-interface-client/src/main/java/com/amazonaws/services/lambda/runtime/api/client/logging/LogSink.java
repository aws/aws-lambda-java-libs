/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import java.io.Closeable;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.amazonaws.services.lambda.runtime.logging.LogFormat;

public interface LogSink extends Closeable {

    void log(byte[] message);

    void log(LogLevel logLevel, LogFormat logFormat, byte[] message);

}
