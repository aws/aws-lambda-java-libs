/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto;

import java.util.Collection;

public class XRayErrorCause {
    public String working_directory;
    public Collection<XRayException> exceptions;
    public Collection<String> paths;

    @SuppressWarnings("unused")
    public XRayErrorCause() {

    }

    public XRayErrorCause(String working_directory, Collection<XRayException> exceptions, Collection<String> paths) {
        this.working_directory = working_directory;
        this.exceptions = exceptions;
        this.paths = paths;
    }
}
