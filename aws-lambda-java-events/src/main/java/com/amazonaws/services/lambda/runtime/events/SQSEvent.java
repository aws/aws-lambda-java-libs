/*
 * Copyright 2012-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.amazonaws.services.lambda.runtime.events;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Represents an Amazon SQS event.
 */
public class SQSEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -5663700178408796225L;

    private List<SQSMessage> records;

    public static class MessageAttribute implements Serializable, Cloneable {

        private static final long serialVersionUID = -1602746537669100978L;

        private String stringValue;

        private ByteBuffer binaryValue;

        private List<String> stringListValues;

        private List<ByteBuffer> binaryListValues;

        private String dataType;

        /**
         * Default constructor
         */
        public MessageAttribute() {}

        /**
         * Gets the value of message attribute of type String or type Number
         * @return stringValue
         */
        public String getStringValue() {
            return stringValue;
        }

        /**
         * Sets the value of message attribute of type String or type Number
         * @param stringValue A string representing the value of attribute of type String or type Number
         */
        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        /**
         * Gets the value of message attribute of type Binary
         * @return binaryValue
         */
        public ByteBuffer getBinaryValue() {
            return binaryValue;
        }

        /**
         * Sets the value of message attribute of type Binary
         * @param binaryValue A string representing the value of attribute of type Binary
         */
        public void setBinaryValue(ByteBuffer binaryValue) {
            this.binaryValue = binaryValue;
        }

        /**
         * Gets the list of String values of message attribute
         * @return stringListValues
         */
        public List<String> getStringListValues() {
            return stringListValues;
        }

        /**
         * Sets the list of String values of message attribute
         * @param stringListValues A list of String representing the value of attribute
         */
        public void setStringListValues(List<String> stringListValues) {
            this.stringListValues = stringListValues;
        }

        /**
         * Gets the list of Binary values of message attribute
         * @return binaryListValues
         */
        public List<ByteBuffer> getBinaryListValues() {
            return binaryListValues;
        }

        /**
         * Sets the list of Binary values of message attribute
         * @param binaryListValues A list of Binary representing the value of attribute
         */
        public void setBinaryListValues(List<ByteBuffer> binaryListValues) {
            this.binaryListValues = binaryListValues;
        }

        /**
         * Gets the dataType of message attribute
         * @return dataType
         */
        public String getDataType() {
            return dataType;
        }

        /**
         * Sets the dataType of message attribute
         * @param dataType A string representing the data type of attribute
         */
        public void setDataType(String dataType) {
            this.dataType = dataType;
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
            if (getStringValue() != null)
                sb.append("stringValue: ").append(getStringValue()).append(",");
            if (getBinaryValue() != null)
                sb.append("binaryValue: ").append(getBinaryValue().toString()).append(",");
            if (getStringListValues() != null)
                sb.append("stringListValues: ").append(getStringListValues()).append(",");
            if (getBinaryListValues() != null)
                sb.append("binaryListValues: ").append(getBinaryListValues()).append(",");
            if (getDataType() != null)
                sb.append("dataType: ").append(getDataType());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (obj instanceof MessageAttribute == false)
                return false;
            MessageAttribute other = (MessageAttribute) obj;
            if (other.getStringValue() == null ^ this.getStringValue() == null)
                return false;
            if (other.getStringValue() != null && other.getStringValue().equals(this.getStringValue()) == false)
                return false;
            if (other.getBinaryValue() == null ^ this.getBinaryValue() == null)
                return false;
            if (other.getBinaryValue() != null && other.getBinaryValue().equals(this.getBinaryValue()) == false)
                return false;
            if (other.getStringListValues() == null ^ this.getStringListValues() == null)
                return false;
            if (other.getStringListValues() != null
                    && other.getStringListValues().equals(this.getStringListValues()) == false)
                return false;
            if (other.getBinaryListValues() == null ^ this.getBinaryListValues() == null)
                return false;
            if (other.getBinaryListValues() != null
                    && other.getBinaryListValues().equals(this.getBinaryListValues()) == false)
                return false;
            if (other.getDataType() == null ^ this.getDataType() == null)
                return false;
            if (other.getDataType() != null && other.getDataType().equals(this.getDataType()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getStringValue() == null) ? 0 : getStringValue().hashCode());
            hashCode = prime * hashCode + ((getBinaryValue() == null) ? 0 : getBinaryValue().hashCode());
            hashCode = prime * hashCode + ((getStringListValues() == null) ? 0 : getStringListValues().hashCode());
            hashCode = prime * hashCode + ((getBinaryListValues() == null) ? 0 : getBinaryListValues().hashCode());
            hashCode = prime * hashCode + ((getDataType() == null) ? 0 : getDataType().hashCode());
            return hashCode;
        }

        @Override
        public MessageAttribute clone() {
            try {
                return (MessageAttribute) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    public static class SQSMessage implements Serializable, Cloneable {

        private static final long serialVersionUID = -2300083946005987098L;

        private String messageId;

        private String receiptHandle;

        private String body;

        private String md5OfBody;

        private String md5OfMessageAttributes;

        private String eventSourceArn;

        private String eventSource;

        private String awsRegion;

        private Map<String, String> attributes;

        private Map<String, MessageAttribute> messageAttributes;

        /**
         * Default constructor
         */
        public SQSMessage() {}

        /**
         * Gets the message id
         * @return messageId
         */
        public String getMessageId() { return messageId; }

        /**
         * Sets the message id
         * @param messageId
         **/
        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        /**
         * Gets the receipt handle
         * @return receiptHandle
         */
        public String getReceiptHandle() { return receiptHandle; }

        /**
         * Sets the receipt handle
         * @param receiptHandle
         **/
        public void setReceiptHandle(String receiptHandle) {
            this.receiptHandle = receiptHandle;
        }

        /**
         * Gets the body
         * @return body
         */
        public String getBody() { return body; }

        /**
         * Sets the body
         * @param body
         **/
        public void setBody(String body) {
            this.body = body;
        }

        /**
         * Gets the md5 of body
         * @return md5OfBody
         */
        public String getMd5OfBody() { return md5OfBody; }

        /**
         * Sets the md5 of body
         * @param md5OfBody
         **/
        public void setMd5OfBody(String md5OfBody) {
            this.md5OfBody = md5OfBody;
        }

        /**
         * Gets the md5 of message attributes
         * @return md5OfMessageAttributes
         */
        public String getMd5OfMessageAttributes() { return md5OfMessageAttributes; }

        /**
         * Sets the md5 of message attributes
         * @param md5OfMessageAttributes
         **/
        public void setMd5OfMessageAttributes(String md5OfMessageAttributes) {
            this.md5OfMessageAttributes = md5OfMessageAttributes;
        }

        /**
         * Gets the Event Source ARN
         * @return eventSourceArn
         */
        public String getEventSourceArn() { return eventSourceArn; }

        /**
         * Sets the Event Source ARN  
         * @param eventSourceArn
         **/
        public void setEventSourceArn(String eventSourceArn) {
            this.eventSourceArn = eventSourceArn;
        }

        /**
         * Gets the Event Source
         * @return eventSource
         */
        public String getEventSource() { return eventSource; }

        /**
         * Sets the Event Source
         * @param eventSource
         **/
        public void setEventSource(String eventSource) {
            this.eventSource = eventSource;
        }

        /**
         * Gets the AWS Region
         * @return awsRegion
         */
        public String getAwsRegion() { return awsRegion; }

        /**
         * Sets the AWS Region
         * @param awsRegion
         **/
        public void setAwsRegion(String awsRegion) {
            this.awsRegion = awsRegion;
        }

        /**
         * Gets the attributes associated with the queue
         * @return attributes
         */
        public Map<String, String> getAttributes() { return attributes; }

        /**
         * Sets the queue attributes associated with the queue
         * @param attributes
         **/
        public void setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
        }

        /**
         * Gets the attributes associated with the message
         * @return message attributes
         */
        public Map<String, MessageAttribute> getMessageAttributes() {
            return messageAttributes;
        }

        /**
         * Sets the attributes associated with the message
         * @param messageAttributes A map object with string and message attribute key/value pairs
         */
        public void setMessageAttributes(Map<String, MessageAttribute> messageAttributes) {
            this.messageAttributes = messageAttributes;
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
            if (getMessageId() != null)
                sb.append("messageId: ").append(getMessageId()).append(",");
            if (getReceiptHandle() != null)
                sb.append("receiptHandle: ").append(getReceiptHandle()).append(",");
            if (getEventSourceArn() != null)
                sb.append("eventSourceARN: ").append(getEventSourceArn()).append(",");
            if (getEventSource() != null)
                sb.append("eventSource: ").append(getEventSource()).append(",");
            if (getAwsRegion() != null)
                sb.append("awsRegion: ").append(getAwsRegion()).append(",");
            if (getBody() != null)
                sb.append("body: ").append(getBody()).append(",");
            if (getMd5OfBody() != null)
                sb.append("md5OfBody: ").append(getMd5OfBody()).append(",");
            if (getMd5OfMessageAttributes() != null)
                sb.append("md5OfMessageAttributes: ").append(getMd5OfMessageAttributes()).append(",");
            if (getAttributes() != null)
                sb.append("attributes: ").append(getAttributes().toString()).append(",");
            if (getMessageAttributes() != null)
                sb.append("messageAttributes: ").append(getMessageAttributes().toString());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (obj instanceof SQSMessage == false)
                return false;
            SQSMessage other = (SQSMessage) obj;
            if (other.getMessageId() == null ^ this.getMessageId() == null)
                return false;
            if (other.getMessageId() != null && other.getMessageId().equals(this.getMessageId()) == false)
                return false;
            if (other.getReceiptHandle() == null ^ this.getReceiptHandle() == null)
                return false;
            if (other.getReceiptHandle() != null && other.getReceiptHandle().equals(this.getReceiptHandle()) == false)
                return false;
            if (other.getEventSourceArn() == null ^ this.getEventSourceArn() == null)
                return false;
            if (other.getEventSourceArn() != null && other.getEventSourceArn().equals(this.getEventSourceArn()) == false)
                return false;
            if (other.getEventSource() == null ^ this.getEventSource() == null)
                return false;
            if (other.getEventSource() != null && other.getEventSource().equals(this.getEventSource()) == false)
                return false;
            if (other.getAwsRegion() == null ^ this.getAwsRegion() == null)
                return false;
            if (other.getAwsRegion() != null && other.getAwsRegion().equals(this.getAwsRegion()) == false)
                return false;
            if (other.getBody() == null ^ this.getBody() == null)
                return false;
            if (other.getBody() != null && other.getBody().equals(this.getBody()) == false)
                return false;
            if (other.getMd5OfBody() == null ^ this.getMd5OfBody() == null)
                return false;
            if (other.getMd5OfBody() != null && other.getMd5OfBody().equals(this.getMd5OfBody()) == false)
                return false;
            if (other.getMd5OfMessageAttributes() == null ^ this.getMd5OfMessageAttributes() == null)
                return false;
            if (other.getMd5OfMessageAttributes() != null
                    && other.getMd5OfMessageAttributes().equals(this.getMd5OfMessageAttributes()) == false)
                return false;
            if (other.getAttributes() == null ^ this.getAttributes() == null)
                return false;
            if (other.getAttributes() != null && other.getAttributes().equals(this.getAttributes()) == false)
                return false;
            if (other.getMessageAttributes() == null ^ this.getMessageAttributes() == null)
                return false;
            if (other.getMessageAttributes() != null
                    && other.getMessageAttributes().equals(this.getMessageAttributes()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getMessageAttributes() == null) ? 0 : getMessageAttributes().hashCode());
            hashCode = prime * hashCode + ((getMessageId() == null) ? 0 : getMessageId().hashCode());
            hashCode = prime * hashCode + ((getReceiptHandle() == null) ? 0 : getReceiptHandle().hashCode());
            hashCode = prime * hashCode + ((getEventSourceArn() == null) ? 0 : getEventSourceArn().hashCode());
            hashCode = prime * hashCode + ((getEventSource() == null) ? 0 : getEventSource().hashCode());
            hashCode = prime * hashCode + ((getAwsRegion() == null) ? 0 : getAwsRegion().hashCode());
            hashCode = prime * hashCode + ((getBody() == null) ? 0 : getBody().hashCode());
            hashCode = prime * hashCode + ((getMd5OfBody() == null) ? 0 : getMd5OfBody().hashCode());
            hashCode = prime * hashCode + ((getMd5OfMessageAttributes() == null) ? 0 : getMd5OfMessageAttributes().hashCode());
            hashCode = prime * hashCode + ((getAttributes() == null) ? 0 : getAttributes().hashCode());
            return hashCode;
        }

        @Override
        public SQSMessage clone() {
            try {
                return (SQSMessage) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    /**
     * Default constructor
     */
    public SQSEvent() {}

    /**
     * Gets the list of SQS messages
     * @return List of messages
     */
    public List<SQSMessage> getRecords() { return records; }

    /**
     * Sets a list of SQS messages
     * @param records A list of SQS message objects
     */
    public void setRecords(List<SQSMessage> records) {
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
            sb.append("Records: ").append(getRecords());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SQSEvent))
            return false;
        SQSEvent other = (SQSEvent) obj;
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
    public SQSEvent clone() {
        try {
            return (SQSEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }
}
