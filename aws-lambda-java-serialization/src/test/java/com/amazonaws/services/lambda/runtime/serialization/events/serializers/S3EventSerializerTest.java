/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.serializers;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.lambda.runtime.serialization.events.tck.EventUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class S3EventSerializerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final ClassLoader SYSTEM_CLASS_LOADER = ClassLoader.getSystemClassLoader();

    @Test
    public void testSerDeS3Event() throws IOException {
        S3EventSerializer<S3Event> s3EventSerializer = getS3EventSerializerWithClass(S3Event.class);

        String expected = EventUtils.readEvent("s3_event.json");
        String actual = deserializeSerializeJsonToString(s3EventSerializer, expected);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testSerDeS3EventNotification() throws IOException {
        S3EventSerializer<S3EventNotification> s3EventSerializer = getS3EventSerializerWithClass(S3EventNotification.class);

        String expected = EventUtils.readEvent("s3_event.json");
        String actual = deserializeSerializeJsonToString(s3EventSerializer, expected);

        assertJsonEqual(expected, actual);
    }

    private <T> S3EventSerializer<T> getS3EventSerializerWithClass(Class<T> modelClass) {
        return new S3EventSerializer<T>()
                .withClass(modelClass)
                .withClassLoader(SYSTEM_CLASS_LOADER);
    }


    private void assertJsonEqual(String expected, String actual) throws IOException {
        assertEquals(OBJECT_MAPPER.readTree(expected), OBJECT_MAPPER.readTree(actual));
    }

    private <T> String deserializeSerializeJsonToString(S3EventSerializer<T> s3EventSerializer, String expected) {
        T event = s3EventSerializer.fromJson(expected);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        s3EventSerializer.toJson(event, baos);
        return EventUtils.bytesToString(baos.toByteArray());
    }

}
