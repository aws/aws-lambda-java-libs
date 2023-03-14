/*
 *  Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.amazonaws.services.lambda.crac;

public abstract class Context<R extends Resource> implements Resource {

    protected Context() {
    }

    @Override
    public abstract void beforeCheckpoint(Context<? extends Resource> context)
            throws CheckpointException;

    @Override
    public abstract void afterRestore(Context<? extends Resource> context)
            throws RestoreException;

    public abstract void register(R resource);
}
