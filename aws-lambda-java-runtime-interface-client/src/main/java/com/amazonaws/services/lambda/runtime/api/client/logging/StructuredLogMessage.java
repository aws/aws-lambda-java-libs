/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;

class StructuredLogMessage {
    public String timestamp;
    public String message;
    public LogLevel level;
    public String AWSRequestId;
}
