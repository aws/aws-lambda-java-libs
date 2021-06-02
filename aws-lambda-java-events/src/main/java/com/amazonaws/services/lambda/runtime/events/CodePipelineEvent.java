package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an CodePipeline event sent to Lambda.
 * See: <a href="https://docs.aws.amazon.com/codepipeline/latest/userguide/actions-invoke-lambda-function.html">Invoke an AWS Lambda function in a pipeline in CodePipeline</a>
 */
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class CodePipelineEvent implements Cloneable, Serializable {
    private static final long serialVersionUID = -4828716548429210697L;

    private Job codePipelineJob;

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class Job implements Serializable, Cloneable {
        private static final long serialVersionUID = 2211711169692638977L;

        private String id;
        private String accountId;
        private Data data;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class Data implements Serializable, Cloneable {
        private static final long serialVersionUID = 8786599041834868262L;

        private ActionConfiguration actionConfiguration;
        private List<Artifact> inputArtifacts;
        private List<Artifact> outputArtifacts;
        private ArtifactCredentials artifactCredentials;
        private String continuationToken;
        private EncryptionKey encryptionKey;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class ActionConfiguration implements Serializable, Cloneable {
        private static final long serialVersionUID = -7285651174501621217L;

        private Configuration configuration;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class Configuration implements Serializable, Cloneable {
        private static final long serialVersionUID = 580024317691702894L;

        private String functionName;
        private String userParameters;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class Artifact implements Serializable, Cloneable {
        private static final long serialVersionUID = 6406621244704594358L;

        private String name;
        private String revision;
        private Location location;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class Location implements Serializable, Cloneable {
        private static final long serialVersionUID = 149382199413534713L;

        private String type;
        private S3Location s3Location;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class S3Location implements Serializable, Cloneable {
        private static final long serialVersionUID = -8922449809993769709L;

        private String bucketName;
        private String objectKey;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class ArtifactCredentials implements Serializable, Cloneable {
        private static final long serialVersionUID = 7710347495607396747L;

        private String accessKeyId;
        private String secretAccessKey;
        private String sessionToken;
        private long expirationTime;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class EncryptionKey implements Serializable, Cloneable {
        private static final long serialVersionUID = -9105569908901180610L;

        String id;
        String type;
    }
}
