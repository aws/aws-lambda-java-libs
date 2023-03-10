/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The IAM Policy Response required for API Gateway REST APIs
 *
 * https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-lambda-authorizer-output.html
 *
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class IamPolicyResponseV1 implements Serializable {

    public static final String EXECUTE_API_INVOKE = "execute-api:Invoke";
    public static final String VERSION_2012_10_17 = "2012-10-17";
    public static final String ALLOW = "Allow";
    public static final String DENY = "Deny";

    private String principalId;
    private PolicyDocument policyDocument;
    private Map<String, Object> context;
    private String usageIdentifierKey;

    @JsonIgnore
    public Map<String, Object> getPolicyDocumentData() {
        Map<String, Object> serializablePolicy = new HashMap<>();
        serializablePolicy.put("Version", policyDocument.getVersion());

        int numberOfStatements = policyDocument.getStatement().size();
        Map<String, Object>[] serializableStatementArray = new Map[numberOfStatements];
        for (int i = 0; i < numberOfStatements; i++) {
            Statement statement = policyDocument.getStatement().get(i);
            Map<String, Object> serializableStatement = new HashMap<>();
            serializableStatement.put("Effect", statement.getEffect());
            serializableStatement.put("Action", statement.getAction());
            serializableStatement.put("Resource", statement.getResource().toArray(new String[0]));
            serializableStatement.put("Condition", statement.getCondition());
            serializableStatementArray[i] = serializableStatement;
        }
        serializablePolicy.put("Statement", serializableStatementArray);
        return serializablePolicy;
    }

    public static Statement allowStatement(String resource) {
        return Statement.builder()
                .withEffect(ALLOW)
                .withResource(Collections.singletonList(resource))
                .withAction(Arrays.asList(EXECUTE_API_INVOKE))
                .build();
    }

    public static Statement denyStatement(String resource) {
        return Statement.builder()
                .withEffect(DENY)
                .withResource(Collections.singletonList(resource))
                .withAction(Arrays.asList(EXECUTE_API_INVOKE))
                .build();
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PolicyDocument implements Serializable {

        @JsonProperty("Version")
        private String version;

        @JsonProperty("Statement")
        private List<Statement> statement;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Statement implements Serializable {

        @JsonProperty("Action")
        private List<String> action;

        @JsonProperty("Effect")
        private String effect;

        @JsonProperty("Resource")
        private List<String> resource;

        @JsonProperty("Condition")
        private Map<String, Map<String, Object>> condition;
    }
}
