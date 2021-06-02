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
 * Represent the class for the Cognito User Pool Pre Sign-up Lambda Trigger
 *
 * See <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/user-pool-lambda-pre-sign-up.html">Pre Sign-up Lambda Trigger</a>
 *
 * @author jvdl <jvdl@amazon.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CognitoUserPoolPreSignUpEvent extends CognitoUserPoolEvent {

    /**
     * The request from the Amazon Cognito service.
     */
    private Request request;

    /**
     * The response from your Lambda trigger.
     */
    private Response response;

    @Builder(setterPrefix = "with")
    public CognitoUserPoolPreSignUpEvent(
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
         * One or more name-value pairs containing the validation data in the request to register a user.
         * The validation data is set and then passed from the client in the request to register a user.
         */
        private Map<String, String> validationData;

        /**
         * One or more key-value pairs that you can provide as custom input
         * to the Lambda function that you specify for the pre sign-up trigger.
         */
        private Map<String, String> clientMetadata;

        @Builder(setterPrefix = "with")
        public Request(Map<String, String> userAttributes, Map<String, String> validationData, Map<String, String> clientMetadata) {
            super(userAttributes);
            this.validationData = validationData;
            this.clientMetadata = clientMetadata;
        }
    }

    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @Data
    @NoArgsConstructor
    public static class Response {
        /**
         * Set to true to auto-confirm the user, or false otherwise.
         */
        private boolean autoConfirmUser;

        /**
         * Set to true to set as verified the phone number of a user who is signing up, or false otherwise.
         * If autoVerifyPhone is set to true, the phone_number attribute must have a valid, non-null value.
         */
        private boolean autoVerifyPhone;

        /**
         * Set to true to set as verified the email of a user who is signing up, or false otherwise.
         * If autoVerifyEmail is set to true, the email attribute must have a valid, non-null value.
         */
        private boolean autoVerifyEmail;
    }
}
