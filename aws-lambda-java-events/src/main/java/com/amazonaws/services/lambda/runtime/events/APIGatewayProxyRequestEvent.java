package com.amazonaws.services.lambda.runtime.events;

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
public class APIGatewayProxyRequestEvent {

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

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProxyRequestContext {

        private String accountId;
        private String stage;
        private String resourceId;
        private String requestId;
        private String operationName;
        private RequestIdentity identity;
        private String resourcePath;
        private String httpMethod;
        private String apiId;
        private String path;
        private Map<String, Object> authorizer;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestIdentity {

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
    }
}
