/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import java.io.Closeable;

public interface LogSink extends Closeable {

    void log(byte[] message);

}
