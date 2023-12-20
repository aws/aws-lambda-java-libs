/* Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * Represent the class for the Cognito User Pool Pre Token Generation Lambda Trigger V2
 * <p>
 * See <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/user-pool-lambda-pre-token-generation.html">Pre Token Generation Lambda Trigger</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class CognitoUserPoolPreTokenGenerationEventV2 extends CognitoUserPoolEvent {
    /**
     * The request from the Amazon Cognito service.
     */
    private Request request;

    /**
     * The response from your Lambda trigger.
     */
    private Response response;

    @Builder(setterPrefix = "with")
    public CognitoUserPoolPreTokenGenerationEventV2(
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
    @ToString(callSuper = true)
    public static class Request extends CognitoUserPoolEvent.Request {

        private String[] scopes;
        private GroupConfiguration groupConfiguration;
        private Map<String, String> clientMetadata;

        @Builder(setterPrefix = "with")
        public Request(Map<String, String> userAttributes, String[] scopes, GroupConfiguration groupConfiguration, Map<String, String> clientMetadata) {
            super(userAttributes);
            this.scopes = scopes;
            this.groupConfiguration = groupConfiguration;
            this.clientMetadata = clientMetadata;
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
        private ClaimsAndScopeOverrideDetails claimsAndScopeOverrideDetails;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class ClaimsAndScopeOverrideDetails {
        private IdTokenGeneration idTokenGeneration;
        private AccessTokenGeneration accessTokenGeneration;
        private GroupOverrideDetails groupOverrideDetails;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class IdTokenGeneration {
        private Map<String, String> claimsToAddOrOverride;
        private String[] claimsToSuppress;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class AccessTokenGeneration {
        private Map<String, String> claimsToAddOrOverride;
        private String[] claimsToSuppress;
        private String[] scopesToAdd;
        private String[] scopesToSuppress;
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class GroupOverrideDetails {
        private Map<String, String> groupsToOverride;
        private Map<String, String> iamRolesToOverride;
        private String preferredRole;
    }
}