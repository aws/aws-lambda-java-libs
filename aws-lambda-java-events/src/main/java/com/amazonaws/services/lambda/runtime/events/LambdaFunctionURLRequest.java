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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Class to represent a Lambda function HTTP Request.
 *
 * @see <a href="https://docs.aws.amazon.com/lambda/latest/dg/urls-invocation.html">Invoking Lambda function URLs</a>
 */
@AllArgsConstructor
@Builder(setterPrefix = "with")
@Data
@NoArgsConstructor
public class LambdaFunctionURLRequest {
    private String version;
    private String rawPath;
    private String rawQueryString;
    private List<String> cookies;
    private Map<String, String> headers;
    private Map<String, String> queryStringParameters;
    private String body;
    private boolean isBase64Encoded;
    private APIGatewayV2HTTPEvent.RequestContext requestContext;

    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @Data
    @NoArgsConstructor
    public static class RequestContext {
        private String accountId;
        private String apiId;
        private String domainName;
        private String domainPrefix;
        private String time;
        private long timeEpoch;
        private APIGatewayV2HTTPEvent.RequestContext.Http http;
        private APIGatewayV2HTTPEvent.RequestContext.Authorizer authorizer;
        private String requestId;

        @AllArgsConstructor
        @Builder(setterPrefix = "with")
        @Data
        @NoArgsConstructor
        public static class Authorizer {
            private APIGatewayV2HTTPEvent.RequestContext.Authorizer.JWT jwt;
            private Map<String, Object> lambda;
            private APIGatewayV2HTTPEvent.RequestContext.IAM iam;

            @AllArgsConstructor
            @Builder(setterPrefix = "with")
            @Data
            @NoArgsConstructor
            public static class JWT {
                private Map<String, String> claims;
                private List<String> scopes;
            }
        }

        @AllArgsConstructor
        @Builder(setterPrefix = "with")
        @Data
        @NoArgsConstructor
        public static class Http {
            private String method;
            private String path;
            private String protocol;
            private String sourceIp;
            private String userAgent;
        }

        @AllArgsConstructor
        @Builder(setterPrefix = "with")
        @Data
        @NoArgsConstructor
        public static class IAM {
            private String accessKey;
            private String accountId;
            private String callerId;
            private String userArn;
            private String userId;
        }
    }
}
