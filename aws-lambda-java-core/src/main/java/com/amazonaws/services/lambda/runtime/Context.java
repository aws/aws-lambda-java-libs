/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime;

/**
 * 
 * The context object allows you to access useful information available within
 * the Lambda execution environment
 *
 */
public interface Context {

	/**
	 * Gets the AWS request ID associated with the request.
	 * <p>
	 * This is the same ID returned to the client that called invoke(). This ID
	 * is reused for retries on the same request.
	 * </p>
	 */
	String getAwsRequestId();

	/**
	 * Gets the CloudWatch log group that this container is configured to log
	 * to.
	 * <p>
	 * The return value may be null:
	 * <ul>
	 * <li>
	 * If the container is not configured to log to CloudWatch.</li>
         * <li>
	 * If the role provided to the function does not have sufficient
	 * permissions.</li>
	 * </ul>
	 * </p>
	 */
	String getLogGroupName();

	/**
	 * Gets the CloudWatch log stream that this container is configured to log
	 * to.
	 * <p>
	 * The return value may be null:
	 * <ul>
	 * <li>
	 * If the container is not configured to log to CloudWatch.</li>
	 * <li>
	 * If the role provided to the function does not have sufficient
	 * permissions.</li>
	 * </ul>
	 * </p>
	 */
	String getLogStreamName();

	/**
	 * Gets the name of the function being executed.
	 * 
	 */
	String getFunctionName();

	/**
	 * Gets the version of the function being executed.
	 * 
	 */
	String getFunctionVersion();

	/**
	 * Gets the function Arn of the resource being invoked.
	 * 
	 */
	String getInvokedFunctionArn();

	/**
	 * Gets information about the Amazon Cognito identity provider when invoked
	 * through the AWS Mobile SDK. It can be null
	 * 
	 */
	CognitoIdentity getIdentity();

	/**
	 * Gets information about the client application and device when invoked
	 * through the AWS Mobile SDK. It can be null.
	 * 
	 */
	ClientContext getClientContext();

	/**
	 * Gets the time remaining for this execution in milliseconds
	 */
	int getRemainingTimeInMillis();

	/**
	 * Gets the memory size configured for the Lambda function
	 * 
	 */
	int getMemoryLimitInMB();

	/**
	 * Gets the lambda logger instance associated with the context object
	 * 
	 */
	LambdaLogger getLogger();

}
