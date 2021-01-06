package com.amazonaws.services.lambda.runtime.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

public class IamPolicyResponseTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String REGION = "eu-west-1";
    public static final String AWS_ACCOUNT_ID = "123456789012";
    public static final String REST_API_ID = "1234abc";
    public static final String STAGE = "$deafult";
    public static final String PRINCIPAL_ID = "me";

    @Test
    public void testAllowAllStatement() throws JsonProcessingException {
        IamPolicyResponse iamPolicyResponse = new IamPolicyResponse(PRINCIPAL_ID, IamPolicyResponse.PolicyDocument.getAllowAllPolicy(REGION, AWS_ACCOUNT_ID, REST_API_ID, STAGE));

        String json = OBJECT_MAPPER.writeValueAsString(iamPolicyResponse);

        assertThatJson(json).isEqualTo(readResource("allow-all.json"));
    }

    @Test
    public void testAllowOneStatement() throws JsonProcessingException {
        IamPolicyResponse iamPolicyResponse = new IamPolicyResponse(PRINCIPAL_ID, IamPolicyResponse.PolicyDocument.getAllowOnePolicy(REGION, AWS_ACCOUNT_ID, REST_API_ID, STAGE, IamPolicyResponse.HttpMethod.GET, "/test"));

        String json = OBJECT_MAPPER.writeValueAsString(iamPolicyResponse);

        assertThatJson(json).isEqualTo(readResource("allow-one.json"));
    }

    @Test
    public void testDenyAllStatement() throws JsonProcessingException {
        IamPolicyResponse iamPolicyResponse = new IamPolicyResponse(PRINCIPAL_ID, IamPolicyResponse.PolicyDocument.getDenyAllPolicy(REGION, AWS_ACCOUNT_ID, REST_API_ID, STAGE));

        String json = OBJECT_MAPPER.writeValueAsString(iamPolicyResponse);
        System.out.println(json);

        assertThatJson(json).isEqualTo(readResource("deny-all.json"));
    }

    @Test
    public void testDenyOneStatement() throws JsonProcessingException {
        IamPolicyResponse iamPolicyResponse = new IamPolicyResponse(PRINCIPAL_ID, IamPolicyResponse.PolicyDocument.getDenyOnePolicy(REGION, AWS_ACCOUNT_ID, REST_API_ID, STAGE, IamPolicyResponse.HttpMethod.GET, "/test"));

        String json = OBJECT_MAPPER.writeValueAsString(iamPolicyResponse);
        System.out.println(json);

        assertThatJson(json).isEqualTo(readResource("deny-one.json"));
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