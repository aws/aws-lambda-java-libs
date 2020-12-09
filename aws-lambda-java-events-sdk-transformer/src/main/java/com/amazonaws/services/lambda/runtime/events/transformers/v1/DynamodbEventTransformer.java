package com.amazonaws.services.lambda.runtime.events.transformers.v1;

import com.amazonaws.services.dynamodbv2.model.Record;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.transformers.v1.dynamodb.DynamodbRecordTransformer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DynamodbEventTransformer {

    public static List<Record> toRecordsV1(final DynamodbEvent dynamodbEvent) {
        return dynamodbEvent
                .getRecords()
                .stream()
                .filter(record -> !Objects.isNull(record))
                .map(DynamodbRecordTransformer::toRecordV1)
                .collect(Collectors.toList());
    }
}
