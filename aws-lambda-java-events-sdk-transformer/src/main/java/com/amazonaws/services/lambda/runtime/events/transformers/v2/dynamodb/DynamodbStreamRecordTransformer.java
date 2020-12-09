package com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb;

import software.amazon.awssdk.services.dynamodb.model.StreamRecord;

public class DynamodbStreamRecordTransformer {

    public static StreamRecord toStreamRecordV2(final com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord streamRecord) {

        return StreamRecord.builder()
                .approximateCreationDateTime(
                    streamRecord.getApproximateCreationDateTime().toInstant()
                )
                .keys(
                    DynamodbAttributeValueTransformer.toAttributeValueMapV2(streamRecord.getKeys())
                )
                .newImage(
                    streamRecord.getNewImage() != null
                        ? DynamodbAttributeValueTransformer.toAttributeValueMapV2(streamRecord.getNewImage())
                        : null
                )
                .oldImage(
                    streamRecord.getOldImage() != null
                        ? DynamodbAttributeValueTransformer.toAttributeValueMapV2(streamRecord.getOldImage())
                        : null
                )
                .sequenceNumber(streamRecord.getSequenceNumber())
                .sizeBytes(streamRecord.getSizeBytes())
                .streamViewType(streamRecord.getStreamViewType())
                .build();
    }
}
