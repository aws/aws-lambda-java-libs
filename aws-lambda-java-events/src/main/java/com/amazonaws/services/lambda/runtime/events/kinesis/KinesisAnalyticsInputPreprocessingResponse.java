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

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Response model for Kinesis Analytics Preprocessing Lambda function.
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
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

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record implements Serializable {

        private static final long serialVersionUID = -1583558686451236985L;

        public String recordId;

        public Result result;

        public ByteBuffer data;

    }
}
