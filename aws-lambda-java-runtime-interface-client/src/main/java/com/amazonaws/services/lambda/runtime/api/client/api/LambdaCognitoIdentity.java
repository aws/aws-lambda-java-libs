/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.api;

import com.amazonaws.services.lambda.runtime.CognitoIdentity;

public class LambdaCognitoIdentity implements CognitoIdentity {

    private final String cognitoIdentityId;
    private final String cognitoIdentityPoolId;

    public LambdaCognitoIdentity(String identityid, String poolid) {
        this.cognitoIdentityId = identityid;
        this.cognitoIdentityPoolId = poolid;
    }

    public String getIdentityId() {
        return this.cognitoIdentityId;
    }

    public String getIdentityPoolId() {
        return this.cognitoIdentityPoolId;
    }
}
