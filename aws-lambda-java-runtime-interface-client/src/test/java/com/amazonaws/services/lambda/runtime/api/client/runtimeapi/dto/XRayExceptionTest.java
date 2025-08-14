package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class XRayExceptionTest {

    @Test
    void testDefaultConstructor() {
        XRayException exception = new XRayException();
        assertNotNull(exception);
    }

    @Test
    void testParameterizedConstructor() {
        XRayException exception = new XRayException("msg", "type", Arrays.asList());
        
        assertEquals("msg", exception.message);
        assertEquals("type", exception.type);
        assertNotNull(exception.stack);
    }
}