/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;

public class LogFiltering {
    private final LogLevel minimumLogLevel;

    public LogFiltering(LogLevel minimumLogLevel) {
        this.minimumLogLevel = minimumLogLevel;
    }

    boolean isEnabled(LogLevel logLevel) {
        return (logLevel == LogLevel.UNDEFINED || logLevel.ordinal() >= minimumLogLevel.ordinal());
    }
}
