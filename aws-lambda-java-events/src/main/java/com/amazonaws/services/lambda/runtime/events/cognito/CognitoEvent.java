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

package com.amazonaws.services.lambda.runtime.events.cognito;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * Represents an Amazon Cognito event sent to Lambda Functions
 *
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class CognitoEvent implements Serializable {

    private static final long serialVersionUID = -3471890133562627751L;

    private String region;
    private Map<String, DatasetRecord> datasetRecords;
    private String identityPoolId;
    private String identityId;
    private String datasetName;
    private String eventType;
    private Integer version;

	/**
	 * DatasetRecord contains the information about each record in a data set.
	 *
	 */

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatasetRecord implements Serializable {

        private static final long serialVersionUID = -8853471047466644850L;

        private String oldValue;
        private String newValue;

        /**
         * The operation associated with the record
         * <p>
         * <ul>
         * <li>
         * For a new record or any updates to existing record it is set to "replace".
         * </li>
         * <li>
         * For deleting a record it is set to "remove".
         * </li>
         * </ul>
         * </p>
         */
        private String op;
    }
}
