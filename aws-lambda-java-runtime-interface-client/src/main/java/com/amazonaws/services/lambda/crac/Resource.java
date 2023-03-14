/*
 *  Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.amazonaws.services.lambda.crac;

public interface Resource {
    void afterRestore(Context<? extends Resource> context) throws Exception;

    void beforeCheckpoint(Context<? extends Resource> context) throws Exception;
}
