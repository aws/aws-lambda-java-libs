/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.ErrorRequest;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayErrorCause;

public class LambdaError {

    public final ErrorRequest errorRequest;

    public final XRayErrorCause xRayErrorCause;

    public final RapidErrorType errorType;

    public LambdaError(ErrorRequest errorRequest, XRayErrorCause xRayErrorCause, RapidErrorType errorType) {
        this.errorRequest = errorRequest;
        this.xRayErrorCause = xRayErrorCause;
        this.errorType = errorType;
    }

    public LambdaError(ErrorRequest errorRequest, RapidErrorType errorType) {
        this(errorRequest, null, errorType);
    }
}
