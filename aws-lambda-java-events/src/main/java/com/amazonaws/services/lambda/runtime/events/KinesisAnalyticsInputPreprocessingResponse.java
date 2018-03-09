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
 * Response model for Kinesis Analytics Preprocessing Lambda function.
 */
public class KinesisAnalyticsInputPreprocessingResponse implements Serializable {

    public enum Result {

        /**
         * Indicates that processing of this item succeeded.
         */
        Ok,

        /**
         * Indicate that the processing of this item failed
         */
        ProcessingFailed,

        /**
         * Indicates that this item should be silently dropped
         */
        Dropped
    }

    private static final long serialVersionUID = -4651154757825854471L;
    public List<Record> records;

    public KinesisAnalyticsInputPreprocessingResponse() {
        super();
    }

    public KinesisAnalyticsInputPreprocessingResponse(List<Record> records) {
        super();
        this.records = records;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public static class Record implements Serializable {
        private static final long serialVersionUID = -1583558686451236985L;
        public String recordId;
        public Result result;

        public Record() {
            super();
        }

        public Record(String recordId, Result result, ByteBuffer data) {
            super();
            this.recordId = recordId;
            this.result = result;
            this.data = data;
        }

        public ByteBuffer data;

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        public ByteBuffer getData() {
            return data;
        }

        public void setData(ByteBuffer data) {
            this.data = data;
        }
    }
}
