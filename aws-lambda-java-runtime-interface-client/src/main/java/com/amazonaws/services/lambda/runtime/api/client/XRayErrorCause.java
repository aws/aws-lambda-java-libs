/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * helper class for serializing an exception in the format expected by XRay's web console.
 */
public class XRayErrorCause {
    private final String working_directory;
    private final Collection<XRayException> exceptions;
    private final Collection<String> paths;

    public XRayErrorCause(Throwable throwable) {
        working_directory = System.getProperty("user.dir");
        exceptions = Collections.unmodifiableCollection(Collections.singletonList(new XRayException(throwable)));
        paths = Collections.unmodifiableCollection(
                Arrays.stream(throwable.getStackTrace())
                        .map(XRayErrorCause::determineFileName)
                        .collect(Collectors.toSet()));
    }

    public String getWorking_directory() {
        return working_directory;
    }

    public Collection<XRayException> getExceptions() {
        return exceptions;
    }

    public Collection<String> getPaths() {
        return paths;
    }

    /**
     * This method provides compatibility between Java 8 and Java 11 in determining the fileName of the class in the
     * StackTraceElement.
     *
     * If the fileName property of the StackTraceElement is null (as it can be for native methods in Java 11), it
     * constructs it using the className by stripping out the package and appending ".java".
     */
    private static String determineFileName(StackTraceElement e) {
        String fileName = null;
        if(e.getFileName() != null) {
            fileName = e.getFileName();
        }
        if(fileName == null) {
            String className = e.getClassName();
            fileName = className == null ? null : className.substring(className.lastIndexOf('.') + 1) + ".java";
        }
        return fileName;
    }

    public static class XRayException {
        private final String message;
        private final String type;
        private final List<StackElement> stack;

        public XRayException(Throwable throwable) {
            this.message = throwable.getMessage();
            this.type = throwable.getClass().getName();
            this.stack = Arrays.stream(throwable.getStackTrace()).map(this::toStackElement).collect(Collectors.toList());
        }

        private StackElement toStackElement(StackTraceElement e) {
            return new StackElement(
                    e.getMethodName(),
                    determineFileName(e),
                    e.getLineNumber());
        }

        public String getMessage() {
            return message;
        }

        public String getType() {
            return type;
        }

        public List<StackElement> getStack() {
            return stack;
        }

        public static class StackElement {
            private final String label;
            private final String path;
            private final int line;

            private StackElement(String label, String path, int line) {
                this.label = label;
                this.path = path;
                this.line = line;
            }

            public String getLabel() {
                return label;
            }

            public String getPath() {
                return path;
            }

            public int getLine() {
                return line;
            }
        }
    }

}
