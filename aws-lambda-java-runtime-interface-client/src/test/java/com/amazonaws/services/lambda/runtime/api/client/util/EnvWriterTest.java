/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EnvWriterTest {

    private EnvWriter underTest;

    private EnvReader envReader;

    @AfterEach
    public void tearDown() {
        if(underTest != null)
            underTest.close();
    }

    @Test
    public void shouldModifyEnv() {
        String key = "test";
        String expected = "notNullValue";
        setEnv(expected, key);

        underTest.modifyEnv(env -> env.put(key, expected));

        assertEquals(envReader.getEnv(key), expected);
    }

    @Test
    public void shouldRemoveEnvironmentCredentialsWhenTheyAreEmpty() {
        setEnv("", "AWS_ACCESS_KEY_ID", "AWS_SECRET_ACCESS_KEY", "AWS_SESSION_TOKEN");

        underTest.setupEnvironmentCredentials();

        assertNull(envReader.getEnv("AWS_ACCESS_KEY_ID"));
        assertNull(envReader.getEnv("AWS_SECRET_ACCESS_KEY"));
        assertNull(envReader.getEnv("AWS_SESSION_TOKEN"));
    }

    @Test
    public void shouldSetupEnvironmentCredentialsWhenTheyAreNotEmpty() {
        setEnv("notEmptyValue", "AWS_ACCESS_KEY_ID", "AWS_SECRET_ACCESS_KEY");

        underTest.setupEnvironmentCredentials();

        assertEquals(envReader.getEnv("AWS_ACCESS_KEY"), envReader.getEnv("AWS_ACCESS_KEY_ID"));
        assertEquals(envReader.getEnv("AWS_SECRET_KEY"), envReader.getEnv("AWS_SECRET_ACCESS_KEY"));
    }

    @Test
    public void shouldSetTheAwsExecutionEnvVar_Java8() {
        // Given
        EnvReaderMock reader = new EnvReaderMock(unmodifiableMap(new HashMap<>()));
        EnvWriter writer = new EnvWriter(reader);

        // When
        System.setProperty("java.version", "1.8.0_202");
        writer.setupAwsExecutionEnv();

        // Then
        assertEquals("AWS_Lambda_java8", reader.getEnv("AWS_EXECUTION_ENV"));
    }

    @Test
    public void shouldSetTheAwsExecutionEnvVar_Java11() {
        // Given
        EnvReaderMock reader = new EnvReaderMock(unmodifiableMap(new HashMap<>()));
        EnvWriter writer = new EnvWriter(reader);

        // When
        System.setProperty("java.version", "11.0.3");
        writer.setupAwsExecutionEnv();

        // Then
        assertEquals("AWS_Lambda_java11", reader.getEnv("AWS_EXECUTION_ENV"));
    }

    private void setEnv(String value, String... keys) {
        Map<String, String> mutableMap = new HashMap<>();
        for (String key : keys) {
            mutableMap.put(key, value);
        }

        Map<String, String> unmodifiableMap = unmodifiableMap(mutableMap);
        EnvReader envReader = new EnvReaderMock(unmodifiableMap);
        this.envReader = envReader;
        this.underTest = new EnvWriter(envReader);
    }

    private static class EnvReaderMock extends EnvReader {

        private final Map<String, String> env;

        EnvReaderMock(Map<String, String> env) {
            this.env = env;
        }

        @Override
        public Map<String, String> getEnv() {
            return env;
        }

        @Override
        public String getEnv(String envVariableName) {
            return env.get(envVariableName);
        }
    }

}
