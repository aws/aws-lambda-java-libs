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

package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Builder(setterPrefix = "with")
@Data
@NoArgsConstructor
public class APIGatewayV2HTTPEvent {
    private String version;
    private String routeKey;
    private String rawPath;
    private String rawQueryString;
    private List<String> cookies;
    private Map<String, String> headers;
    private Map<String, String> queryStringParameters;
    private Map<String, String> pathParameters;
    private Map<String, String> stageVariables;
    private String Body;
    private boolean isBase64Encoded;
    private RequestContext requestContext;

    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @Data
    @NoArgsConstructor
    public static class RequestContext {
        private String routeKey;
        private String accountId;
        private String stage;
        private String apiId;
        private String domainName;
        private String domainPrefix;
        private String time;
        private long timeEpoch;
        private Http http;
        private Authorizer authorizer;

        @AllArgsConstructor
        @Builder(setterPrefix = "with")
        @Data
        @NoArgsConstructor
        public static class Authorizer {
            private JWT jwt;

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
    }
}
