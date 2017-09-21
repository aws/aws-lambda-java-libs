/*
 * Copyright 2012-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents an Amazon SNS event.
 */
public class SNSEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -727529735144605167L;

    private List<SNSRecord> records;

    /**
     * Represents an SNS message attribute
     *
     */
    public static class MessageAttribute implements Serializable, Cloneable {

        private static final long serialVersionUID = -5656179310535967619L;

        private String type;

        private String value;

        /**
         * default constructor
         * (not available in v1)
         */
        public MessageAttribute() {}

        /**
         * Gets the attribute type
         * @return type
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the attribute type
         * @param type A string representing the attribute type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @param type type
         * @return MessageAttribute
         */
        public MessageAttribute withType(String type) {
            setType(type);
            return this;
        }

        /**
         * Gets the attribute value
         * @return value
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the attribute value
         * @param value A string containing the attribute value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * @param value attriute value
         * @return MessageAttribute
         */
        public MessageAttribute withValue(String value) {
            setValue(value);
            return this;
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
            if (getType() != null)
                sb.append("type: ").append(getType()).append(",");
            if (getValue() != null)
                sb.append("value: ").append(getValue());
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
            if (other.getType() == null ^ this.getType() == null)
                return false;
            if (other.getType() != null && other.getType().equals(this.getType()) == false)
                return false;
            if (other.getValue() == null ^ this.getValue() == null)
                return false;
            if (other.getValue() != null && other.getValue().equals(this.getValue()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getType() == null) ? 0 : getType().hashCode());
            hashCode = prime * hashCode + ((getValue() == null) ? 0 : getValue().hashCode());
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

    /**
     * Represents an SNS message
     */
    public static class SNS implements Serializable, Cloneable {

        private static final long serialVersionUID = -7038894618736475592L;

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
         * default constructor
         * (Not available in v1)
         */
        public SNS() {}

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
        public void setMessageAttributes(
                Map<String, MessageAttribute> messageAttributes) {
            this.messageAttributes = messageAttributes;
        }

        /**
         * @param messageAttributes message attributes
         * @return SNS
         */
        public SNS withMessageAttributes(Map<String, MessageAttribute> messageAttributes) {
            setMessageAttributes(messageAttributes);
            return this;
        }

        /**
         * Gets the URL for the signing certificate
         * @return signing certificate url
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
         * @param signingCertUrl signing cert url
         * @return SNS
         */
        public SNS withSigningCertUrl(String signingCertUrl) {
            setSigningCertUrl(signingCertUrl);
            return this;
        }

        /**
         * Gets the message id
         * @return message id
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
         * @param messageId message id
         * @return SNS
         */
        public SNS withMessageId(String messageId) {
            setMessageId(messageId);
            return this;
        }

        /**
         * Gets the message
         * @return message string
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
         * @param message string message
         * @return SNS
         */
        public SNS withMessage(String message) {
            setMessage(message);
            return this;
        }

        /**
         * Gets the subject for the message
         * @return subject of message
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
         * @param subject subject of message
         * @return SNS
         */
        public SNS withSubject(String subject) {
            setSubject(subject);
            return this;
        }

        /**
         * Gets the message unsubscribe URL
         * @return unsubscribe url
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
         * @param unsubscribeUrl unsubscribe url
         * @return SNS
         */
        public SNS withUnsubscribeUrl(String unsubscribeUrl) {
            setUnsubscribeUrl(unsubscribeUrl);
            return this;
        }

        /**
         * Gets the message type
         * @return message type
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
         * @param type type
         * @return SNS
         */
        public SNS withType(String type) {
            setType(type);
            return this;
        }

        /**
         * Gets the signature version used to sign the message
         * @return signature version
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
         * @param signatureVersion signature version
         * @return SNS
         */
        public SNS withSignatureVersion(String signatureVersion) {
            setSignatureVersion(signatureVersion);
            return this;
        }

        /**
         * Gets the message signature
         * @return message signature
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
         * @param signature signature
         * @return SNS
         */
        public SNS withSignature(String signature) {
            setSignature(signature);
            return this;
        }

        /**
         * Gets the message time stamp
         * @return timestamp of sns message
         */
        public DateTime getTimestamp() {
            return timestamp;
        }

        /**
         * Sets the message time stamp
         * @param timestamp A Date object representing the message time stamp
         */
        public void setTimestamp(DateTime timestamp) {
            this.timestamp = timestamp;
        }

        /**
         * @param timestamp timestamp
         * @return SNS
         */
        public SNS withTimestamp(DateTime timestamp) {
            setTimestamp(timestamp);
            return this;
        }

        /**
         * Gets the topic ARN
         * @return topic arn
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

        /**
         * @param topicArn topic ARN
         * @return SNS
         */
        public SNS withTopicArn(String topicArn) {
            setTopicArn(topicArn);
            return this;
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
            if (getMessageAttributes() != null)
                sb.append("messageAttributes: ").append(getMessageAttributes().toString()).append(",");
            if (getSigningCertUrl() != null)
                sb.append("signingCertUrl: ").append(getSigningCertUrl()).append(",");
            if (getMessageId() != null)
                sb.append("messageId: ").append(getMessageId()).append(",");
            if (getMessage() != null)
                sb.append("message: ").append(getMessage()).append(",");
            if (getSubject() != null)
                sb.append("subject: ").append(getSubject()).append(",");
            if (getUnsubscribeUrl() != null)
                sb.append("unsubscribeUrl: ").append(getUnsubscribeUrl()).append(",");
            if (getType() != null)
                sb.append("type: ").append(getType()).append(",");
            if (getSignatureVersion() != null)
                sb.append("signatureVersion: ").append(getSignatureVersion()).append(",");
            if (getSignature() != null)
                sb.append("signature: ").append(getSignature()).append(",");
            if (getTimestamp() != null)
                sb.append("timestamp: ").append(getTimestamp().toString()).append(",");
            if (getTopicArn() != null)
                sb.append("topicArn: ").append(getTopicArn());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof SNS == false)
                return false;
            SNS other = (SNS) obj;
            if (other.getMessageAttributes() == null ^ this.getMessageAttributes() == null)
                return false;
            if (other.getMessageAttributes() != null && other.getMessageAttributes().equals(this.getMessageAttributes()) == false)
                return false;
            if (other.getSigningCertUrl() == null ^ this.getSigningCertUrl() == null)
                return false;
            if (other.getSigningCertUrl() != null && other.getSigningCertUrl().equals(this.getSigningCertUrl()) == false)
                return false;
            if (other.getMessageId() == null ^ this.getMessageId() == null)
                return false;
            if (other.getMessageId() != null && other.getMessageId().equals(this.getMessageId()) == false)
                return false;
            if (other.getMessage() == null ^ this.getMessage() == null)
                return false;
            if (other.getMessage() != null && other.getMessage().equals(this.getMessage()) == false)
                return false;
            if (other.getSubject() == null ^ this.getSubject() == null)
                return false;
            if (other.getSubject() != null && other.getSubject().equals(this.getSubject()) == false)
                return false;
            if (other.getUnsubscribeUrl() == null ^ this.getUnsubscribeUrl() == null)
                return false;
            if (other.getUnsubscribeUrl() != null && other.getUnsubscribeUrl().equals(this.getUnsubscribeUrl()) == false)
                return false;
            if (other.getType() == null ^ this.getType() == null)
                return false;
            if (other.getType() != null && other.getType().equals(this.getType()) == false)
                return false;
            if (other.getSignatureVersion() == null ^ this.getSignatureVersion() == null)
                return false;
            if (other.getSignatureVersion() != null && other.getSignatureVersion().equals(this.getSignatureVersion()) == false)
                return false;
            if (other.getSignature() == null ^ this.getSignature() == null)
                return false;
            if (other.getSignature() != null && other.getSignature().equals(this.getSignature()) == false)
                return false;
            if (other.getTimestamp() == null ^ this.getTimestamp() == null)
                return false;
            if (other.getTimestamp() != null && other.getTimestamp().equals(this.getTimestamp()) == false)
                return false;
            if (other.getTopicArn() == null ^ this.getTopicArn() == null)
                return false;
            if (other.getTopicArn() != null && other.getTopicArn().equals(this.getTopicArn()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getMessageAttributes() == null) ? 0 : getMessageAttributes().hashCode());
            hashCode = prime * hashCode + ((getSigningCertUrl() == null) ? 0 : getSigningCertUrl().hashCode());
            hashCode = prime * hashCode + ((getMessageId() == null) ? 0 : getMessageId().hashCode());
            hashCode = prime * hashCode + ((getMessage() == null) ? 0 : getMessage().hashCode());
            hashCode = prime * hashCode + ((getSubject() == null) ? 0 : getSubject().hashCode());
            hashCode = prime * hashCode + ((getUnsubscribeUrl() == null) ? 0 : getUnsubscribeUrl().hashCode());
            hashCode = prime * hashCode + ((getType() == null) ? 0 : getType().hashCode());
            hashCode = prime * hashCode + ((getSignatureVersion() == null) ? 0 : getSignatureVersion().hashCode());
            hashCode = prime * hashCode + ((getSignature() == null) ? 0 : getSignature().hashCode());
            hashCode = prime * hashCode + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
            hashCode = prime * hashCode + ((getTopicArn() == null) ? 0 : getTopicArn().hashCode());
            return hashCode;
        }

        @Override
        public SNS clone() {
            try {
                return (SNS) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }
    }

    /**
     * Represents an SNS message record. SNS message records are used to send
     * SNS messages to Lambda Functions.
     *
     */
    public static class SNSRecord implements Serializable, Cloneable {

        private static final long serialVersionUID = -209065548155161859L;

        private SNS sns;

        private String eventVersion;

        private String eventSource;

        private String eventSubscriptionArn;

        /**
         * default constructor
         * (Not available in v1)
         */
        public SNSRecord() {}

        /**
         * Gets the SNS message
         * @return sns body of message
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
         * @param sns SNS message object
         * @return SNSRecord
         */
        public SNSRecord withSns(SNS sns) {
            setSns(sns);
            return this;
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
         * @param eventVersion event version
         * @return SNSRecord
         */
        public SNSRecord withEventVersion(String eventVersion) {
            setEventVersion(eventVersion);
            return this;
        }

        /**
         * Gets the event source
         * @return event source
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
         * @param eventSource event source
         * @return SNSRecord
         */
        public SNSRecord withEventSource(String eventSource) {
            setEventSource(eventSource);
            return this;
        }

        /**
         * Gets the event subscription ARN
         * @return event subscription arn
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

        /**
         * @param eventSubscriptionArn event subscription arn
         * @return SNSRecord
         */
        public SNSRecord withEventSubscriptionArn(String eventSubscriptionArn) {
            setEventSubscriptionArn(eventSubscriptionArn);
            return this;
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
            if (getSNS() != null)
                sb.append("sns: ").append(getSNS().toString()).append(",");
            if (getEventVersion() != null)
                sb.append("eventVersion: ").append(getEventVersion()).append(",");
            if (getEventSource() != null)
                sb.append("eventSource: ").append(getEventSource()).append(",");
            if (getEventSubscriptionArn() != null)
                sb.append("eventSubscriptionArn: ").append(getEventSubscriptionArn());
            sb.append("}");
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if (obj instanceof SNSRecord == false)
                return false;
            SNSRecord other = (SNSRecord) obj;
            if (other.getSNS() == null ^ this.getSNS() == null)
                return false;
            if (other.getSNS() != null && other.getSNS().equals(this.getSNS()) == false)
                return false;
            if (other.getEventVersion() == null ^ this.getEventVersion() == null)
                return false;
            if (other.getEventVersion() != null && other.getEventVersion().equals(this.getEventVersion()) == false)
                return false;
            if (other.getEventSource() == null ^ this.getEventSource() == null)
                return false;
            if (other.getEventSource() != null && other.getEventSource().equals(this.getEventSource()) == false)
                return false;
            if (other.getEventSubscriptionArn() == null ^ this.getEventSubscriptionArn() == null)
                return false;
            if (other.getEventSubscriptionArn() != null && other.getEventSubscriptionArn().equals(this.getEventSubscriptionArn()) == false)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hashCode = 1;

            hashCode = prime * hashCode + ((getSNS() == null) ? 0 : getSNS().hashCode());
            hashCode = prime * hashCode + ((getEventVersion() == null) ? 0 : getEventVersion().hashCode());
            hashCode = prime * hashCode + ((getEventSource() == null) ? 0 : getEventSource().hashCode());
            hashCode = prime * hashCode + ((getEventSubscriptionArn() == null) ? 0 : getEventSubscriptionArn().hashCode());
            return hashCode;
        }

        @Override
        public SNSRecord clone() {
            try {
                return (SNSRecord) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
            }
        }

    }

    /**
     * default constructor
     * (Not available in v1)
     */
    public SNSEvent() {}

    /**
     *  Gets the list of SNS records
     * @return List of records
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

    /**
     * @param records a List of SNSRecords
     * @return SNSEvent
     */
    public SNSEvent withRecords(List<SNSRecord> records) {
        setRecords(records);
        return this;
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

        if (obj instanceof SNSEvent == false)
            return false;
        SNSEvent other = (SNSEvent) obj;
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
    public SNSEvent clone() {
        try {
            return (SNSEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }

}