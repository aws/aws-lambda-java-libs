/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

public enum RapidErrorType {
    BadFunctionCode,
    UserException,
    BeforeCheckpointError,
    AfterRestoreError;

    public String getRapidError() {
        return "Runtime." + this;
    }
}
