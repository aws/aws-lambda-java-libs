package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.lambda.thirdparty.org.json.JSONObject;
import com.amazonaws.services.lambda.runtime.api.client.api.LambdaContext;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.factories.GsonFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;

public class JsonLogFormatterTest {

    /*
     * Gson serialization-neutrality tests for the String -> Object type change of
     * StructuredLogMessage.message. These pin the exact serialized behavior so any
     * future regression (e.g. from a Gson upgrade changing runtime-type adapter
     * resolution) is caught here rather than in customers' log pipelines.
     */

    @Test
    void testStringMessageStillSerializesAsJsonStringWithObjectDeclaredField() {
        JsonLogFormatter formatter = new JsonLogFormatter();
        String output = formatter.format("test log", LogLevel.INFO);

        JSONObject parsed = new JSONObject(output);
        // message must be a JSON string value, not an object or anything else
        assertTrue(parsed.get("message") instanceof String);
        assertEquals("test log", parsed.getString("message"));
        assertEquals("INFO", parsed.getString("level"));
    }

    @Test
    void testStringMessageEscapingUnchangedWithObjectDeclaredField() {
        // quotes, backslashes, newlines, unicode and HTML-sensitive chars must
        // round-trip exactly as before the field type change
        String tricky = "quote\" backslash\\ newline\n tab\t unicode\u00e9 html<>&";
        JsonLogFormatter formatter = new JsonLogFormatter();
        String output = formatter.format(tricky, LogLevel.WARN);

        JSONObject parsed = new JSONObject(output);
        assertEquals(tricky, parsed.getString("message"));
    }

    @Test
    void testNullStringMessageOmittedExactlyAsBefore() {
        // serializeNulls(false) omitted a null String message before the change;
        // a null Object message must behave identically
        JsonLogFormatter formatter = new JsonLogFormatter();
        String output = formatter.format((String) null, LogLevel.INFO);

        JSONObject parsed = new JSONObject(output);
        assertFalse(parsed.has("message"));
        assertNotNull(parsed.getString("timestamp"));
    }

    static class SampleStructuredEvent {
        final String event = "sample_event";
        final int intValue;
        final String textValue;

        SampleStructuredEvent(int intValue, String textValue) {
            this.intValue = intValue;
            this.textValue = textValue;
        }
    }

    @Test
    void testObjectMessageSerializesAsNestedJsonObject() {
        JsonLogFormatter formatter = new JsonLogFormatter();
        String output = formatter.format(new SampleStructuredEvent(42, "abc"), LogLevel.DEBUG);

        JSONObject parsed = new JSONObject(output);
        JSONObject message = parsed.getJSONObject("message");
        assertEquals("sample_event", message.getString("event"));
        assertEquals(42, message.getInt("intValue"));
        assertEquals("abc", message.getString("textValue"));
        assertEquals(3, message.length());
        assertEquals("DEBUG", parsed.getString("level"));
        assertNotNull(parsed.getString("timestamp"));
    }

    @Test
    void testObjectMessageWithLambdaContextKeepsEnvelopeFields() {
        JsonLogFormatter formatter = new JsonLogFormatter();
        formatter.setLambdaContext(new LambdaContext(
                0, 0, "request-id", null, null, "function-name",
                null, null, "function-arn", "tenant-id", null, null));
        String output = formatter.format(new SampleStructuredEvent(1, "x"), LogLevel.DEBUG);

        JSONObject parsed = new JSONObject(output);
        assertEquals("request-id", parsed.getString("AWSRequestId"));
        assertEquals("tenant-id", parsed.getString("tenantId"));
        assertEquals(1, parsed.getJSONObject("message").getInt("intValue"));
    }

    @Test
    void testReflectiveStringAccessToMessageFieldStillWorks() throws Exception {
        // StructuredLogMessage is internal, but be conservative about reflective
        // consumers: setting and reading a String through the field must not break.
        StructuredLogMessage msg = new StructuredLogMessage();
        java.lang.reflect.Field field = StructuredLogMessage.class.getDeclaredField("message");
        field.set(msg, "reflective string");
        assertEquals("reflective string", (String) field.get(msg));

        PojoSerializer<StructuredLogMessage> serializer =
                GsonFactory.getInstance().getSerializer(StructuredLogMessage.class);
        java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
        serializer.toJson(msg, stream);
        JSONObject parsed = new JSONObject(stream.toString("UTF-8"));
        assertEquals("reflective string", parsed.getString("message"));
    }

    @Test
    void testStringMessageDeserializationRoundTripUnchanged() {
        // fromJson of a string-message log line must still yield a String in the field
        JsonLogFormatter formatter = new JsonLogFormatter();
        String output = formatter.format("round trip", LogLevel.INFO);

        PojoSerializer<StructuredLogMessage> serializer =
                GsonFactory.getInstance().getSerializer(StructuredLogMessage.class);
        StructuredLogMessage result = serializer.fromJson(output);
        assertTrue(result.message instanceof String);
        assertEquals("round trip", result.message);
    }

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

        PojoSerializer<StructuredLogMessage> serializer = GsonFactory.getInstance().getSerializer(StructuredLogMessage.class);
        assert_expected_log_message(serializer.fromJson(output), message, logLevel, context);
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
