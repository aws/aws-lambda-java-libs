/*
 *  Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.amazonaws.services.lambda.crac;

/**
 * Provides the global context for registering resources.
 */
public final class Core {

    private Core() {
    }

    private static Context<Resource> globalContext = new ContextImpl();

    public static Context<Resource> getGlobalContext() {
        return globalContext;
    }

    public static void checkpointRestore() {
        throw new UnsupportedOperationException();
    }

    static void resetGlobalContext() {
        globalContext = new ContextImpl();
    }
}
