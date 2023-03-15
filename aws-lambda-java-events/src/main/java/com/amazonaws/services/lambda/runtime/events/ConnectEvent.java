/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * Class to represent an Amazon Connect contact flow event.
 *
 * @see <a href="https://docs.aws.amazon.com/connect/latest/adminguide/connect-lambda-functions.html>Connect Lambda Functions</a>
 *
 * @author msailes <msailes@amazon.co.uk>
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class ConnectEvent implements Serializable {

    @JsonProperty("Details")
    private Details details;

    @JsonProperty("Name")
    private String name;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Details implements Serializable {

        @JsonProperty("ContactData")
        private ContactData contactData;

        @JsonProperty("Parameters")
        private Map<String,Object> parameters;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactData implements Serializable {

        @JsonProperty("Attributes")
        private Map<String, String> attributes;

        @JsonProperty("Channel")
        private String channel;

        @JsonProperty("ContactId")
        private String contactId;

        @JsonProperty("CustomerEndpoint")
        private CustomerEndpoint customerEndpoint;

        @JsonProperty("InitialContactId")
        private String initialContactId;

        @JsonProperty("InitiationMethod")
        private String initiationMethod;

        @JsonProperty("InstanceARN")
        private String instanceArn;

        @JsonProperty("MediaStreams")
        private MediaStreams mediaStreams;

        @JsonProperty("PreviousContactId")
        private String previousContactId;

        @JsonProperty("Queue")
        private String queue;

        @JsonProperty("SystemEndpoint")
        private SystemEndpoint systemEndpoint;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerEndpoint implements Serializable {

        @JsonProperty("Address")
        private String address;

        @JsonProperty("Type")
        private String type;
    }

    /**
     * Class to represent a MediaStreams object for Kinesis Media Streaming.
     *
     * @see <a href="https://docs.aws.amazon.com/connect/latest/adminguide/media-streaming-attributes.html">Media Streaming Attributes</a>
     *
     */
    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MediaStreams implements Serializable {
        @JsonProperty("Customer")
        private Customer customer;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Customer implements Serializable {
        @JsonProperty("Audio")
        private Audio audio;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Audio implements Serializable {
        @JsonProperty("StartFragmentNumber")
        private String startFragmentNumber;
        @JsonProperty("StartTimestamp")
        private String startTimestamp;
        @JsonProperty("StreamARN")
        private String streamARN;
        @JsonProperty("StopFragmentNumber")
        private String stopFragmentNumber;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemEndpoint implements Serializable {

        @JsonProperty("Address")
        private String address;

        @JsonProperty("Type")
        private String type;
    }
}
