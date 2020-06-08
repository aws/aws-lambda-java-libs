package com.amazonaws.services.lambda.runtime.events.transformers.dynamodb;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.StreamRecord;

import java.util.Map;

public class DynamodbStreamRecordTransformer {

    public static StreamRecord toStreamRecordV2(final com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord streamRecord) {
        Map<String, AttributeValue> newImage = streamRecord.getNewImage() != null
                ? DynamodbAttributeValueTransformer.toAttributeValueMapV2(streamRecord.getNewImage())
                : null;
        Map<String, AttributeValue> oldImage = streamRecord.getOldImage() != null
                ? DynamodbAttributeValueTransformer.toAttributeValueMapV2(streamRecord.getOldImage())
                : null;

        return StreamRecord.builder()
                .approximateCreationDateTime(
                        streamRecord.getApproximateCreationDateTime().toInstant()
                )
                .keys(
                        DynamodbAttributeValueTransformer.toAttributeValueMapV2(streamRecord.getKeys())
                )
                .newImage(newImage)
                .oldImage(oldImage)
                .sequenceNumber(streamRecord.getSequenceNumber())
                .sizeBytes(streamRecord.getSizeBytes())
                .streamViewType(streamRecord.getStreamViewType())
                .build();
    }
}
