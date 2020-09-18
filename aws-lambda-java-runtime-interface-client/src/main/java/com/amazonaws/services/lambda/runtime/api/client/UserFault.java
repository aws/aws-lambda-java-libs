/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class UserFault extends RuntimeException {
    private static final long serialVersionUID = 0;

    public final String msg;
    public final String exception;
    public final String trace;
    public final Boolean fatal;

    private static final String packagePrefix = AWSLambda.class.getPackage().getName();

    public UserFault(String msg, String exception, String trace) {
        this.msg = msg;
        this.exception = exception;
        this.trace = trace;
        this.fatal = false;
    }

    public UserFault(String msg, String exception, String trace, Boolean fatal) {
        this.msg = msg;
        this.exception = exception;
        this.trace = trace;
        this.fatal = fatal;
    }

    /**
     * Convenience function to report a fault given an exception. The constructed fault is marked non-fatal.
     * No more user code should run after a fault.
     */
    public static UserFault makeUserFault(Throwable t) {
        return makeUserFault(t, false);
    }

    public static UserFault makeUserFault(Throwable t, boolean fatal) {
        final String msg = t.getLocalizedMessage() == null ? t.getClass().getName() : t.getLocalizedMessage();
        return new UserFault(msg, t.getClass().getName(), trace(t), fatal);
    }

    /**
     * Convenience function to report a fault given a message.
     * No more user code should run after a fault.
     */
    public static UserFault makeUserFault(String msg) {
        return new UserFault(msg, null, null);
    }

    /**
     * Convert a throwable's stack trace to a String
     */
    public static String trace(Throwable t) {
        filterStackTrace(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * remove our runtime code from the stack trace recursively. Returns
     * the same object for convenience.
     */
    public static <T extends Throwable> T filterStackTrace(T t) {
        StackTraceElement[] trace = t.getStackTrace();
        for(int i = 0; i < trace.length; i++) {
            if(trace[i].getClassName().startsWith(packagePrefix)) {
                StackTraceElement[] newTrace = new StackTraceElement[i];
                System.arraycopy(trace, 0, newTrace, 0, i);
                t.setStackTrace(newTrace);
                break;
            }
        }


        Throwable cause = t.getCause();
        if(cause != null) {
            filterStackTrace(cause);
        }
        return t;
    }

    static UserFault makeInitErrorUserFault(Throwable e, String className) {
        return new UserFault(
                "Error loading class " + className + (e.getMessage() == null ? "" : ": " + e.getMessage()),
                e.getClass().getName(),
                trace(e),
                true
        );
    }

    static UserFault makeClassNotFoundUserFault(Throwable e, String className) {
        return new UserFault(
                "Class not found: " + className,
                e.getClass().getName(),
                trace(e),
                false
        );
    }

    public String reportableError() {
        if(this.exception != null || this.trace != null) {
            return String.format("%s: %s\n%s\n",
                    this.msg,
                    this.exception == null ? "" : this.exception,
                    this.trace == null ? "" : this.trace);
        }
        return String.format("%s\n", this.msg);
    }
}
