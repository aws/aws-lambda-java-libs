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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Response type to return a new state for the time window and to report batch item failures. This should be used along with {@link KinesisTimeWindowEvent} or {@link DynamodbTimeWindowEvent}.
 * https://docs.aws.amazon.com/lambda/latest/dg/with-kinesis.html#services-kinesis-windows
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class TimeWindowEventResponse implements Serializable {
    private static final long serialVersionUID = 2259096191791166028L;

    /**
     * New state after processing a batch of records.
     */
    private Map<String, String> state;

    /**
     * A list of records which failed processing. Returning the first record which failed would retry all remaining records from the batch.
     */
    private List<BatchItemFailure> batchItemFailures;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class BatchItemFailure implements Serializable {
        private static final long serialVersionUID = 5224634072234167773L;

        /**
         * Sequence number of the record which failed processing.
         */
        String itemIdentifier;
    }
}
