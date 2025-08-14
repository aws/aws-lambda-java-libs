package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RapidErrorTypeTest {

    @Test
    void testGetRapidError() {
        assertEquals("Runtime.BadFunctionCode", RapidErrorType.BadFunctionCode.getRapidError());
        assertEquals("Runtime.UserException", RapidErrorType.UserException.getRapidError());
    }
}