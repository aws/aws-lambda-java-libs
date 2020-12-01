/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static testpkg.StackTraceHelper.callThenThrowRuntimeException;
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
}
