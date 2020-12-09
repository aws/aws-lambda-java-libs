package com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.OperationType;
import software.amazon.awssdk.services.dynamodb.model.Record;

import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbIdentityTransformerTest.identity_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbIdentityTransformerTest.identity_v2;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbStreamRecordTransformerTest.streamRecord_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbStreamRecordTransformerTest.streamRecord_v2;

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

    //region Record_v2
    public static final Record record_v2 =
            Record.builder()
                    .eventID(eventId)
                    .eventName(eventName)
                    .eventVersion(eventVersion)
                    .eventSource(eventSource)
                    .awsRegion(awsRegion)
                    .dynamodb(streamRecord_v2)
                    .userIdentity(identity_v2)
                    .build();
    //endregion

    @Test
    public void testToRecordV2() {
        Record convertedRecord = DynamodbRecordTransformer.toRecordV2(record_event);
        Assertions.assertEquals(record_v2, convertedRecord);
    }

    @Test
    public void testToRecordV2WhenUserIdentityIsNull() {
        DynamodbEvent.DynamodbStreamRecord record = record_event.clone();
        record.setUserIdentity(null);

        Assertions.assertDoesNotThrow(() -> {
            DynamodbRecordTransformer.toRecordV2(record);
        });
    }

}