/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import org.junit.jupiter.api.Test;

import java.io.IOError;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FailureTest {
    static class MySecretException extends ClassNotFoundException {}

    @Test
    public void doesNotReportCustomException() {
        Throwable throwable = new MySecretException();
        assertEquals(ClassNotFoundException.class, Failure.getReportableExceptionClass(throwable));

        MySecretException mySecretException = new MySecretException();
        assertEquals(ClassNotFoundException.class, Failure.getReportableExceptionClass(mySecretException));
    }

    @Test
    public void correctlyReportsExceptionsWeTrack() {
        Throwable ioError = new IOError(new Throwable());
        assertEquals(IOError.class, Failure.getReportableExceptionClass(ioError));

        Throwable error = new Error(new Throwable());
        assertEquals(Error.class, Failure.getReportableExceptionClass(error));

        ClassNotFoundException classNotFoundException = new ClassNotFoundException();
        assertEquals(ClassNotFoundException.class, Failure.getReportableExceptionClass(classNotFoundException));

        VirtualMachineError virtualMachineError = new OutOfMemoryError();
        assertEquals(VirtualMachineError.class, Failure.getReportableExceptionClass(virtualMachineError));

        Throwable linkageError = new LinkageError();
        assertEquals(LinkageError.class, Failure.getReportableExceptionClass(linkageError));

        Throwable exceptionInInitializerError = new ExceptionInInitializerError();
        assertEquals(ExceptionInInitializerError.class, Failure.getReportableExceptionClass(exceptionInInitializerError));

        Throwable noClassDefFoundError = new NoClassDefFoundError();
        assertEquals(NoClassDefFoundError.class, Failure.getReportableExceptionClass(noClassDefFoundError));

        Throwable invalidHandlerException = new HandlerInfo.InvalidHandlerException();
        assertEquals(HandlerInfo.InvalidHandlerException.class, Failure.getReportableExceptionClass(invalidHandlerException));

        Throwable throwable = new Throwable();
        assertEquals(Throwable.class, Failure.getReportableExceptionClass(throwable));
    }

    @Test
    public void verifyCorrectClassName() {
        Throwable ioError = new IOError(new Throwable());
        assertEquals("java.io.IOError", Failure.getReportableExceptionClassName(ioError));

        Throwable error = new Error(new Throwable());
        assertEquals("java.lang.Error", Failure.getReportableExceptionClassName(error));

        ClassNotFoundException classNotFoundException = new ClassNotFoundException();
        assertEquals("java.lang.ClassNotFoundException", Failure.getReportableExceptionClassName(classNotFoundException));
    }
}
