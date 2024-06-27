/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.logging.LogFormat;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import java.io.Closeable;

public interface LogSink extends Closeable {

    void log(byte[] message);

    void log(LogLevel logLevel, LogFormat logFormat, byte[] message);

}
