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

package com.amazonaws.services.lambda.runtime.events.api_gateway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * The V2 API Gateway customer authorizer event object as described - https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api-lambda-authorizer.html
 *
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class APIGatewayV2CustomAuthorizerEvent {

    private String version;
    private String type;
    private String routeArn;
    private List<String> identitySource;
    private String routeKey;
    private String rawPath;
    private String rawQueryString;
    private List<String> cookies;
    private Map<String, String> headers;
    private Map<String, String> queryStringParameters;
    private RequestContext requestContext;
    private Map<String, String> pathParameters;
    private Map<String, String> stageVariables;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestContext {

        private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");

        private String accountId;
        private String apiId;
        private Authentication authentication;
        private String domainName;
        private String domainPrefix;
        private Http http;
        private String requestId;
        private String routeKey;
        private String stage;
        private String time;
        private long timeEpoch;

        @JsonIgnore
        public Instant getEpochTime() {
            return Instant.ofEpochMilli(timeEpoch);
        }

        @JsonIgnore
        public ZonedDateTime getDateTime() {
            return ZonedDateTime.parse(time, dateTimeFormatter);
        }

        @AllArgsConstructor
        @Builder(setterPrefix = "with")
        @Data
        @NoArgsConstructor
        public static class Authentication {

            private APIGatewayV2HTTPEvent.RequestContext.Authentication.ClientCert clientCert;

            @AllArgsConstructor
            @Builder(setterPrefix = "with")
            @Data
            @NoArgsConstructor
            public static class ClientCert {

                private String clientCertPem;
                private String issuerDN;
                private String serialNumber;
                private String subjectDN;
                private APIGatewayV2HTTPEvent.RequestContext.Authentication.ClientCert.Validity validity;

                @AllArgsConstructor
                @Builder(setterPrefix = "with")
                @Data
                @NoArgsConstructor
                public static class Validity {

                    private String notAfter;
                    private String notBefore;
                }
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