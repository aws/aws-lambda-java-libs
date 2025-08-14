package com.amazonaws.services.lambda.crac;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckpointExceptionTest {

    @Test
    void testConstructor() {
        CheckpointException exception = new CheckpointException();
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
}