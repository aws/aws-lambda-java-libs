/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.api;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class LambdaContext implements Context {

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
    private final LambdaLogger logger;

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

    public LambdaLogger getLogger() {
        return logger;
    }
}
