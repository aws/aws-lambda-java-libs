/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.events;

import com.amazonaws.services.s3.event.S3EventNotification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents and AmazonS3 event.
 *
 */
public class S3Event extends S3EventNotification implements Serializable, Cloneable {

    private static final long serialVersionUID = -8094860465750962044L;

    /**
     * default constructor
     * (Not available in v1)
     */
    public S3Event() {
        super(new ArrayList<S3EventNotificationRecord>());
    }

    /**
     * Create a new instance of S3Event
     * @param records A list of S3 event notification records
     */
    public S3Event(List<S3EventNotification.S3EventNotificationRecord> records) {
        super(records);
    }

    /**
     * Returns a string representation of this object; useful for testing and debugging.
     *
     * @return A string representation of this object.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getRecords() != null)
            sb.append(getRecords().toString());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof S3Event == false)
            return false;
        S3Event other = (S3Event) obj;
        if (other.getRecords() == null ^ this.getRecords() == null)
            return false;
        if (other.getRecords() != null && other.getRecords().equals(this.getRecords()) == false)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode + ((getRecords() == null) ? 0 : getRecords().hashCode());

        return hashCode;
    }

    @Override
    public S3Event clone() {
        try {
            return (S3Event) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }

}
