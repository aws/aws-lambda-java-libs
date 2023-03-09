/*
 * Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

package com.amazonaws.services.lambda.runtime.events.dynamodb;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


/**
* <p>
* Contains details about the type of identity that made the request.
* </p>
*
* @see <a href="http://docs.aws.amazon.com/goto/WebAPI/streams-dynamodb-2012-08-10/Identity" target="_top">AWS API
*      Documentation</a>
*/

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Identity implements Serializable {

    /**
     * <p>
     * A unique identifier for the entity that made the call. For Time To Live, the principalId is
     * "dynamodb.amazonaws.com".
     * </p>
     */
    private String principalId;

    /**
     * <p>
     * The type of the identity. For Time To Live, the type is "Service".
     * </p>
     */
    private String type;
}
