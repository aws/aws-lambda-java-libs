/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class SQSEventMixin {

    // Needed because Jackson expects "records" instead of "Records"
    @JsonProperty("Records") abstract List<?> getRecords();
    @JsonProperty("Records") abstract void setRecords(List<?> records);

    public abstract class SQSMessageMixin {

        // needed because Jackson expects "eventSourceArn" instead of "eventSourceARN"
        @JsonProperty("eventSourceARN") abstract String getEventSourceArn();
        @JsonProperty("eventSourceARN") abstract void setEventSourceArn(String eventSourceArn);
    }

}
