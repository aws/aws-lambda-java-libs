/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.converters.XRayErrorCauseConverter;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayErrorCause;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayException;
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
        XRayErrorCause xRayErrorCause = XRayErrorCauseConverter.fromThrowable(t);

        assertEquals(TEST_WORKING_DIR, xRayErrorCause.working_directory);

        assertEquals(1, xRayErrorCause.paths.size());
        assertTrue(xRayErrorCause.paths.contains("StackTraceHelper.java"));

        assertEquals(1, xRayErrorCause.exceptions.size());
        XRayException xRayException = xRayErrorCause.exceptions.iterator().next();
        assertEquals("woops", xRayException.message);
        assertEquals("java.lang.RuntimeException", xRayException.type);

        assertEquals("throwRuntimeException", xRayException.stack.get(0).label);
        assertEquals("StackTraceHelper.java", xRayException.stack.get(0).path);
        assertTrue(xRayException.stack.get(0).line > 0);

        assertEquals("callThenThrowRuntimeException", xRayException.stack.get(1).label);
        assertEquals("StackTraceHelper.java", xRayException.stack.get(1).path);
        assertTrue(xRayException.stack.get(0).line > 0);
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
