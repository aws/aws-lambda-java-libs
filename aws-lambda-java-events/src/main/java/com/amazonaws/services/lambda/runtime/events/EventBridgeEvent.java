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
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

/**
 * Object representation of an Amazon EventBridge event.
 *
 * @see <a href="https://docs.aws.amazon.com/lambda/latest/dg/services-cloudwatchevents.html">Using AWS Lambda with Amazon EventBridge (CloudWatch Events)</a>
 *
 * @author msailes <msailes@amazon.co.uk>
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class EventBridgeEvent {

    private String version;
    private String id;
    private String detailType;
    private String source;
    private String account;
    private DateTime time;
    private String region;
    private List<String> resources;
    private Map<String, Object> detail;
}
