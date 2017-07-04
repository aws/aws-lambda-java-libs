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

import static com.amazonaws.services.dynamodbv2.document.internal.InternalUtils.toSimpleMapValue;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.dynamodbv2.model.StreamViewType;

/**
 * <p>
 * An item-centric description of a single data modification that was performed
 * on an item in a DynamoDB table using the DynamoDB Document API.
 * </p>
 */
public class StreamItem implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * The underlying low-level stream record on which this stream item is
     * built.
     */
    private final StreamRecord record;

    /**
     * Internal cache to provide the behavior of access by reference instead of
     * by value.
     */
    private PrimaryKey primaryKey;
    private Item newImage;
    private Item oldImage;

    public StreamItem(StreamRecord record) {
        this.record = record == null ? new StreamRecord() : record;
    }

    public StreamItem() {
        this(new StreamRecord()); // a placeholder
    }

    /**
     * <p>
     * The primary key(s) for the DynamoDB item that was modified.
     * </p>
     *
     * @return The primary key(s) for the DynamoDB item that was modified.
     */
    public PrimaryKey getPrimaryKey() {
        if (primaryKey == null) {
            Map<String, Object> keys = toSimpleMapValue(record.getKeys());
            List<KeyAttribute> keyList = new LinkedList<KeyAttribute>();
            for (Map.Entry<String, Object> e : keys.entrySet())
                keyList.add(new KeyAttribute(e.getKey(), e.getValue()));
            KeyAttribute[] components = keyList
                    .toArray(new KeyAttribute[keyList.size()]);
            primaryKey = new PrimaryKey(components);
            record.clearKeysEntries();
        }
        return primaryKey;
    }

    /**
     * <p>
     * The primary key(s) for the DynamoDB item that was modified.
     * </p>
     *
     * @param primaryKey
     *            The primary key(s) for the DynamoDB item that was
     *            modified.
     */
    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
        record.clearKeysEntries();
    }

    /**
     * <p>
     * The primary key(s) for the DynamoDB item that was modified.
     * </p>
     *
     * @param primaryKey
     *            The primary key(s) for the DynamoDB item that was modified.
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     */
    public StreamItem withPrimaryKey(PrimaryKey primaryKey) {
        setPrimaryKey(primaryKey);
        return this;
    }

    public StreamItem addKeysEntry(String attrName, Object value) {
        PrimaryKey pk = this.getPrimaryKey();
        if (pk.hasComponent(attrName))
            throw new IllegalArgumentException("Duplicated keys (" + attrName
                    + ") are provided.");
        pk.addComponent(attrName, value);
        return this;
    }

    /**
     * Removes all the entries added into Keys. &lt;p> Returns a reference to
     * this object so that method calls can be chained together.
     */
    public StreamItem clearKeysEntries() {
        this.primaryKey = null;
        record.clearKeysEntries();
        return this;
    }

    /**
     * <p>
     * The item in the DynamoDB table as it appeared after it was modified.
     * </p>
     *
     * @return The item in the DynamoDB table as it appeared after it was
     *         modified.
     */
    public Item getNewImage() {
        if (newImage == null) {
            newImage = Item.fromMap(toSimpleMapValue(record.getNewImage()));
            record.clearNewImageEntries();
        }
        return newImage;
    }

    /**
     * <p>
     * The item in the DynamoDB table as it appeared after it was modified.
     * </p>
     *
     * @param newImage
     *            The item in the DynamoDB table as it appeared after it was
     *            modified.
     */
    public void setNewImage(Item newImage) {
        this.newImage = newImage;
        record.clearNewImageEntries();
    }

    /**
     * <p>
     * The item in the DynamoDB table as it appeared after it was modified.
     * </p>
     *
     * @param newImage
     *            The item in the DynamoDB table as it appeared after it was
     *            modified.
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     */
    public StreamItem withNewImage(Item newImage) {
        setNewImage(newImage);
        return this;
    }

    public StreamItem addNewImageEntry(String key, Object value) {
        Item newImage = this.getNewImage();
        if (newImage.hasAttribute(key))
            throw new IllegalArgumentException("Duplicated keys (" + key
                    + ") are provided.");
        newImage.with(key, value);
        return this;
    }

    /**
     * Removes all the entries added into NewImage. &lt;p> Returns a reference
     * to this object so that method calls can be chained together.
     */
    public StreamItem clearNewImageEntries() {
        this.newImage = null;
        record.clearNewImageEntries();
        return this;
    }

    /**
     * <p>
     * The item in the DynamoDB table as it appeared before it was modified.
     * </p>
     *
     * @return The item in the DynamoDB table as it appeared before it was
     *         modified.
     */
    public Item getOldImage() {
        if (oldImage == null) {
            oldImage = Item.fromMap(toSimpleMapValue(record.getOldImage()));
            record.clearOldImageEntries();
        }
        return oldImage;
    }

    /**
     * <p>
     * The item in the DynamoDB table as it appeared before it was modified.
     * </p>
     *
     * @param oldImage
     *            The item in the DynamoDB table as it appeared before it was
     *            modified.
     */
    public void setOldImage(Item oldImage) {
        this.oldImage = oldImage;
        record.clearOldImageEntries();
    }

    /**
     * <p>
     * The item in the DynamoDB table as it appeared before it was modified.
     * </p>
     *
     * @param oldImage
     *            The item in the DynamoDB table as it appeared before it was
     *            modified.
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     */
    public StreamItem withOldImage(Item oldImage) {
        setOldImage(oldImage);
        return this;
    }

    public StreamItem addOldImageEntry(String key, Object value) {
        Item oldImage = this.getOldImage();
        if (oldImage.hasAttribute(key))
            throw new IllegalArgumentException("Duplicated keys (" + key
                    + ") are provided.");
        oldImage.with(key, value);
        return this;
    }

    /**
     * Removes all the entries added into OldImage. &lt;p> Returns a reference
     * to this object so that method calls can be chained together.
     */
    public StreamItem clearOldImageEntries() {
        this.oldImage = null;
        return this;
    }

    /**
     * <p>
     * The sequence number of the stream record.
     * </p>
     *
     * @param sequenceNumber
     *            The sequence number of the stream record.
     */
    public void setSequenceNumber(String sequenceNumber) {
        record.setSequenceNumber(sequenceNumber);
    }

    /**
     * <p>
     * The sequence number of the stream record.
     * </p>
     *
     * @return The sequence number of the stream record.
     */
    public String getSequenceNumber() {
        return record.getSequenceNumber();
    }

    /**
     * <p>
     * The sequence number of the stream record.
     * </p>
     *
     * @param sequenceNumber
     *            The sequence number of the stream record.
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     */
    public StreamItem withSequenceNumber(String sequenceNumber) {
        setSequenceNumber(sequenceNumber);
        return this;
    }

    /**
     * <p>
     * The size of the stream record, in bytes.
     * </p>
     *
     * @param sizeBytes
     *            The size of the stream record, in bytes.
     */
    public void setSizeBytes(Long sizeBytes) {
        record.setSizeBytes(sizeBytes);
    }

    /**
     * <p>
     * The size of the stream record, in bytes.
     * </p>
     *
     * @return The size of the stream record, in bytes.
     */
    public Long getSizeBytes() {
        return record.getSizeBytes();
    }

    /**
     * <p>
     * The size of the stream record, in bytes.
     * </p>
     *
     * @param sizeBytes
     *            The size of the stream record, in bytes.
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     */
    public StreamItem withSizeBytes(Long sizeBytes) {
        setSizeBytes(sizeBytes);
        return this;
    }

    /**
     * <p>
     * The type of data from the modified DynamoDB item that was captured in
     * this stream record:
     * </p>
     * <ul>
     * <li>
     * <p>
     * <code>KEYS_ONLY</code> - only the key attributes of the modified item.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_IMAGE</code> - the entire item, as it appears after it was
     * modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>OLD_IMAGE</code> - the entire item, as it appeared before it was
     * modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_AND_OLD_IMAGES</code> — both the new and the old item images of
     * the item.
     * </p>
     * </li>
     * </ul>
     *
     * @param streamViewType
     *            The type of data from the modified DynamoDB item that was
     *            captured in this stream record:</p>
     *            <ul>
     *            <li>
     *            <p>
     *            <code>KEYS_ONLY</code> - only the key attributes of the
     *            modified item.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>NEW_IMAGE</code> - the entire item, as it appears after
     *            it was modified.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>OLD_IMAGE</code> - the entire item, as it appeared
     *            before it was modified.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>NEW_AND_OLD_IMAGES</code> — both the new and the old
     *            item images of the item.
     *            </p>
     *            </li>
     * @see StreamViewType
     */
    public void setStreamViewType(String streamViewType) {
        record.setStreamViewType(streamViewType);
    }

    /**
     * <p>
     * The type of data from the modified DynamoDB item that was captured in
     * this stream record:
     * </p>
     * <ul>
     * <li>
     * <p>
     * <code>KEYS_ONLY</code> - only the key attributes of the modified item.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_IMAGE</code> - the entire item, as it appears after it was
     * modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>OLD_IMAGE</code> - the entire item, as it appeared before it was
     * modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_AND_OLD_IMAGES</code> — both the new and the old item images of
     * the item.
     * </p>
     * </li>
     * </ul>
     *
     * @return The type of data from the modified DynamoDB item that was
     *         captured in this stream record:</p>
     *         <ul>
     *         <li>
     *         <p>
     *         <code>KEYS_ONLY</code> - only the key attributes of the modified
     *         item.
     *         </p>
     *         </li>
     *         <li>
     *         <p>
     *         <code>NEW_IMAGE</code> - the entire item, as it appears after it
     *         was modified.
     *         </p>
     *         </li>
     *         <li>
     *         <p>
     *         <code>OLD_IMAGE</code> - the entire item, as it appeared before
     *         it was modified.
     *         </p>
     *         </li>
     *         <li>
     *         <p>
     *         <code>NEW_AND_OLD_IMAGES</code> — both the new and the old item
     *         images of the item.
     *         </p>
     *         </li>
     * @see StreamViewType
     */
    public String getStreamViewType() {
        return record.getStreamViewType();
    }

    /**
     * <p>
     * The type of data from the modified DynamoDB item that was captured in
     * this stream record:
     * </p>
     * <ul>
     * <li>
     * <p>
     * <code>KEYS_ONLY</code> - only the key attributes of the modified item.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_IMAGE</code> - the entire item, as it appears after it was
     * modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>OLD_IMAGE</code> - the entire item, as it appeared before it was
     * modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_AND_OLD_IMAGES</code> — both the new and the old item images of
     * the item.
     * </p>
     * </li>
     * </ul>
     *
     * @param streamViewType
     *            The type of data from the modified DynamoDB item that was
     *            captured in this stream record:</p>
     *            <ul>
     *            <li>
     *            <p>
     *            <code>KEYS_ONLY</code> - only the key attributes of the
     *            modified item.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>NEW_IMAGE</code> - the entire item, as it appears after
     *            it was modified.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>OLD_IMAGE</code> - the entire item, as it appeared
     *            before it was modified.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>NEW_AND_OLD_IMAGES</code> — both the new and the old
     *            item images of the item.
     *            </p>
     *            </li>
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     * @see StreamViewType
     */
    public StreamItem withStreamViewType(String streamViewType) {
        setStreamViewType(streamViewType);
        return this;
    }

    /**
     * <p>
     * The type of data from the modified DynamoDB item that was captured in
     * this stream record:
     * </p>
     * <ul>
     * <li>
     * <p>
     * <code>KEYS_ONLY</code> - only the key attributes of the modified item.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_IMAGE</code> - the entire item, as it appears after it was
     * modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>OLD_IMAGE</code> - the entire item, as it appeared before it was
     * modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_AND_OLD_IMAGES</code> — both the new and the old item images of
     * the item.
     * </p>
     * </li>
     * </ul>
     *
     * @param streamViewType
     *            The type of data from the modified DynamoDB item that was
     *            captured in this stream record:</p>
     *            <ul>
     *            <li>
     *            <p>
     *            <code>KEYS_ONLY</code> - only the key attributes of the
     *            modified item.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>NEW_IMAGE</code> - the entire item, as it appears after
     *            it was modified.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>OLD_IMAGE</code> - the entire item, as it appeared
     *            before it was modified.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>NEW_AND_OLD_IMAGES</code> — both the new and the old
     *            item images of the item.
     *            </p>
     *            </li>
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     * @see StreamViewType
     */
    public void setStreamViewType(StreamViewType streamViewType) {
        record.setStreamViewType(streamViewType);
    }

    /**
     * <p>
     * The type of data from the modified DynamoDB item that was captured in
     * this stream record:
     * </p>
     * <ul>
     * <li>
     * <p>
     * <code>KEYS_ONLY</code> - only the key attributes of the modified item.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_IMAGE</code> - the entire item, as it appears after it was
     * modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>OLD_IMAGE</code> - the entire item, as it appeared before it was
     * modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_AND_OLD_IMAGES</code> — both the new and the old item images of
     * the item.
     * </p>
     * </li>
     * </ul>
     *
     * @param streamViewType
     *            The type of data from the modified DynamoDB item that was
     *            captured in this stream record:</p>
     *            <ul>
     *            <li>
     *            <p>
     *            <code>KEYS_ONLY</code> - only the key attributes of the
     *            modified item.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>NEW_IMAGE</code> - the entire item, as it appears after
     *            it was modified.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>OLD_IMAGE</code> - the entire item, as it appeared
     *            before it was modified.
     *            </p>
     *            </li>
     *            <li>
     *            <p>
     *            <code>NEW_AND_OLD_IMAGES</code> — both the new and the old
     *            item images of the item.
     *            </p>
     *            </li>
     * @return Returns a reference to this object so that method calls can be
     *         chained together.
     * @see StreamViewType
     */
    public StreamItem withStreamViewType(StreamViewType streamViewType) {
        setStreamViewType(streamViewType);
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
        if (getPrimaryKey() != null)
            sb.append("Keys: " + getPrimaryKey() + ",");
        if (getNewImage() != null)
            sb.append("NewImage: " + getNewImage() + ",");
        if (getOldImage() != null)
            sb.append("OldImage: " + getOldImage() + ",");
        if (getSequenceNumber() != null)
            sb.append("SequenceNumber: " + getSequenceNumber() + ",");
        if (getSizeBytes() != null)
            sb.append("SizeBytes: " + getSizeBytes() + ",");
        if (getStreamViewType() != null)
            sb.append("StreamViewType: " + getStreamViewType());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;

        if (obj instanceof StreamItem == false)
            return false;
        StreamItem other = (StreamItem) obj;
        if (other.getPrimaryKey() == null ^ this.getPrimaryKey() == null)
            return false;
        if (other.getPrimaryKey() != null
                && other.getPrimaryKey().equals(this.getPrimaryKey()) == false)
            return false;
        if (other.getNewImage() == null ^ this.getNewImage() == null)
            return false;
        if (other.getNewImage() != null
                && other.getNewImage().equals(this.getNewImage()) == false)
            return false;
        if (other.getOldImage() == null ^ this.getOldImage() == null)
            return false;
        if (other.getOldImage() != null
                && other.getOldImage().equals(this.getOldImage()) == false)
            return false;
        if (other.getSequenceNumber() == null
                ^ this.getSequenceNumber() == null)
            return false;
        if (other.getSequenceNumber() != null
                && other.getSequenceNumber().equals(this.getSequenceNumber()) == false)
            return false;
        if (other.getSizeBytes() == null ^ this.getSizeBytes() == null)
            return false;
        if (other.getSizeBytes() != null
                && other.getSizeBytes().equals(this.getSizeBytes()) == false)
            return false;
        if (other.getStreamViewType() == null
                ^ this.getStreamViewType() == null)
            return false;
        if (other.getStreamViewType() != null
                && other.getStreamViewType().equals(this.getStreamViewType()) == false)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;

        hashCode = prime * hashCode
                + ((getPrimaryKey() == null) ? 0 : getPrimaryKey().hashCode());
        hashCode = prime * hashCode
                + ((getNewImage() == null) ? 0 : getNewImage().hashCode());
        hashCode = prime * hashCode
                + ((getOldImage() == null) ? 0 : getOldImage().hashCode());
        hashCode = prime
                * hashCode
                + ((getSequenceNumber() == null) ? 0 : getSequenceNumber()
                        .hashCode());
        hashCode = prime * hashCode
                + ((getSizeBytes() == null) ? 0 : getSizeBytes().hashCode());
        hashCode = prime
                * hashCode
                + ((getStreamViewType() == null) ? 0 : getStreamViewType()
                        .hashCode());
        return hashCode;
    }

    @Override
    public StreamItem clone() {
        try {
            return (StreamItem) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(
                    "Got a CloneNotSupportedException from Object.clone() "
                            + "even though we're Cloneable!", e);
        }
    }
}