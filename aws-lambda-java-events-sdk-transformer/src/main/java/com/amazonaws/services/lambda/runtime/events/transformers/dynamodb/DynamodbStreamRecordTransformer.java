package com.amazonaws.services.lambda.runtime.events.transformers.dynamodb;

import software.amazon.awssdk.services.dynamodb.model.StreamRecord;

public class DynamodbStreamRecordTransformer {

    public static StreamRecord toStreamRecordV2(final com.amazonaws.services.dynamodbv2.model.StreamRecord streamRecord) {
        return StreamRecord.builder()
                .approximateCreationDateTime(
                        streamRecord.getApproximateCreationDateTime().toInstant()
                )
                .keys(
                        DynamodbAttributeValueTransformer.toAttributeValueMapV2(streamRecord.getKeys())
                )
                .newImage(
                        DynamodbAttributeValueTransformer.toAttributeValueMapV2(streamRecord.getNewImage())
                )
                .oldImage(
                        DynamodbAttributeValueTransformer.toAttributeValueMapV2(streamRecord.getOldImage())
                )
                .sequenceNumber(streamRecord.getSequenceNumber())
                .sizeBytes(streamRecord.getSizeBytes())
                .streamViewType(streamRecord.getStreamViewType())
                .build();
    }
}
