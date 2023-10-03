package com.amazonaws.services.lambda.runtime.events.apigateway;

import com.amazonaws.services.lambda.runtime.events.apigateway.RequestSource;

/**
 * Interface to abstract APIGWY and ALB request classes, reduce complexity and increase efficiency.
 */
public interface LambdaRequestEvent {
    RequestSource getRequestSource();
}
