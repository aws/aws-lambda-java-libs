/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

import java.io.Serializable;
import java.util.Map;

/**
 * Class to represent the custom resource request event from CloudFormation.
 *
 * CloudFormation invokes your Lambda function asynchronously with this event and includes a callback URL. The function
 * is responsible for returning a response to the callback URL that indicates success or failure.
 *
 * @see <a href="https://docs.aws.amazon.com/lambda/latest/dg/services-cloudformation.html">Using AWS Lambda with AWS CloudFormation</a>
 *
 * @author msailes <msailes@amazon.co.uk>
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class CloudFormationCustomResourceEvent implements Serializable, Cloneable  {

    private String requestType;
    private String serviceToken;
    private String responseUrl;
    private String stackId;
    private String requestId;
    private String logicalResourceId;
    private String physicalResourceId;
    private String resourceType;
    private Map<String, Object> resourceProperties;
    private Map<String, Object> oldResourceProperties;
}
