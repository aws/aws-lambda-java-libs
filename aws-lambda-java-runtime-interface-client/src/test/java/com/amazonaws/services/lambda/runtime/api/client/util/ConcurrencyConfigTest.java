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

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcurrencyConfigTest {
    @Mock
    private LambdaContextLogger lambdaLogger;

    @Mock
    private EnvReader envReader;

    private static final String exitingRuntimeString = String.format("User configured %s is invalid.", ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_MAX_CONCURRENCY);

    @Test
    void testDefaultConfiguration() {
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_MAX_CONCURRENCY)).thenReturn(null);
        
        ConcurrencyConfig config = new ConcurrencyConfig(lambdaLogger, envReader);
        verifyNoInteractions(lambdaLogger);
        assertEquals(0, config.getNumberOfPlatformThreads());
        assertEquals(false, config.isMultiConcurrent());
    }

    @Test
    void testMinValidPlatformThreadsConfig() {
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_MAX_CONCURRENCY)).thenReturn("1");

        ConcurrencyConfig config = new ConcurrencyConfig(lambdaLogger, envReader);
        verifyNoInteractions(lambdaLogger);
        assertEquals(1, config.getNumberOfPlatformThreads());
        assertEquals(true, config.isMultiConcurrent());
    }

    @Test
    void testValidPlatformThreadsConfig() {
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_MAX_CONCURRENCY)).thenReturn("4");

        ConcurrencyConfig config = new ConcurrencyConfig(lambdaLogger, envReader);
        verifyNoInteractions(lambdaLogger);
        assertEquals(4, config.getNumberOfPlatformThreads());
        assertEquals(true, config.isMultiConcurrent());
    }

    @Test
    void testInvalidPlatformThreadsConfig() {
        when(lambdaLogger.getLogFormat()).thenReturn(LogFormat.JSON);
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_MAX_CONCURRENCY)).thenReturn("invalid");

        assertThrows(NumberFormatException.class, () -> new ConcurrencyConfig(lambdaLogger, envReader));
        verify(lambdaLogger).log(contains(exitingRuntimeString), eq(LogLevel.ERROR));
    }

    @Test
    void testGetConcurrencyConfigMessage() {
        when(envReader.getEnv(ReservedRuntimeEnvironmentVariables.AWS_LAMBDA_MAX_CONCURRENCY)).thenReturn("4");

        ConcurrencyConfig config = new ConcurrencyConfig(lambdaLogger, envReader);
        String expectedMessage = "Starting 4 concurrent function handler threads.";
        verifyNoInteractions(lambdaLogger);
        assertEquals(expectedMessage, config.getConcurrencyConfigMessage());
        assertEquals(true, config.isMultiConcurrent());
    }

    @Test
    void testGetConcurrencyConfigWithNoConcurrency() {
        ConcurrencyConfig config = new ConcurrencyConfig(lambdaLogger, envReader);
        verifyNoInteractions(lambdaLogger);
        assertEquals(0, config.getNumberOfPlatformThreads());
        assertEquals(false, config.isMultiConcurrent());
    }
}
