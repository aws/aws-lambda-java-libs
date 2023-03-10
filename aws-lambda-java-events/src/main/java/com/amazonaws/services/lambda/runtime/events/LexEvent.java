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

import java.io.Serializable;
import java.util.Map;

/**
 * represents a Lex event
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class LexEvent implements Serializable {

    private static final long serialVersionUID = 8660021082133163891L;

    private String messageVersion;
    private String invocationSource;
    private String userId;
    private Map<String, String> sessionAttributes;
    private String outputDialogMode;
    private CurrentIntent currentIntent;
    private Bot bot;

    /**
     * Represents a Lex bot
     */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bot implements Serializable {

        private static final long serialVersionUID = -5764739951985883358L;

        private String name;
        private String alias;
        private String version;
    }

    /**
     * models CurrentIntent of Lex event
     */
    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor

    public static class CurrentIntent implements Serializable {

        private static final long serialVersionUID = 7405357938118538229L;

        private String name;
        private Map<String, String> slots;
        private String confirmationStatus;
    }
}
