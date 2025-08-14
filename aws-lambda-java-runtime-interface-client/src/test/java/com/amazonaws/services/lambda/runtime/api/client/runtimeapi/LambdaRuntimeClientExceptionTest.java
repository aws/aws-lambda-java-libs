package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LambdaRuntimeClientExceptionTest {

    @Test
    void testConstructor() {
        String message = "Test error";
        int responseCode = 500;
        
        LambdaRuntimeClientException exception = new LambdaRuntimeClientException(message, responseCode);
        
        assertEquals("Test error Response code: '500'.", exception.getMessage());
    }
}