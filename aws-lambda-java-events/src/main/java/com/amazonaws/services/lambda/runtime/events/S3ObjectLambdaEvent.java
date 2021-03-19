package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Event to allow transformations to occur before an S3 object is returned to the calling service.
 *
 * https://aws.amazon.com/blogs/aws/introducing-amazon-s3-object-lambda-use-your-code-to-process-data-as-it-is-being-retrieved-from-s3/
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class S3ObjectLambdaEvent {

    private String xAmzRequestId;
    private GetObjectContext getObjectContext;
    private Configuration configuration;
    private UserRequest userRequest;
    private UserIdentity userIdentity;
    private String protocolVersion;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetObjectContext {
        private String inputS3Url;
        private String outputRoute;
        private String outputToken;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Configuration {
        private String accessPointArn;
        private String supportingAccessPointArn;
        private String payload;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRequest {
        private String url;
        private Map<String, String> headers;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserIdentity {
        private String type;
        private String principalId;
        private String arn;
        private String accountId;
        private String accessKeyId;
    }
}