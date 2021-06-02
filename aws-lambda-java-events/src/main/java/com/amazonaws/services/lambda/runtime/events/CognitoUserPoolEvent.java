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

import java.util.Map;

/**
 * Represent the base class for all Cognito User Pool Events
 *
 * See <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/cognito-user-identity-pools-working-with-aws-lambda-triggers.html#cognito-user-pools-lambda-trigger-event-parameter-shared">Customizing User Pool Workflows with Lambda Triggers</a>
 *
 * @author jvdl <jvdl@amazon.com>
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public abstract class CognitoUserPoolEvent {

    /**
     * The version number of your Lambda function.
     */
    private String version;

    /**
     * The name of the event that triggered the Lambda function.
     */
    private String triggerSource;

    /**
     * The AWS Region.
     */
    private String region;

    /**
     * The user pool ID for the user pool.
     */
    private String userPoolId;

    /**
     * The username of the current user.
     */
    private String userName;

    /**
     * The caller context.
     */
    private CallerContext callerContext;

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public static abstract class Request {
        /**
         * One or more pairs of user attribute names and values.
         */
        private Map<String, String> userAttributes;
    }

    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @Data
    @NoArgsConstructor
    public static class CallerContext {
        /**
         * The AWS SDK version number.
         */
        private String awsSdkVersion;

        /**
         * The ID of the client associated with the user pool.
         */
        private String clientId;
    }
}
