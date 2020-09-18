/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package testpkg;

/**
 * A helper class for throwing exception which is not in the `lambdainternal` package to avoid the stack traces from
 * being filtered out.
 *
 */
public class StackTraceHelper {
    /**
     * Throws a RuntimeException directly with msg as the message.
     */
    public static void throwRuntimeException(String msg){
        throw new RuntimeException(msg);
    }

    /**
     * Calls another method which throws a RuntimeException with msg as the message.
     */
    public static void callThenThrowRuntimeException(String msg){
        throwRuntimeException(msg);
    }
}
