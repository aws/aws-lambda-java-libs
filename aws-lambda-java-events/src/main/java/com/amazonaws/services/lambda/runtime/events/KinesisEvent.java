/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.events;
import java.util.List;

/**
 * Represents an Amazon Kinesis event.
 */
public class KinesisEvent {
    private List<KinesisEventRecord> records;

    /**
     * Gets the list of Kinesis event records
     * 
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
     * The unit of data of an Amazon Kinesis stream
     *
     */
    @SuppressWarnings("serial")
    public static class Record extends com.amazonaws.services.kinesis.model.Record {
        private String kinesisSchemaVersion;

        /**
         * Gets the schema version for the record
         * 
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

        /* (non-Javadoc)
         * @see com.amazonaws.services.kinesis.model.Record#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime
                    * result
                    + ((getKinesisSchemaVersion() == null) ? 0
                            : getKinesisSchemaVersion().hashCode());
            return result;
        }

        /* (non-Javadoc)
         * @see com.amazonaws.services.kinesis.model.Record#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Record other = (Record) obj;
            if (kinesisSchemaVersion == null) {
                if (other.kinesisSchemaVersion != null)
                    return false;
            } else if (!kinesisSchemaVersion.equals(other.kinesisSchemaVersion))
                return false;
            return true;
        }
    }
    
    /**
     * Kinesis event records provide contextual data about a Kinesis record
     *
     */
    public static class KinesisEventRecord {
        private String eventSource;

        private Record kinesis;

        private String eventID;

        private String invokeIdentityArn;

        private String eventName;

        private String eventVersion;

        private String eventSourceARN;

        private String awsRegion;

        /**
         * Gets the source of the event
         * 
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
         * 
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
         * 
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
         * 
         * 
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
         * 
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
         * 
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
         * 
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
         * 
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
    }
}
