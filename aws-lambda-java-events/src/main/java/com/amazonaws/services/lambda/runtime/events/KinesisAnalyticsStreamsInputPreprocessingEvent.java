/*
 * Copyright 2012-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Event model for pre-processing Kinesis Streams records through Kinesis
 * Analytics Lambda pre-processing function.
 */
public class KinesisAnalyticsStreamsInputPreprocessingEvent implements Serializable {
    private static final long serialVersionUID = 1770320710876513596L;
    public String invocationId;
    public String applicationArn;
    public String streamArn;
    public List<Record> records;

    public KinesisAnalyticsStreamsInputPreprocessingEvent() {
    }

    public KinesisAnalyticsStreamsInputPreprocessingEvent(String invocationId, String applicationArn, String streamArn,
            List<Record> records) {
        super();
        this.invocationId = invocationId;
        this.applicationArn = applicationArn;
        this.streamArn = streamArn;
        this.records = records;
    }

    public String getInvocationId() {
        return invocationId;
    }

    public void setInvocationId(String invocationId) {
        this.invocationId = invocationId;
    }

    public String getApplicationArn() {
        return applicationArn;
    }

    public void setApplicationArn(String applicationArn) {
        this.applicationArn = applicationArn;
    }

    public String getStreamArn() {
        return streamArn;
    }

    public void setStreamArn(String streamArn) {
        this.streamArn = streamArn;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public static class Record implements Serializable {
        private static final long serialVersionUID = -2070268774061223434L;
        public String recordId;
        public KinesisStreamRecordMetadata kinesisStreamRecordMetadata;
        public ByteBuffer data;

        public Record() {
        }

        public Record(String recordId, KinesisStreamRecordMetadata kinesisStreamRecordMetadata, ByteBuffer data) {
            super();
            this.recordId = recordId;
            this.kinesisStreamRecordMetadata = kinesisStreamRecordMetadata;
            this.data = data;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public ByteBuffer getData() {
            return data;
        }

        public void setData(ByteBuffer data) {
            this.data = data;
        }

        public KinesisStreamRecordMetadata getKinesisStreamRecordMetadata() {
            return kinesisStreamRecordMetadata;
        }

        public void setKinesisStreamRecordMetadata(KinesisStreamRecordMetadata kinesisStreamRecordMetadata) {
            this.kinesisStreamRecordMetadata = kinesisStreamRecordMetadata;
        }

        public static class KinesisStreamRecordMetadata implements Serializable {
            private static final long serialVersionUID = 8831719215562345916L;
            public String sequenceNumber;
            public String partitionKey;
            public String shardId;
            public Long approximateArrivalTimestamp;

            public KinesisStreamRecordMetadata() {
            }

            public KinesisStreamRecordMetadata(String sequenceNumber, String partitionKey, String shardId,
                    Long approximateArrivalTimestamp) {
                super();
                this.sequenceNumber = sequenceNumber;
                this.partitionKey = partitionKey;
                this.shardId = shardId;
                this.approximateArrivalTimestamp = approximateArrivalTimestamp;
            }

            public String getSequenceNumber() {
                return sequenceNumber;
            }

            public void setSequenceNumber(String sequenceNumber) {
                this.sequenceNumber = sequenceNumber;
            }

            public String getPartitionKey() {
                return partitionKey;
            }

            public void setPartitionKey(String partitionKey) {
                this.partitionKey = partitionKey;
            }

            public String getShardId() {
                return shardId;
            }

            public void setShardId(String shardId) {
                this.shardId = shardId;
            }

            public Long getApproximateArrivalTimestamp() {
                return approximateArrivalTimestamp;
            }

            public void setApproximateArrivalTimestamp(Long approximateArrivalTimestamp) {
                this.approximateArrivalTimestamp = approximateArrivalTimestamp;
            }
        }
    }
}