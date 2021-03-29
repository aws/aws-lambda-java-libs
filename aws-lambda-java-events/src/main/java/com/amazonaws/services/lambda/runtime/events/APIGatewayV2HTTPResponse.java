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

import com.amazonaws.services.lambda.runtime.events.models.HttpHeaders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Builder(setterPrefix = "with")
@Data
@NoArgsConstructor
public class APIGatewayV2HTTPResponse {
    private int statusCode;
    private HttpHeaders<String> headers;
    private HttpHeaders<List<String>> multiValueHeaders;
    private List<String> cookies;
    private String body;
    private boolean isBase64Encoded;

    public static APIGatewayV2HTTPResponseBuilder builder() {
        return new APIGatewayV2HTTPResponseBuilder();
    }

    public static class APIGatewayV2HTTPResponseBuilder {
        private HttpHeaders<String> headers;
        private HttpHeaders<List<String>> multiValueHeaders;

        public APIGatewayV2HTTPResponseBuilder withHeaders(Map<String, String> headers) {
            if (headers == null || headers.isEmpty()) {
                this.headers = null;
                return this;
            }

            if (this.headers == null) {
                this.headers = new HttpHeaders<>();
            }
            this.headers.putAll(headers);
            return this;
        }

        public APIGatewayV2HTTPResponseBuilder withMultiValueHeaders(Map<String, List<String>> multiValueHeaders) {
            if (multiValueHeaders == null || multiValueHeaders.isEmpty()) {
                this.multiValueHeaders = null;
                return this;
            }

            if (this.multiValueHeaders == null) {
                this.multiValueHeaders = new HttpHeaders<>();
            }
            this.multiValueHeaders.putAll(multiValueHeaders);
            return this;
        }
    }
}
