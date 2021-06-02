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
 * Represent the class for the Cognito User Pool Pre Token Generation Lambda Trigger
 *
 * See <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/user-pool-lambda-pre-token-generation.html">Pre Token Generation Lambda Trigger</a>
 *
 * @author jvdl <jvdl@amazon.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CognitoUserPoolPreTokenGenerationEvent extends CognitoUserPoolEvent {
    /**
     * The request from the Amazon Cognito service.
     */
    private Request request;

    /**
     * The response from your Lambda trigger.
     */
    private Response response;

    @Builder(setterPrefix = "with")
    public CognitoUserPoolPreTokenGenerationEvent(
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
         * One or more key-value pairs that you can provide as custom input to the Lambda function that you specify for the pre token generation trigger.
         */
        private Map<String, String> clientMetadata;

        /**
         * The input object containing the current group configuration.
         */
        private GroupConfiguration groupConfiguration;

        @Builder(setterPrefix = "with")
        public Request(Map<String, String> userAttributes, Map<String, String> clientMetadata, GroupConfiguration groupConfiguration) {
            super(userAttributes);
            this.clientMetadata = clientMetadata;
            this.groupConfiguration = groupConfiguration;
        }
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class GroupConfiguration {
        /**
         * A list of the group names that are associated with the user that the identity token is issued for.
         */
        private String[] groupsToOverride;
        /**
         * A list of the current IAM roles associated with these groups.
         */
        private String[] iamRolesToOverride;
        /**
         * Indicates the preferred IAM role.
         */
        private String preferredRole;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Response {
        private ClaimsOverrideDetails claimsOverrideDetails;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class ClaimsOverrideDetails {
        /**
         * A map of one or more key-value pairs of claims to add or override.
         * For group related claims, use groupOverrideDetails instead.
         */
        private Map<String, String> claimsToAddOrOverride;
        /**
         * A list that contains claims to be suppressed from the identity token.
         */
        private String[] claimsToSuppress;
        /**
         * The output object containing the current group configuration.
         */
        private GroupConfiguration groupOverrideDetails;
    }
}
