/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.api;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.Client;

public class LambdaClientContext implements ClientContext {

    private LambdaClientContextClient client;
    private Map<String, String> custom;
    private Map<String, String> env;

    public Client getClient() {
        return client;
    }

    public Map<String, String> getCustom() {
        return custom;
    }

    public Map<String, String> getEnvironment() {
        return env;
    }
}
