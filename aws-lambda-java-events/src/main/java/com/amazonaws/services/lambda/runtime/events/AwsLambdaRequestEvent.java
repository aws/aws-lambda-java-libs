package com.amazonaws.services.lambda.runtime.events;

import com.amazonaws.services.lambda.runtime.events.helper.RequestSource;

/**
 * Interface to abstract APIGWY and ALB request classes, reduce complexity and increase efficiency.
 */
public interface AwsLambdaRequestEvent {
    RequestSource getRequestSource();
}
