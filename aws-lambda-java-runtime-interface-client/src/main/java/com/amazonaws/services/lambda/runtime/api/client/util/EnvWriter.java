/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.util;

import com.amazonaws.services.lambda.runtime.api.client.ReservedRuntimeEnvironmentVariables;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class EnvWriter implements AutoCloseable {

    private Map<String, String> envMap;
    private final Field field;

    public EnvWriter(EnvReader envReader) {
        Map<String, String> env = envReader.getEnv();
        try {
            field = env.getClass().getDeclaredField("m");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, String> map = (Map<String, String>) field.get(env);
            envMap = map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (field != null) {
            field.setAccessible(false);
        }
    }

    public void modifyEnv(Consumer<Map<String, String>> modifier) {
        modifier.accept(envMap);
    }

    public void unsetLambdaInternalEnv() {
        modifyEnv(env -> env.remove("_LAMBDA_TELEMETRY_LOG_FD"));
    }

    public void setupEnvironmentCredentials() {
        modifyEnv((env) -> {
            // AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_SESSION_TOKEN are set by the runtime API daemon when
            // executing the runtime's bootstrap. Ensure these are not empty values.
            removeIfEmpty(env, ReservedRuntimeEnvironmentVariables.AWS_ACCESS_KEY_ID);
            removeIfEmpty(env, ReservedRuntimeEnvironmentVariables.AWS_SECRET_ACCESS_KEY);
            removeIfEmpty(env, ReservedRuntimeEnvironmentVariables.AWS_SESSION_TOKEN);

            // The AWS Java SDK supports two alternate keys for the aws access and secret keys for compatibility.
            // These are not set by the runtime API daemon when executing a runtime's bootstrap so set them here.
            addIfNotNull(env, "AWS_ACCESS_KEY", env.get(ReservedRuntimeEnvironmentVariables.AWS_ACCESS_KEY_ID));
            addIfNotNull(env, "AWS_SECRET_KEY", env.get(ReservedRuntimeEnvironmentVariables.AWS_SECRET_ACCESS_KEY));
        });
    }

    public void setupAwsExecutionEnv() {
        executionEnvironmentForJavaVersion()
                .ifPresent(val -> modifyEnv(env -> env.put(ReservedRuntimeEnvironmentVariables.AWS_EXECUTION_ENV, val)));
    }

    private Optional<String> executionEnvironmentForJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.8")) {
            return Optional.of("AWS_Lambda_java8");
        } else if (version.startsWith("11")) {
            return Optional.of("AWS_Lambda_java11");
        }
        return Optional.empty();
    }

    private void addIfNotNull(Map<String, String> env, String key, String value) {
        if (value != null && !value.isEmpty()) {
            env.put(key, value);
        }
    }

    private void removeIfEmpty(Map<String, String> env, String key) {
        env.computeIfPresent(key, (k, v) -> v.isEmpty() ? null : v);
    }

}
