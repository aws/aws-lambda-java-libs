package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvocationRequestTest {

    @Test
    void testGettersAndSetters() {
        InvocationRequest request = new InvocationRequest();
        
        request.setId("test-id");
        assertEquals("test-id", request.getId());
        
        request.setXrayTraceId("trace-id");
        assertEquals("trace-id", request.getXrayTraceId());
        
        request.setInvokedFunctionArn("arn");
        assertEquals("arn", request.getInvokedFunctionArn());
        
        request.setDeadlineTimeInMs(12345L);
        assertEquals(12345L, request.getDeadlineTimeInMs());
        
        request.setClientContext("context");
        assertEquals("context", request.getClientContext());
        
        request.setCognitoIdentity("identity");
        assertEquals("identity", request.getCognitoIdentity());
        
        request.setTenantId("tenant");
        assertEquals("tenant", request.getTenantId());
        
        byte[] content = "test".getBytes();
        request.setContent(content);
        assertArrayEquals(content, request.getContent());
    }
}