/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Verifies serialization round-trip fidelity for Lambda response event types.
 *
 * <p>Response events are POJOs that Lambda functions return to the RIC, which
 * serializes them to JSON via {@code EventHandlerLoader.getSerializer()}.
 * None of these types are registered in {@code SUPPORTED_EVENTS}, so they
 * go through the bare Jackson path ({@code JacksonFactory.getInstance()
 * .getSerializer(type)}).</p>
 *
 * <p>Although the RIC only calls {@code toJson()} on response types (never
 * {@code fromJson()}), the round-trip test is a stricter check: if a response
 * type survives JSON &rarr; POJO &rarr; JSON &rarr; POJO &rarr; JSON, it
 * certainly survives the production POJO &rarr; JSON path.</p>
 *
 * @see SerializationRoundTripTest for registered input events
 * @see UnregisteredEventSerializationRoundTripTest for unregistered input events
 */
@SuppressWarnings("deprecation") // APIGatewayV2ProxyResponseEvent is deprecated
public class ResponseEventSerializationRoundTripTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("passingCases")
    void roundTrip(String displayName, String fixture, Class<?> eventClass) {
        LambdaEventAssert.assertSerializationRoundTrip(fixture, eventClass);
    }

    private static Stream<Arguments> passingCases() {
        return Stream.of(
                // API Gateway responses
                args(APIGatewayProxyResponseEvent.class, "response/apigw_proxy_response.json"),
                args(APIGatewayV2HTTPResponse.class, "response/apigw_v2_http_response.json"),
                args(APIGatewayV2WebSocketResponse.class, "response/apigw_v2_websocket_response.json"),
                args(APIGatewayV2ProxyResponseEvent.class, "response/apigw_v2_websocket_response.json"),
                // ALB response
                args(ApplicationLoadBalancerResponseEvent.class, "response/alb_response.json"),
                // S3 Batch response
                args(S3BatchResponse.class, "response/s3_batch_response.json"),
                // SQS Batch response
                args(SQSBatchResponse.class, "response/sqs_batch_response.json"),
                // Simple IAM Policy response (HTTP API)
                args(SimpleIAMPolicyResponse.class, "response/simple_iam_policy_response.json"),
                // MSK Firehose response
                args(MSKFirehoseResponse.class, "response/msk_firehose_response.json"));
    }

    private static Arguments args(Class<?> clazz, String fixture) {
        return Arguments.of(clazz.getSimpleName(), fixture, clazz);
    }
}
