package com.amazonaws.services.lambda.runtime.events.apigateway;

/**
 * A common interface shared by event sources which produce HTTP as JSON
 */
public interface HttpRequestEvent {
    RequestSource getRequestSource();
}
