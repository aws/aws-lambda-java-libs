package com.amazonaws.services.lambda.runtime.events.helper;

/**
 * Interface to abstract APIGWY and ALB request classes, reduce complexity and increase efficiency.
 */
public interface AwsLambdaRequestEvent {
    RequestSource getRequestSource();
}
