/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.util;

import com.amazonaws.services.lambda.runtime.api.client.ReservedRuntimeEnvironmentVariables;
import com.amazonaws.services.lambda.runtime.api.client.logging.LambdaContextLogger;
import com.amazonaws.services.lambda.runtime.logging.LogFormat;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcurrencyConfigTest {
    @Mock
    private LambdaContextLogger lambdaLogger;

    @Mock
    private EnvReader envReader;

    @Test
    void testDefaultConfiguration() {
        when(lambdaLogger.getLogFormat()).thenReturn(LogFormat.JSON);
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_ENABLE_MULTICONCURRENT_RIC)).thenReturn("true");
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY)).thenReturn(null);
        
        ConcurrencyConfig config = new ConcurrencyConfig(lambdaLogger, envReader);
        assertEquals(Runtime.getRuntime().availableProcessors(), config.getNumberOfPlatformThreads());
        assertEquals(Runtime.getRuntime().availableProcessors() >= 2, config.isMultiConcurrent());
    }

    @Test
    void testValidPlatformThreadsConfig() {
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_ENABLE_MULTICONCURRENT_RIC)).thenReturn("true");
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY)).thenReturn("4");

        ConcurrencyConfig config = new ConcurrencyConfig(lambdaLogger, envReader);
        assertEquals(4, config.getNumberOfPlatformThreads());
        assertEquals(true, config.isMultiConcurrent());
    }

    @Test
    void testInvalidPlatformThreadsConfig() {
        when(lambdaLogger.getLogFormat()).thenReturn(LogFormat.JSON);
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_ENABLE_MULTICONCURRENT_RIC)).thenReturn("true");
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY)).thenReturn("invalid");

        ConcurrencyConfig config = new ConcurrencyConfig(lambdaLogger, envReader);
        assertEquals(Runtime.getRuntime().availableProcessors(), config.getNumberOfPlatformThreads());
        verify(lambdaLogger).log(anyString(), eq(LogLevel.WARN));
        assertEquals(Runtime.getRuntime().availableProcessors() >= 2, config.isMultiConcurrent());
    }

    @Test
    void testExceedingPlatformThreadsLimit() {
        when(lambdaLogger.getLogFormat()).thenReturn(LogFormat.JSON);
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_ENABLE_MULTICONCURRENT_RIC)).thenReturn("true");
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY)).thenReturn("1001");

        ConcurrencyConfig config = new ConcurrencyConfig(lambdaLogger, envReader);
        assertEquals(Runtime.getRuntime().availableProcessors(), config.getNumberOfPlatformThreads());
        verify(lambdaLogger).log(anyString(), eq(LogLevel.WARN));
        assertEquals(Runtime.getRuntime().availableProcessors() >= 2, config.isMultiConcurrent());
    }

    @Test
    void testGetConcurrencyConfigMessage() {
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_ENABLE_MULTICONCURRENT_RIC)).thenReturn("true");
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_RUNTIME_MAX_CONCURRENCY)).thenReturn("4");

        ConcurrencyConfig config = new ConcurrencyConfig(lambdaLogger, envReader);
        String expectedMessage = "Starting 4 concurrent function handler threads.";
        assertEquals(expectedMessage, config.getConcurrencyConfigMessage());
        assertEquals(true, config.isMultiConcurrent());
    }

    @Test
    void testGetConcurrencyConfigWithNoConcurrency() {
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_ENABLE_MULTICONCURRENT_RIC)).thenReturn("false");

        ConcurrencyConfig config = new ConcurrencyConfig(lambdaLogger, envReader);
        assertEquals(0, config.getNumberOfPlatformThreads());
        assertEquals(false, config.isMultiConcurrent());
    }
}
