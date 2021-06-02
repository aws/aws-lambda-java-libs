/*
 * Copyright 2015-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

/**
* Represents an Active MQ event sent to Lambda
* <a href="https://docs.aws.amazon.com/lambda/latest/dg/with-mq.html">Onboarding Amazon MQ as event source to Lambda</a>
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class ActiveMQEvent {
    private String eventSource;
    private String eventSourceArn;
    private List<ActiveMQMessage> messages;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class ActiveMQMessage {
        private String messageID;
        private String messageType;
        private long timestamp;
        private int deliveryMode;
        private String correlationID;
        private String replyTo;
        private Destination destination;
        private boolean redelivered;
        private String type;
        private long expiration;
        private int priority;
        /** Message data sent to Active MQ broker encooded in Base 64 **/
        private String data;
        private long brokerInTime;
        private long brokerOutTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class Destination {
        /** Queue Name **/
        private String physicalName;
    }
}
