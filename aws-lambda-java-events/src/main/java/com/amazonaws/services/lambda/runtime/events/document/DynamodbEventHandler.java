package com.amazonaws.services.lambda.runtime.events.document;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

/**
 *
 * Dynamodb event handlers implement AWS Lambda Function application logic using
 * plain old java objects as input and output.
 *
 * @param <O> The output parameter type
 */
public abstract class DynamodbEventHandler<O> implements RequestHandler<DynamodbEvent, O> {

    /**
     * Handles a Dynamodb event using the Document API
     *
     * @param input The DynamoDB document event
     * @param context The Lambda execution environment context object.
     * @return The Lambda Function output
     */
    public abstract O handleEvent(DynamodbDocumentEvent input, Context context);

    @Override
    public final O handleRequest(DynamodbEvent input, Context context) {
        return handleEvent(new DynamodbDocumentEvent(input), context);
    }
}
