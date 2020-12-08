package com.amazonaws.services.lambda.runtime.events.transformers.v2;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.transformers.v2.dynamodb.DynamodbRecordTransformer;
import software.amazon.awssdk.services.dynamodb.model.Record;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DynamodbEventTransformer {

    public static List<Record> toRecordsV2(final DynamodbEvent dynamodbEvent) {
        return dynamodbEvent
                .getRecords()
                .stream()
                .filter(record -> !Objects.isNull(record))
                .map(DynamodbRecordTransformer::toRecordV2)
                .collect(Collectors.toList());
    }
}
