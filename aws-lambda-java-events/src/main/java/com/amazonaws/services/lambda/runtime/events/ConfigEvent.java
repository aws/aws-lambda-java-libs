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

/**
 * Represents an event for an AWS Config rule's function.
 */
public class ConfigEvent implements Serializable, Cloneable {

    private static final long serialVersionUID = -3484211708255634243L;

    private String version;

    private String invokingEvent;

    private String ruleParameters;

    private String resultToken;

    private String configRuleArn;

    private String configRuleId;

    private String configRuleName;

    private String accountId;

    private String executionRoleArn;

    private boolean eventLeftScope;

    /**
     * default constructor
     */
    public ConfigEvent() {}

    /**
     * Gets the AWS Config event version.
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the AWS Config event version.
     * @param version String containing the event version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @param version config event version
     * @return Config Event
     */
    public ConfigEvent withVersion(String version) {
        setVersion(version);
        return this;
    }
    
    /**
     * Gets the JSON-encoded notification published by AWS Config.
     * 
     */
    public String getInvokingEvent() {
        return invokingEvent;
    }

    /**
     * Sets the JSON-encoded notification published by AWS Config.
     * @param invokingEvent String containing the notification published by AWS Config.
     */
    public void setInvokingEvent(String invokingEvent) {
        this.invokingEvent = invokingEvent;
    }

    /**
     * @param invokingEvent invoking event
     * @return Config Event
     */
    public ConfigEvent withInvokingEvent(String invokingEvent) {
        setInvokingEvent(invokingEvent);
        return this;
    }
    
    /**
     * Gets the JSON-encoded map containing the AWS Config rule parameters.
     * 
     */
    public String getRuleParameters() {
        return ruleParameters;
    }

    /**
     * Sets the JSON-encoded map containing the AWS Config rule parameters.
     * @param ruleParameters String containing the AWS Config rule parameters.
     */
    public void setRuleParameters(String ruleParameters) {
        this.ruleParameters = ruleParameters;
    }

    /**
     * @param ruleParameters String with rule parameters
     * @return ConfigEvent
     */
    public ConfigEvent withRuleParameters(String ruleParameters) {
        setRuleParameters(ruleParameters);
        return this;
    }
    
    /**
     * Gets the token associated with the invocation of the AWS Config rule's Lambda function.
     * 
     */
    public String getResultToken() {
        return resultToken;
    }

    /**
     * Sets the token associated with the invocation of the AWS Config rule's Lambda function.
     * @param resultToken String containing the token associated to the invocation.
     */
    public void setResultToken(String resultToken) {
        this.resultToken = resultToken;
    }

    /**
     * @param resultToken result token
     * @return ConfigEvent
     */
    public ConfigEvent withResultToken(String resultToken) {
        setResultToken(resultToken);
        return this;
    }
    
    /**
     * Gets the ARN of the AWS Config rule that triggered the event.
     * 
     */
    public String getConfigRuleArn() {
        return configRuleArn;
    }

    /**
     * Sets the ARN of the AWS Config rule that triggered the event.
     * @param configRuleArn String containing the AWS Config rule ARN.
     */
    public void setConfigRuleArn(String configRuleArn) {
        this.configRuleArn = configRuleArn;
    }

    /**
     * @param configRuleArn config rule for arn
     * @return ConfigEvent
     */
    public ConfigEvent withConfigRuleArn(String configRuleArn) {
        setConfigRuleArn(configRuleArn);
        return this;
    }
    
    /**
     * Gets the ID of the AWS Config rule that triggered the event.
     * 
     */
    public String getConfigRuleId() {
        return configRuleId;
    }

    /**
     * Sets the ID of the AWS Config rule that triggered the event.
     * @param configRuleId String containing the AWS Config rule ID.
     */
    public void setConfigRuleId(String configRuleId) {
        this.configRuleId = configRuleId;
    }

    /**
     * @param configRuleId config rule id
     * @return ConfigEvent
     */
    public ConfigEvent withConfigRuleId(String configRuleId) {
        setConfigRuleId(configRuleId);
        return this;
    }
    
    /**
     * Gets the name of the AWS Config rule that triggered the event.
     * 
     */
    public String getConfigRuleName() {
        return configRuleName;
    }

    /**
     * Sets the name of the AWS Config rule that triggered the event.
     * @param configRuleName String containing the AWS Config rule name.
     */
    public void setConfigRuleName(String configRuleName) {
        this.configRuleName = configRuleName;
    }

    /**
     * @param configRuleName config rule name
     * @return ConfigEvent
     */
    public ConfigEvent withConfigRuleName(String configRuleName) {
        setConfigRuleName(configRuleName);
        return this;
    }
    
    /**
     * Gets the account ID of the AWS Config rule that triggered the event.
     * 
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Sets the account ID of the AWS Config rule that triggered the event.
     * @param accountId String containing the account ID of the AWS Config rule.
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * @param accountId Account id 
     * @return Config Event
     */
    public ConfigEvent withAccountId(String accountId) {
        setAccountId(accountId);
        return this;
    }
    
    /**
     * Gets the ARN of the IAM role that is assigned to AWS Config.
     * 
     */
    public String getExecutionRoleArn() {
        return executionRoleArn;
    }

    /**
     * Sets the ARN of the IAM role that is assigned to AWS Config.
     * @param executionRoleArn String containing the IAM role assigned to AWS Config.
     */
    public void setExecutionRoleArn(String executionRoleArn) {
        this.executionRoleArn = executionRoleArn;
    }

    /**
     * @param executionRoleArn execution role arn
     * @return ConfigEvent
     */
    public ConfigEvent withExecutionRoleArn(String executionRoleArn) {
        setExecutionRoleArn(executionRoleArn);
        return this;
    }
    
    /**
     * Whether the AWS resource to be evaluated has been removed from the AWS Config rule's scope.
     * 
     */
    public boolean getEventLeftScope() {
        return eventLeftScope;
    }

    /**
     * Sets whether the AWS resource to be evaluated has been removed from the AWS Config rule's scope.
     * @param eventLeftScope Boolean flag indicating that the resource is no longer in scope.
     */
    public void setEventLeftScope(boolean eventLeftScope) {
        this.eventLeftScope = eventLeftScope;
    }

    /**
     * @param eventLeftScope event left scope
     * @return ConfigEvent
     */
    public ConfigEvent withEventLeftScope(Boolean eventLeftScope) {
        setEventLeftScope(eventLeftScope);
        return this;
    }

    /**
     * Returns a string representation of this object; useful for testing and debugging.
     *
     * @return A string representation of this object.
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (getAccountId() != null)
            sb.append("accountId: ").append(getAccountId()).append(",");
        if (getConfigRuleArn() != null)
            sb.append("configRuleArn: ").append(getConfigRuleArn()).append(",");
        if (getConfigRuleId() != null)
            sb.append("configRulelId: ").append(getConfigRuleId()).append(",");
        if (getConfigRuleName() != null)
            sb.append("configRuleName: ").append(getConfigRuleName()).append(",");
        sb.append("eventLeftScope: ").append(getEventLeftScope()).append(",");
        if (getExecutionRoleArn() != null)
            sb.append("executionRoleArn: ").append(getExecutionRoleArn()).append(",");
        if (getInvokingEvent() != null)
            sb.append("invokingEvent: ").append(getInvokingEvent()).append(",");
        if (getResultToken() != null)
            sb.append("resultToken: ").append(getResultToken()).append(",");
        if (getRuleParameters() != null)
            sb.append("ruleParameters: ").append(getRuleParameters()).append(",");
        if (getVersion() != null)
            sb.append("version: ").append(getVersion());
        sb.append("}");
        return sb.toString();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
        result = prime * result + ((configRuleArn == null) ? 0 : configRuleArn.hashCode());
        result = prime * result + ((configRuleId == null) ? 0 : configRuleId.hashCode());
        result = prime * result + ((configRuleName == null) ? 0 : configRuleName.hashCode());
        result = prime * result + (eventLeftScope ? 1231 : 1237);
        result = prime * result + ((executionRoleArn == null) ? 0 : executionRoleArn.hashCode());
        result = prime * result + ((invokingEvent == null) ? 0 : invokingEvent.hashCode());
        result = prime * result + ((resultToken == null) ? 0 : resultToken.hashCode());
        result = prime * result + ((ruleParameters == null) ? 0 : ruleParameters.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConfigEvent other = (ConfigEvent) obj;
        if (accountId == null) {
            if (other.accountId != null)
                return false;
        } else if (!accountId.equals(other.accountId))
            return false;
        if (configRuleArn == null) {
            if (other.configRuleArn != null)
                return false;
        } else if (!configRuleArn.equals(other.configRuleArn))
            return false;
        if (configRuleId == null) {
            if (other.configRuleId != null)
                return false;
        } else if (!configRuleId.equals(other.configRuleId))
            return false;
        if (configRuleName == null) {
            if (other.configRuleName != null)
                return false;
        } else if (!configRuleName.equals(other.configRuleName))
            return false;
        if (eventLeftScope != other.eventLeftScope)
            return false;
        if (executionRoleArn == null) {
            if (other.executionRoleArn != null)
                return false;
        } else if (!executionRoleArn.equals(other.executionRoleArn))
            return false;
        if (invokingEvent == null) {
            if (other.invokingEvent != null)
                return false;
        } else if (!invokingEvent.equals(other.invokingEvent))
            return false;
        if (resultToken == null) {
            if (other.resultToken != null)
                return false;
        } else if (!resultToken.equals(other.resultToken))
            return false;
        if (ruleParameters == null) {
            if (other.ruleParameters != null)
                return false;
        } else if (!ruleParameters.equals(other.ruleParameters))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    @Override
    public ConfigEvent clone() {
        try {
            return (ConfigEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone()", e);
        }
    }

}
