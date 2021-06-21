package com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb;

import com.amazonaws.services.dynamodbv2.model.StreamRecord;

public class DynamodbStreamRecordTransformer {

    public static StreamRecord toStreamRecordV1(final com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord streamRecord) {
        return new StreamRecord()
                .withApproximateCreationDateTime(
                        streamRecord.getApproximateCreationDateTime()
                )
                .withKeys(
                        DynamodbAttributeValueTransformer.toAttributeValueMapV1(streamRecord.getKeys())
                )
                .withNewImage(
                        DynamodbAttributeValueTransformer.toAttributeValueMapV1(streamRecord.getNewImage())
                )
                .withOldImage(
                        DynamodbAttributeValueTransformer.toAttributeValueMapV1(streamRecord.getOldImage())
                )
                .withSequenceNumber(streamRecord.getSequenceNumber())
                .withSizeBytes(streamRecord.getSizeBytes())
                .withStreamViewType(streamRecord.getStreamViewType());
    }
}
