/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto;

public class ErrorRequest {
    public String errorMessage;
    public String errorType;
    public String[] stackTrace;

    @SuppressWarnings("unused")
    public ErrorRequest() {
    }

    public ErrorRequest(String errorMessage, String errorType, String[] stackTrace) {
        this.errorMessage = errorMessage;
        this.errorType = errorType;
        this.stackTrace = stackTrace;
    }
}
