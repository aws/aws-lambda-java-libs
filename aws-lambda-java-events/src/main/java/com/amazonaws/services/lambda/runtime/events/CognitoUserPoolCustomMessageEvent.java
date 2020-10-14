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
 * Represent the class for the Cognito User Pool Custom Message Lambda Trigger
 *
 * See <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/user-pool-lambda-custom-message.html">Custom Message Lambda Trigger</a>
 *
 * @author jvdl <jvdl@amazon.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CognitoUserPoolCustomMessageEvent extends CognitoUserPoolEvent {
    /**
     * The request from the Amazon Cognito service.
     */
    private Request request;

    /**
     * The response from your Lambda trigger.
     */
    private Response response;

    @Builder(setterPrefix = "with")
    public CognitoUserPoolCustomMessageEvent(
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
         * One or more key-value pairs that you can provide as custom input to the Lambda function that you specify for the custom message trigger.
         */
        private Map<String, String> clientMetadata;
        /**
         * A string for you to use as the placeholder for the verification code in the custom message.
         */
        private String codeParameter;
        /**
         * The username parameter. It is a required request parameter for the admin create user flow.
         */
        private String usernameParameter;

        @Builder(setterPrefix = "with")
        public Request(Map<String, String> userAttributes, Map<String, String> clientMetadata, String codeParameter, String usernameParameter) {
            super(userAttributes);
            this.clientMetadata = clientMetadata;
            this.codeParameter = codeParameter;
            this.usernameParameter = usernameParameter;
        }
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Response {
        /**
         * The custom SMS message to be sent to your users. Must include the codeParameter value received in the request.
         */
        private String smsMessage;
        /**
         * The custom email message to be sent to your users. Must include the codeParameter value received in the request.
         */
        private String emailMessage;
        /**
         * The subject line for the custom message.
         */
        private String emailSubject;
    }
}
