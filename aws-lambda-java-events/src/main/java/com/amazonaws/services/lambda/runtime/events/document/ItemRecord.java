/*
 * Copyright 2010-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.services.lambda.runtime.events.document;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.model.OperationType;
import com.amazonaws.services.dynamodbv2.model.Record;

/**
 * <p>
 * An item-centric description of an unique event within a stream.
 * </p>
 */
public class ItemRecord implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    /**
     * The low-level record on which this item record is built.
     */
    private final Record record;

    /**
     * <p>
     * The main body of the stream item, containing all of the
     * DynamoDB-specific fields.
     * </p>
     */
    private StreamItem streamItem;

    public ItemRecord(Record record) {
        if (this.record == null)
            throw new IllegalArgumentException("record must be specified");
        this.record = record;
    }

    /**
     * <p>
     * A globally unique identifier for the event that was recorded in this
     * stream record.
     * </p>
     *
     * @param eventID
     *        A globally unique identifier for the event that was recorded in
     *        this stream record.
     */
    public void setEventID(String eventID) {
        record.setEventID(eventID);
    }

    /**
     * <p>
     * A globally unique identifier for the event that was recorded in this
     * stream record.
     * </p>
     *
     * @return A globally unique identifier for the event that was recorded in
     *         this stream record.
     */
    public String getEventID() {
        return record.getEventID();
    }

    /**
     * <p>
     * A globally unique identifier for the event that was recorded in this
     * stream record.
     * </p>
     *
     * @param eventID
     *        A globally unique identifier for the event that was recorded in
     *        this stream record.
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     */
    public ItemRecord withEventID(String eventID) {
        setEventID(eventID);
        return this;
    }

    /**
     * <p>
     * The type of data modification that was performed on the DynamoDB table:
     * </p>
     * <ul>
     * <li>
     * <p>
     * <code>INSERT</code> - a new item was added to the table.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>MODIFY</code> - one or more of the item's attributes were updated.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>REMOVE</code> - the item was deleted from the table
     * </p>
     * </li>
     * </ul>
     *
     * @param eventName
     *        The type of data modification that was performed on the DynamoDB
     *        table:</p>
     *        <ul>
     *        <li>
     *        <p>
     *        <code>INSERT</code> - a new item was added to the table.
     *        </p>
     *        </li>
     *        <li>
     *        <p>
     *        <code>MODIFY</code> - one or more of the item's attributes were
     *        updated.
     *        </p>
     *        </li>
     *        <li>
     *        <p>
     *        <code>REMOVE</code> - the item was deleted from the table
     *        </p>
     *        </li>
     * @see OperationType
     */
    public void setEventName(String eventName) {
        record.setEventName(eventName);
    }

    /**
     * <p>
     * The type of data modification that was performed on the DynamoDB table:
     * </p>
     * <ul>
     * <li>
     * <p>
     * <code>INSERT</code> - a new item was added to the table.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>MODIFY</code> - one or more of the item's attributes were updated.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>REMOVE</code> - the item was deleted from the table
     * </p>
     * </li>
     * </ul>
     *
     * @return The type of data modification that was performed on the DynamoDB
     *         table:</p>
     *         <ul>
     *         <li>
     *         <p>
     *         <code>INSERT</code> - a new item was added to the table.
     *         </p>
     *         </li>
     *         <li>
     *         <p>
     *         <code>MODIFY</code> - one or more of the item's attributes were
     *         updated.
     *         </p>
     *         </li>
     *         <li>
     *         <p>
     *         <code>REMOVE</code> - the item was deleted from the table
     *         </p>
     *         </li>
     * @see OperationType
     */
    public String getEventName() {
        return record.getEventName();
    }

    /**
     * <p>
     * The type of data modification that was performed on the DynamoDB table:
     * </p>
     * <ul>
     * <li>
     * <p>
     * <code>INSERT</code> - a new item was added to the table.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>MODIFY</code> - one or more of the item's attributes were updated.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>REMOVE</code> - the item was deleted from the table
     * </p>
     * </li>
     * </ul>
     *
     * @param eventName
     *        The type of data modification that was performed on the DynamoDB
     *        table:</p>
     *        <ul>
     *        <li>
     *        <p>
     *        <code>INSERT</code> - a new item was added to the table.
     *        </p>
     *        </li>
     *        <li>
     *        <p>
     *        <code>MODIFY</code> - one or more of the item's attributes were
     *        updated.
     *        </p>
     *        </li>
     *        <li>
     *        <p>
     *        <code>REMOVE</code> - the item was deleted from the table
     *        </p>
     *        </li>
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     * @see OperationType
     */
    public ItemRecord withEventName(String eventName) {
        setEventName(eventName);
        return this;
    }

    /**
     * <p>
     * The type of data modification that was performed on the DynamoDB table:
     * </p>
     * <ul>
     * <li>
     * <p>
     * <code>INSERT</code> - a new item was added to the table.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>MODIFY</code> - one or more of the item's attributes were updated.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>REMOVE</code> - the item was deleted from the table
     * </p>
     * </li>
     * </ul>
     *
     * @param eventName
     *        The type of data modification that was performed on the DynamoDB
     *        table:</p>
     *        <ul>
     *        <li>
     *        <p>
     *        <code>INSERT</code> - a new item was added to the table.
     *        </p>
     *        </li>
     *        <li>
     *        <p>
     *        <code>MODIFY</code> - one or more of the item's attributes were
     *        updated.
     *        </p>
     *        </li>
     *        <li>
     *        <p>
     *        <code>REMOVE</code> - the item was deleted from the table
     *        </p>
     *        </li>
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     * @see OperationType
     */
    public void setEventName(OperationType eventName) {
        record.setEventName(eventName);
    }

    /**
     * <p>
     * The type of data modification that was performed on the DynamoDB table:
     * </p>
     * <ul>
     * <li>
     * <p>
     * <code>INSERT</code> - a new item was added to the table.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>MODIFY</code> - one or more of the item's attributes were updated.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>REMOVE</code> - the item was deleted from the table
     * </p>
     * </li>
     * </ul>
     *
     * @param eventName
     *        The type of data modification that was performed on the DynamoDB
     *        table:</p>
     *        <ul>
     *        <li>
     *        <p>
     *        <code>INSERT</code> - a new item was added to the table.
     *        </p>
     *        </li>
     *        <li>
     *        <p>
     *        <code>MODIFY</code> - one or more of the item's attributes were
     *        updated.
     *        </p>
     *        </li>
     *        <li>
     *        <p>
     *        <code>REMOVE</code> - the item was deleted from the table
     *        </p>
     *        </li>
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     * @see OperationType
     */
    public ItemRecord withEventName(OperationType eventName) {
        setEventName(eventName);
        return this;
    }

    /**
     * <p>
     * The version number of the stream record format. Currently, this is
     * <i>1.0</i>.
     * </p>
     *
     * @param eventVersion
     *        The version number of the stream record format. Currently, this is
     *        <i>1.0</i>.
     */
    public void setEventVersion(String eventVersion) {
        record.setEventVersion(eventVersion);
    }

    /**
     * <p>
     * The version number of the stream record format. Currently, this is
     * <i>1.0</i>.
     * </p>
     *
     * @return The version number of the stream record format. Currently, this
     *         is <i>1.0</i>.
     */
    public String getEventVersion() {
        return record.getEventVersion();
    }

    /**
     * <p>
     * The version number of the stream record format. Currently, this is
     * <i>1.0</i>.
     * </p>
     *
     * @param eventVersion
     *        The version number of the stream record format. Currently, this is
     *        <i>1.0</i>.
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     */
    public ItemRecord withEventVersion(String eventVersion) {
        setEventVersion(eventVersion);
        return this;
    }

    /**
     * <p>
     * The AWS service from which the stream record originated. For DynamoDB
     * Streams, this is <i>aws:dynamodb</i>.
     * </p>
     *
     * @param eventSource
     *        The AWS service from which the stream record originated. For
     *        DynamoDB Streams, this is <i>aws:dynamodb</i>.
     */
    public void setEventSource(String eventSource) {
        record.setEventSource(eventSource);
    }

    /**
     * <p>
     * The AWS service from which the stream record originated. For DynamoDB
     * Streams, this is <i>aws:dynamodb</i>.
     * </p>
     *
     * @return The AWS service from which the stream record originated. For
     *         DynamoDB Streams, this is <i>aws:dynamodb</i>.
     */
    public String getEventSource() {
        return record.getEventSource();
    }

    /**
     * <p>
     * The AWS service from which the stream record originated. For DynamoDB
     * Streams, this is <i>aws:dynamodb</i>.
     * </p>
     *
     * @param eventSource
     *        The AWS service from which the stream record originated. For
     *        DynamoDB Streams, this is <i>aws:dynamodb</i>.
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     */
    public ItemRecord withEventSource(String eventSource) {
        setEventSource(eventSource);
        return this;
    }

    /**
     * <p>
     * The region in which the <i>GetRecords</i> request was received.
     * </p>
     *
     * @param awsRegion
     *        The region in which the <i>GetRecords</i> request was received.
     */
    public void setAwsRegion(String awsRegion) {
        record.setAwsRegion(awsRegion);
    }

    /**
     * <p>
     * The region in which the <i>GetRecords</i> request was received.
     * </p>
     *
     * @return The region in which the <i>GetRecords</i> request was received.
     */
    public String getAwsRegion() {
        return record.getAwsRegion();
    }

    /**
     * <p>
     * The region in which the <i>GetRecords</i> request was received.
     * </p>
     *
     * @param awsRegion
     *        The region in which the <i>GetRecords</i> request was received.
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     */
    public ItemRecord withAwsRegion(String awsRegion) {
        setAwsRegion(awsRegion);
        return this;
    }

    /**
     * <p>
     * The main body of the stream item, containing all of the
     * DynamoDB-specific fields.
     * </p>
     *
     * @param streamItem
     *        The main body of the stream item, containing all of the
     *        DynamoDB-specific fields.
     */
    public void setStreamItem(StreamItem streamItem) {
        this.streamItem = streamItem;
        this.record.setDynamodb(null);
    }

    /**
     * <p>
     * The main body of the stream item, containing all of the
     * DynamoDB-specific fields.
     * </p>
     *
     * @return The main body of the stream item, containing all of the
     *         DynamoDB-specific fields.
     */
    public StreamItem getStreamItem() {
        if (streamItem == null)
            streamItem = new StreamItem(record.getDynamodb());
        return streamItem;
    }

    /**
     * <p>
     * The main body of the stream item, containing all of the
     * DynamoDB-specific fields.
     * </p>
     *
     * @param dynamodb
     *        The main body of the stream item, containing all of the
     *        DynamoDB-specific fields.
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     */
    public ItemRecord withStreamItem(StreamItem streamItem) {
        setStreamItem(streamItem);
        return this;
    }

    /**
     * Returns a string representation of this object; useful for testing and
     * debugging.
     *
     * @return A string representation of this object.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getEventID() != null)
            sb.append("EventID: " + getEventID() + ",");
        if (getEventName() != null)
            sb.append("EventName: " + getEventName() + ",");
        if (getEventVersion() != null)
            sb.append("EventVersion: " + getEventVersion() + ",");
        if (getEventSource() != null)
            sb.append("EventSource: " + getEventSource() + ",");
        if (getAwsRegion() != null)
            sb.append("AwsRegion: " + getAwsRegion() + ",");
        if (getStreamItem() != null)
            sb.append("StreamItem: " + getStreamItem());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof ItemRecord == false)
            return false;
        ItemRecord other = (ItemRecord) obj;
        if (other.getEventID() == null ^ this.getEventID() == null)
            return false;
        if (other.getEventID() != null
                && other.getEventID().equals(this.getEventID()) == false)
            return false;
        if (other.getEventName() == null ^ this.getEventName() == null)
            return false;
        if (other.getEventName() != null
                && other.getEventName().equals(this.getEventName()) == false)
            return false;
        if (other.getEventVersion() == null ^ this.getEventVersion() == null)
            return false;
        if (other.getEventVersion() != null
                && other.getEventVersion().equals(this.getEventVersion()) == false)
            return false;
        if (other.getEventSource() == null ^ this.getEventSource() == null)
            return false;
        if (other.getEventSource() != null
                && other.getEventSource().equals(this.getEventSource()) == false)
            return false;
        if (other.getAwsRegion() == null ^ this.getAwsRegion() == null)
            return false;
        if (other.getAwsRegion() != null
                && other.getAwsRegion().equals(this.getAwsRegion()) == false)
            return false;
        if (other.getStreamItem() == null ^ this.getStreamItem() == null)
            return false;
        if (other.getStreamItem() != null
                && other.getStreamItem().equals(this.getStreamItem()) == false)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode
                + ((getEventID() == null) ? 0 : getEventID().hashCode());
        hashCode = prime * hashCode
                + ((getEventName() == null) ? 0 : getEventName().hashCode());
        hashCode = prime
                * hashCode
                + ((getEventVersion() == null) ? 0 : getEventVersion()
                        .hashCode());
        hashCode = prime
                * hashCode
                + ((getEventSource() == null) ? 0 : getEventSource().hashCode());
        hashCode = prime * hashCode
                + ((getAwsRegion() == null) ? 0 : getAwsRegion().hashCode());
        hashCode = prime * hashCode
                + ((getStreamItem() == null) ? 0 : getStreamItem().hashCode());
        return hashCode;
    }

    @Override
    public ItemRecord clone() {
        try {
            return (ItemRecord) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(
                    "Got a CloneNotSupportedException from Object.clone() "
                            + "even though we're Cloneable!", e);
        }
    }
}