package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class XRayErrorCauseTest {

    @Test
    void testDefaultConstructor() {
        XRayErrorCause cause = new XRayErrorCause();
        assertNotNull(cause);
    }

    @Test
    void testParameterizedConstructor() {
        XRayErrorCause cause = new XRayErrorCause("/dir", Arrays.asList(), Arrays.asList("path1"));
        
        assertEquals("/dir", cause.working_directory);
        assertNotNull(cause.exceptions);
        assertNotNull(cause.paths);
    }
}