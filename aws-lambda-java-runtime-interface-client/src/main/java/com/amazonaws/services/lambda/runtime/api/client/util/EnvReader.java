/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.util;

import java.util.Map;

public class EnvReader {

    public Map<String, String> getEnv() {
        return System.getenv();
    }

    public String getEnv(String envVariableName) {
        return System.getenv(envVariableName);
    }

    public String getEnvOrDefault(String envVariableName, String defaultVal) {
        String val = getEnv(envVariableName);
        return val == null ? defaultVal : val;
    }

}
