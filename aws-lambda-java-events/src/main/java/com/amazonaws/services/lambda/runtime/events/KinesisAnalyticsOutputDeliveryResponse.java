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
import java.util.List;

/**
 * Response model for Kinesis Analytics Lambda Output delivery.
 */
public class KinesisAnalyticsOutputDeliveryResponse implements Serializable {

    public enum Result {

        /**
         * Indicates that record has been delivered successfully.
         */
        Ok,

        /**
         * Indicates that the delivery of the record failed.
         */
        DeliveryFailed
    }

    private static final long serialVersionUID = 4530412727972559896L;
    public List<Record> records;

    public KinesisAnalyticsOutputDeliveryResponse() {
        super();
    }

    public KinesisAnalyticsOutputDeliveryResponse(List<Record> records) {
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
        private static final long serialVersionUID = -4034554254120467176L;
        public String recordId;
        public Result result;

        public Record() {
            super();
        }

        public Record(String recordId, Result result) {
            super();
            this.recordId = recordId;
            this.result = result;
        }

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
    }
}
