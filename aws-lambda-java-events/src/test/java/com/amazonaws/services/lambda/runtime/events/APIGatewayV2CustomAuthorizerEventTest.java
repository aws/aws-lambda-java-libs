package com.amazonaws.services.lambda.runtime.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class APIGatewayV2CustomAuthorizerEventTest {

    private static final long TIME_EPOCH = 1601306426515L;
    private static final String TIME = "28/Sep/2020:15:14:43 +0000";

    @Test
    public void testEpochLongAsAnInstant() {
        APIGatewayV2CustomAuthorizerEvent customAuthorizerEvent = APIGatewayV2CustomAuthorizerEvent.builder()
                .withRequestContext(APIGatewayV2CustomAuthorizerEvent.RequestContext.builder()
                        .withTimeEpoch(TIME_EPOCH)
                        .build())
                .build();

        assertEquals(Instant.ofEpochMilli(1601306426515L), customAuthorizerEvent.getRequestContext().getTimeEpoch());
    }

    @Test
    public void testTimeStringAsDateTime() {
        APIGatewayV2CustomAuthorizerEvent customAuthorizerEvent = APIGatewayV2CustomAuthorizerEvent.builder()
                .withRequestContext(APIGatewayV2CustomAuthorizerEvent.RequestContext.builder()
                        .withTime(TIME)
                        .build())
                .build();

        assertNotNull(customAuthorizerEvent.getRequestContext().getTime());
    }
}