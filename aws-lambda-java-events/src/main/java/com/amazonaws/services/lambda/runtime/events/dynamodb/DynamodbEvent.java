/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
import java.util.List;

/**
 * Represents an Amazon DynamoDB event
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class DynamodbEvent implements Serializable {

    private static final long serialVersionUID = -2354616079899981231L;

    @JsonProperty("Records")
    private List<DynamodbStreamRecord> records;

    /**
     * The unit of data of an Amazon DynamoDB event
     */
    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DynamodbStreamRecord implements Serializable {

        private static final long serialVersionUID = 3638381544604354963L;

        private String eventSourceARN;

        /**
         * <p>
         * A globally unique identifier for the event that was recorded in this stream record.
         * </p>
         */
        private String eventID;

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
         * <code>MODIFY</code> - one or more of an existing item's attributes were modified.
         * </p>
         * </li>
         * <li>
         * <p>
         * <code>REMOVE</code> - the item was deleted from the table
         * </p>
         * </li>
         * </ul>
         */
        private String eventName;

        /**
         * <p>
         * The version number of the stream record format. This number is updated whenever the structure of
         * <code>Record</code> is modified.
         * </p>
         * <p>
         * Client applications must not assume that <code>eventVersion</code> will remain at a particular value, as this
         * number is subject to change at any time. In general, <code>eventVersion</code> will only increase as the
         * low-level DynamoDB Streams API evolves.
         * </p>
         */
        private String eventVersion;

        /**
         * <p>
         * The AWS service from which the stream record originated. For DynamoDB Streams, this is <code>aws:dynamodb</code>.
         * </p>
         */
        private String eventSource;

        /**
         * <p>
         * The region in which the <code>GetRecords</code> request was received.
         * </p>
         */
        private String awsRegion;

        /**
         * <p>
         * The main body of the stream record, containing all of the DynamoDB-specific fields.
         * </p>
         */
        private StreamRecord dynamodb;

        /**
         * <p>
         * Items that are deleted by the Time to Live process after expiration have the following fields:
         * </p>
         * <ul>
         * <li>
         * <p>
         * Records[].userIdentity.type
         * </p>
         * <p>
         * "Service"
         * </p>
         * </li>
         * <li>
         * <p>
         * Records[].userIdentity.principalId
         * </p>
         * <p>
         * "dynamodb.amazonaws.com"
         * </p>
         * </li>
         * </ul>
         */
        private Identity userIdentity;

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
         * <code>MODIFY</code> - one or more of an existing item's attributes were modified.
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
         *        The type of data modification that was performed on the DynamoDB table:</p>
         *        <ul>
         *        <li>
         *        <p>
         *        <code>INSERT</code> - a new item was added to the table.
         *        </p>
         *        </li>
         *        <li>
         *        <p>
         *        <code>MODIFY</code> - one or more of an existing item's attributes were modified.
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
            this.eventName = eventName;
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
         * <code>MODIFY</code> - one or more of an existing item's attributes were modified.
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
         *        The type of data modification that was performed on the DynamoDB table:</p>
         *        <ul>
         *        <li>
         *        <p>
         *        <code>INSERT</code> - a new item was added to the table.
         *        </p>
         *        </li>
         *        <li>
         *        <p>
         *        <code>MODIFY</code> - one or more of an existing item's attributes were modified.
         *        </p>
         *        </li>
         *        <li>
         *        <p>
         *        <code>REMOVE</code> - the item was deleted from the table
         *        </p>
         *        </li>
         * @see OperationType
         */
        public void setEventName(OperationType eventName) {
            this.eventName = eventName.toString();
        }
    }
}
