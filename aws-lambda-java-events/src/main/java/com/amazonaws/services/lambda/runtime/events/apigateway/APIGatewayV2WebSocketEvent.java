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

package com.amazonaws.services.lambda.runtime.events.apigateway;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Tim Gustafson <tjg@amazon.com>
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class APIGatewayV2WebSocketEvent implements Serializable {

    private static final long serialVersionUID = 5695319264103347099L;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestIdentity implements Serializable {

        private static final long serialVersionUID = -3276649362684921217L;

        private String cognitoIdentityPoolId;
        private String accountId;
        private String cognitoIdentityId;
        private String caller;
        private String apiKey;
        private String apiKeyId;
        private String sourceIp;
        private String cognitoAuthenticationType;
        private String cognitoAuthenticationProvider;
        private String userArn;
        private String userAgent;
        private String user;
        private String accessKey;

    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestContext implements Serializable {

        private static final long serialVersionUID = -6641935365992304860L;

        private String accountId;
        private String resourceId;
        private String stage;
        private String requestId;
        private RequestIdentity identity;
        private String ResourcePath;
        private Map<String, Object> authorizer;
        private String httpMethod;
        private String apiId;
        private long connectedAt;
        private String connectionId;
        private String domainName;
        private String error;
        private String eventType;
        private String extendedRequestId;
        private String integrationLatency;
        private String messageDirection;
        private String messageId;
        private String requestTime;
        private long requestTimeEpoch;
        private String routeKey;
        private String status;

    }

    private String resource;
    private String path;
    private String httpMethod;
    private Map<String, String> headers;
    private Map<String, List<String>> multiValueHeaders;
    private Map<String, String> queryStringParameters;
    private Map<String, List<String>> multiValueQueryStringParameters;
    private Map<String, String> pathParameters;
    private Map<String, String> stageVariables;
    private RequestContext requestContext;
    private String body;
    @JsonProperty("isBase64Encoded")
    @Builder.Default private boolean isBase64Encoded = false;

}
