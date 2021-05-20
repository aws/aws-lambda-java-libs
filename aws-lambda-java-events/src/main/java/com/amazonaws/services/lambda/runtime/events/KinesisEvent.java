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
 * Represents an Amazon Kinesis event.
 */
public class KinesisEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = 8145257839787754632L;

    private List<KinesisEventRecord> records;

    /**
     * The unit of data of an Amazon Kinesis stream
     */
    public static class Record extends com.amazonaws.services.lambda.runtime.events.models.kinesis.Record {

        private static final long serialVersionUID = 7856672931457425976L;

        private String kinesisSchemaVersion;

        /**
         * default constructor
         * (Not available in v1)
         */
        public Record() {}

        /**
         * Gets the schema version for the record
         * @return kinesis schema version
         */
        public String getKinesisSchemaVersion() {
            return kinesisSchemaVersion;
        }

        /**
         * Sets the schema version for the record
         * @param kinesisSchemaVersion A string containing the schema version
         */
        public void setKinesisSchemaVersion(String kinesisSchemaVersion) {
            this.kinesisSchemaVersion = kinesisSchemaVersion;
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
            if (getSequenceNumber() != null)
                sb.append("SequenceNumber: ").append(getSequenceNumber()).append(",");
            if (getApproximateArrivalTimestamp() != null)
                sb.append("ApproximateArrivalTimestamp: ").append(getApproximateArrivalTimestamp()).append(",");
            if (getData() != null)
                sb.append("Data: ").append(getData()).append(",");
            if (getPartitionKey() != null)
                sb.append("PartitionKey: ").append(getPartitionKey()).append(",");
            if (getEncryptionType() != null)
                sb.append("EncryptionType: ").append(getEncryptionType()).append(",");
            if (getKinesisSchemaVersion() != null)
                sb.append("KinesisSchemaVersion: ").append(getKinesisSchemaVersion());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof Record == false)
                return false;
            Record other = (Record) obj;
            if (other.getSequenceNumber() == null ^ this.getSequenceNumber() == null)
                return false;
            if (other.getSequenceNumber() != null && other.getSequenceNumber().equals(this.getSequenceNumber()) == false)
                return false;
            if (other.getApproximateArrivalTimestamp() == null ^ this.getApproximateArrivalTimestamp() == null)
                return false;
            if (other.getApproximateArrivalTimestamp() != null && other.getApproximateArrivalTimestamp().equals(this.getApproximateArrivalTimestamp()) == false)
                return false;
            if (other.getData() == null ^ this.getData() == null)
                return false;
            if (other.getData() != null && other.getData().equals(this.getData()) == false)
                return false;
            if (other.getPartitionKey() == null ^ this.getPartitionKey() == null)
                return false;
            if (other.getPartitionKey() != null && other.getPartitionKey().equals(this.getPartitionKey()) == false)
                return false;
            if (other.getEncryptionType() == null ^ this.getEncryptionType() == null)
                return false;
            if (other.getEncryptionType() != null && other.getEncryptionType().equals(this.getEncryptionType()) == false)
                return false;
            if (other.getKinesisSchemaVersion() == null ^ this.getKinesisSchemaVersion() == null)
                return false;
            if (other.getKinesisSchemaVersion() != null && other.getKinesisSchemaVersion().equals(this.getKinesisSchemaVersion()) == false)
                return false;
            return true;
        }

        /* (non-Javadoc)
         * @see com.amazonaws.services.lambda.runtime.events.models.kinesis.Record#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((getKinesisSchemaVersion() == null) ? 0 : getKinesisSchemaVersion().hashCode());
            return result;
        }

        @Override
        public Record clone() {
            return (Record) super.clone();
        }
    }
    
    /**
     * Kinesis event records provide contextual data about a Kinesis record
     *
     */
    public static class KinesisEventRecord implements Serializable, Cloneable {

        private static final long serialVersionUID = -3855723544907905206L;

        private String eventSource;

        private Record kinesis;

        private String eventID;

        private String invokeIdentityArn;

        private String eventName;

        private String eventVersion;

        private String eventSourceARN;

        private String awsRegion;

        /**
         * default constructor
         * (not available in v1)
         */
        public KinesisEventRecord() {}

        /**
         * Gets the source of the event
         * @return event source
         */
        public String getEventSource() {
            return eventSource;
        }

        /**
         * Sets the source of the event
         * @param eventSource A string representing the event source
         */
        public void setEventSource(String eventSource) {
            this.eventSource = eventSource;
        }

        /**
         * Gets the underlying Kinesis record associated with the event.
         * @return Kinesis Record object
         */
        public Record getKinesis() {
            return kinesis;
        }

        /**
         * Sets the underlying Kinesis record associated with the event.
         * @param kinesis A Kineis record object.
         */
        public void setKinesis(Record kinesis) {
            this.kinesis = kinesis;
        }

        /**
         * Gets the event id.
         * @return event id
         */
        public String getEventID() {
            return eventID;
        }

        /**
         * Sets the event id
         * @param eventID A string representing the event id.
         */
        public void setEventID(String eventID) {
            this.eventID = eventID;
        }

        /**
         * Gets then ARN for the identity used to invoke the Lambda Function. 
         * @return invoke arn
         */
        public String getInvokeIdentityArn() {
            return invokeIdentityArn;
        }

        /**
         * Sets an ARN for the identity used to invoke the Lambda Function.
         * @param invokeIdentityArn A string representing the invoke identity ARN
         */
        public void setInvokeIdentityArn(String invokeIdentityArn) {
            this.invokeIdentityArn = invokeIdentityArn;
        }

        /**
         * Gets the name of the event
         * @return event name
         */
        public String getEventName() {
            return eventName;
        }

        /**
         * Sets the name of the event
         * @param eventName A string containing the event name
         */
        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        /**
         * Gets the event version
         * @return event version
         */
        public String getEventVersion() {
            return eventVersion;
        }

        /**
         * Sets the event version
         * @param eventVersion A string containing the event version
         */
        public void setEventVersion(String eventVersion) {
            this.eventVersion = eventVersion;
        }

        /**
         * Gets the ARN of the event source
         * @return event source arn
         */
        public String getEventSourceARN() {
            return eventSourceARN;
        }

        /**
         * Sets the ARN of the event source
         * @param eventSourceARN A string containing the event source ARN
         */
        public void setEventSourceARN(String eventSourceARN) {
            this.eventSourceARN = eventSourceARN;
        }

        /**
         * Gets the AWS region where the event originated
         * @return aws region
         */
        public String getAwsRegion() {
            return awsRegion;
        }

        /**
         * Sets the AWS region where the event originated
         * @param awsRegion A string containing the AWS region
         */
        public void setAwsRegion(String awsRegion) {
            this.awsRegion = awsRegion;
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
            if (getEventSource() != null)
                sb.append("eventSource: ").append(getEventSource()).append(",");
            if (getKinesis() != null)
                sb.append("kinesis: ").append(getKinesis().toString()).append(",");
            if (getEventID() != null)
                sb.append("eventId: ").append(getEventID()).append(",");
            if (getInvokeIdentityArn() != null)
                sb.append("invokeIdentityArn: ").append(getInvokeIdentityArn()).append(",");
            if (getEventName() != null)
                sb.append("eventName: ").append(getEventName()).append(",");
            if (getEventVersion() != null)
                sb.append("eventVersion: ").append(getEventVersion()).append(",");
            if (getEventSourceARN() != null)
                sb.append("eventSourceARN: ").append(getEventSourceARN()).append(",");
            if (getAwsRegion() != null)
                sb.append("awsRegion: ").append(getAwsRegion());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof KinesisEventRecord == false)
                return false;
            KinesisEventRecord other = (KinesisEventRecord) obj;
            if (other.getEventSource() == null ^ this.getEventSource() == null)
                return false;
            if (other.getEventSource() != null && other.getEventSource().equals(this.getEventSource()) == false)
                return false;
            if (other.getKinesis() == null ^ this.getKinesis() == null)
                return false;
            if (other.getKinesis() != null && other.getKinesis().equals(this.getKinesis()) == false)
                return false;
            if (other.getEventID() == null ^ this.getEventID() == null)
                return false;
            if (other.getEventID() != null && other.getEventID().equals(this.getEventID()) == false)
                return false;
            if (other.getInvokeIdentityArn() == null ^ this.getInvokeIdentityArn() == null)
                return false;
            if (other.getInvokeIdentityArn() != null && other.getInvokeIdentityArn().equals(this.getInvokeIdentityArn()) == false)
                return false;
            if (other.getEventName() == null ^ this.getEventName() == null)
                return false;
            if (other.getEventName() != null && other.getEventName().equals(this.getEventName()) == false)
                return false;
            if (other.getEventVersion() == null ^ this.getEventVersion() == null)
                return false;
            if (other.getEventVersion() != null && other.getEventVersion().equals(this.getEventVersion()) == false)
                return false;
            if (other.getEventSourceARN() == null ^ this.getEventSourceARN() == null)
                return false;
            if (other.getEventSourceARN() != null && other.getEventSourceARN().equals(this.getEventSourceARN()) == false)
                return false;
            if (other.getAwsRegion() == null ^ this.getAwsRegion() == null)
                return false;
            if (other.getAwsRegion() != null && other.getAwsRegion().equals(this.getAwsRegion()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getEventSource() == null) ? 0 : getEventSource().hashCode());
            hashCode = prime * hashCode + ((getKinesis() == null) ? 0 : getKinesis().hashCode());
            hashCode = prime * hashCode + ((getEventID() == null) ? 0 : getEventID().hashCode());
            hashCode = prime * hashCode + ((getInvokeIdentityArn() == null) ? 0 : getInvokeIdentityArn().hashCode());
            hashCode = prime * hashCode + ((getEventName() == null) ? 0 : getEventName().hashCode());
            hashCode = prime * hashCode + ((getEventVersion() == null) ? 0 : getEventVersion().hashCode());
            hashCode = prime * hashCode + ((getEventSourceARN() == null) ? 0 : getEventSourceARN().hashCode());
            hashCode = prime * hashCode + ((getAwsRegion() == null) ? 0 : getAwsRegion().hashCode());

            return hashCode;
        }

        @Override
        public KinesisEventRecord clone() {
            try {
                return (KinesisEventRecord) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    /**
     * default constructor
     * (Not available in v1)
     */
    public KinesisEvent() {}

    /**
     * Gets the list of Kinesis event records
     * @return list of records
     */
    public List<KinesisEventRecord> getRecords() {
        return records;
    }

    /**
     * Sets the list of Kinesis event records
     * @param records a list of Kinesis event records
     */
    public void setRecords(List<KinesisEventRecord> records) {
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

        if (obj instanceof KinesisEvent == false)
            return false;
        KinesisEvent other = (KinesisEvent) obj;
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
    public KinesisEvent clone() {
        try {
            return (KinesisEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }

}
