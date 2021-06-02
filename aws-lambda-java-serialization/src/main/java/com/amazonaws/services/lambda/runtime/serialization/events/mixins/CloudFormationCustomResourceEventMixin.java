/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public abstract class CloudFormationCustomResourceEventMixin {

    // needed because jackson expects "requestType" instead of "RequestType"
    @JsonProperty("RequestType") abstract String getRequestType();
    @JsonProperty("RequestType") abstract void setRequestType(String requestType);

    // needed because jackson expects "serviceToken" instead of "ServiceToken"
    @JsonProperty("ServiceToken") abstract String getServiceToken();
    @JsonProperty("ServiceToken") abstract void setServiceToken(String serviceToken);

    // needed because jackson expects "physicalResourceId" instead of "PhysicalResourceId"
    @JsonProperty("PhysicalResourceId") abstract String getPhysicalResourceId();
    @JsonProperty("PhysicalResourceId") abstract void setPhysicalResourceId(String physicalResourceId);

    // needed because jackson expects "responseUrl" instead of "ResponseURL"
    @JsonProperty("ResponseURL") abstract String getResponseUrl();
    @JsonProperty("ResponseURL") abstract void setResponseUrl(String responseUrl);

    // needed because jackson expects "stackId" instead of "StackId"
    @JsonProperty("StackId") abstract String getStackId();
    @JsonProperty("StackId") abstract void setStackId(String stackId);

    // needed because jackson expects "requestId" instead of "RequestId"
    @JsonProperty("RequestId") abstract String getRequestId();
    @JsonProperty("RequestId") abstract void setRequestId(String requestId);

    // needed because jackson expects "logicalResourceId" instead of "LogicalResourceId"
    @JsonProperty("LogicalResourceId") abstract String getLogicalResourceId();
    @JsonProperty("LogicalResourceId") abstract void setLogicalResourceId(String logicalResourceId);

    // needed because jackson expects "resourceType" instead of "ResourceType"
    @JsonProperty("ResourceType") abstract String getResourceType();
    @JsonProperty("ResourceType") abstract void setResourceType(String resourceType);

    // needed because jackson expects "resourceProperties" instead of "ResourceProperties"
    @JsonProperty("ResourceProperties") abstract Map<String, Object> getResourceProperties();
    @JsonProperty("ResourceProperties") abstract void setResourceProperties(Map<String, Object> resourceProperties);

    // needed because jackson expects "oldResourceProperties" instead of "OldResourceProperties"
    @JsonProperty("OldResourceProperties") abstract Map<String, Object> getOldResourceProperties();
    @JsonProperty("OldResourceProperties") abstract void setOldResourceProperties(Map<String, Object> oldResourceProperties);

}
