package com.amazonaws.services.lambda.runtime.events;

import java.nio.ByteBuffer;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for Amazon Data Firehose Lambda transformation with MSK as a source.
 * [+] Amazon Data Firehose Data Transformation - Data Transformation and Status Model - <a href="https://docs.aws.amazon.com/firehose/latest/dev/data-transformation.html#data-transformation-status-model">...</a>
 * OK : Indicates that processing of this item succeeded.
 * ProcessingFailed : Indicate that the processing of this item failed.
 * Dropped : Indicates that this item should be silently dropped
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
    @NoArgsConstructor
    @Builder(setterPrefix = "with")
    @AllArgsConstructor

    public static class Record {
        public String recordId;
        public Result result;
        public ByteBuffer kafkaRecordValue;

    }
}
