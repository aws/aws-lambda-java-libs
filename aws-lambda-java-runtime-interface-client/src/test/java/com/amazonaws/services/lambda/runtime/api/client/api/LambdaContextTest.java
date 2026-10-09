/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.api;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.factories.GsonFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public static final String TENANT_ID = "tenant-id";
    public static final String X_RAY_TRACE_ID = "x-ray-trace-id";

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
                FUNCTION_NAME, IDENTITY, FUNCTION_VERSION, INVOKED_FUNCTION_ARN, TENANT_ID, X_RAY_TRACE_ID, CLIENT_CONTEXT);
    }

    private LambdaContext w3cContext(ClientContext clientContext) {
        return new LambdaContext(MEMORY_LIMIT, System.currentTimeMillis() + 1000, REQUEST_ID, LOG_GROUP_NAME,
                LOG_STREAM_NAME, FUNCTION_NAME, IDENTITY, FUNCTION_VERSION, INVOKED_FUNCTION_ARN, TENANT_ID,
                X_RAY_TRACE_ID, clientContext);
    }

    private LambdaClientContext clientContextFromJson(String json) {
        PojoSerializer<LambdaClientContext> serializer =
                GsonFactory.getInstance().getSerializer(LambdaClientContext.class);
        return serializer.fromJson(json);
    }

    @Test
    public void w3c_returnsEmptyWhenClientContextIsNull() {
        assertTrue(w3cContext(null).w3c().isEmpty());
    }

    @Test
    public void w3c_returnsEmptyWhenNoW3cKey() {
        LambdaClientContext cc = clientContextFromJson("{\"custom\": {\"value\": \"test\"}}");
        assertTrue(w3cContext(cc).w3c().isEmpty());
    }

    @Test
    public void w3c_returnsAllAllowlistedFields() {
        LambdaClientContext cc = clientContextFromJson(
                "{\"w3c\": {" +
                        "\"traceparent\": \"00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01\"," +
                        "\"tracestate\": \"rojo=00f067aa0ba902b7\"," +
                        "\"baggage\": \"userId=alice\"}}");

        Map<String, String> w3c = w3cContext(cc).w3c();

        assertEquals(3, w3c.size());
        assertEquals("00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01", w3c.get("traceparent"));
        assertEquals("rojo=00f067aa0ba902b7", w3c.get("tracestate"));
        assertEquals("userId=alice", w3c.get("baggage"));
    }

    @Test
    public void w3c_dropsNonAllowlistedKeysAndNonStringValues() {
        LambdaClientContext cc = clientContextFromJson(
                "{\"w3c\": {" +
                        "\"baggage\": \"keep=me\"," +
                        "\"unknownField\": \"drop-me\"," +
                        "\"traceparent\": 42," +
                        "\"tracestate\": null}}");

        Map<String, String> w3c = w3cContext(cc).w3c();

        assertEquals(1, w3c.size());
        assertEquals("keep=me", w3c.get("baggage"));
    }

    @Test
    public void w3c_treatsNonObjectPayloadAsEmpty() {
        assertTrue(w3cContext(clientContextFromJson("{\"w3c\": \"not-an-object\"}")).w3c().isEmpty());
        assertTrue(w3cContext(clientContextFromJson("{\"w3c\": [\"baggage=abc\"]}")).w3c().isEmpty());
    }

    @Test
    public void w3c_isStrippedFromClientContextAndIsUnmodifiable() {
        LambdaClientContext cc = clientContextFromJson(
                "{\"custom\": {\"value\": \"test\"}, \"w3c\": {\"baggage\": \"userId=alice\"}}");

        LambdaContext context = w3cContext(cc);

        assertEquals("userId=alice", context.w3c().get("baggage"));
        assertThrows(UnsupportedOperationException.class, () -> context.w3c().put("x", "y"));
        assertEquals("test", cc.getCustom().get("value"));
        assertTrue(w3cContext(cc).w3c().isEmpty());
    }

    @Test
    public void w3c_defaultsToEmptyOnBareContextInterface() {
        Context bare = new Context() {
            public String getAwsRequestId() { return null; }
            public String getLogGroupName() { return null; }
            public String getLogStreamName() { return null; }
            public String getFunctionName() { return null; }
            public String getFunctionVersion() { return null; }
            public String getInvokedFunctionArn() { return null; }
            public com.amazonaws.services.lambda.runtime.CognitoIdentity getIdentity() { return null; }
            public ClientContext getClientContext() { return null; }
            public int getRemainingTimeInMillis() { return 0; }
            public int getMemoryLimitInMB() { return 0; }
            public com.amazonaws.services.lambda.runtime.LambdaLogger getLogger() { return null; }
        };

        assertTrue(bare.w3c().isEmpty());
        assertFalse(bare.w3c() == null);
    }
}
