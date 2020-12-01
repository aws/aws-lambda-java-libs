/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class SNSEventMixin {

    // needed because Jackson expects "records" instead of "Records"
    @JsonProperty("Records") abstract List<?> getRecords();
    @JsonProperty("Records") abstract void setRecords(List<?> records);

    public abstract class SNSRecordMixin {

        // needed because Jackson expects "getSns" instead of "getSNS"
        @JsonProperty("Sns") abstract Object getSNS();
        @JsonProperty("Sns") abstract void setSns(Object sns);

    }

}
