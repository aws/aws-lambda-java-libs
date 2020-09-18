package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * An invocation request represents the response of the runtime API's next invocation API.
 *
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

    /**
     * An input stream of the invocation's request body.
     */
    private InputStream stream;

    private byte[] content;

    public String getId() {
        return id;
    }

    public String getXrayTraceId() {
        return xrayTraceId;
    }

    public String getInvokedFunctionArn() {
        return invokedFunctionArn;
    }

    public long getDeadlineTimeInMs() {
        return deadlineTimeInMs;
    }

    public String getClientContext() {
        return clientContext;
    }

    public String getCognitoIdentity() {
        return cognitoIdentity;
    }

    public InputStream getContentAsStream() {
        return new ByteArrayInputStream(content);
    }

}
