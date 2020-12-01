/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Jackson annotations for ConnectEvent
 */
public abstract class ConnectEventMixin {

    // needed because Jackson expects "details" instead of "Details"
    @JsonProperty("Details") abstract Map<String,?> getDetails();
    @JsonProperty("Details") abstract void setDetails(Map<String,?> details);

    // needed because Jackson expects "name" instead of "Name"
    @JsonProperty("Name") abstract String getName();
    @JsonProperty("Name") abstract void setName(String name);

    public abstract class DetailsMixin {

        // needed because Jackson expects "contactData" instead of "ContactData"
        @JsonProperty("ContactData") abstract Map<String,?> getContactData();
        @JsonProperty("ContactData") abstract void setContactData(Map<String,?> contactData);

        // needed because Jackson expects "parameters" instead of "Parameters"
        @JsonProperty("Parameters") abstract Map<String,Object> getParameters();
        @JsonProperty("Parameters") abstract void setParameters(Map<String,Object> parameters);
    }

    public abstract class ContactDataMixin {

        // needed because Jackson expects "attributes" instead of "Attributes"
        @JsonProperty("Attributes") abstract Map<String,String>  getAttributes();
        @JsonProperty("Attributes") abstract void setAttributes(Map<String,String>  attributes);

        // needed because Jackson expects "channel" instead of "Channel"
        @JsonProperty("Channel") abstract String getChannel();
        @JsonProperty("Channel") abstract void setChannel(String channel);

        // needed because Jackson expects "contactId" instead of "ContactId"
        @JsonProperty("ContactId") abstract String getContactId();
        @JsonProperty("ContactId") abstract void setContactId(String contactId);

        // needed because Jackson expects "customerEndpoint" instead of "CustomerEndpoint"
        @JsonProperty("CustomerEndpoint") abstract Map<String,String>  getCustomerEndpoint();
        @JsonProperty("CustomerEndpoint") abstract void setCustomerEndpoint(Map<String,String>  systemEndpoint);

        // needed because Jackson expects "initialContactId" instead of "InitialContactId"
        @JsonProperty("InitialContactId") abstract String getInitialContactId();
        @JsonProperty("InitialContactId") abstract void setInitialContactId(String initialContactId);

        // needed because Jackson expects "initiationMethod" instead of "InitiationMethod"
        @JsonProperty("InitiationMethod") abstract String getInitiationMethod();
        @JsonProperty("InitiationMethod") abstract void setInitiationMethod(String initiationMethod);

        // needed because Jackson expects "instanceARN" instead of "InstanceARN"
        @JsonProperty("InstanceARN") abstract String getInstanceArn();
        @JsonProperty("InstanceARN") abstract void setInstanceArn(String instanceArn);

        // needed because Jackson expects "previousContactId" instead of "PreviousContactId"
        @JsonProperty("PreviousContactId") abstract String getPreviousContactId();
        @JsonProperty("PreviousContactId") abstract void setPreviousContactId(String previousContactId);

        // needed because Jackson expects "queue" instead of "Queue"
        @JsonProperty("Queue") abstract String getQueue();
        @JsonProperty("Queue") abstract void setQueue(String queue);

        // needed because Jackson expects "systemEndpoint" instead of "SystemEndpoint"
        @JsonProperty("SystemEndpoint") abstract Map<String,String>  getSystemEndpoint();
        @JsonProperty("SystemEndpoint") abstract void setSystemEndpoint(Map<String,String>  systemEndpoint);

    }

    public abstract class CustomerEndpointMixin {

        // needed because Jackson expects "address" instead of "Address"
        @JsonProperty("Address") abstract String getAddress();
        @JsonProperty("Address") abstract void setAddress(String previousContactId);

        // needed because Jackson expects "type" instead of "Type"
        @JsonProperty("Type") abstract String getType();
        @JsonProperty("Type") abstract void setType(String type);
    }

    public abstract class SystemEndpointMixin {

        // needed because Jackson expects "address" instead of "Address"
        @JsonProperty("Address") abstract String getAddress();
        @JsonProperty("Address") abstract void setAddress(String previousContactId);

        // needed because Jackson expects "type" instead of "Type"
        @JsonProperty("Type") abstract String getType();
        @JsonProperty("Type") abstract void setType(String type);
    }
}
