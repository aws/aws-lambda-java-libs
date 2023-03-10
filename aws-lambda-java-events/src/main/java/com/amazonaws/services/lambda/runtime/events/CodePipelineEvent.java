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

package com.amazonaws.services.lambda.runtime.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an CodePipeline event sent to Lambda.
 * See: <a href="https://docs.aws.amazon.com/codepipeline/latest/userguide/actions-invoke-lambda-function.html">Invoke an AWS Lambda function in a pipeline in CodePipeline</a>
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class CodePipelineEvent implements Serializable {

    private static final long serialVersionUID = -4828716548429210697L;

    @JsonProperty("CodePipeline.job")
    private Job codePipelineJob;

    @lombok.Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Job implements Serializable {

        private static final long serialVersionUID = 2211711169692638977L;

        private String id;
        private String accountId;
        private Data data;
    }

    @lombok.Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data implements Serializable {

        private static final long serialVersionUID = 8786599041834868262L;

        private ActionConfiguration actionConfiguration;
        private List<Artifact> inputArtifacts;
        private List<Artifact> outputArtifacts;
        private ArtifactCredentials artifactCredentials;
        private String continuationToken;
        private EncryptionKey encryptionKey;
    }

    @lombok.Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionConfiguration implements Serializable {

        private static final long serialVersionUID = -7285651174501621217L;

        private Configuration configuration;
    }

    @lombok.Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Configuration implements Serializable {

        private static final long serialVersionUID = 580024317691702894L;

        @JsonProperty("FunctionName")
        private String functionName;

        @JsonProperty("UserParameters")
        private String userParameters;
    }

    @lombok.Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Artifact implements Serializable {

        private static final long serialVersionUID = 6406621244704594358L;

        private String name;
        private String revision;
        private Location location;

        @JsonInclude
        public String getRevision() {
            return revision;
        }

        @JsonInclude
        public void setRevision(String revision) {
            this.revision = revision;
        }
    }

    @lombok.Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location implements Serializable {

        private static final long serialVersionUID = 149382199413534713L;

        private String type;
        private S3Location s3Location;
    }

    @lombok.Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class S3Location implements Serializable {

        private static final long serialVersionUID = -8922449809993769709L;

        private String bucketName;
        private String objectKey;
    }

    @lombok.Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtifactCredentials implements Serializable {

        private static final long serialVersionUID = 7710347495607396747L;

        private String accessKeyId;
        private String secretAccessKey;
        private String sessionToken;
    }

    @lombok.Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EncryptionKey implements Serializable {

        private static final long serialVersionUID = -9105569908901180610L;

        String id;
        String type;
    }
}
