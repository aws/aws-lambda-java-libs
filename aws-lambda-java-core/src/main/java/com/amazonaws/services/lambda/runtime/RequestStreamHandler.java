/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Low-level request-handling interface, Lambda stream request handlers implement AWS Lambda Function application logic 
 * using input and output stream
 */
public interface RequestStreamHandler {
    /**
     * Handles a Lambda Function request
     * @param input The Lambda Function input stream
     * @param output The Lambda function output stream
     * @param context The Lambda execution environment context object.
     * @throws IOException
     */
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException;
}
