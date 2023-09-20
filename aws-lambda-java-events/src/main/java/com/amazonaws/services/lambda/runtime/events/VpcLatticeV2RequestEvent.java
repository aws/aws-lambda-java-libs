// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://docs.aws.amazon.com/vpc-lattice/latest/ug/lambda-functions.html">Lambda functions as targets in VPC Lattice</a>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class VpcLatticeV2RequestEvent {

    private String version;
    private String path;
    private String method;
    private Map<String, List<String>> headers;
    @Nullable
    private Map<String, String> queryStringParameters;
    private RequestContext requestContext;
    private String body;
    @Nullable
    private Boolean isBase64Encoded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class RequestContext {
        private String serviceNetworkArn;
        private String serviceArn;
        private String targetGroupArn;
        private Identity identity;
        private String region;
        /**
         * Number of microseconds from the epoch
         */
        private String timeEpoch;

        public LocalDateTime getlocalDateTime() {
            // Truncating to milliseconds. Java 8 only supports Epoch with precision of milliseconds. Microseconds are supported in Java 9
            long epochMilli = Long.parseLong(timeEpoch.substring(0, timeEpoch.length() - 3));
            return Instant.ofEpochMilli(epochMilli).atZone(ZoneOffset.UTC).toLocalDateTime();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "with")
    public static class Identity {
        private String sourceVpcArn;
        private String type;
        private String principal;
        private String sessionName;
        private String x509SanDns;
    }
}
