package com.amazonaws.services.lambda.runtime.api.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservedRuntimeEnvironmentVariablesTest {

    @Test
    void testConstants() {
        assertEquals("_HANDLER", ReservedRuntimeEnvironmentVariables.HANDLER);
        assertEquals("AWS_REGION", ReservedRuntimeEnvironmentVariables.AWS_REGION);
        assertEquals("AWS_LAMBDA_FUNCTION_NAME", ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_FUNCTION_NAME);
        assertEquals("AWS_LAMBDA_RUNTIME_API", ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_RUNTIME_API);
    }
}