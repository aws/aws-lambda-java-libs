package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.ErrorRequest;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayErrorCause;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LambdaErrorTest {

    @Test
    void testConstructorWithXRayErrorCause() {
        ErrorRequest errorRequest = new ErrorRequest();
        XRayErrorCause xRayErrorCause = new XRayErrorCause();
        RapidErrorType errorType = RapidErrorType.BadFunctionCode;

        LambdaError lambdaError = new LambdaError(errorRequest, xRayErrorCause, errorType);

        assertEquals(errorRequest, lambdaError.errorRequest);
        assertEquals(xRayErrorCause, lambdaError.xRayErrorCause);
        assertEquals(errorType, lambdaError.errorType);
    }

    @Test
    void testConstructorWithoutXRayErrorCause() {
        ErrorRequest errorRequest = new ErrorRequest();
        RapidErrorType errorType = RapidErrorType.UserException;

        LambdaError lambdaError = new LambdaError(errorRequest, errorType);

        assertEquals(errorRequest, lambdaError.errorRequest);
        assertNull(lambdaError.xRayErrorCause);
        assertEquals(errorType, lambdaError.errorType);
    }
}