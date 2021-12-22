package com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb;

import com.amazonaws.services.dynamodbv2.model.Record;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

public class DynamodbRecordTransformer {

    public static Record toRecordV1(final DynamodbEvent.DynamodbStreamRecord record) {
        return new Record()
                .withAwsRegion(record.getAwsRegion())
                .withDynamodb(
                        DynamodbStreamRecordTransformer.toStreamRecordV1(record.getDynamodb())
                )
                .withEventID(record.getEventID())
                .withEventName(record.getEventName())
                .withEventSource(record.getEventSource())
                .withEventVersion(record.getEventVersion())
                .withUserIdentity(
                        record.getUserIdentity() != null
                                ? DynamodbIdentityTransformer.toIdentityV1(record.getUserIdentity())
                                : null
                );
    }
}
