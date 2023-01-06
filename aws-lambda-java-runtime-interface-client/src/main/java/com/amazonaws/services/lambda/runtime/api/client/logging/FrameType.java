/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

public enum FrameType {

    LOG(0xa55a0003);

    private final int val;

    FrameType(int val) {
        this.val = val;
    }

    public int getValue() {
        return this.val;
    }
}
