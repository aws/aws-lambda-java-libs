/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

public final class HandlerInfo {
    public static class InvalidHandlerException extends RuntimeException {
        public static final long serialVersionUID = -1;
    }

    public final Class<?> clazz;
    public final String methodName;

    public HandlerInfo (Class<?> clazz, String methodName) {
        this.clazz = clazz;
        this.methodName = methodName;
    }

    public static HandlerInfo fromString(String handler, ClassLoader cl) throws ClassNotFoundException, NoClassDefFoundError, InvalidHandlerException {
        final int colonLoc = handler.lastIndexOf("::");
        final String className;
        final String methodName;
        if(colonLoc < 0) {
            className = handler;
            methodName = null;
        } else {
            className = handler.substring(0, colonLoc);
            methodName = handler.substring(colonLoc + 2);
        }

        if(className.isEmpty() || (methodName != null && methodName.isEmpty())) {
            throw new InvalidHandlerException();
        }
        return new HandlerInfo(Class.forName(className, true, cl), methodName);
    }

    public static String className(String handler) {
        final int colonLoc = handler.lastIndexOf("::");
        return (colonLoc < 0) ? handler : handler.substring(0, colonLoc);
    }
}
