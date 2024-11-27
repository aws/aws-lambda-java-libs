/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.api;

import com.amazonaws.services.lambda.runtime.Client;
import com.amazonaws.services.lambda.runtime.ClientContext;
import java.util.Map;

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
