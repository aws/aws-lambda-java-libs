package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.ErrorRequest;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayErrorCause;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DtoSerializersTest {

    @Test
    void testSerializeErrorRequest() {
        ErrorRequest errorRequest = new ErrorRequest();
        errorRequest.errorMessage = "test error";
        errorRequest.errorType = "TestException";
        
        byte[] result = DtoSerializers.serialize(errorRequest);
        
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void testSerializeXRayErrorCause() {
        XRayErrorCause xRayErrorCause = new XRayErrorCause();
        xRayErrorCause.working_directory = "/test";
        
        byte[] result = DtoSerializers.serialize(xRayErrorCause);
        
        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}