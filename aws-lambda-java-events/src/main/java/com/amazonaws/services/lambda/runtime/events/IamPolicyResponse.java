package com.amazonaws.services.lambda.runtime.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The IAM Policy Response required for API Gateway HTTP APIs
 *
 * https://docs.aws.amazon.com/apigateway/latest/developerguide/http-api-lambda-authorizer.html
 *
 */

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class IamPolicyResponse implements Serializable, Cloneable {

    public static final String EXECUTE_API_INVOKE = "execute-api:Invoke";
    public static final String VERSION_2012_10_17 = "2012-10-17";
    public static final String ALLOW = "Allow";
    public static final String DENY = "Deny";

    private String principalId;
    private PolicyDocument policyDocument;
    private Map<String, Object> context;

    public Map<String, Object> getPolicyDocument() {
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
                .withAction(EXECUTE_API_INVOKE)
                .build();
    }

    public static Statement denyStatement(String resource) {
        return Statement.builder()
                .withEffect(DENY)
                .withResource(Collections.singletonList(resource))
                .withAction(EXECUTE_API_INVOKE)
                .build();
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PolicyDocument implements Serializable, Cloneable {

        private String version;
        private List<Statement> statement;
    }

    @Data
    @Builder(setterPrefix = "with")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Statement implements Serializable, Cloneable {

        private String action;
        private String effect;
        private List<String> resource;
        private Map<String, Map<String, Object>> condition;
    }
}