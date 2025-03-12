/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnsafeUtilTest {

    @Test
    void testTheUnsafeIsInitialized() {
        assertNotNull(UnsafeUtil.TheUnsafe);
    }

    @Test
    void testDisableIllegalAccessWarning() {
        assertDoesNotThrow(() -> UnsafeUtil.disableIllegalAccessWarning());
    }

    @Test
    void testThrowException() {
        Exception testException = new Exception("Test exception");
        
        try {
            UnsafeUtil.throwException(testException);
            fail("Should have thrown an exception");
        } catch (Throwable e) {
            assertEquals("Test exception", e.getMessage());
            assertSame(testException, e);
        }
    }

    @Test
    void testDisableIllegalAccessWarning() {
        assertDoesNotThrow(() -> UnsafeUtil.disableIllegalAccessWarning());
        try {
            Class<?> illegalAccessLoggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field loggerField = illegalAccessLoggerClass.getDeclaredField("logger");
            loggerField.setAccessible(true);
            Object loggerValue = loggerField.get(null);
            assertNull(loggerValue);
        } catch (ClassNotFoundException e) {
            assertTrue(true);
        } catch (NoSuchFieldException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testPrivateConstructor() {
        assertThrows(IllegalAccessException.class, () -> {
            UnsafeUtil.class.getDeclaredConstructor().newInstance();
        });
    }
}
