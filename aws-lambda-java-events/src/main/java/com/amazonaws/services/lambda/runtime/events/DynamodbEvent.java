/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.events;
import java.util.List;

import com.amazonaws.services.dynamodbv2.model.Record;

/**
 * Represents an Amazon DynamoDB event
 */
public class DynamodbEvent {
    private List<DynamodbStreamRecord> records;
    
    /**
     * Gets the list of DynamoDB event records
     * 
     */
    public List<DynamodbStreamRecord> getRecords() {
        return records;
    }

    /**
     * Sets the list of DynamoDB event records
     * @param records a list of DynamoDb event records
     */
    public void setRecords(List<DynamodbStreamRecord> records) {
        this.records = records;
    }

    /**
     * The unit of data of an Amazon DynamoDB event
     */
    @SuppressWarnings("serial")
    public static class DynamodbStreamRecord extends Record{
        private String eventSourceARN;

        /**
         * Gets the event source arn of DynamoDB
         * 
         */
        public String getEventSourceARN() {
            return eventSourceARN;
        }

        /**
         * Sets the event source arn of DynamoDB
         * @param eventSourceArn A string containing the event source arn
         */
        public void setEventSourceARN(String eventSourceARN) {
            this.eventSourceARN = eventSourceARN;
        }
    }
}
