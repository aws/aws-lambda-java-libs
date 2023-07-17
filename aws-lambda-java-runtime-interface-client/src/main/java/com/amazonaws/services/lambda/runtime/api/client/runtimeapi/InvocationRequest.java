package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * An invocation request represents the response of the runtime API's next invocation API.
 * <p>
 * Copyright (c) 2019 Amazon. All rights reserved.
 */
public class InvocationRequest {

    /**
     * The Lambda request ID associated with the request.
     */
    private String id;

    /**
     * The X-Ray tracing ID.
     */
    private String xrayTraceId;

    /**
     * The ARN of the Lambda function being invoked.
     */
    private String invokedFunctionArn;

    /**
     * Function execution deadline counted in milliseconds since the Unix epoch.
     */
    private long deadlineTimeInMs;

    /**
     * The client context header. This field is populated when the function is invoked from a mobile client.
     */
    private String clientContext;

    /**
     * The Cognito Identity context for the invocation. This field is populated when the function is invoked with AWS
     * credentials obtained from Cognito Identity.
     */
    private String cognitoIdentity;

    private byte[] content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXrayTraceId() {
        return xrayTraceId;
    }

    public void setXrayTraceId(String xrayTraceId) {
        this.xrayTraceId = xrayTraceId;
    }

    public String getInvokedFunctionArn() {
        return invokedFunctionArn;
    }

    public void setInvokedFunctionArn(String invokedFunctionArn) {
        this.invokedFunctionArn = invokedFunctionArn;
    }

    public long getDeadlineTimeInMs() {
        return deadlineTimeInMs;
    }

    public void setDeadlineTimeInMs(long deadlineTimeInMs) {
        this.deadlineTimeInMs = deadlineTimeInMs;
    }

    public String getClientContext() {
        return clientContext;
    }

    public void setClientContext(String clientContext) {
        this.clientContext = clientContext;
    }

    public String getCognitoIdentity() {
        return cognitoIdentity;
    }

    public void setCognitoIdentity(String cognitoIdentity) {
        this.cognitoIdentity = cognitoIdentity;
    }

    public InputStream getContentAsStream() {
        return new ByteArrayInputStream(content);
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
