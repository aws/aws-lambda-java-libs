/* Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;

class StructuredLogMessage {
    public String timestamp;
    public String message;
    public LogLevel level;
    public String AWSRequestId;
}
