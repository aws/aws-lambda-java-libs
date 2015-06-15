package com.amazonaws.services.lambda.runtime;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * 
 * Lambda request handlers implement AWS Lambda Function application logic using plain old java objects 
 * as input and output.
 *
 * @param <I> The input parameter type
 * @param <O> The output parameter type
 */
public interface RequestHandler<I, O> {
    /**
     * Handles a Lambda Function request
     * @param input The Lambda Function input
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     */
    public O handleRequest(I input, Context context);
}
