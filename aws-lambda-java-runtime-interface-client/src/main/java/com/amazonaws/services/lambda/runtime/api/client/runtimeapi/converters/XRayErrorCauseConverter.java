/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.converters;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.StackElement;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayErrorCause;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class XRayErrorCauseConverter {
    private XRayErrorCauseConverter() {
    }

    public static XRayErrorCause fromThrowable(Throwable throwable) {
        String workingDirectory = System.getProperty("user.dir");
        XRayException xRayException = getXRayExceptionFromThrowable(throwable);
        Collection<XRayException> exceptions = Collections.singletonList(xRayException);
        Collection<String> paths = Arrays.stream(throwable.getStackTrace())
                .map(XRayErrorCauseConverter::determineFileName)
                .collect(Collectors.toSet());

        return new XRayErrorCause(workingDirectory, exceptions, paths);
    }

    static XRayException getXRayExceptionFromThrowable(Throwable throwable) {
        String message = throwable.getMessage();
        String type = throwable.getClass().getName();
        List<StackElement> stack = Arrays.stream(throwable.getStackTrace())
                .map(XRayErrorCauseConverter::convertStackTraceElement)
                .collect(Collectors.toList());
        return new XRayException(message, type, stack);
    }

    static String determineFileName(StackTraceElement e) {
        String fileName = null;
        if (e.getFileName() != null) {
            fileName = e.getFileName();
        }
        if (fileName == null) {
            String className = e.getClassName();
            fileName = className.substring(className.lastIndexOf('.') + 1) + ".java";
        }
        return fileName;
    }

    static StackElement convertStackTraceElement(StackTraceElement e) {
        return new StackElement(
                e.getMethodName(),
                determineFileName(e),
                e.getLineNumber());
    }
}
