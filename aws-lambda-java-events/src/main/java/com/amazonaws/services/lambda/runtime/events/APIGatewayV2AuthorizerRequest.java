package com.amazonaws.services.lambda.runtime.events;

import java.util.Map;

/**
 * @author Tim Gustafson <tjg@amazon.com>
 */
public class APIGatewayV2AuthorizerRequest {

  private Map<String, String> headers;
  private Map<String, String[]> multiValueHeaders;
  private Map<String, String> queryStringParameters;
  private Map<String, String[]> multiValueQueryStringParameters;
  private Map<String, String> stageVariables;
  private APIGatewayV2ProxyRequestEvent.RequestContext requestContext;

}
