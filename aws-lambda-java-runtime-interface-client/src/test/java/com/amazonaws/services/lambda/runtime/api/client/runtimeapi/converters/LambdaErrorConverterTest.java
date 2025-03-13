/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.converters;

import com.amazonaws.services.lambda.runtime.api.client.UserFault;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.ErrorRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LambdaErrorConverterTest {

    @Test
    void testFromUserFaultWithMessageAndException() {
        UserFault userFault = new UserFault("Test error message", "TestException", "Test stack trace");
        ErrorRequest errorRequest = LambdaErrorConverter.fromUserFault(userFault);
        
        assertNotNull(errorRequest);
        assertEquals("Test error message", errorRequest.errorMessage);
        assertEquals("TestException", errorRequest.errorType);
        assertNull(errorRequest.stackTrace);
    }

    @Test
    void testFromUserFaultWithNullValues() {
        UserFault userFault = new UserFault(null, null, null);
        ErrorRequest errorRequest = LambdaErrorConverter.fromUserFault(userFault);
        
        assertNotNull(errorRequest);
        assertNull(errorRequest.errorMessage);
        assertNull(errorRequest.errorType);
        assertNull(errorRequest.stackTrace);
    }

    @Test
    void testFromUserFaultWithFatalError() {
        UserFault userFault = new UserFault("Fatal error", "FatalException", "Test stack trace", true);
        ErrorRequest errorRequest = LambdaErrorConverter.fromUserFault(userFault);
        
        assertNotNull(errorRequest);
        assertEquals("Fatal error", errorRequest.errorMessage);
        assertEquals("FatalException", errorRequest.errorType);
        assertNull(errorRequest.stackTrace);
    }

    @Test
    void testFromUserFaultCreatedFromException() {
        Exception exception = new RuntimeException("Test exception message");
        UserFault userFault = UserFault.makeUserFault(exception);
        ErrorRequest errorRequest = LambdaErrorConverter.fromUserFault(userFault);
        
        assertNotNull(errorRequest);
        assertEquals("Test exception message", errorRequest.errorMessage);
        assertEquals("java.lang.RuntimeException", errorRequest.errorType);
        assertNull(errorRequest.stackTrace);
    }

    @Test
    void testFromUserFaultCreatedFromMessage() {
        UserFault userFault = UserFault.makeUserFault("Simple message");
        ErrorRequest errorRequest = LambdaErrorConverter.fromUserFault(userFault);
        
        assertNotNull(errorRequest);
        assertEquals("Simple message", errorRequest.errorMessage);
        assertNull(errorRequest.errorType);
        assertNull(errorRequest.stackTrace);
    }

    @Test
    void testFromThrowableWithMessage() {
        Exception exception = new RuntimeException("Test exception message");
        ErrorRequest errorRequest = LambdaErrorConverter.fromThrowable(exception);
        
        assertNotNull(errorRequest);
        assertEquals("Test exception message", errorRequest.errorMessage);
        assertEquals("java.lang.RuntimeException", errorRequest.errorType);
        assertNotNull(errorRequest.stackTrace);
        assertTrue(errorRequest.stackTrace.length > 0);
    }

    @Test
    void testFromThrowableWithNullMessage() {
        Exception exception = new RuntimeException();
        ErrorRequest errorRequest = LambdaErrorConverter.fromThrowable(exception);
        
        assertNotNull(errorRequest);
        assertEquals("java.lang.RuntimeException", errorRequest.errorMessage);
        assertEquals("java.lang.RuntimeException", errorRequest.errorType);
        assertNotNull(errorRequest.stackTrace);
        assertTrue(errorRequest.stackTrace.length > 0);
    }

    @Test
    void testFromThrowableStackTraceContent() {
        Exception exception = new RuntimeException("Test message");
        ErrorRequest errorRequest = LambdaErrorConverter.fromThrowable(exception);
        
        String[] stackTrace = errorRequest.stackTrace;
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
        
        boolean foundTestClass = false;
        for (String traceLine : stackTrace) {
            if (traceLine.contains(LambdaErrorConverterTest.class.getSimpleName())) {
                foundTestClass = true;
                break;
            }
        }
        assertTrue(foundTestClass);
    }
}
