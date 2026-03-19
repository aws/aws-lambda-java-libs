/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

public class UnsafeUtilTest {

    @Test
    void testTheUnsafeIsInitialized() {
        assertNotNull(UnsafeUtil.TheUnsafe);
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

    // IllegalAccessLogger only exists on JDK 9-16; skipped on JDK 8 (no module
    // system) and JDK 17+ (class removed).
    @Test
    @EnabledForJreRange(min = JRE.JAVA_9, max = JRE.JAVA_16)
    void testDisableIllegalAccessWarning() throws Exception {
        // We disable the warning log for "jdk.internal.module.IllegalAccessLogger"
        UnsafeUtil.disableIllegalAccessWarning();

        Class<?> illegalAccessLoggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
        Field loggerField = illegalAccessLoggerClass.getDeclaredField("logger");

        // Now we are getting it back with getObjectVolatile and verify that the logger
        // is null. We are not using default reflection because that will throw because
        // that field is private, defeating the point of the test.
        Object loggerValue = UnsafeUtil.TheUnsafe.getObjectVolatile(
                illegalAccessLoggerClass,
                UnsafeUtil.TheUnsafe.staticFieldOffset(loggerField));
        assertNull(loggerValue);
    }

    @Test
    void testDisableIllegalAccessWarningDoesNotThrow() {
        assertDoesNotThrow(() -> UnsafeUtil.disableIllegalAccessWarning());
    }

    @Test
    void testPrivateConstructor() {
        assertThrows(IllegalAccessException.class, () -> {
            UnsafeUtil.class.getDeclaredConstructor().newInstance();
        });
    }
}
