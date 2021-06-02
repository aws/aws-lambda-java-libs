/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Interface with Jackson annotations for CloudWatchLogsEvent
 */
public abstract class CloudWatchLogsEventMixin {

    // needed because jackson expects "awsLogs" instead of "awslogs"
    @JsonProperty("awslogs") abstract Object getAwsLogs();
    @JsonProperty("awslogs") abstract void setAwsLogs(Object awsLogs);

}
