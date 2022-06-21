/*
 *  Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class KinesisTimeWindowEventMixin extends KinesisEventMixin {

    // needed because Jackson expects "eventSourceArn" instead of "eventSourceARN"
    @JsonProperty("eventSourceARN") abstract String getEventSourceArn();
    @JsonProperty("eventSourceARN") abstract void setEventSourceArn(String eventSourceArn);
}
