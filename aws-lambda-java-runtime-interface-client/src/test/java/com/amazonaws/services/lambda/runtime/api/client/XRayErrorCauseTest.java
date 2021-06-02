/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static testpkg.StackTraceHelper.callThenThrowRuntimeException;

public class XRayErrorCauseTest {

    private static final String TEST_WORKING_DIR = "/tmp";
    private static final String ORIGINAL_WORKING_DIR = System.getProperty("user.dir");

    @BeforeEach
    public void before() {
        System.setProperty("user.dir", TEST_WORKING_DIR);
    }

    @AfterEach
    public void after() {
        System.setProperty("user.dir", ORIGINAL_WORKING_DIR);
    }

    @Test
    public void xrayErrorCauseTest() {
        try {
            callThenThrowRuntimeException("woops");
        } catch (Throwable t) {
            UserFault.filterStackTrace(t);
            assertXrayErrorCause(t);
        }
    }

    @Test
    public void xrayErrorCauseTestNoFileName() {
        try {
            callThenThrowRuntimeException("woops");
        } catch (Throwable t) {
            UserFault.filterStackTrace(t);
            clearStackTraceElementsFilename(t);
            assertXrayErrorCause(t);
        }
    }

    private void assertXrayErrorCause(Throwable t) {
        XRayErrorCause xRayErrorCause = new XRayErrorCause(t);

        assertEquals(TEST_WORKING_DIR, xRayErrorCause.getWorking_directory());

        assertEquals(1, xRayErrorCause.getPaths().size());
        assertTrue(xRayErrorCause.getPaths().contains("StackTraceHelper.java"));

        assertEquals(1, xRayErrorCause.getExceptions().size());
        XRayErrorCause.XRayException xRayException = xRayErrorCause.getExceptions().iterator().next();
        assertEquals("woops", xRayException.getMessage());
        assertEquals("java.lang.RuntimeException", xRayException.getType());

        assertEquals("throwRuntimeException", xRayException.getStack().get(0).getLabel());
        assertEquals("StackTraceHelper.java", xRayException.getStack().get(0).getPath());
        assertTrue(xRayException.getStack().get(0).getLine() > 0);

        assertEquals("callThenThrowRuntimeException", xRayException.getStack().get(1).getLabel());
        assertEquals("StackTraceHelper.java", xRayException.getStack().get(1).getPath());
        assertTrue(xRayException.getStack().get(0).getLine() > 0);
    }

    private void clearStackTraceElementsFilename(Throwable t) {
        StackTraceElement[] stackTrace = t.getStackTrace();
        StackTraceElement[] updatedStackTrace = new StackTraceElement[stackTrace.length];

        for(int i  = 0; i < updatedStackTrace.length; i++) {
            StackTraceElement curr = stackTrace[i];
            updatedStackTrace[i] = new StackTraceElement(curr.getClassName(), curr.getMethodName(), null, curr.getLineNumber());
        }

        t.setStackTrace(updatedStackTrace);
    }


}
