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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Represents an Amazon SNS event.
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class SNSEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -727529735144605167L;

    @JsonProperty("Records")
    private List<SNSRecord> records;

    /**
     * Represents an SNS message attribute
     *
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageAttribute implements Serializable, Cloneable {

        private static final long serialVersionUID = -5656179310535967619L;

        @JsonProperty("Type")
        private String type;

        @JsonProperty("Value")
        private String value;

        @Override
        public MessageAttribute clone() {
            try {
                return (MessageAttribute) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    /**
     * Represents an SNS message
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SNS implements Serializable, Cloneable {

        private static final long serialVersionUID = -7038894618736475592L;

        @JsonProperty("MessageAttributes")
        private Map<String, MessageAttribute> messageAttributes;

        @JsonProperty("SigningCertUrl")
        private String signingCertUrl;

        @JsonProperty("MessageId")
        private String messageId;

        @JsonProperty("Message")
        private String message;

        @JsonProperty("Subject")
        private String subject;

        @JsonProperty("UnsubscribeUrl")
        private String unsubscribeUrl;

        @JsonProperty("Type")
        private String type;

        @JsonProperty("SignatureVersion")
        private String signatureVersion;

        @JsonProperty("Signature")
        private String signature;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
        @JsonProperty("Timestamp")
        private Instant timestamp;

        @JsonProperty("TopicArn")
        private String topicArn;

        @Override
        public SNS clone() {
            try {
                return (SNS) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    /**
     * Represents an SNS message record. SNS message records are used to send
     * SNS messages to Lambda Functions.
     *
     */
    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SNSRecord implements Serializable, Cloneable {

        private static final long serialVersionUID = -209065548155161859L;

        @JsonProperty("Sns")
        private SNS sns;

        @JsonProperty("EventVersion")
        private String eventVersion;

        @JsonProperty("EventSource")
        private String eventSource;

        @JsonProperty("EventSubscriptionArn")
        private String eventSubscriptionArn;

        @Override
        public SNSRecord clone() {
            try {
                return (SNSRecord) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    @Override
    public SNSEvent clone() {
        try {
            return (SNSEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
}