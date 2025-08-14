package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorRequestTest {

    @Test
    void testDefaultConstructor() {
        ErrorRequest request = new ErrorRequest();
        assertNotNull(request);
    }

    @Test
    void testParameterizedConstructor() {
        String[] stackTrace = {"line1", "line2"};
        ErrorRequest request = new ErrorRequest("error", "type", stackTrace);
        
        assertEquals("error", request.errorMessage);
        assertEquals("type", request.errorType);
        assertArrayEquals(stackTrace, request.stackTrace);
    }
}