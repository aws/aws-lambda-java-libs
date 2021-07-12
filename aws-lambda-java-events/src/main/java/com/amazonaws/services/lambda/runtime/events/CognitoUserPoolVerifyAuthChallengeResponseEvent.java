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
 * Represent the class for the Cognito User Pool Verify Auth Challenge Response Lambda Trigger
 *
 * See <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/user-pool-lambda-verify-auth-challenge-response.html">Verify Auth Challenge Response Lambda Trigger</a>
 *
 * @author jvdl <jvdl@amazon.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CognitoUserPoolVerifyAuthChallengeResponseEvent extends CognitoUserPoolEvent {
    /**
     * The request from the Amazon Cognito service.
     */
    private Request request;

    /**
     * The response from your Lambda trigger.
     */
    private Response response;

    @Builder(setterPrefix = "with")
    public CognitoUserPoolVerifyAuthChallengeResponseEvent(
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
         * One or more key-value pairs that you can provide as custom input to the Lambda function that you specify for the verify auth challenge trigger.
         */
        private Map<String, String> clientMetadata;
        /**
         * This parameter comes from the Create Auth Challenge trigger, and is compared against a user's challengeAnswer to determine whether the user passed the challenge.
         */
        private Map<String, String> privateChallengeParameters;
        /**
         * The answer from the user's response to the challenge.
         */
        private Map<String, String> challengeAnswer;
        /**
         * This boolean is populated when PreventUserExistenceErrors is set to ENABLED for your User Pool client
         */
        private boolean userNotFound;

        @Builder(setterPrefix = "with")
        public Request(Map<String, String> userAttributes,
                       Map<String, String> clientMetadata,
                       Map<String, String> challengeAnswer,
                       Map<String, String> privateChallengeParameters,
                       boolean userNotFound) {
            super(userAttributes);
            this.clientMetadata = clientMetadata;
            this.userNotFound = userNotFound;
            this.challengeAnswer = challengeAnswer;
            this.privateChallengeParameters = privateChallengeParameters;
        }
    }

    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @Data
    @NoArgsConstructor
    public static class Response {
        /**
         * Set to true if the user has successfully completed the challenge, or false otherwise.
         */
        private boolean answerCorrect;
    }
}
