package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * The API Gateway customer authorizer event object as described - https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api-lambda-authorizer.html
 *
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class APIGatewayCustomAuthorizerEvent {

    private String version;
    private String type;
    private String methodArn;
    private String identitySource;
    private String authorizationToken;
    private String resource;
    private String path;
    private String httpMethod;
    private Map<String, String> headers;
    private Map<String, String> queryStringParameters;
    private Map<String, String> pathParameters;
    private Map<String, String> stageVariables;
    private RequestContext requestContext;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestContext {
        private String path;
        private String accountId;
        private String resourceId;
        private String stage;
        private String requestId;
        private Identity identity;
        private String resourcePath;
        private String httpMethod;
        private String apiId;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Identity {
        private String apiKey;
        private String sourceIp;
    }
}