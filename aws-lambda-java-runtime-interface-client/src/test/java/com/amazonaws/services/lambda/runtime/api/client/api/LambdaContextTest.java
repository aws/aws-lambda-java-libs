/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LambdaContextTest {

    private static final String REQUEST_ID = "request-id";
    private static final String LOG_GROUP_NAME = "log-group-name";
    private static final String LOG_STREAM_NAME = "log-stream-name";
    private static final String FUNCTION_NAME = "function-name";
    private static final LambdaCognitoIdentity IDENTITY = new LambdaCognitoIdentity("identity-id", "pool-id");
    private static final String FUNCTION_VERSION = "function-version";
    private static final String INVOKED_FUNCTION_ARN = "invoked-function-arn";
    private static final LambdaClientContext CLIENT_CONTEXT = new LambdaClientContext();
    public static final int MEMORY_LIMIT = 128;

    @Test
    public void getRemainingTimeInMillis() {
        long now = System.currentTimeMillis();
        LambdaContext ctx = createContextWithDeadline(now + 1000);

        int actual = ctx.getRemainingTimeInMillis();

        assertTrue(actual > 0);
        assertTrue(actual <= 1000);
    }

    @Test
    public void getRemainingTimeInMillis_Sleep() throws InterruptedException {
        long now = System.currentTimeMillis();
        LambdaContext ctx = createContextWithDeadline(now + 1000);

        int before = ctx.getRemainingTimeInMillis();
        Thread.sleep(100);
        int after = ctx.getRemainingTimeInMillis();

        assertTrue((before - after) >= 100);
    }

    @Test
    public void getRemainingTimeInMillis_Deadline() throws InterruptedException {
        long now = System.currentTimeMillis();
        LambdaContext ctx = createContextWithDeadline(now + 100);

        Thread.sleep(110);

        assertEquals(0, ctx.getRemainingTimeInMillis());
    }

    private LambdaContext createContextWithDeadline(long deadlineTimeInMs) {
        return new LambdaContext(MEMORY_LIMIT, deadlineTimeInMs, REQUEST_ID, LOG_GROUP_NAME, LOG_STREAM_NAME,
                FUNCTION_NAME, IDENTITY, FUNCTION_VERSION, INVOKED_FUNCTION_ARN, CLIENT_CONTEXT);
    }
}
