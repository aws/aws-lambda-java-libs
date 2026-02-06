/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a Scheduled V2 event sent to Lambda
 * <a href="https://docs.aws.amazon.com/lambda/latest/dg/with-eventbridge-scheduler.html">Using Lambda with Amazon EventBridge Scheduler</a>
 */
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledV2Event implements Serializable, Cloneable {

    private static final long serialVersionUID = -463442139623175611L;

    private String version;

    private String account;

    private String region;

    private String detail;

    private String detailType;

    private String source;

    private String id;

    private DateTime time;

    private List<String> resources;

}
