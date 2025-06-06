/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static testpkg.StackTraceHelper.callThenThrowRuntimeException;
import static testpkg.StackTraceHelper.throwCheckpointExceptionWithTwoSuppressedExceptions;
import static testpkg.StackTraceHelper.throwRuntimeException;

public class UserFaultTest {

    @Test
    public void testReportableErrorNoTraces() {
        UserFault userFault = UserFault.makeUserFault(new RuntimeException("woops"));

        String actual = userFault.reportableError();
        String expected = "woops: java.lang.RuntimeException\n" +
                "java.lang.RuntimeException: woops\n\n";

        assertEquals(expected, actual);
    }

    @Test
    public void testReportableErrorSingleTrace() {
        try {
            throwRuntimeException("woops");
        } catch (RuntimeException e) {
            UserFault userFault = UserFault.makeUserFault(e);

            String actual = userFault.reportableError();
            String expected = "woops: java.lang.RuntimeException\n" +
                    "java.lang.RuntimeException: woops\n" +
                    "\tat testpkg.StackTraceHelper.throwRuntimeException\\(StackTraceHelper.java:\\d+\\)\n\n";

            assertTrue(actual.matches(expected), String.format("'%s' did not match '%s'", actual, expected));
            return;
        }

        fail("Exception should have been thrown and caught");
    }

    @Test
    public void testReportableErrorMultipleTraces() {
        try {
            callThenThrowRuntimeException("woops");
        } catch (RuntimeException e) {
            UserFault userFault = UserFault.makeUserFault(e);

            String actual = userFault.reportableError();
            String expected = "woops: java.lang.RuntimeException\n" +
                    "java.lang.RuntimeException: woops\n" +
                    "\tat testpkg.StackTraceHelper.throwRuntimeException\\(StackTraceHelper.java:\\d+\\)\n" +
                    "\tat testpkg.StackTraceHelper.callThenThrowRuntimeException\\(StackTraceHelper.java:\\d+\\)\n\n";

            assertTrue(actual.matches(expected), String.format("'%s' did not match '%s'", actual, expected));
            return;
        }

        fail("Exception should have been thrown and caught");
    }

    @Test
    public void testReportableErrorOnlyMessage() {
        String msg = "No public method named handleRequest with appropriate method signature found on class example.Function";
        UserFault userFault = UserFault.makeUserFault(msg);

        String expected = msg + '\n';
        String actual = userFault.reportableError();
        assertEquals(expected, actual);
    }

    @Test
    public void testSuppressedExceptionsAreIncluded() {
        try{
            throwCheckpointExceptionWithTwoSuppressedExceptions("error 1", "error 2");
        } catch(Exception e1) {
            UserFault userFault = UserFault.makeUserFault(e1);
            String reportableUserFault = userFault.reportableError();

            assertTrue(reportableUserFault.contains("com.amazonaws.services.lambda.crac.CheckpointException"), "CheckpointException missing in reported UserFault");
            assertTrue(reportableUserFault.contains("Suppressed: java.lang.RuntimeException: error 1"), "Suppressed error 1 missing in reported UserFault");
            assertTrue(reportableUserFault.contains("Suppressed: java.lang.RuntimeException: error 2"), "Suppressed error 2 missing in reported UserFault");
        }
    }

    @Test
    public void testCircularExceptionReference() {
        RuntimeException e1 = new RuntimeException();
        RuntimeException e2 = new RuntimeException(e1);
        e1.initCause(e2);

        try {
            throw e2;
        } catch (Exception e) {
            String stackTrace = UserFault.trace(e);
            String expectedStackTrace = "java.lang.RuntimeException: java.lang.RuntimeException\n" +
                            "Caused by: java.lang.RuntimeException\n" +
                            "Caused by: [CIRCULAR REFERENCE: java.lang.RuntimeException: java.lang.RuntimeException]\n";

            assertEquals(expectedStackTrace, stackTrace);
        }
    }

    @Test
    public void testCircularSuppressedExceptionReference() {
        RuntimeException e1 = new RuntimeException("Primary Exception");
        RuntimeException e2 = new RuntimeException(e1);
        RuntimeException e3 = new RuntimeException("Exception with suppressed");

        e1.addSuppressed(e2);
        e3.addSuppressed(e2);

        try {
            throw e3;
        } catch (Exception e) {
            String stackTrace = UserFault.trace(e);
            String expectedStackTrace = "java.lang.RuntimeException: Exception with suppressed\n" +
                            "\tSuppressed: java.lang.RuntimeException: java.lang.RuntimeException: Primary Exception\n" +
                            "\tCaused by: java.lang.RuntimeException: Primary Exception\n" +
                            "\t\tSuppressed: [CIRCULAR REFERENCE: java.lang.RuntimeException: java.lang.RuntimeException: Primary Exception]\n";

            assertEquals(expectedStackTrace, stackTrace);
        }
    }

    private Exception createExceptionWithStackTrace() {
        try {
            throw new RuntimeException("Test exception");
        } catch (RuntimeException e) {
            return e;
        }
    }

    @Test
    void testMakeInitErrorUserFault() {
        String className = "com.example.TestClass";
        Exception testException = createExceptionWithStackTrace();
        
        UserFault initFault = UserFault.makeInitErrorUserFault(testException, className);
        UserFault notFoundFault = UserFault.makeClassNotFoundUserFault(testException, className);
        
        assertNotNull(initFault.trace);
        assertNotNull(notFoundFault.trace);
        
        assertFalse(initFault.trace.contains("com.amazonaws.services.lambda.runtime"));
        assertFalse(notFoundFault.trace.contains("com.amazonaws.services.lambda.runtime"));
    }

    @Test
    void testMakeClassNotFoundUserFault() {
        String className = "com.example.MissingClass";
        Exception testException = new ClassNotFoundException("Class not found in classpath");
        
        UserFault fault = UserFault.makeClassNotFoundUserFault(testException, className);
        
        assertNotNull(fault);
        assertEquals("Class not found: com.example.MissingClass", fault.msg);
        assertEquals("java.lang.ClassNotFoundException", fault.exception);
        assertNotNull(fault.trace);
        assertFalse(fault.fatal);
        assertTrue(fault.trace.contains("ClassNotFoundException"));
    }
}
