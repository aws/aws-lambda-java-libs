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

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Created by adsuresh on 7/13/17.
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class KinesisFirehoseEvent implements Serializable {

    private static final long serialVersionUID = -2890373471008001695L;

    private String invocationId;

    private String deliveryStreamArn;

    private String region;

    private List<Record> records;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record implements Serializable {

        private static final long serialVersionUID = -7231161900431910379L;

        /**
         * <p>
         * The data blob, which is base64-encoded when the blob is serialized. The maximum size of the data blob, before
         * base64-encoding, is 1,000 KB.
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
        private ByteBuffer data;

        private String recordId;

        private Long approximateArrivalEpoch;

        private Long approximateArrivalTimestamp;

        private Map<String, String> kinesisRecordMetadata;

    }
}
