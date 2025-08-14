package com.amazonaws.services.lambda.runtime.api.client.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnvReaderTest {

    @Test
    void testGetEnv() {
        EnvReader reader = new EnvReader();
        assertNotNull(reader.getEnv());
    }

    @Test
    void testGetEnvOrDefault() {
        EnvReader reader = new EnvReader();
        assertEquals("default", reader.getEnvOrDefault("NON_EXISTENT_VAR", "default"));
    }
}