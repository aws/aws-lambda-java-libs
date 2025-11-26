package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.api.client.GsonFactory;
import com.amazonaws.services.lambda.runtime.api.client.api.LambdaContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializer;

public class JsonLogFormatterTest {

    @Test
    void testFormattingWithoutLambdaContext() {
        assertFormatsString("test log", LogLevel.WARN, null);
    }

    @Test
    void testFormattingWithLambdaContext() {
        LambdaContext context = new LambdaContext(
                0,
                0,
                "request-id",
                null,
                null,
                "function-name",
                null,
                null,
                "function-arn",
                null,
                null,
                null
        );
        assertFormatsString("test log", LogLevel.WARN, context);
    }

    @Test
    void testFormattingWithTenantIdInLambdaContext() {
        LambdaContext context = new LambdaContext(
                0,
                0,
                "request-id",
                null,
                null,
                "function-name",
                null,
                null,
                "function-arn",
                "tenant-id",
                "xray-trace-id",
                null
        );
        assertFormatsString("test log", LogLevel.WARN, context);
    }

    void assertFormatsString(String message, LogLevel logLevel, LambdaContext context) {
        JsonLogFormatter logFormatter = new JsonLogFormatter();
        if (context != null) {
            logFormatter.setLambdaContext(context);
        }
        String output = logFormatter.format(message, logLevel);

        LambdaSerializer<StructuredLogMessage> serializer = GsonFactory.getInstance().getLambdaSerializer(StructuredLogMessage.class);
        assert_expected_log_message(serializer.deserialize(output), message, logLevel, context);
    }

    void assert_expected_log_message(StructuredLogMessage result, String message, LogLevel logLevel, LambdaContext context) {
        assertEquals(message, result.message);
        assertEquals(logLevel, result.level);
        assertNotNull(result.timestamp);

        if (context != null) {
            assertEquals(context.getAwsRequestId(), result.AWSRequestId);
            assertEquals(context.getTenantId(), result.tenantId);
        }
    }
}
