package com.amazonaws.services.lambda.crac;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestoreExceptionTest {

    @Test
    void testConstructor() {
        RestoreException exception = new RestoreException();
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
}