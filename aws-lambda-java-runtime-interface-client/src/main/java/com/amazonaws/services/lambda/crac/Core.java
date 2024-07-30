/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.crac;

/**
 * Provides the global context for registering resources.
 */
public final class Core {

    private static Context<Resource> globalContext = new ContextImpl();

    private Core() {
    }

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
