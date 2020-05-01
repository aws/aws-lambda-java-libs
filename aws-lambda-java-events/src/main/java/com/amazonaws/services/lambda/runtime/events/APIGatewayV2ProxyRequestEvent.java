package com.amazonaws.services.lambda.runtime.events;

/**
 * @deprecated
 * This class is for use with API Gateway WebSockets, and has been renamed explicitly as {@link APIGatewayV2WebSocketEvent}
 * To integrate with API Gateway's HTTP API Events, use one of:
 *   * {@link APIGatewayV2HTTPEvent} (payload version 2.0)
 *   * {@link APIGatewayProxyRequestEvent} (payload version 1.0)
 */
@Deprecated()
public class APIGatewayV2ProxyRequestEvent extends APIGatewayV2WebSocketEvent {}
