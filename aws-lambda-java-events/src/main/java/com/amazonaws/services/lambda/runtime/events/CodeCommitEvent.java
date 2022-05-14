/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * References a CodeCommit event
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class CodeCommitEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = 2404735479795009282L;

    @JsonProperty("Records")
    private List<Record> records;

    /**
     * represents a Reference object in a CodeCommit object
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reference implements Serializable, Cloneable {

        private static final long serialVersionUID = 9166524005926768827L;

        private String commit;

        private String ref;

        private Boolean created;

        @Override
        public Reference clone() {
            try {
                return (Reference) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    /**
     * Represents a CodeCommit object in a record
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CodeCommit implements Serializable, Cloneable {

        private static final long serialVersionUID = 2594306162311794147L;

        private List<Reference> references;

        @Override
        public CodeCommit clone() {
            try {
                return (CodeCommit) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    /**
     * represents a CodeCommit record
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record implements Serializable, Cloneable {

        private static final long serialVersionUID = 1116409777237432728L;

        private String eventId;

        private String eventVersion;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
        private Instant eventTime;

        private String eventTriggerName;

        private Integer eventPartNumber;

        @JsonProperty("codecommit")
        private CodeCommit codeCommit;

        private String eventName;

        private String eventTriggerConfigId;

        @JsonProperty("eventSourceARN")
        private String eventSourceArn;

        @JsonProperty("userIdentityARN")
        private String userIdentityArn;

        private String eventSource;

        private String awsRegion;

        private String customData;

        private Integer eventTotalParts;

        @Override
        public Record clone() {
            try {
                return (Record) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    @Override
    public CodeCommitEvent clone() {
        try {
            return (CodeCommitEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
}
