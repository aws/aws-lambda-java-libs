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

package com.amazonaws.services.lambda.runtime.events.kinesis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an Amazon Kinesis event.
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class KinesisEvent implements Serializable {

    private static final long serialVersionUID = 8145257839787754632L;

    @JsonProperty("Records")
    private List<KinesisEventRecord> records;

    /**
     * The unit of data of an Amazon Kinesis stream
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record implements Serializable {

        private static final long serialVersionUID = 7856672931457425976L;

        private String kinesisSchemaVersion;

        /**
         * <p>
         * The unique identifier of the record within its shard.
         * </p>
         */
        private String sequenceNumber;
        /**
         * <p>
         * The approximate time that the record was inserted into the stream.
         * </p>
         */
        private java.time.Instant approximateArrivalTimestamp;
        /**
         * <p>
         * The data blob. The data in the blob is both opaque and immutable to Kinesis Data Streams, which does not inspect,
         * interpret, or change the data in the blob in any way. When the data blob (the payload before base64-encoding) is
         * added to the partition key size, the total size must not exceed the maximum record size (1 MB).
         * </p>
         * <p>
         * The AWS SDK for Java performs a Base64 encoding on this field before sending this request to the AWS service.
         * Users of the SDK should not perform Base64 encoding on this field.
         * </p>
         * <p>
         * Warning: ByteBuffers returned by the SDK are mutable. Changes to the content or position of the byte buffer will
         * be seen by all objects that have a reference to this object. It is recommended to call ByteBuffer.duplicate() or
         * ByteBuffer.asReadOnlyBuffer() before using or reading from the buffer. This behavior will be changed in a future
         * major version of the SDK.
         * </p>
         */
        private java.nio.ByteBuffer data;
        /**
         * <p>
         * Identifies which shard in the stream the data record is assigned to.
         * </p>
         */
        private String partitionKey;
        /**
         * <p>
         * The encryption type used on the record. This parameter can be one of the following values:
         * </p>
         * <ul>
         * <li>
         * <p>
         * <code>NONE</code>: Do not encrypt the records in the stream.
         * </p>
         * </li>
         * <li>
         * <p>
         * <code>KMS</code>: Use server-side encryption on the records in the stream using a customer-managed AWS KMS key.
         * </p>
         * </li>
         * </ul>
         */
        private String encryptionType;

        public enum EncryptionType {

            NONE("NONE"),
            KMS("KMS");

            private String value;

            private EncryptionType(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return this.value;
            }

            /**
             * Use this in place of valueOf.
             *
             * @param value
             *        real value
             * @return EncryptionType corresponding to the value
             *
             * @throws IllegalArgumentException
             *         If the specified value does not map to one of the known values in this enum.
             */
            public static EncryptionType fromValue(String value) {
                if (value == null || "".equals(value)) {
                    throw new IllegalArgumentException("Value cannot be null or empty!");
                }

                for (EncryptionType enumEntry : EncryptionType.values()) {
                    if (enumEntry.toString().equals(value)) {
                        return enumEntry;
                    }
                }

                throw new IllegalArgumentException("Cannot create enum from " + value + " value!");
            }
        }
    }
    
    /**
     * Kinesis event records provide contextual data about a Kinesis record
     *
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KinesisEventRecord implements Serializable {

        private static final long serialVersionUID = -3855723544907905206L;

        private String eventSource;

        private Record kinesis;

        private String eventID;

        private String invokeIdentityArn;

        private String eventName;

        private String eventVersion;

        private String eventSourceARN;

        private String awsRegion;
    }
}
