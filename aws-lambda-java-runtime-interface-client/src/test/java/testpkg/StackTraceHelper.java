/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package testpkg;

import com.amazonaws.services.lambda.crac.CheckpointException;

/**
 * A helper class for throwing exception which is not in the com.amazonaws.services.lambda.runtime.api.client package
 * to avoid the stack traces from being filtered out.
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

    public static void throwCheckpointExceptionWithTwoSuppressedExceptions(String msg1, String msg2) throws CheckpointException {
        CheckpointException e1 = new CheckpointException();
        e1.addSuppressed(new RuntimeException(msg1));
        e1.addSuppressed(new RuntimeException(msg2));
        throw e1;
    }
}
