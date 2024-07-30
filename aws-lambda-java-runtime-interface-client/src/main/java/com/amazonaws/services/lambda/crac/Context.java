/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
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
