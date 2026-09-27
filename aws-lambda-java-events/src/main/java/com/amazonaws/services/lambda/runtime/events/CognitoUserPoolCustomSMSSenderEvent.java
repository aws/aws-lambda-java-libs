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
 * Represent the class for the Cognito User Pool Custom SMS Sender Lambda Trigger
 *
 * See <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/user-pool-lambda-custom-sms-sender.html">Custom SMS Sender Lambda Trigger</a>
 *
 *
 * @author Saath Satheeshkumar <saths008>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class CognitoUserPoolCustomSMSSenderEvent extends CognitoUserPoolEvent {
    /**
     * The request from the Amazon Cognito service.
     */
    private Request request;

    /**
     * The response from your Lambda trigger.
     */
    private Response response;

    @Builder(setterPrefix = "with")
    public CognitoUserPoolCustomSMSSenderEvent(
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

        private String code;
        private String type;
        private Map<String, String> clientMetadata;

        @Builder(setterPrefix = "with")
        public Request(Map<String, String> userAttributes, String code, String type, Map<String, String> clientMetadata) {
            super(userAttributes);
            this.code = code;
            this.type = type;
            this.clientMetadata = clientMetadata;
        }
    }

    @Data
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    public static class Response {
    }
}
