/* Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime;

/**
 *  Provides information related to Amazon Congnito identities.
 *
 */
public interface CognitoIdentity {
    /**
     * Gets the Amazon Cognito identity ID
     * 
     */
    public String getIdentityId();

    /**
     * Gets the Amazon Cognito identity pool ID
     * 
     */
    public String getIdentityPoolId();
}
