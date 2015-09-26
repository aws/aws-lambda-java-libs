/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.events.document;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

/**
 * The unit of data of an Amazon DynamoDB event.
 */
public class DynamodbStreamItem extends ItemRecord {
    private static final long serialVersionUID = 1L;
    private final DynamodbStreamRecord record;

    public DynamodbStreamItem(DynamodbStreamRecord record) {
        super(record);
        this.record = record;
    }

    /**
     * Gets the event source arn of DynamoDB
     *
     */
    public String getEventSourceARN() {
        return record.getEventSourceARN();
    }

    /**
     * Sets the event source arn of DynamoDB
     *
     * @param eventSourceArn
     *            A string containing the event source arn
     */
    public void setEventSourceARN(String eventSourceARN) {
        record.setEventSourceARN(eventSourceARN);
    }
}