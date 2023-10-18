package com.amazonaws.services.lambda.runtime.events.apigateway;

public enum RequestSource {
    API_GATEWAY_REST,
    API_GATEWAY_HTTP,
    ALB,
    VPC_LATTICE_V2
}
