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

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Represents an Amazon SQS event.
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class SQSEvent implements Serializable {

    private static final long serialVersionUID = -5663700178408796225L;

    @JsonProperty("Records")
    private List<SQSMessage> records;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageAttribute implements Serializable {

        private static final long serialVersionUID = -1602746537669100978L;

        /**
         * The value of message attribute of type String or type Number
         */
        private String stringValue;

        /**
         * The value of message attribute of type Binary
         */
        private ByteBuffer binaryValue;

        /**
         * The list of String values of message attribute
         */
        private List<String> stringListValues;

        /**
         * The list of Binary values of message attribute
         */
        private List<ByteBuffer> binaryListValues;

        /**
         * The dataType of message attribute
         */
        private String dataType;

    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SQSMessage implements Serializable {

        private static final long serialVersionUID = -2300083946005987098L;

        private String messageId;

        private String receiptHandle;

        private String body;

        private String md5OfBody;

        private String md5OfMessageAttributes;

        @JsonProperty("eventSourceARN")
        private String eventSourceArn;

        private String eventSource;

        private String awsRegion;

        private Map<String, String> attributes;

        private Map<String, MessageAttribute> messageAttributes;

    }
}
