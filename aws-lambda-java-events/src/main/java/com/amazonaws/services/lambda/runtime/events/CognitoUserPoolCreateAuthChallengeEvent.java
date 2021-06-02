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

import lombok.*;

import java.util.Map;

/**
 * Represent the class for the Cognito User Pool Create Auth Challenge Lambda Trigger
 *
 * See <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/user-pool-lambda-create-auth-challenge.html">Create Auth Challenge Lambda Trigger</a>
 *
 * @author jvdl <jvdl@amazon.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CognitoUserPoolCreateAuthChallengeEvent extends CognitoUserPoolEvent {

    /**
     * The request from the Amazon Cognito service.
     */
    private Request request;

    /**
     * The response from your Lambda trigger.
     */
    private Response response;

    @Builder(setterPrefix = "with")
    public CognitoUserPoolCreateAuthChallengeEvent(
            String version,
            String triggerSource,
            String region,
            String userPoolId,
            String userName,
            CallerContext callerContext,
            Request request,
            Response response) {
        super(version, triggerSource, region, userPoolId, userName, callerContext);
        this.request = request;
        this.response = response;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class Request extends CognitoUserPoolEvent.Request {
        /**
         * One or more key-value pairs that you can provide as custom input to the Lambda function that you specify for the create auth challenge trigger.
         */
        private Map<String, String> clientMetadata;
        /**
         * The name of the new challenge.
         */
        private String challengeName;
        private ChallengeResult[] session;
        /**
         * This boolean is populated when PreventUserExistenceErrors is set to ENABLED for your User Pool client.
         */
        private boolean userNotFound;

        @Builder(setterPrefix = "with")
        public Request(Map<String, String> userAttributes, Map<String, String> clientMetadata, String challengeName, ChallengeResult[] session, boolean userNotFound) {
            super(userAttributes);
            this.clientMetadata = clientMetadata;
            this.session = session;
            this.userNotFound = userNotFound;
            this.challengeName = challengeName;
        }
    }

    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @Data
    @NoArgsConstructor
    public static class ChallengeResult {
        /**
         * The challenge type. One of: "CUSTOM_CHALLENGE", "PASSWORD_VERIFIER", "SMS_MFA", "DEVICE_SRP_AUTH", "DEVICE_PASSWORD_VERIFIER", or "ADMIN_NO_SRP_AUTH".
         */
        private String challengeName;
        /**
         * Set to true if the user successfully completed the challenge, or false otherwise.
         */
        private boolean challengeResult;
        /**
         * Your name for the custom challenge. Used only if challengeName is CUSTOM_CHALLENGE.
         */
        private String challengeMetadata;
    }

    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @Data
    @NoArgsConstructor
    public static class Response {
        /**
         * One or more key-value pairs for the client app to use in the challenge to be presented to the user.
         * Contains the question that is presented to the user.
         */
        private Map<String, String> publicChallengeParameters;
        /**
         * Contains the valid answers for the question in publicChallengeParameters
         */
        private Map<String, String> privateChallengeParameters;
        /**
         * Your name for the custom challenge, if this is a custom challenge.
         */
        private String challengeMetadata;
    }
}
