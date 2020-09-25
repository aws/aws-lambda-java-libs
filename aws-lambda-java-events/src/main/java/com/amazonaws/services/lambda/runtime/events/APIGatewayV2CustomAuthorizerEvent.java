package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

        private String accountId;
        private String apiId;
        private String domainName;
        private String domainPrefix;
        private Http http;
        private String requestId;
        private String routeKey;
        private String stage;
        private String time; // "time": "12/Mar/2020:19:03:58 +0000",
        private int timeEpoch;
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