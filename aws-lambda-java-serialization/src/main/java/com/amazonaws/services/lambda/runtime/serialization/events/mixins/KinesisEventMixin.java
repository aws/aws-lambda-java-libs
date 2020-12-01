/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class KinesisEventMixin {

    // needed because Jackson expects "records" instead of "Records"
    @JsonProperty("Records") abstract List<?> getRecords();
    @JsonProperty("Records") abstract void setRecords(List<?> records);

    public abstract class RecordMixin {

        // needed because Jackson cannot distinguish between Enum encryptionType and String encryptionType
        @JsonProperty("encryptionType") abstract String getEncryptionType();
        @JsonProperty("encryptionType") abstract void setEncryptionType(String encryptionType);

    }

}
