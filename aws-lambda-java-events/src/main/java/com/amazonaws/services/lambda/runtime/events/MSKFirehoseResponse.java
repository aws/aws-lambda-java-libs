package com.amazonaws.services.lambda.runtime.events;

import java.nio.ByteBuffer;
import java.util.List;

import lombok.*;

/**
 * Response model for Amazon Data Firehose Lambda transformation with MSK as a source.
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor

public class MSKFirehoseResponse {

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
    public List<Record> records;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor

    public static class Record {
        public String recordId;
        public Result result;
        public ByteBuffer kafkaRecordValue;

        public Record(String recordId, Result result, ByteBuffer kafkaRecordValue) {
            super();
            this.recordId = recordId;
            this.result = result;
            this.kafkaRecordValue = kafkaRecordValue;
        }
    }
}
