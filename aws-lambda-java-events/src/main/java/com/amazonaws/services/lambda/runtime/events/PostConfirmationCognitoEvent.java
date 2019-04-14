/*
 * Copyright 2012-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

import java.io.Serializable;
import java.util.Map;

/**
 * Represents an Amazon Post Confirmation Cognito event sent to Lambda Functions
 */
public class PostConfirmationCognitoEvent extends CognitoEvent {

    private static final long serialVersionUID = -3969305143274277607L;

    private String userPoolId;

    private String userName;

    private Map<String, String> callerContext;

    private String triggerSource;

    private Request request;


    public String getUserPoolId() {
        return userPoolId;
    }


    public void setUserPoolId(String userPoolId) {
        this.userPoolId = userPoolId;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Map<String, String> getCallerContext() {
        return callerContext;
    }


    public void setCallerContext(Map<String, String> callerContext) {
        this.callerContext = callerContext;
    }


    public String getTriggerSource() {
        return triggerSource;
    }


    public void setTriggerSource(String triggerSource) {
        this.triggerSource = triggerSource;
    }


    public Request getRequest() {
        return request;
    }


    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getRegion() != null)
            sb.append("region: ").append(getRegion()).append(",");
        if (getDatasetRecords() != null)
            sb.append("datasetRecords: ").append(getDatasetRecords().toString()).append(",");
        if (getIdentityPoolId() != null)
            sb.append("identityPoolId: ").append(getIdentityPoolId()).append(",");
        if (getIdentityId() != null)
            sb.append("identityId: ").append(getIdentityId()).append(",");
        if (getDatasetName() != null)
            sb.append("datasetName: ").append(getDatasetName()).append(",");
        if (getEventType() != null)
            sb.append("eventType: ").append(getEventType()).append(",");
        if (getVersion() != null)
            sb.append("version: ").append(getVersion().toString());
        if (getUserPoolId() != null)
            sb.append("userPoolId: ").append(getVersion()).append(",");
        if (getUserName() != null)
            sb.append("userName: ").append(getUserName()).append(",");
        if (getCallerContext() != null)
            sb.append("callerContext: ").append(getCallerContext()).append(",");
        if (getTriggerSource() != null)
            sb.append("triggerSource: ").append(getTriggerSource()).append(",");
        if (getRequest() != null)
            sb.append("request: ").append(getRequest());
        sb.append("}");
        return sb.toString();
    }

    /**
     * class that represents a Post Confirmation Cognito event request
     */
    public static class Request implements Serializable, Cloneable {

        private static final long serialVersionUID = -5284784635192026866L;

        private Map<String, String> userAttributes;

        public Map<String, String> getUserAttributes() {
            return userAttributes;
        }

        public void setUserAttributes(Map<String, String> userAttributes) {
            this.userAttributes = userAttributes;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (getUserAttributes() != null)
                sb.append("userAttributes: ").append(getUserAttributes());
            sb.append("}");
            return sb.toString();
        }
    }
}