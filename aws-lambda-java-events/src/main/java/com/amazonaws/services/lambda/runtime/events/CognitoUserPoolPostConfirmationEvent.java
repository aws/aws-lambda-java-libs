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
 * Represent the class for the Cognito User Pool Post Confirmation Lambda Trigger
 *
 * See <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/user-pool-lambda-post-confirmation.html">Post Confirmation Lambda Trigger</a>
 *
 * @author jvdl <jvdl@amazon.com>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CognitoUserPoolPostConfirmationEvent extends CognitoUserPoolEvent {

    /**
     * The request from the Amazon Cognito service.
     */
    private Request request;

    @Builder(setterPrefix = "with")
    public CognitoUserPoolPostConfirmationEvent(
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
         * One or more key-value pairs that you can provide as custom input to the Lambda function that you specify for the post confirmation trigger.
         */
        private Map<String, String> clientMetadata;

        @Builder(setterPrefix = "with")
        public Request(Map<String, String> userAttributes, Map<String, String> clientMetadata) {
            super(userAttributes);
            this.clientMetadata = clientMetadata;
        }
    }
}
