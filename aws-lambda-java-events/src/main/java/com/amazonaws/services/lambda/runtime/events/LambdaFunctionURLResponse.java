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

import lombok.ToString;

/**
 * Class to represent a Lambda function HTTP Response.
 *
 * @see <a href="https://docs.aws.amazon.com/lambda/latest/dg/urls-invocation.html">Invoking Lambda function URLs</a>
 */
@ToString(callSuper = true)
public class LambdaFunctionURLResponse extends APIGatewayV2HTTPResponse {

}
