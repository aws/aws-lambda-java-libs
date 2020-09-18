/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Utilities for easy access to sun.misc.Unsafe
 */
public final class UnsafeUtil {
    public static final Unsafe TheUnsafe;

    static {
        try {
            final Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            TheUnsafe = (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new Error("failed to load Unsafe", e);
        }
    }

    private UnsafeUtil() {
    }

    public static void disableIllegalAccessWarning() {
        try {
            Class illegalAccessLoggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field loggerField = illegalAccessLoggerClass.getDeclaredField("logger");
            TheUnsafe.putObjectVolatile(illegalAccessLoggerClass, TheUnsafe.staticFieldOffset(loggerField), null);
        } catch (Throwable t) { /* ignore */ }
    }

    public static RuntimeException throwException(Throwable t) {
        TheUnsafe.throwException(t);
        throw new Error("should never get here");
    }
}
