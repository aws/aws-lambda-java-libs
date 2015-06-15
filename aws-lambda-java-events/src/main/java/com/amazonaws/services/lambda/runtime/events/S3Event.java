/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.events;

import com.amazonaws.services.s3.event.S3EventNotification;
import java.util.List;

/**
 * Represents and AmazonS3 event.
 *
 */
public class S3Event extends S3EventNotification {
    /**
     * Create a new instance of S3Event
     * @param records A list of S3 event notification records
     */
    public S3Event(List<S3EventNotification.S3EventNotificationRecord> records) {
        super(records);
    }
}
