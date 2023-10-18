// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class VpcLatticeResponse {

    private boolean isBase64Encoded;
    private int statusCode;
    private String statusDescription;
    private Map<String, String> headers;
    private String body;

}