package com.amazonaws.services.lambda.crac;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {

    @Test
    void testResourceInterface() {
        Resource resource = new Resource() {
            @Override
            public void afterRestore(Context<? extends Resource> context) throws Exception {
                // Test implementation
            }

            @Override
            public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
                // Test implementation
            }
        };

        assertNotNull(resource);
        assertDoesNotThrow(() -> resource.afterRestore(null));
        assertDoesNotThrow(() -> resource.beforeCheckpoint(null));
    }
}