/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Mixin for CloudFrontEvent
 */
public abstract class CloudFrontEventMixin {

    // needed because jackson expects "records" instead of "Records"
    @JsonProperty("Records") abstract List<?> getRecords();
    @JsonProperty("Records") abstract void setRecords(List<?> records);

}
