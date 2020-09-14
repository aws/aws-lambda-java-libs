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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ConnectEvent implements Serializable, Cloneable {

    private Details details;
    private String name;

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Details implements Serializable, Cloneable {
        private ContactData contactData;
        private Map<String, Object> parameters;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactData implements Serializable, Cloneable {
        private Map<String, String> attributes;
        private String channel;
        private String contactId;
        private CustomerEndpoint customerEndpoint;
        private String initialContactId;
        private String initiationMethod;
        private String instanceArn;
        private String previousContactId;
        private String queue;
        private SystemEndpoint systemEndpoint;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerEndpoint implements Serializable, Cloneable {
        private String address;
        private String type;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemEndpoint implements Serializable, Cloneable {
        private String address;
        private String type;
    }
}
