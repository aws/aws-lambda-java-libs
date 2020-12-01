/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events;

import com.amazonaws.services.lambda.runtime.events.*;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LambdaEventSerializersTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final ClassLoader SYSTEM_CLASS_LOADER = ClassLoader.getSystemClassLoader();

    @Test
    public void testAPIGatewayProxyRequestEvent() throws IOException {
        String expected = readEvent("api_gateway_proxy_request_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayProxyRequestEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testAPIGatewayProxyResponseEvent() throws IOException {
        String expected = readEvent("api_gateway_proxy_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayProxyResponseEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCloudFrontEvent() throws IOException {
        String expected = readEvent("cloud_front_event.json");
        String actual = deserializeSerializeJsonToString(expected, CloudFrontEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCloudWatchLogsEvent() throws IOException {
        String expected = readEvent("cloud_watch_logs_event.json");
        String actual = deserializeSerializeJsonToString(expected, CloudWatchLogsEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCodeCommitEvent() throws IOException {
        String expected = readEvent("code_commit_event.json");
        String actual = deserializeSerializeJsonToString(expected, CodeCommitEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoEvent() throws IOException {
        String expected = readEvent("cognito_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testConfigEvent() throws IOException {
        String expected = readEvent("config_event.json");
        String actual = deserializeSerializeJsonToString(expected, ConfigEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testDynamodbEvent() throws IOException {
        String expected = readEvent("dynamodb_event.json");
        String actual = deserializeSerializeJsonToString(expected, DynamodbEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testIoTButtonEvent() throws IOException {
        String expected = readEvent("iot_button_event.json");
        String actual = deserializeSerializeJsonToString(expected, IoTButtonEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisAnalyticsFirehoseInputPreprocessingEvent() throws IOException {
        String expected = readEvent("kinesis_analytics_firehose_input_preprocessing_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsFirehoseInputPreprocessingEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisAnalyticsInputPreprocessingResponse() throws IOException {
        String expected = readEvent("kinesis_analytics_input_preprocessing_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsInputPreprocessingResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisAnalyticsOutputDeliveryEvent() throws IOException {
        String expected = readEvent("kinesis_analytics_output_delivery_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsOutputDeliveryEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisAnalyticsOutputDeliveryResponse() throws IOException {
        String expected = readEvent("kinesis_analytics_output_delivery_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsOutputDeliveryResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisAnalyticsStreamsInputPreprocessingEvent() throws IOException {
        String expected = readEvent("kinesis_analytics_streams_input_preprocessing_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsStreamsInputPreprocessingEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisEvent() throws IOException {
        String expected = readEvent("kinesis_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisFirehoseEvent() throws IOException {
        String expected = readEvent("kinesis_firehose_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisFirehoseEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testLexEvent() throws IOException {
        String expected = readEvent("lex_event.json");
        String actual = deserializeSerializeJsonToString(expected, LexEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testS3Event() throws IOException {
        String expected = readEvent("s3_event.json");
        String actual = deserializeSerializeJsonToString(expected, S3Event.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testScheduledEvent() throws IOException {
        String expected = readEvent("scheduled_event.json");
        String actual = deserializeSerializeJsonToString(expected, ScheduledEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testSNSEvent() throws IOException {
        String expected = readEvent("sns_event.json");
        String actual = deserializeSerializeJsonToString(expected, SNSEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testSQSEvent() throws IOException {
        String expected = readEvent("sqs_event.json");
        String actual = deserializeSerializeJsonToString(expected, SQSEvent.class);

        assertJsonEqual(expected, actual);
    }

    private String readEvent(String filename) throws IOException {
        Path filePath = Paths.get("src", "test", "resources", "event_models", filename);
        byte[] bytes = Files.readAllBytes(filePath);
        return bytesToString(bytes);
    }

    private <T> String deserializeSerializeJsonToString(String expected, Class<T> modelClass) {
        PojoSerializer<T> serializer = LambdaEventSerializers.serializerFor(modelClass, SYSTEM_CLASS_LOADER);

        T event = serializer.fromJson(expected);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        serializer.toJson(event, baos);
        return bytesToString(baos.toByteArray());
    }

    private String bytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private void assertJsonEqual(String expected, String actual) throws IOException {
        assertEquals(OBJECT_MAPPER.readTree(expected), OBJECT_MAPPER.readTree(actual));
    }

}
