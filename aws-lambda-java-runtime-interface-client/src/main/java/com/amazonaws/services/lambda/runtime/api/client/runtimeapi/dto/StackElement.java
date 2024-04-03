/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto;

public class StackElement {
    public String label;
    public String path;
    public int line;

    @SuppressWarnings("unused")
    public StackElement() {
    }

    public StackElement(String label, String path, int line) {
        this.label = label;
        this.path = path;
        this.line = line;
    }
}
