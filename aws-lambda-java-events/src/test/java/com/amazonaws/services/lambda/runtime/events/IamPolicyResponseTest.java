package com.amazonaws.services.lambda.runtime.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponse.ALLOW;
import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponse.EXECUTE_API_INVOKE;
import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponse.VERSION_2012_10_17;
import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponse.allowStatement;
import static com.amazonaws.services.lambda.runtime.events.IamPolicyResponse.denyStatement;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

public class IamPolicyResponseTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void testAllowStatement() throws JsonProcessingException {
        IamPolicyResponse iamPolicyResponse = IamPolicyResponse.builder()
                .withPrincipalId("me")
                .withPolicyDocument(IamPolicyResponse.PolicyDocument.builder()
                        .withVersion(VERSION_2012_10_17)
                        .withStatement(singletonList(allowStatement("arn:aws:execute-api:eu-west-1:123456789012:1234abc/$deafult/*/*")))
                        .build())
                .build();

        String json = OBJECT_MAPPER.writeValueAsString(iamPolicyResponse);

        assertThatJson(json).isEqualTo(readResource("iamPolicyResponses/allow.json"));
    }

    @Test
    public void testDenyStatement() throws JsonProcessingException {
        IamPolicyResponse iamPolicyResponse = IamPolicyResponse.builder()
                .withPrincipalId("me")
                .withPolicyDocument(IamPolicyResponse.PolicyDocument.builder()
                        .withVersion(VERSION_2012_10_17)
                        .withStatement(singletonList(denyStatement("arn:aws:execute-api:eu-west-1:123456789012:1234abc/$deafult/*/*")))
                        .build())
                .build();

        String json = OBJECT_MAPPER.writeValueAsString(iamPolicyResponse);

        assertThatJson(json).isEqualTo(readResource("iamPolicyResponses/deny.json"));
    }

    @Test
    public void testStatementWithCondition() throws JsonProcessingException {
        Map<String, Map<String, Object>> conditions = new HashMap<>();
        conditions.put("DateGreaterThan", singletonMap("aws:TokenIssueTime", "2020-01-01T00:00:01Z"));

        IamPolicyResponse iamPolicyResponse = IamPolicyResponse.builder()
                .withPrincipalId("me")
                .withPolicyDocument(IamPolicyResponse.PolicyDocument.builder()
                        .withVersion(VERSION_2012_10_17)
                        .withStatement(singletonList(IamPolicyResponse.Statement.builder()
                                .withAction(EXECUTE_API_INVOKE)
                                .withEffect(ALLOW)
                                .withResource(singletonList("arn:aws:execute-api:eu-west-1:123456789012:1234abc/$deafult/*/*"))
                                .withCondition(conditions)
                                .build()))
                        .build())
                .build();

        String json = OBJECT_MAPPER.writeValueAsString(iamPolicyResponse);

        assertThatJson(json).isEqualTo(readResource("iamPolicyResponses/allow-with-condition.json"));
    }

    private String readResource(String name) {
        Path filePath = Paths.get("src", "test", "resources", name);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}