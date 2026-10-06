/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.api;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LambdaContext implements Context {

    /**
     * Allowlist of W3C trace-context fields that may be surfaced through
     * {@link #w3c()}. Any other key carried on {@code clientContext.w3c} is
     * ignored, and any allowlisted key whose value is not a string is dropped.
     */
    public static final List<String> W3C_ALLOWED_FIELDS =
            Collections.unmodifiableList(Arrays.asList("traceparent", "tracestate", "baggage"));

    private int memoryLimit;
    private final String awsRequestId;
    private final String logGroupName;
    private final String logStreamName;
    private final String functionName;
    private final String functionVersion;
    private final String invokedFunctionArn;
    private final long deadlineTimeInMs;
    private final CognitoIdentity cognitoIdentity;
    private final ClientContext clientContext;
    private final String tenantId;
    private final String xrayTraceId;
    private final LambdaLogger logger;
    private final Map<String, String> w3cFields;

    public LambdaContext(
            int memoryLimit,
            long deadlineTimeInMs,
            String requestId,
            String logGroupName,
            String logStreamName,
            String functionName,
            CognitoIdentity identity,
            String functionVersion,
            String invokedFunctionArn,
            String tenantId,
            String xrayTraceId,
            ClientContext clientContext
    ) {
        this.memoryLimit = memoryLimit;
        this.deadlineTimeInMs = deadlineTimeInMs;
        this.awsRequestId = requestId;
        this.logGroupName = logGroupName;
        this.logStreamName = logStreamName;
        this.functionName = functionName;
        this.cognitoIdentity = identity;
        this.clientContext = clientContext;
        this.functionVersion = functionVersion;
        this.invokedFunctionArn = invokedFunctionArn;
        this.tenantId = tenantId;
        this.xrayTraceId = xrayTraceId;
        this.w3cFields = extractAndStripW3c(clientContext);
        this.logger = com.amazonaws.services.lambda.runtime.LambdaRuntime.getLogger();
    }

    public int getMemoryLimitInMB() {
        return memoryLimit;
    }

    public String getAwsRequestId() {
        return awsRequestId;
    }

    public String getLogGroupName() {
        return logGroupName;
    }

    public String getLogStreamName() {
        return logStreamName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionVersion() {
        return functionVersion;
    }

    public String getInvokedFunctionArn() {
        return invokedFunctionArn;
    }

    public CognitoIdentity getIdentity() {
        return cognitoIdentity;
    }

    public ClientContext getClientContext() {
        return clientContext;
    }

    public int getRemainingTimeInMillis() {
        long now = System.currentTimeMillis();
        int delta = (int) (this.deadlineTimeInMs - now);
        return delta > 0 ? delta : 0;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getXrayTraceId() {
        return xrayTraceId;
    }

    @Override
    public Map<String, String> w3c() {
        return w3cFields;
    }

    private static Map<String, String> extractAndStripW3c(ClientContext clientContext) {
        if (!(clientContext instanceof LambdaClientContext)) {
            return Collections.emptyMap();
        }

        Object rawW3c = ((LambdaClientContext) clientContext).readAndStripW3c();
        if (!(rawW3c instanceof Map)) {
            return Collections.emptyMap();
        }

        Map<?, ?> source = (Map<?, ?>) rawW3c;
        Map<String, String> fields = new LinkedHashMap<>();
        for (String key : W3C_ALLOWED_FIELDS) {
            Object value = source.get(key);
            if (value instanceof String) {
                fields.put(key, (String) value);
            }
        }
        return Collections.unmodifiableMap(fields);
    }

    public LambdaLogger getLogger() {
        return logger;
    }
}
