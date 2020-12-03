/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.*;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlerParamsTest {

    @ParameterizedTest
    @HandlerParams(
            event = @Event(value = "apigw/events/apigw_event.json", type = APIGatewayProxyRequestEvent.class),
            response = @Response(value = "apigw/responses/apigw_response.json", type = APIGatewayProxyResponseEvent.class))
    public void testSimpleEventResponse(APIGatewayProxyRequestEvent event, APIGatewayProxyResponseEvent response) {
        assertThat(event).isNotNull();
        assertThat(event.getBody()).contains("Lambda rocks");
        assertThat(event.getHeaders()).hasSize(18);
        assertThat(event.getHttpMethod()).isEqualTo("POST");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody()).contains("Lambda rocks");
    }

    @ParameterizedTest
    @HandlerParams(
            events = @Events(folder = "apigw/events/", type = APIGatewayProxyRequestEvent.class),
            responses = @Responses(folder = "apigw/responses/", type = APIGatewayProxyResponseEvent.class))
    public void testMultipleEventsResponsesinFolder(APIGatewayProxyRequestEvent event, APIGatewayProxyResponseEvent response) {
        assertThat(event).isNotNull();
        assertThat(event.getHeaders()).hasSize(18);
        assertThat(event.getHttpMethod()).isEqualTo("POST");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(200);

        assertThat(response.getBody()).isEqualTo(event.getBody());
    }

    @ParameterizedTest
    @HandlerParams(
            events = @Events(
                    events = {
                            @Event("apigw/events/apigw_event.json"),
                            @Event("apigw/events/apigw_event_nobody.json"),
                    },
                    type = APIGatewayProxyRequestEvent.class
            ),
            responses = @Responses(
                    responses = {
                            @Response("apigw/responses/apigw_response.json"),
                            @Response("apigw/responses/apigw_response2.json")
                    },
                    type = APIGatewayProxyResponseEvent.class
            )
    )
    public void testMultipleEventsResponses(APIGatewayProxyRequestEvent event, APIGatewayProxyResponseEvent response) {
        assertThat(event).isNotNull();
        assertThat(event.getHeaders()).hasSize(18);
        assertThat(event.getHttpMethod()).isEqualTo("POST");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(200);

        assertThat(response.getBody()).isEqualTo(event.getBody());
    }

}
