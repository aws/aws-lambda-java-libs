/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.events;

import java.util.Map;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Represents an Amazon SNS event.
 */
public class SNSEvent {
	
	/**
	 * Represents an SNS message
	 *
	 */
    public static class SNS {
        private Map<String, MessageAttribute> messageAttributes;

        private String signingCertUrl;

        private String messageId;

        private String message;

        private String subject;

        private String unsubscribeUrl;

        private String type;

        private String signatureVersion;

        private String signature;

        private DateTime timestamp;

        private String topicArn;

        /**
         * Gets the attributes associated with the message 
         * 
         */
        public Map<String, MessageAttribute> getMessageAttributes() {
            return messageAttributes;
        }

        /**
         * Sets the attributes associated with the message
         * @param messageAttributes A map object with string and message attribute key/value pairs
         */
        public void setMessageAttributes(
                Map<String, MessageAttribute> messageAttributes) {
            this.messageAttributes = messageAttributes;
        }

        /**
         * Gets the URL for the signing certificate
         * 
         */
        public String getSigningCertUrl() {
            return signingCertUrl;
        }

        /**
         * Sets the URL for the signing certificate
         * @param signingCertUrl A string containing a URL
         */
        public void setSigningCertUrl(String signingCertUrl) {
            this.signingCertUrl = signingCertUrl;
        }

        /**
         * Gets the message id
         * 
         */
        public String getMessageId() {
            return messageId;
        }

        /**
         * Sets the message id
         * @param messageId A string containing the message ID
         */
        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        /**
         * Gets the message
         * 
         */
        public String getMessage() {
            return message;
        }

        /**
         * Sets the message
         * @param message A string containing the message body
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * Gets the subject for the message
         * 
         */
        public String getSubject() {
            return subject;
        }

        /**
         * Sets the subject for the message
         * @param subject A string containing the message subject
         */
        public void setSubject(String subject) {
            this.subject = subject;
        }

        /**
         * Gets the message unsubscribe URL
         * 
         */
        public String getUnsubscribeUrl() {
            return unsubscribeUrl;
        }

        /**
         * Sets the message unsubscribe URL
         * @param unsubscribeUrl A string with the URL
         */
        public void setUnsubscribeUrl(String unsubscribeUrl) {
            this.unsubscribeUrl = unsubscribeUrl;
        }

        /**
         * Gets the message type
         * 
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the message type
         * @param type A string containing the message type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * Gets the signature version used to sign the message
         * 
         */
        public String getSignatureVersion() {
            return signatureVersion;
        }

        /**
         * The signature version used to sign the message
         * @param signatureVersion A string containing the signature version
         */
        public void setSignatureVersion(String signatureVersion) {
            this.signatureVersion = signatureVersion;
        }

        /**
         * Gets the message signature
         * 
         */
        public String getSignature() {
            return signature;
        }

        /**
         * Sets the message signature
         * @param signature A string containing the message signature
         */
        public void setSignature(String signature) {
            this.signature = signature;
        }

        /**
         * Gets the message time stamp
         *  
         */
        public DateTime getTimestamp() {
            return timestamp;
        }

        /**
         * Sets the message time stamp
         * @param timestamp A DateTime object representing the message time stamp
         */
        public void setTimestamp(DateTime timestamp) {
            this.timestamp = timestamp;
        }

        /**
         * Gets the topic ARN
         * 
         */
        public String getTopicArn() {
            return topicArn;
        }

        /**
         * Sets the topic ARN
         * @param topicArn A string containing the topic ARN
         */
        public void setTopicArn(String topicArn) {
            this.topicArn = topicArn;
        }
    }
    

    /**
     * Represents an SNS message record. SNS message records are used to send
     * SNS messages to Lambda Functions.
     *
     */
    public static class SNSRecord {
        private SNS sns;

        private String eventVersion;

        private String eventSource;

        private String eventSubscriptionArn;

        /**
         *  Gets the SNS message
         * 
         */
        public SNS getSNS() {
            return sns;
        }

        /**
         * Sets the SNS message
         * @param sns An SNS object representing the SNS message
         */
        public void setSns(SNS sns) {
            this.sns = sns;
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
         * Gets the event source
         * 
         */
        public String getEventSource() {
            return eventSource;
        }

        /**
         * Sets the event source
         * @param eventSource A string containing the event source
         */
        public void setEventSource(String eventSource) {
            this.eventSource = eventSource;
        }

        /**
         * Gets the event subscription ARN
         * 
         * 
         */
        public String getEventSubscriptionArn() {
            return eventSubscriptionArn;
        }

        /**
         * Sets the event subscription ARN
         * @param eventSubscriptionArn A string containing the event subscription ARN
         */
        public void setEventSubscriptionArn(String eventSubscriptionArn) {
            this.eventSubscriptionArn = eventSubscriptionArn;
        }
    }

    /**
     * Represents an SNS message attribute
     *
     */
    public static class MessageAttribute {
        private String type;
        private String value;

        /**
         * Gets the attribute type
         * 
         */
        public String getType() {
            return type;
        }

        /**
         * Gets the attribute value
         * 
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the attribute type
         * @param type A string representing the attribute type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * Sets the attribute value
         * @param value A string containing the attribute value
         */
        public void setValue(String value) {
            this.value = value;
        }
    }

    private List<SNSRecord> records;

    /**
     *  Gets the list of SNS records
     * 
     */
    public List<SNSRecord> getRecords() {
        return records;
    }

    /**
     * Sets a list of SNS records
     * @param records A list of SNS record objects
     */
    public void setRecords(List<SNSRecord> records) {
        this.records = records;
    }
}
