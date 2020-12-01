/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.serializers;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class S3EventSerializerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final ClassLoader SYSTEM_CLASS_LOADER = ClassLoader.getSystemClassLoader();

    @Test
    public void testSerDeS3Event() throws IOException {
        S3EventSerializer<S3Event> s3EventSerializer = getS3EventSerializerWithClass(S3Event.class);

        String expected = readEvent("s3_event.json");
        String actual = deserializeSerializeJsonToString(s3EventSerializer, expected);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testSerDeS3EventNotification() throws IOException {
        S3EventSerializer<S3EventNotification> s3EventSerializer = getS3EventSerializerWithClass(S3EventNotification.class);

        String expected = readEvent("s3_event.json");
        String actual = deserializeSerializeJsonToString(s3EventSerializer, expected);

        assertJsonEqual(expected, actual);
    }

    private <T> S3EventSerializer<T> getS3EventSerializerWithClass(Class<T> modelClass) {
        return new S3EventSerializer<T>()
                .withClass(modelClass)
                .withClassLoader(SYSTEM_CLASS_LOADER);
    }

    private String readEvent(String filename) throws IOException {
        Path filePath = Paths.get("src", "test", "resources", "event_models", filename);
        byte[] bytes = Files.readAllBytes(filePath);
        return bytesToString(bytes);
    }

    private String bytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private void assertJsonEqual(String expected, String actual) throws IOException {
        assertEquals(OBJECT_MAPPER.readTree(expected), OBJECT_MAPPER.readTree(actual));
    }

    private <T> String deserializeSerializeJsonToString(S3EventSerializer<T> s3EventSerializer, String expected) {
        T event = s3EventSerializer.fromJson(expected);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        s3EventSerializer.toJson(event, baos);
        return bytesToString(baos.toByteArray());
    }

}
