package com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb;

import com.amazonaws.services.dynamodbv2.model.OperationType;
import com.amazonaws.services.dynamodbv2.model.Record;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.transformers.v1.DynamodbEventTransformer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbIdentityTransformerTest.identity_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbIdentityTransformerTest.identity_v1;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbStreamRecordTransformerTest.streamRecord_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbStreamRecordTransformerTest.streamRecord_v1;

public class DynamodbRecordTransformerTest {

    private static final String eventId = "2";
    private static final String eventName = OperationType.MODIFY.toString();
    private static final String eventVersion = "1.0";
    private static final String eventSource = "aws:dynamodb";
    private static final String awsRegion = "us-west-2";

    //region Record_event
    public static final DynamodbEvent.DynamodbStreamRecord record_event = (DynamodbEvent.DynamodbStreamRecord)
            new DynamodbEvent.DynamodbStreamRecord()
                    .withEventID(eventId)
                    .withEventName(eventName)
                    .withEventVersion(eventVersion)
                    .withEventSource(eventSource)
                    .withAwsRegion(awsRegion)
                    .withDynamodb(streamRecord_event)
                    .withUserIdentity(identity_event);
    //endregion

    //region Record_v1
    public static final Record record_v1 =
            new Record()
                    .withEventID(eventId)
                    .withEventName(eventName)
                    .withEventVersion(eventVersion)
                    .withEventSource(eventSource)
                    .withAwsRegion(awsRegion)
                    .withDynamodb(streamRecord_v1)
                    .withUserIdentity(identity_v1);
    //endregion

    @Test
    public void testToRecordV1() {
        Record convertedRecord = DynamodbRecordTransformer.toRecordV1(record_event);
        Assertions.assertEquals(record_v1, convertedRecord);
    }

    @Test
    public void testToRecordV1WhenUserIdentityIsNull() {
        DynamodbEvent.DynamodbStreamRecord record = record_event.clone();
        record.setUserIdentity(null);

        Assertions.assertDoesNotThrow(() -> {
            com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbRecordTransformer.toRecordV1(record);
        });
    }

}