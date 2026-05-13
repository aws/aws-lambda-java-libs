package com.amazonaws.services.lambda.runtime.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class APIGatewayV2WebSocketEventTest {

    @Test
    void requestContextBuildsConnectionContext() {
        APIGatewayV2WebSocketEvent.RequestContext requestContext = new APIGatewayV2WebSocketEvent.RequestContext();
        requestContext.setConnectionId("conn-123");
        requestContext.setDomainName("abc.execute-api.us-east-1.amazonaws.com");
        requestContext.setStage("prod");

        APIGatewayV2WebSocketEvent.WebSocketConnectionContext connectionContext = requestContext.getConnectionContext();

        assertNotNull(connectionContext);
        assertEquals("conn-123", connectionContext.getConnectionId());
        assertEquals("abc.execute-api.us-east-1.amazonaws.com", connectionContext.getDomainName());
        assertEquals("prod", connectionContext.getStage());
        assertEquals("https://abc.execute-api.us-east-1.amazonaws.com/prod", connectionContext.getManagementApiEndpoint());
    }

    @Test
    void eventExposesConnectionContextFromRequestContext() {
        APIGatewayV2WebSocketEvent.RequestContext requestContext = new APIGatewayV2WebSocketEvent.RequestContext();
        requestContext.setConnectionId("conn-456");
        requestContext.setDomainName("xyz.execute-api.us-east-1.amazonaws.com");
        requestContext.setStage("dev");

        APIGatewayV2WebSocketEvent event = new APIGatewayV2WebSocketEvent();
        event.setRequestContext(requestContext);

        APIGatewayV2WebSocketEvent.WebSocketConnectionContext connectionContext = event.getConnectionContext();

        assertNotNull(connectionContext);
        assertEquals("conn-456", connectionContext.getConnectionId());
        assertEquals("https://xyz.execute-api.us-east-1.amazonaws.com/dev", connectionContext.getManagementApiEndpoint());
    }

    @Test
    void connectionContextRequiresConnectionId() {
        APIGatewayV2WebSocketEvent.RequestContext requestContext = new APIGatewayV2WebSocketEvent.RequestContext();
        requestContext.setDomainName("abc.execute-api.us-east-1.amazonaws.com");
        requestContext.setStage("prod");

        assertNull(requestContext.getConnectionContext());

        APIGatewayV2WebSocketEvent event = new APIGatewayV2WebSocketEvent();
        assertNull(event.getConnectionContext());
    }

    @Test
    void managementEndpointRequiresDomainAndStage() {
        APIGatewayV2WebSocketEvent.WebSocketConnectionContext connectionContext =
                new APIGatewayV2WebSocketEvent.WebSocketConnectionContext("conn-789", null, "prod");

        assertNull(connectionContext.getManagementApiEndpoint());
    }

    @Test
    void managementEndpointRequiresNonEmptyDomainAndStage() {
        APIGatewayV2WebSocketEvent.WebSocketConnectionContext emptyDomain =
                new APIGatewayV2WebSocketEvent.WebSocketConnectionContext("conn-111", "", "prod");
        APIGatewayV2WebSocketEvent.WebSocketConnectionContext emptyStage =
                new APIGatewayV2WebSocketEvent.WebSocketConnectionContext("conn-222", "abc.execute-api.us-east-1.amazonaws.com", "");

        assertNull(emptyDomain.getManagementApiEndpoint());
        assertNull(emptyStage.getManagementApiEndpoint());
    }
}

