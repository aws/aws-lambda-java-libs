/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.events.document;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

/**
 * Represents an Amazon DynamoDB event for use with the DynamoDB Document API.
 */
public class DynamodbDocumentEvent {
    private List<DynamodbStreamItem> itemRecords;

    private final DynamodbEvent event;

    public DynamodbDocumentEvent(DynamodbEvent event) {
        if (this.event == null)
            throw new IllegalArgumentException("event must be specified");
        this.event = event;
    }

    /**
     * Gets the list of DynamoDB event records
     *
     */
    public List<DynamodbStreamItem> getItemRecords() {
        if (itemRecords == null) {
            List<DynamodbStreamRecord> src = event.getRecords();
            List<DynamodbStreamItem> dst = new ArrayList<DynamodbStreamItem>(
                    src.size());

            for (DynamodbStreamRecord r : src)
                dst.add(new DynamodbStreamItem(r));
            event.setRecords(null);
            itemRecords = dst;
        }

        return itemRecords;
    }

    /**
     * Sets the list of DynamoDB event records
     *
     * @param itemRecords
     *            a list of DynamoDb event records
     */
    public void setItemRecords(List<DynamodbStreamItem> itemRecords) {
        this.itemRecords = itemRecords;
    }
}
