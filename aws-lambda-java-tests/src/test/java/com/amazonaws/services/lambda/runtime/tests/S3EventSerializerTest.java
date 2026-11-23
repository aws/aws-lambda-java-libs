/* Copyright 2026 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.events.LambdaEventSerializers;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class S3EventSerializerTest {

    private final PojoSerializer<S3Event> serializer = LambdaEventSerializers.serializerFor(
            S3Event.class, S3Event.class.getClassLoader());

    static Stream<Arguments> absentEventTimes() {
        return Stream.of(
                Arguments.of("", false),
                Arguments.of("", true),
                Arguments.of("\"eventTime\":null,", false),
                Arguments.of("\"eventTime\":null,", true));
    }

    @ParameterizedTest
    @MethodSource("absentEventTimes")
    public void preservesAbsentEventTime(String eventTimeField, boolean inputStream) {
        String json = "{\"Records\":[" + record(eventTimeField) + "]}";
        S3Event event = inputStream
                ? serializer.fromJson(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)))
                : serializer.fromJson(json);

        assertThat(event.getRecords()).hasSize(1);
        assertThat(event.getRecords().get(0).getEventTime()).isNull();
        assertThat(event.getRecords().get(0).getS3().getObject().getKey()).isEqualTo("test-key");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2022-06-15T14:58:25.123Z", "2022-06-15T15:58:25.123+01:00"})
    public void preservesValidEventTime(String eventTime) {
        S3Event event = serializer.fromJson("{\"Records\":[" + record("\"eventTime\":\"" + eventTime + "\",") + "]}");

        assertThat(event.getRecords().get(0).getEventTime()).isEqualTo(DateTime.parse(eventTime));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "not-a-timestamp"})
    public void rejectsMalformedEventTime(String eventTime) {
        String json = "{\"Records\":[" + record("\"eventTime\":\"" + eventTime + "\",") + "]}";

        assertThatThrownBy(() -> serializer.fromJson(json)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void deserializesRecordsWithAndWithoutEventTime() {
        String eventTime = "2022-06-15T14:58:25.123Z";
        S3Event event = serializer.fromJson("{\"Records\":[" + record("") + ","
                + record("\"eventTime\":null,") + "," + record("\"eventTime\":\"" + eventTime + "\",") + "]}");

        assertThat(event.getRecords()).hasSize(3);
        assertThat(event.getRecords().get(0).getEventTime()).isNull();
        assertThat(event.getRecords().get(1).getEventTime()).isNull();
        assertThat(event.getRecords().get(2).getEventTime()).isEqualTo(DateTime.parse(eventTime));
    }

    private static String record(String eventTimeField) {
        return "{" + eventTimeField + "\"s3\":{\"object\":{\"key\":\"test-key\"}}}";
    }
}
