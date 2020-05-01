package com.amazonaws.services.lambda.runtime.events;

/**
 * @deprecated
 * This class is for responding to API Gateway WebSocket events, and has been renamed explicitly as {@link APIGatewayV2WebSocketResponse}
 * To response to API Gateway's HTTP API Events, use {@link APIGatewayV2HTTPResponse}
 */
@Deprecated
public class APIGatewayV2ProxyResponseEvent extends APIGatewayV2WebSocketResponse {}
