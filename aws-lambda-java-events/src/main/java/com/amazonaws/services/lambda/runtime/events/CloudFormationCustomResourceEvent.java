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

package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * Class to represent the custom resource request event from CloudFormation.
 *
 * @see <a href="https://docs.aws.amazon.com/lambda/latest/dg/services-cloudformation.html">Using AWS Lambda with AWS CloudFormation</a>
 *
 * @author msailes <msailes@amazon.co.uk>
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class CloudFormationCustomResourceEvent implements Serializable  {

    @JsonProperty("RequestType")
    private String requestType;

    @JsonProperty("ServiceToken")
    private String serviceToken;

    @JsonProperty("ResponseURL")
    private String responseUrl;

    @JsonProperty("StackId")
    private String stackId;

    @JsonProperty("RequestId")
    private String requestId;

    @JsonProperty("LogicalResourceId")
    private String logicalResourceId;

    @JsonProperty("PhysicalResourceId")
    private String physicalResourceId;

    @JsonProperty("ResourceType")
    private String resourceType;

    @JsonProperty("ResourceProperties")
    private Map<String, Object> resourceProperties;

    @JsonProperty("OldResourceProperties")
    private Map<String, Object> oldResourceProperties;
}
