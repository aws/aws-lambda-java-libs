package com.amazonaws.services.lambda.runtime.events;

import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.factories.JacksonFactory;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

public class CloudWatchAlarmEventTest {

    JacksonFactory jacksonFactory = JacksonFactory.getInstance();

    @Test
    public void serdeCloudWatchCompositeAlarmEvent() throws JSONException {
        String expected = readResource("cloudwatch-composite-alarm.json");
        String actual = deserializeSerializeJsonToString(expected, CloudWatchAlarmEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void serdeCloudWatchMetricAlarmEvent() throws JSONException {
        String expected = readResource("cloudwatch-composite-alarm.json");
        String actual = deserializeSerializeJsonToString(expected, CloudWatchAlarmEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    private <T> String deserializeSerializeJsonToString(String expected, Class<T> modelClass) {
        PojoSerializer<T> serializer = jacksonFactory.getSerializer(modelClass);
        T event = serializer.fromJson(expected);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.toJson(event, baos);

        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    private String readResource(String name) {
        Path filePath = Paths.get("src", "test", "resources", name);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}