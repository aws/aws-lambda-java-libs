package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class CodePipelineEventMixin {

    // needed because Jackson expects "codePipelineJob" instead of "CodePipeline.job"
    @JsonProperty("CodePipeline.job") abstract Object getCodePipelineJob();
    @JsonProperty("CodePipeline.job") abstract void setCodePipelineJob(Object job);

    public abstract static class ConfigurationMixin {
        // needed because Jackson expects "functionName" instead of "FunctionName"
        @JsonProperty("FunctionName") abstract String getFunctionName();
        @JsonProperty("FunctionName") abstract void setFunctionName(String functionName);

        // needed because Jackson expects "userParameters" instead of "UserParameters"
        @JsonProperty("UserParameters") abstract String getUserParameters();
        @JsonProperty("UserParameters") abstract void setUserParameters(String userParameters);

    }
}
