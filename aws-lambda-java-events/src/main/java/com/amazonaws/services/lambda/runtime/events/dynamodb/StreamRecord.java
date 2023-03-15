/*
 * Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

package com.amazonaws.services.lambda.runtime.events.dynamodb;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


/**
* <p>
* A description of a single data modification that was performed on an item in a DynamoDB table.
* </p>
*
* @see <a href="http://docs.aws.amazon.com/goto/WebAPI/streams-dynamodb-2012-08-10/StreamRecord" target="_top">AWS API
*      Documentation</a>
*/

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class StreamRecord implements Serializable {

    /**
     * <p>
     * The approximate date and time when the stream record was created, in <a
     * href="http://www.epochconverter.com/">UNIX epoch time</a> format.
     * </p>
     */
    @JsonProperty("ApproximateCreationDateTime")
    private java.time.Instant approximateCreationDateTime;

    /**
     * <p>
     * The primary key attribute(s) for the DynamoDB item that was modified.
     * </p>
     */
    @JsonProperty("Keys")
    private java.util.Map<String, AttributeValue> keys;

    /**
     * <p>
     * The item in the DynamoDB table as it appeared after it was modified.
     * </p>
     */
    @JsonProperty("NewImage")
    private java.util.Map<String, AttributeValue> newImage;

    /**
     * <p>
     * The item in the DynamoDB table as it appeared before it was modified.
     * </p>
     */
    @JsonProperty("OldImage")
    private java.util.Map<String, AttributeValue> oldImage;

    /**
     * <p>
     * The sequence number of the stream record.
     * </p>
     */
    @JsonProperty("SequenceNumber")
    private String sequenceNumber;

    /**
     * <p>
     * The size of the stream record, in bytes.
     * </p>
     */
    @JsonProperty("SizeBytes")
    private Long sizeBytes;

    /**
     * <p>
     * The type of data from the modified DynamoDB item that was captured in this stream record:
     * </p>
     * <ul>
     * <li>
     * <p>
     * <code>KEYS_ONLY</code> - only the key attributes of the modified item.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_IMAGE</code> - the entire item, as it appeared after it was modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>OLD_IMAGE</code> - the entire item, as it appeared before it was modified.
     * </p>
     * </li>
     * <li>
     * <p>
     * <code>NEW_AND_OLD_IMAGES</code> - both the new and the old item images of the item.
     * </p>
     * </li>
     * </ul>
     */
    @JsonProperty("StreamViewType")
    private StreamViewType streamViewType;

    public StreamRecord addKeysEntry(String key, AttributeValue value) {
        if (null == this.keys) {
            this.keys = new java.util.HashMap<String, AttributeValue>();
        }
        if (this.keys.containsKey(key))
            throw new IllegalArgumentException("Duplicated keys (" + key.toString() + ") are provided.");
        this.keys.put(key, value);
        return this;
    }

    /**
     * Removes all the entries added into Keys.
     *
     * @return Returns a reference to this object so that method calls can be chained together.
     */
    public StreamRecord clearKeysEntries() {
        this.keys = null;
        return this;
    }

    public StreamRecord addNewImageEntry(String key, AttributeValue value) {
        if (null == this.newImage) {
            this.newImage = new java.util.HashMap<String, AttributeValue>();
        }
        if (this.newImage.containsKey(key))
            throw new IllegalArgumentException("Duplicated keys (" + key.toString() + ") are provided.");
        this.newImage.put(key, value);
        return this;
    }

    /**
     * Removes all the entries added into NewImage.
     *
     * @return Returns a reference to this object so that method calls can be chained together.
     */
    public StreamRecord clearNewImageEntries() {
        this.newImage = null;
        return this;
    }

    public StreamRecord addOldImageEntry(String key, AttributeValue value) {
        if (null == this.oldImage) {
            this.oldImage = new java.util.HashMap<String, AttributeValue>();
        }
        if (this.oldImage.containsKey(key))
            throw new IllegalArgumentException("Duplicated keys (" + key.toString() + ") are provided.");
        this.oldImage.put(key, value);
        return this;
    }

    /**
     * Removes all the entries added into OldImage.
     *
     * @return Returns a reference to this object so that method calls can be chained together.
     */
    public StreamRecord clearOldImageEntries() {
        this.oldImage = null;
        return this;
    }
}
