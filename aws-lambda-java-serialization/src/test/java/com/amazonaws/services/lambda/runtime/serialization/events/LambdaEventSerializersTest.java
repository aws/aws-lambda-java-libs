/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events;

import com.amazonaws.services.lambda.runtime.events.*;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LambdaEventSerializersTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final ClassLoader SYSTEM_CLASS_LOADER = ClassLoader.getSystemClassLoader();

    private static Stream<Arguments> serdeArguments() {
        return Stream.of(
            Arguments.of("api_gateway_proxy_request_event.json", APIGatewayProxyRequestEvent.class),
            Arguments.of("api_gateway_proxy_response_event.json", APIGatewayProxyResponseEvent.class),
            Arguments.of("cloud_front_event.json", CloudFrontEvent.class),
            Arguments.of("cloud_watch_logs_event.json", CloudWatchLogsEvent.class),
            Arguments.of("code_commit_event.json", CodeCommitEvent.class),
            Arguments.of("api_gateway_proxy_response_event.json", APIGatewayProxyResponseEvent.class),
            Arguments.of("cognito_event.json", CognitoEvent.class),
            Arguments.of("config_event.json", ConfigEvent.class),
            Arguments.of("dynamodb_event.json", DynamodbEvent.class),
            Arguments.of("dynamodb_time_window_event.json", DynamodbTimeWindowEvent.class),
            Arguments.of("iot_button_event.json", IoTButtonEvent.class),
            Arguments.of("kinesis_analytics_firehose_input_preprocessing_event.json", KinesisAnalyticsFirehoseInputPreprocessingEvent.class),
            Arguments.of("kinesis_analytics_input_preprocessing_response_event.json", KinesisAnalyticsInputPreprocessingResponse.class),
            Arguments.of("kinesis_analytics_output_delivery_event.json", KinesisAnalyticsOutputDeliveryEvent.class),
            Arguments.of("kinesis_analytics_output_delivery_response_event.json", KinesisAnalyticsOutputDeliveryResponse.class),
            Arguments.of("kinesis_analytics_streams_input_preprocessing_event.json", KinesisAnalyticsStreamsInputPreprocessingEvent.class),
            Arguments.of("kinesis_event.json", KinesisEvent.class),
            Arguments.of("kinesis_time_window_event.json", KinesisTimeWindowEvent.class),
            Arguments.of("kinesis_firehose_event.json", KinesisFirehoseEvent.class),
            Arguments.of("lex_event.json", LexEvent.class),
            Arguments.of("s3_event.json", S3Event.class),
            Arguments.of("scheduled_event.json", ScheduledEvent.class),
            Arguments.of("sns_event.json", SNSEvent.class),
            Arguments.of("sqs_event.json", SQSEvent.class)
        );
    }

    @ParameterizedTest(name = "Serde {0} Event")
    @MethodSource("serdeArguments")
    public void testAPIGatewayProxyRequestEvent(final String json,
                                                final Class<?> eventClass) throws IOException {
        String expected = readEvent(json);
        String actual = deserializeSerializeJsonToString(expected, eventClass);

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
