/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * interface with Jackson annotations for CodeCommitEvent
 */
public abstract class CodeCommitEventMixin {

    // needed because Jackson expects "records" instead of "Records"
    @JsonProperty("Records") abstract List<?> getRecords();
    @JsonProperty("Records") abstract void setRecords(List<?> records);

    public abstract class RecordMixin {

        // needed because Jackson expects "codeCommit" instead of "codeCommit"
        @JsonProperty("codecommit") abstract Object getCodeCommit();
        @JsonProperty("codecommit") abstract void setCodeCommit(Object codeCommit);
        // needed because Jackson expects "eventSourceArn" instead of "eventSourceARN"
        @JsonProperty("eventSourceARN") abstract String getEventSourceArn();
        @JsonProperty("eventSourceARN") abstract void setEventSourceArn(String eventSourceArn);
        // needed because Jackson expects "userIdentityArn" instead of "UserIdentityArn"
        @JsonProperty("userIdentityARN") abstract String getUserIdentityArn();
        @JsonProperty("userIdentityARN") abstract void setUserIdentityArn(String userIdentityArn);

    }

}
