/*
 * Copyright 2015-2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an Amazon DynamoDB event
 */
public class DynamodbEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -2354616079899981231L;

    private List<DynamodbStreamRecord> records;

    /**
     * The unit of data of an Amazon DynamoDB event
     */
    public static class DynamodbStreamRecord extends com.amazonaws.services.lambda.runtime.events.models.dynamodb.Record {

        private static final long serialVersionUID = 3638381544604354963L;

        private String eventSourceARN;

        /**
         * default constructor
         * (Not available in v1)
         */
        public DynamodbStreamRecord() {}

        /**
         * Gets the event source arn of DynamoDB
         * @return event source arn
         */
        public String getEventSourceARN() {
            return eventSourceARN;
        }

        /**
         * Sets the event source arn of DynamoDB
         * @param eventSourceARN A string containing the event source arn
         */
        public void setEventSourceARN(String eventSourceARN) {
            this.eventSourceARN = eventSourceARN;
        }

        /**
         * Returns a string representation of this object; useful for testing and debugging.
         *
         * @return A string representation of this object.
         *
         * @see Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (getEventID() != null)
                sb.append("eventID: ").append(getEventID()).append(",");
            if (getEventName() != null)
                sb.append("eventName: ").append(getEventName()).append(",");
            if (getEventVersion() != null)
                sb.append("eventVersion: ").append(getEventVersion()).append(",");
            if (getEventSource() != null)
                sb.append("eventSource: ").append(getEventSource()).append(",");
            if (getAwsRegion() != null)
                sb.append("awsRegion: ").append(getAwsRegion()).append(",");
            if (getDynamodb() != null)
                sb.append("dynamodb: ").append(getDynamodb()).append(",");
            if (getUserIdentity() != null)
                sb.append("userIdentity: ").append(getUserIdentity()).append(",");
            if (getEventSourceARN() != null)
                sb.append("eventSourceArn: ").append(getEventSourceARN());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof DynamodbStreamRecord == false)
                return false;
            DynamodbStreamRecord other = (DynamodbStreamRecord) obj;
            if (other.getEventID() == null ^ this.getEventID() == null)
                return false;
            if (other.getEventID() != null && other.getEventID().equals(this.getEventID()) == false)
                return false;
            if (other.getEventName() == null ^ this.getEventName() == null)
                return false;
            if (other.getEventName() != null && other.getEventName().equals(this.getEventName()) == false)
                return false;
            if (other.getEventVersion() == null ^ this.getEventVersion() == null)
                return false;
            if (other.getEventVersion() != null && other.getEventVersion().equals(this.getEventVersion()) == false)
                return false;
            if (other.getEventSource() == null ^ this.getEventSource() == null)
                return false;
            if (other.getEventSource() != null && other.getEventSource().equals(this.getEventSource()) == false)
                return false;
            if (other.getAwsRegion() == null ^ this.getAwsRegion() == null)
                return false;
            if (other.getAwsRegion() != null && other.getAwsRegion().equals(this.getAwsRegion()) == false)
                return false;
            if (other.getDynamodb() == null ^ this.getDynamodb() == null)
                return false;
            if (other.getDynamodb() != null && other.getDynamodb().equals(this.getDynamodb()) == false)
                return false;
            if (other.getUserIdentity() == null ^ this.getUserIdentity() == null)
                return false;
            if (other.getUserIdentity() != null && other.getUserIdentity().equals(this.getUserIdentity()) == false)
                return false;
            if (other.getEventSourceARN() == null ^ this.getEventSourceARN() == null)
                return false;
            if (other.getEventSourceARN() != null && other.getEventSourceARN().equals(this.getEventSourceARN()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = super.hashCode();

            hashCode = prime * hashCode + ((getEventSourceARN() == null) ? 0 : getEventSourceARN().hashCode());
            return hashCode;
        }

        @Override
        public DynamodbStreamRecord clone() {
            return (DynamodbStreamRecord) super.clone();
        }

    }

    /**
     * default constructor
     * (Not available in v1)
     */
    public DynamodbEvent() {}

    /**
     * Gets the list of DynamoDB event records
     * @return list of dynamodb event records
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
     * Returns a string representation of this object; useful for testing and debugging.
     *
     * @return A string representation of this object.
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getRecords() != null)
            sb.append(getRecords());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof DynamodbEvent == false)
            return false;
        DynamodbEvent other = (DynamodbEvent) obj;
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
    public DynamodbEvent clone() {
        try {
            return (DynamodbEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
    
}
