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

import java.io.Serializable;

/**
 * Represents an event for an AWS Config rule's function.
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class ConfigEvent implements Serializable {

    private static final long serialVersionUID = -3484211708255634243L;

    /**
     * The AWS Config event version.
     *
     */
    private String version;

    /**
     * The JSON-encoded notification published by AWS Config.
     *
     */
    private String invokingEvent;

    /**
     * The JSON-encoded map containing the AWS Config rule parameters.
     *
     */
    private String ruleParameters;

    /**
     * The token associated with the invocation of the AWS Config rule's Lambda function.
     *
     */
    private String resultToken;

    /**
     * The ARN of the AWS Config rule that triggered the event.
     *
     */
    private String configRuleArn;

    /**
     * The ID of the AWS Config rule that triggered the event.
     *
     */
    private String configRuleId;

    /**
     * The name of the AWS Config rule that triggered the event.
     *
     */
    private String configRuleName;

    /**
     * The account ID of the AWS Config rule that triggered the event.
     *
     */
    private String accountId;

    /**
     * The ARN of the IAM role that is assigned to AWS Config.
     *
     */
    private String executionRoleArn;

    /**
     * Whether the AWS resource to be evaluated has been removed from the AWS Config rule's scope.
     *
     */
    private boolean eventLeftScope;

}
