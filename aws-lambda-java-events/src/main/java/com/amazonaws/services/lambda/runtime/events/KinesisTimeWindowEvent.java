/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

import com.amazonaws.services.lambda.runtime.events.models.TimeWindow;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents an Amazon Kinesis event when using <a href="https://docs.aws.amazon.com/lambda/latest/dg/with-kinesis.html#services-kinesis-windows">time windows</a>.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KinesisTimeWindowEvent extends KinesisEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = 8926430039233062266L;

    /**
     * Time window for the records in the event.
     */
    private TimeWindow window;

    /**
     * State being built up to this invoke in the time window.
     */
    private Map<String, String> state;

    /**
     * Shard id of the records
     */
    private String shardId;

    /**
     * Kinesis stream or consumer arn.
     */
    private String eventSourceArn;

    /**
     * Set to true for the last invoke of the time window. Subsequent invoke will start a new time window along with a fresh state.
     */
    private Boolean isFinalInvokeForWindow;

    /**
     * Set to true if window is terminated prematurely. Subsequent invoke will continue the same window with a fresh state.
     */
    private Boolean isWindowTerminatedEarly;

    @Builder(setterPrefix = "with")
    public KinesisTimeWindowEvent(
            final List<KinesisEventRecord> records,
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
