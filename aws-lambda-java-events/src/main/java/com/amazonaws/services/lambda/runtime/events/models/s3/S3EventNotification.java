/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
package com.amazonaws.services.lambda.runtime.events.models.s3;

import org.joda.time.DateTime;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
* A helper class that represents a strongly typed S3 EventNotification item sent
* to SQS, SNS, or Lambda.
*/
public class S3EventNotification {

    private final List<S3EventNotificationRecord> records;

    public S3EventNotification(List<S3EventNotificationRecord> records) {
        this.records = records;
    }

    /**
     * @return the records in this notification
     */
    public List<S3EventNotificationRecord> getRecords() {
        return records;
    }


    public static class UserIdentityEntity {

        private final String principalId;

        public UserIdentityEntity(String principalId) {
            this.principalId = principalId;
        }

        public String getPrincipalId() {
            return principalId;
        }
    }

    public static class S3BucketEntity {

        private final String name;
        private final UserIdentityEntity ownerIdentity;
        private final String arn;

        public S3BucketEntity(String name, UserIdentityEntity ownerIdentity, String arn) {
            this.name = name;
            this.ownerIdentity = ownerIdentity;
            this.arn = arn;
        }

        public String getName() {
            return name;
        }

        public UserIdentityEntity getOwnerIdentity() {
            return ownerIdentity;
        }

        public String getArn() {
            return arn;
        }
    }

    public static class S3ObjectEntity {

        private final String key;
        private final Long size;
        private final String eTag;
        private final String versionId;
        private final String sequencer;

        @Deprecated
        public S3ObjectEntity(
                String key,
                Integer size,
                String eTag,
                String versionId)
        {
            this.key = key;
            this.size = size == null ? null : size.longValue();
            this.eTag = eTag;
            this.versionId = versionId;
            this.sequencer = null;
        }

        @Deprecated
        public S3ObjectEntity(
                String key,
                Long size,
                String eTag,
                String versionId)
        {
            this(key, size, eTag, versionId, null);
        }

        public S3ObjectEntity(String key, Long size, String eTag, String versionId, String sequencer) {
            this.key = key;
            this.size = size;
            this.eTag = eTag;
            this.versionId = versionId;
            this.sequencer = sequencer;
        }

        public String getKey() {
            return key;
        }

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

        /**
         * @deprecated use {@link #getSizeAsLong()} instead.
         */
        @Deprecated
        public Integer getSize() {
            return size == null ? null : size.intValue();
        }

        public Long getSizeAsLong() {
            return size;
        }

        public String geteTag() {
            return eTag;
        }

        public String getVersionId() {
            return versionId;
        }

        public String getSequencer() {
            return sequencer;
        }
    }

    public static class S3Entity {

        private final String configurationId;
        private final S3BucketEntity bucket;
        private final S3ObjectEntity object;
        private final String s3SchemaVersion;

        public S3Entity(String configurationId, S3BucketEntity bucket, S3ObjectEntity object, String s3SchemaVersion) {
            this.configurationId = configurationId;
            this.bucket = bucket;
            this.object = object;
            this.s3SchemaVersion = s3SchemaVersion;
        }

        public String getConfigurationId() {
            return configurationId;
        }

        public S3BucketEntity getBucket() {
            return bucket;
        }

        public S3ObjectEntity getObject() {
            return object;
        }

        public String getS3SchemaVersion() {
            return s3SchemaVersion;
        }
    }

    public static class RequestParametersEntity {

        private final String sourceIPAddress;

        public RequestParametersEntity(String sourceIPAddress) {
            this.sourceIPAddress = sourceIPAddress;
        }

        public String getSourceIPAddress() {
            return sourceIPAddress;
        }
    }

    public static class ResponseElementsEntity {

        private final String xAmzId2;
        private final String xAmzRequestId;

        public ResponseElementsEntity(String xAmzId2, String xAmzRequestId)
        {
            this.xAmzId2 = xAmzId2;
            this.xAmzRequestId = xAmzRequestId;
        }

        public String getxAmzId2() {
            return xAmzId2;
        }

        public String getxAmzRequestId() {
            return xAmzRequestId;
        }
    }

    public static class S3EventNotificationRecord {

        private final String awsRegion;
        private final String eventName;
        private final String eventSource;
        private DateTime eventTime;
        private final String eventVersion;
        private final RequestParametersEntity requestParameters;
        private final ResponseElementsEntity responseElements;
        private final S3Entity s3;
        private final UserIdentityEntity userIdentity;

        public S3EventNotificationRecord(String awsRegion, String eventName, String eventSource, String eventTime,
                                         String eventVersion, RequestParametersEntity requestParameters,
                                         ResponseElementsEntity responseElements, S3Entity s3,
                                         UserIdentityEntity userIdentity) {
            this.awsRegion = awsRegion;
            this.eventName = eventName;
            this.eventSource = eventSource;

            if (eventTime != null)
            {
                this.eventTime = DateTime.parse(eventTime);
            }

            this.eventVersion = eventVersion;
            this.requestParameters = requestParameters;
            this.responseElements = responseElements;
            this.s3 = s3;
            this.userIdentity = userIdentity;
        }

        public String getAwsRegion() {
            return awsRegion;
        }

        public String getEventName() {
            return eventName;
        }

        public String getEventSource() {
            return eventSource;
        }

        public DateTime getEventTime() {
            return eventTime;
        }

        public String getEventVersion() {
            return eventVersion;
        }

        public RequestParametersEntity getRequestParameters() {
            return requestParameters;
        }

        public ResponseElementsEntity getResponseElements() {
            return responseElements;
        }

        public S3Entity getS3() {
            return s3;
        }

        public UserIdentityEntity getUserIdentity() {
            return userIdentity;
        }
    }
}
