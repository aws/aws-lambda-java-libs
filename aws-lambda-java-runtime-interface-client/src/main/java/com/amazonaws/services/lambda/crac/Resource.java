/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.crac;

public interface Resource {
    void afterRestore(Context<? extends Resource> context) throws Exception;

    void beforeCheckpoint(Context<? extends Resource> context) throws Exception;
}
