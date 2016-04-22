package com.amazonaws.services.lambda.runtime.events;

/**
 * Represents an event for an AWS Config rule's function.
 */
public class ConfigEvent {

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
     * Whether the AWS resource to be evaluated has been removed from the AWS Config rule's scope.
     * 
     */
    public boolean isEventLeftScope() {
        return eventLeftScope;
    }

    /**
     * Sets whether the AWS resource to be evaluated has been removed from the AWS Config rule's scope.
     * @param eventLeftScope Boolean flag indicating that the resource is no longer in scope.
     */
    public void setEventLeftScope(boolean eventLeftScope) {
        this.eventLeftScope = eventLeftScope;
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

}
