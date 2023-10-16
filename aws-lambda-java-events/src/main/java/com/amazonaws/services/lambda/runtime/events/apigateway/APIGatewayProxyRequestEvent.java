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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Class that represents an APIGatewayProxyRequestEvent
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class APIGatewayProxyRequestEvent implements Serializable, HttpRequestEvent {

    private static final long serialVersionUID = 4189228800688527467L;
    private String version;
    private String resource;
    private String path;
    private String httpMethod;
    private Map<String, String> headers;
    private Map<String, List<String>> multiValueHeaders;
    private Map<String, String> queryStringParameters;
    private Map<String, List<String>> multiValueQueryStringParameters;
    private Map<String, String> pathParameters;
    private Map<String, String> stageVariables;
    private ProxyRequestContext requestContext;
    private String body;
    private Boolean isBase64Encoded;

    @Override
    @JsonIgnore
    public RequestSource getRequestSource() {
        return RequestSource.API_GATEWAY_REST;
    }

    /**
     * class that represents proxy request context
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProxyRequestContext implements Serializable {

        private static final long serialVersionUID = 8783459961042799774L;

        private String accountId;
        private String apiId;
        private Map<String, Object> authorizer;
        private String domainName;
        private String domainPrefix;
        private String extendedRequestId;
        private String httpMethod;
        private RequestIdentity identity;
        private String path;
        private String protocol;
        private String requestId;
        private String requestTime;
        private Long requestTimeEpoch;
        private String resourceId;
        private String resourcePath;
        private String stage;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestIdentity implements Serializable {

        private static final long serialVersionUID = -5283829736983640346L;

        private String cognitoIdentityPoolId;
        private String accountId;
        private String cognitoIdentityId;
        private String caller;
        private String apiKey;
        private String sourceIp;
        private String cognitoAuthenticationType;
        private String cognitoAuthenticationProvider;
        private String userArn;
        private String userAgent;
        private String user;
        private String accessKey;
        private String principalOrgId;
        private ClientCert clientCert;
    }

    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @Data
    @NoArgsConstructor
    public static class ClientCert {

        private String clientCertPem;
        private String issuerDN;
        private String serialNumber;
        private String subjectDN;
        private Validity validity;
    }

    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @Data
    @NoArgsConstructor
    public static class Validity {

        private String notAfter;
        private String notBefore;
    }
}
