/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.InvocationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LambdaRequestHandlerTest {

    private InvocationRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockRequest = mock(InvocationRequest.class);
    }

    @Test
    void testInitErrorHandler() {
        String className = "com.example.TestClass";
        Exception testException = new RuntimeException("initialization error");
        
        LambdaRequestHandler handler = LambdaRequestHandler.initErrorHandler(testException, className);
        
        assertNotNull(handler);
        assertTrue(handler instanceof LambdaRequestHandler.UserFaultHandler);
        
        LambdaRequestHandler.UserFaultHandler userFaultHandler = (LambdaRequestHandler.UserFaultHandler) handler;
        UserFault fault = userFaultHandler.fault;
        
        assertNotNull(fault);
        assertEquals("Error loading class " + className + ": initialization error", fault.msg);
        assertEquals("java.lang.RuntimeException", fault.exception);
        assertTrue(fault.fatal);
    }

    @Test
    void testClassNotFound() {
        String className = "com.example.MissingClass";
        Exception testException = new ClassNotFoundException("class not found");
        
        LambdaRequestHandler handler = LambdaRequestHandler.classNotFound(testException, className);
        
        assertNotNull(handler);
        assertTrue(handler instanceof LambdaRequestHandler.UserFaultHandler);
        
        LambdaRequestHandler.UserFaultHandler userFaultHandler = (LambdaRequestHandler.UserFaultHandler) handler;
        UserFault fault = userFaultHandler.fault;
        
        assertNotNull(fault);
        assertEquals("Class not found: " + className, fault.msg);
        assertEquals("java.lang.ClassNotFoundException", fault.exception);
        assertFalse(fault.fatal);
    }

    @Test
    void testUserFaultHandlerConstructor() {
        UserFault testFault = new UserFault("test message", "TestException", "test trace");
        LambdaRequestHandler.UserFaultHandler handler = new LambdaRequestHandler.UserFaultHandler(testFault);
        
        assertNotNull(handler);
        assertSame(testFault, handler.fault);
    }

    @Test
    void testUserFaultHandlerCallThrowsFault() {
        UserFault testFault = new UserFault("test message", "TestException", "test trace");
        LambdaRequestHandler.UserFaultHandler handler = new LambdaRequestHandler.UserFaultHandler(testFault);
        
        UserFault thrownFault = assertThrows(UserFault.class, () -> handler.call(mockRequest));
        assertSame(testFault, thrownFault);
    }

    @Test
    void testInitErrorHandlerWithNullMessage() {
        String className = "com.example.TestClass";
        Exception testException = new RuntimeException();
        
        LambdaRequestHandler handler = LambdaRequestHandler.initErrorHandler(testException, className);
        
        assertNotNull(handler);
        assertTrue(handler instanceof LambdaRequestHandler.UserFaultHandler);
        
        LambdaRequestHandler.UserFaultHandler userFaultHandler = (LambdaRequestHandler.UserFaultHandler) handler;
        UserFault fault = userFaultHandler.fault;
        
        assertNotNull(fault);
        assertEquals("Error loading class " + className, fault.msg);
        assertEquals("java.lang.RuntimeException", fault.exception);
        assertTrue(fault.fatal);
    }

    @Test
    void testInitErrorHandlerWithNullClassName() {
        Exception testException = new RuntimeException("test error");
        
        LambdaRequestHandler handler = LambdaRequestHandler.initErrorHandler(testException, null);
        
        assertNotNull(handler);
        assertTrue(handler instanceof LambdaRequestHandler.UserFaultHandler);
        
        LambdaRequestHandler.UserFaultHandler userFaultHandler = (LambdaRequestHandler.UserFaultHandler) handler;
        UserFault fault = userFaultHandler.fault;
        
        assertNotNull(fault);
        assertEquals("Error loading class null: test error", fault.msg);
        assertEquals("java.lang.RuntimeException", fault.exception);
        assertTrue(fault.fatal);
    }

    @Test
    void testClassNotFoundWithNullClassName() {
        Exception testException = new ClassNotFoundException("test error");
        
        LambdaRequestHandler handler = LambdaRequestHandler.classNotFound(testException, null);
        
        assertNotNull(handler);
        assertTrue(handler instanceof LambdaRequestHandler.UserFaultHandler);
        
        LambdaRequestHandler.UserFaultHandler userFaultHandler = (LambdaRequestHandler.UserFaultHandler) handler;
        UserFault fault = userFaultHandler.fault;
        
        assertNotNull(fault);
        assertEquals("Class not found: null", fault.msg);
        assertEquals("java.lang.ClassNotFoundException", fault.exception);
        assertFalse(fault.fatal);
    }

    @Test
    void testUserFaultHandlerCallWithNullRequest() {
        UserFault testFault = new UserFault("test message", "TestException", "test trace");
        LambdaRequestHandler.UserFaultHandler handler = new LambdaRequestHandler.UserFaultHandler(testFault);
        
        UserFault thrownFault = assertThrows(UserFault.class, () -> handler.call(null));
        assertSame(testFault, thrownFault);
    }
}
