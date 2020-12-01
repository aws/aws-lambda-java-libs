/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import java.io.IOError;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Failure {

    private static final Class[] reportableExceptionsArray = {
            Error.class,
            ClassNotFoundException.class,
            IOError.class,
            Throwable.class,
            VirtualMachineError.class,
            LinkageError.class,
            ExceptionInInitializerError.class,
            NoClassDefFoundError.class,
            HandlerInfo.InvalidHandlerException.class
    };

    private static final List<Class> sortedExceptions = Collections.unmodifiableList(
            Arrays.stream(reportableExceptionsArray).sorted(new ClassHierarchyComparator()).collect(Collectors.toList()));

    private final String errorMessage;
    private final String errorType;
    private final String[] stackTrace;
    private final Failure cause;

    public Failure(Throwable t) {
        this.errorMessage = t.getLocalizedMessage() == null ? t.getClass().getName() : t.getLocalizedMessage();
        this.errorType = t.getClass().getName();
        StackTraceElement[] trace = t.getStackTrace();
        this.stackTrace = new String[trace.length];
        for( int i = 0; i < trace.length; i++) {
            this.stackTrace[i] = trace[i].toString();
        }
        Throwable cause = t.getCause();
        this.cause = (cause == null) ? null : new Failure(cause);
    }

    public Failure(UserFault userFault) {
        this.errorMessage = userFault.msg;
        this.errorType = userFault.exception;
        // Not setting stacktrace for compatibility with legacy/native runtime
        this.stackTrace = null;
        this.cause = null;
    }

    public static Class getReportableExceptionClass(Throwable customerException) {
        return sortedExceptions
                .stream()
                .filter(e -> e.isAssignableFrom(customerException.getClass()))
                .findFirst()
                .orElse(Throwable.class);
    }

    public static String getReportableExceptionClassName(Throwable f) {
        return getReportableExceptionClass(f).getName();
    }

    public static boolean isInvokeFailureFatal(Throwable t) {
        return t instanceof VirtualMachineError || t instanceof IOError;
    }

    private static class ClassHierarchyComparator implements Comparator<Class> {
        @Override
        public int compare(Class o1, Class o2) {
            if (o1.isAssignableFrom(o2)) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public String getErrorType() {
        return errorType;
    }
}
