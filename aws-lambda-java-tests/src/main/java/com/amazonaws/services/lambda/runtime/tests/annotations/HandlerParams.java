/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests.annotations;

import com.amazonaws.services.lambda.runtime.tests.HandlerParamsArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

/**
 * This annotation must be used in conjunction with {@link org.junit.jupiter.params.ParameterizedTest}.<br/>
 * It enables to inject Events and Responses into the current test.<br/>
 * Either use the {@link #event()} and {@link #response()} for a single event/response
 * or {@link #events()} and {@link #responses()} for multiple ones.<br/>
 *
 * Example:<br/>
 * <pre>{@code
 * @ParameterizedTest
 * @HandlerParams(
 *         event = @Event(value = "apigw/events/apigw_event.json", type = APIGatewayProxyRequestEvent.class),
 *         response = @Response(value = "apigw/responses/apigw_response.json", type = APIGatewayProxyResponseEvent.class))
 * public void testSingleEventResponse(APIGatewayProxyRequestEvent event, APIGatewayProxyResponseEvent response) {
 * }
 *
 * @ParameterizedTest
 * @HandlerParams(
 *         events = @Events(folder = "apigw/events/", type = APIGatewayProxyRequestEvent.class),
 *         responses = @Responses(folder = "apigw/responses/", type = APIGatewayProxyResponseEvent.class))
 * public void testMultipleEventsResponsesInFolder(APIGatewayProxyRequestEvent event, APIGatewayProxyResponseEvent response) {
 * }
 *
 * @ParameterizedTest
 * @HandlerParams(
 *         events = @Events(
 *                 events = {
 *                         @Event("apigw/events/apigw_event.json"),
 *                         @Event("apigw/events/apigw_event2.json"),
 *                 },
 *                 type = APIGatewayProxyRequestEvent.class
 *         ),
 *         responses = @Responses(
 *                 responses = {
 *                         @Response("apigw/responses/apigw_response.json"),
 *                         @Response("apigw/responses/apigw_response2.json")
 *                 },
 *                 type = APIGatewayProxyResponseEvent.class
 *         )
 * )
 * public void testMultipleEventsResponses(APIGatewayProxyRequestEvent event, APIGatewayProxyResponseEvent response) {
 * }
 * }</pre>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(HandlerParamsArgumentsProvider.class)
public @interface HandlerParams {

    Event event() default @Event("");
    Response response() default @Response("");

    Events events() default @Events;
    Responses responses() default @Responses;
}
