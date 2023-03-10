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
import java.util.List;
import java.util.Map;

/**
 * Class that represents a CloudFront event
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class CloudFrontEvent implements Serializable {

    private static final long serialVersionUID = -7169297388214516660L;

    @JsonProperty("Records")
    private List<Record> records;

    /**
     *  class that represents a header
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header implements Serializable {

        private static final long serialVersionUID = 7041042740552686996L;

        private String key;
        private String value;
    }

    /**
     * Class that represents the configuration of a CloudFront message
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Config implements Serializable {

        private static final long serialVersionUID = -286083903805870299L;

        private String distributionId;
    }

    /**
     * class that represents a CLoudFront request
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request implements Serializable {

        private static final long serialVersionUID = 3245036101075464149L;

        private String uri;
        private String method;
        private String httpVersion;
        private String clientIp;
        private Map<String, List<Header>> headers;
    }

    /**
     * class that represents a Response object
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response implements Serializable {

        private static final long serialVersionUID = -3711565862079053710L;

        private String status;
        private String statusDescription;
        private String httpVersion;
        private Map<String, List<Header>> headers;
    }

    /**
     * class that represents the CloudFront body within a record
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CF implements Serializable {

        private static final long serialVersionUID = -5940167419180448832L;

        private Config config;
        private Request request;
        private Response response;
    }

    /**
     * Class that represents a record in a CLoudFront event
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record implements Serializable {

        private static final long serialVersionUID = -6114551370798889850L;

        private CF cf;
    }
}
