package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class SESEventMixin {

    // needed because Jackson expects "records" instead of "Records"
    @JsonProperty("Records") abstract List<?> getRecords();
    @JsonProperty("Records") abstract void setRecords(List<?> records);
}
