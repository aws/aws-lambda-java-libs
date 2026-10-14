/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.Context;

import java.util.LinkedHashMap;
import java.util.Map;


public class W3CHandler {

    public Map<String, String> getW3c(Map<String, Object> event, Context context) {
        return context.w3c();
    }

    public Map<String, Object> getW3cAndCustom(Map<String, Object> event, Context context) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("w3c", context.w3c());
        ClientContext clientContext = context.getClientContext();
        result.put("custom", clientContext == null ? null : clientContext.getCustom());
        return result;
    }
}
