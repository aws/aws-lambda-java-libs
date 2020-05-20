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

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

/**
 * Class to represent the state change of CodePipeline.
 *
 * @see <a href="https://docs.aws.amazon.com/codepipeline/latest/userguide/detect-state-changes-cloudwatch-events.html">Detect state changes</a>
 *
 * @author msailes <msailes@amazon.co.uk>
 */

@NoArgsConstructor
@Data
public class CodePipelineEvent implements Serializable, Cloneable {

    private String version;
    private int id;
    private String detailType;
    private String source;
    private int account;
    private DateTime time;
    private String region;
    private List<String> resources;
    private Detail detail;

    @NoArgsConstructor
    @Data
    public static class Detail implements Serializable, Cloneable {
        private String pipeline;
        private String version;
        private String state;
        private int executionId;
    }
}
