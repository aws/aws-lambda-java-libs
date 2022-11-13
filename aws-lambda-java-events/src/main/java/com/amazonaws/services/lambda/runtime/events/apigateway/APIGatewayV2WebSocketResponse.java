/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.amazonaws.services.lambda.runtime.events.apigateway;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Tim Gustafson <tjg@amazon.com>
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class APIGatewayV2WebSocketResponse implements Serializable {

    private static final long serialVersionUID = -5155789062248356200L;

    @JsonProperty("isBase64Encoded")
    @Builder.Default private boolean isBase64Encoded = false;
    private int statusCode;
    private Map<String, String> headers;
    private Map<String, String[]> multiValueHeaders;
    private String body;

}