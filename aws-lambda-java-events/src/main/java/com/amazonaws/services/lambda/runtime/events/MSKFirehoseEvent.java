package com.amazonaws.services.lambda.runtime.events;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by vermshas on 6/28/24.
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor

public class MSKFirehoseEvent {

    private String invocationId;

    private String deliveryStreamArn;

    private String sourceMSKArn;

    private String region;

    private List<Record> records;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record {

        private ByteBuffer kafkaRecordValue;

        private String recordId;

        private Long approximateArrivalEpoch;

        private Long approximateArrivalTimestamp;

        private Map<String, String> mskRecordMetadata;

    }
}