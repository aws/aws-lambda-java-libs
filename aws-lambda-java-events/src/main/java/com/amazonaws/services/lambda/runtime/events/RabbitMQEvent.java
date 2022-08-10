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

import java.util.List;
import java.util.Map;

/**
 * Represents a Rabbit MQ event sent to Lambda
 * <a href="https://docs.aws.amazon.com/lambda/latest/dg/with-mq.html">Onboarding Amazon MQ as event source to Lambda</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class RabbitMQEvent {

    private String eventSource;
    private String eventSourceArn;
    private Map<String, List<RabbitMessage>> rmqMessagesByQueue;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class RabbitMessage {
        private BasicProperties basicProperties;
        private boolean redelivered;
        private String data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class BasicProperties {

        private String contentType;
        private String contentEncoding;
        private Map<String, Object> headers;
        private int deliveryMode;
        private int priority;
        private String correlationId;
        private String replyTo;
        private String expiration;
        private String messageId;
        private String timestamp;
        private String type;
        private String userId;
        private String appId;
        private String clusterId;
        private int bodySize;
    }
}
