package com.amazonaws.services.lambda.runtime.api.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LambdaEnvironmentTest {

    @Test
    void testConstants() {
        assertNotNull(LambdaEnvironment.ENV_READER);
        assertTrue(LambdaEnvironment.MEMORY_LIMIT >= 128);
        assertNotNull(LambdaEnvironment.LAMBDA_LOG_LEVEL);
        assertNotNull(LambdaEnvironment.LAMBDA_LOG_FORMAT);
    }
}