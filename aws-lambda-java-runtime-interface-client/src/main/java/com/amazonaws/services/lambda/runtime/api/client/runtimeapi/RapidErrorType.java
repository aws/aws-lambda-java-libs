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
