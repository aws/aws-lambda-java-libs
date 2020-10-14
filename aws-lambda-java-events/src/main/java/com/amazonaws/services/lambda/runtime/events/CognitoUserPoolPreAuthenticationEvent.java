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

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represent the class for the Cognito User Pool Pre Authentication Lambda Trigger
 *
 * See <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/user-pool-lambda-pre-authentication.html">Pre Authentication Lambda Trigger</a>
 *
 * @author jvdl <jvdl@amazon.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CognitoUserPoolPreAuthenticationEvent extends CognitoUserPoolEvent {

    /**
     * The request from the Amazon Cognito service.
     */
    private Request request;

    @Builder(setterPrefix = "with")
    public CognitoUserPoolPreAuthenticationEvent(
            String version,
            String triggerSource,
            String region,
            String userPoolId,
            String userName,
            CallerContext callerContext,
            Request request) {
        super(version, triggerSource, region, userPoolId, userName, callerContext);
        this.request = request;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Request extends CognitoUserPoolEvent.Request {
        /**
         * One or more name-value pairs containing the validation data in the request to register a user.
         * The validation data is set and then passed from the client in the request to register a user.
         */
        private Map<String, String> validationData;

        /**
         * This boolean is populated when PreventUserExistenceErrors is set to ENABLED for your User Pool client.
         */
        private boolean userNotFound;

        @Builder(setterPrefix = "with")
        public Request(Map<String, String> userAttributes, Map<String, String> validationData, boolean userNotFound) {
            super(userAttributes);
            this.validationData = validationData;
            this.userNotFound = userNotFound;
        }
    }
}
