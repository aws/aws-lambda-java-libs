/*
 * Copyright 2021 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
import java.util.List;

/**
 * Function response type to report batch item failures for {@link SQSEvent}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class SQSBatchResponse implements Serializable {

    private static final long serialVersionUID = 5075170615239078773L;

    /**
     * A list of messageIds that failed processing. These messageIds will be retried.
     */
    private List<BatchItemFailure> batchItemFailures;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class BatchItemFailure implements Serializable {

        private static final long serialVersionUID = 40727862494377907L;

        /**
         * MessageId that failed processing
         */
        String itemIdentifier;
    }
}
