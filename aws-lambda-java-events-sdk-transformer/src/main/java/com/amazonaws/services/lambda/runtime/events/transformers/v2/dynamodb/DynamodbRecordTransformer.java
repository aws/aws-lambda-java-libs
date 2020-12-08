package com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import software.amazon.awssdk.services.dynamodb.model.Record;

public class DynamodbRecordTransformer {

    public static Record toRecordV2(final DynamodbEvent.DynamodbStreamRecord record) {
        return Record.builder()
                .awsRegion(record.getAwsRegion())
                .dynamodb(
                        DynamodbStreamRecordTransformer.toStreamRecordV2(record.getDynamodb())
                )
                .eventID(record.getEventID())
                .eventName(record.getEventName())
                .eventSource(record.getEventSource())
                .eventVersion(record.getEventVersion())
                .userIdentity(
                        record.getUserIdentity() != null
                                ? DynamodbIdentityTransformer.toIdentityV2(record.getUserIdentity())
                                : null
                )
                .build();
    }
}
