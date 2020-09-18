/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Jackson annotations for ScheduledEvent
 */
public abstract class ScheduledEventMixin {

    // needed because Jackson expects "detailType" instead of "detail-type"
    @JsonProperty("detail-type") abstract String getDetailType();
    @JsonProperty("detail-type") abstract void setDetailType(String detailType);

}
