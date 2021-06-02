package com.amazonaws.services.lambda.runtime.events.transformers.v1;

import com.amazonaws.services.dynamodbv2.model.Record;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbRecordTransformerTest.record_event;
import static com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbRecordTransformerTest.record_v1;

public class DynamodbEventTransformerTest {

    private final DynamodbEvent dynamodbEvent;

    {
        record_event.setEventSourceARN("arn:aws:dynamodb:us-west-2:account-id:table/ExampleTableWithStream/stream/2015-06-27T00:48:05.899");
        dynamodbEvent = new DynamodbEvent();
        dynamodbEvent.setRecords(Collections.singletonList(record_event));
    }

    private final List<Record> expectedRecordsV2 = Collections.singletonList(record_v1);

    @Test
    public void testDynamodbEventToRecordsV2() {
        List<Record> convertedRecords = DynamodbEventTransformer.toRecordsV1(dynamodbEvent);
        Assertions.assertEquals(expectedRecordsV2, convertedRecords);
    }

    @Test
    public void testDynamodbEventToRecordsV2_FiltersNullRecords() {
        DynamodbEvent event = dynamodbEvent.clone();
        event.setRecords(Arrays.asList(record_event, null));
        Assertions.assertEquals(2, event.getRecords().size());

        List<Record> convertedRecords = DynamodbEventTransformer.toRecordsV1(event);
        Assertions.assertEquals(expectedRecordsV2, convertedRecords);
        Assertions.assertEquals(1, convertedRecords.size());
    }
}