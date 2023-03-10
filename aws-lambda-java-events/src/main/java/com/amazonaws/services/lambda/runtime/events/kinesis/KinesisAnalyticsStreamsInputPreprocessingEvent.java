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

package com.amazonaws.services.lambda.runtime.events.kinesis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Event model for pre-processing Kinesis Streams records through Kinesis
 * Analytics Lambda pre-processing function.
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class KinesisAnalyticsStreamsInputPreprocessingEvent implements Serializable {

    private static final long serialVersionUID = 1770320710876513596L;

    public String invocationId;
    public String applicationArn;
    public String streamArn;
    public List<Record> records;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record implements Serializable {

        private static final long serialVersionUID = -2070268774061223434L;

        public String recordId;
        public KinesisStreamRecordMetadata kinesisStreamRecordMetadata;
        public ByteBuffer data;

        @Data
        @Builder(setterPrefix = "with")
        @NoArgsConstructor
        @AllArgsConstructor
        public static class KinesisStreamRecordMetadata implements Serializable {

            private static final long serialVersionUID = 8831719215562345916L;

            public String sequenceNumber;
            public String partitionKey;
            public String shardId;
            public Long approximateArrivalTimestamp;
        }
    }
}
