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

package com.amazonaws.services.lambda.runtime.events.dynamodb;

import com.amazonaws.services.lambda.runtime.events.TimeWindow;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DynamodbTimeWindowEvent extends DynamodbEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -5449871161108629510L;

    private TimeWindow window;
    private Map<String, String> state;
    private String shardId;
    @JsonProperty("eventSourceARN")
    private String eventSourceArn;
    private Boolean isFinalInvokeForWindow;
    private Boolean isWindowTerminatedEarly;

    @Builder(setterPrefix = "with", builderMethodName = "dynamoDbTimeWindowEventBuilder")
    public DynamodbTimeWindowEvent(
            final List<DynamodbStreamRecord> records,
            final TimeWindow window,
            final Map<String, String> state,
            final String shardId,
            final String eventSourceArn,
            final Boolean isFinalInvokeForWindow,
            final Boolean isWindowTerminatedEarly) {
        this.setRecords(records);
        this.window = window;
        this.state = state;
        this.shardId = shardId;
        this.eventSourceArn = eventSourceArn;
        this.isFinalInvokeForWindow = isFinalInvokeForWindow;
        this.isWindowTerminatedEarly = isWindowTerminatedEarly;
    }
}