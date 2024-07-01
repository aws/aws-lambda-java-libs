package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by vermshas on 6/28/24.
 * Event format is below:
 * {
 *     "invocationId": "",
 *     "sourceMSKArn": "",
 *     "deliveryStreamArn": "",
 *     "region": "us-east-1",
 *     "records": [
 *         {
 *             "recordId": "00000000000000000000000000000000000000000000000000000000000000",
 *             "approximateArrivalTimestamp": 1716369573887,
 *             "mskRecordMetadata": {
 *                 "offset": "0",
 *                 "partitionId": "1",
 *                 "approximateArrivalTimestamp": 1716369573887
 *             },
 *             "kafkaRecordValue": ""
 *         }
 *     ]
 * }
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode

public class MskFirehoseEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -2890373471008001695L;

    private String invocationId;

    private String deliveryStreamArn;

    private String region;

    private List<Record> records;

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class Record implements Serializable, Cloneable {

        private static final long serialVersionUID = -7231161900431910379L;

        private ByteBuffer kafkaRecordValue;

        private String recordId;

        private Long approximateArrivalEpoch;

        private Long approximateArrivalTimestamp;

        private Map<String, String> mskRecordMetadata;

        public Record() {}

        public Record withRecordId(String recordId) {
            setRecordId(recordId);
            return this;
        }

        public Record withApproximateArrivalEpoch(Long approximateArrivalEpoch) {
            setApproximateArrivalEpoch(approximateArrivalEpoch);
            return this;
        }

        public Record withApproximateArrivalTimestamp(Long approximateArrivalTimestamp) {
            setApproximateArrivalTimestamp(approximateArrivalTimestamp);
            return this;
        }

        public Record withMskRecordMetadata(Map<String, String> mskRecordMetadata) {
            setMskRecordMetadata(mskRecordMetadata);
            return this;
        }

        @Override
        public Record clone() {
            try {
                return (Record) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }
    public MskFirehoseEvent() {}
    public MskFirehoseEvent withInvocationId(String invocationId) {
        setInvocationId(invocationId);
        return this;
    }
    public MskFirehoseEvent withDeliveryStreamArn(String deliveryStreamArn) {
        setDeliveryStreamArn(deliveryStreamArn);
        return this;
    }
    public MskFirehoseEvent withRegion(String region) {
        setRegion(region);
        return this;
    }
    public MskFirehoseEvent withRecords(List<Record> records) {
        setRecords(records);
        return this;
    }

    @Override
    public MskFirehoseEvent clone() {
        try {
            return (MskFirehoseEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
}
