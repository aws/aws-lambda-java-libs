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

package com.amazonaws.services.lambda.runtime.events.s3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an AmazonS3 event.
 *
 */
@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class S3Event implements Serializable {

    private static final long serialVersionUID = -8094860465750962044L;

    @JsonProperty("Records")
    private List<S3EventNotificationRecord> records;

    /**
     * default constructor
     * (Not available in v1)
     */
    public S3Event() {
        this.records = new ArrayList<S3EventNotificationRecord>();
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserIdentityEntity {

        private String principalId;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class S3BucketEntity {

        private String name;
        private UserIdentityEntity ownerIdentity;
        private String arn;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class S3ObjectEntity {

        private String key;
        private Long size;

        @JsonProperty("eTag")
        private String eTag;

        private String versionId;
        private String sequencer;

        /**
         * S3 URL encodes the key of the object involved in the event. This is
         * a convenience method to automatically URL decode the key.
         * @return The URL decoded object key.
         */
        public String getUrlDecodedKey() {
            return urlDecode(getKey());
        }

        private static final String DEFAULT_ENCODING = "UTF-8";

        /**
         * Decode a string for use in the path of a URL; uses URLDecoder.decode,
         * which decodes a string for use in the query portion of a URL.
         *
         * @param value The value to decode
         * @return The decoded value if parameter is not null, otherwise, null is returned.
         */
        private static String urlDecode(final String value) {
            if (value == null) {
                return null;
            }

            try {
                return URLDecoder.decode(value, DEFAULT_ENCODING);
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class S3Entity {

        private String configurationId;
        private S3BucketEntity bucket;
        private S3ObjectEntity object;
        private String s3SchemaVersion;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestParametersEntity {

        private String sourceIPAddress;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseElementsEntity {

        @JsonProperty("x-amz-id-2")
        private String xAmzId2;

        @JsonProperty("x-amz-request-id")
        private String xAmzRequestId;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class S3EventNotificationRecord {

        private String awsRegion;
        private String eventName;
        private String eventSource;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
        private Instant eventTime;
        private String eventVersion;
        private RequestParametersEntity requestParameters;
        private ResponseElementsEntity responseElements;
        private S3Entity s3;
        private UserIdentityEntity userIdentity;
    }
}
