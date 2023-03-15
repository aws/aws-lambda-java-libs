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

import java.util.Map;

/**
 * Event to allow transformations to occur before an S3 object is returned to the calling service.
 *
 * <strong>Documentation</strong>
 *
 * <a href="https://docs.aws.amazon.com/AmazonS3/latest/userguide/olap-writing-lambda.html">Writing and debugging Lambda functions for S3 Object Lambda Access Points</a>
 *
 * <strong>Example:</strong>
 *
 * <pre>
 * <code>import com.amazonaws.services.lambda.runtime.Context;
 * import com.amazonaws.services.lambda.runtime.events.s3.S3ObjectLambdaEvent;
 * import org.apache.http.client.fluent.Request;
 * import software.amazon.awssdk.services.s3.S3Client;
 * import software.amazon.awssdk.services.s3.model.WriteGetObjectResponseRequest;
 *
 * import java.io.IOException;
 *
 * import static software.amazon.awssdk.core.sync.RequestBody.fromString;
 *
 * public class S3ObjectRequestHandler {
 *
 *      private static final S3Client s3Client = S3Client.create();
 *
 *      public void handleRequest(S3ObjectLambdaEvent event, Context context) throws IOException {
 *          String s3Body = Request.Get(event.inputS3Url()).execute().returnContent().asString();
 *
 *          String responseBody = s3Body.toUpperCase();
 *
 *          WriteGetObjectResponseRequest request = WriteGetObjectResponseRequest.builder()
 *              .requestRoute(event.outputRoute())
 *              .requestToken(event.outputToken())
 *              .build();
 *          s3Client.writeGetObjectResponse(request, fromString(responseBody));
 *      }
 * }
 * </code>
 * </pre>
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class S3ObjectLambdaEvent {

    @JsonProperty("xAmzRequestId")
    private String xAmzRequestId;
    private GetObjectContext getObjectContext;
    private Configuration configuration;
    private UserRequest userRequest;
    private UserIdentity userIdentity;
    private String protocolVersion;

    /**
     * A pre-signed URL that can be used to fetch the original object from Amazon S3.
     *
     * The URL is signed using the original caller's identity, and their permissions
     * will apply when the URL is used. If there are signed headers in the URL, the
     * Lambda function must include these in the call to Amazon S3, except for the Host.
     *
     * @return A pre-signed URL that can be used to fetch the original object from Amazon S3.
     */
    public String inputS3Url() {
        return getGetObjectContext().getInputS3Url();
    }

    /**
     * A routing token that is added to the S3 Object Lambda URL when the Lambda function
     * calls the S3 API WriteGetObjectResponse.
     *
     * @return the outputRoute
     */
    public String outputRoute() {
        return getGetObjectContext().getOutputRoute();
    }

    /**
     * An opaque token used by S3 Object Lambda to match the WriteGetObjectResponse call
     * with the original caller.
     *
     * @return the outputToken
     */
    public String outputToken() {
        return getGetObjectContext().getOutputToken();
    }

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
