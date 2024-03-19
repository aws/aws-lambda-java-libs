/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto;

import java.util.List;

public class XRayException {
    public String message;
    public String type;
    public List<StackElement> stack;

    @SuppressWarnings("unused")
    public XRayException() {
    }

    public XRayException(String message, String type, List<StackElement> stack) {
        this.message = message;
        this.type = type;
        this.stack = stack;
    }
}
