/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.serialization.events.tck;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class EventUtils {
    public static String readEvent(String filename) throws IOException {
        Path filePath = Paths.get("src", "test", "resources", "event_models", filename);
        byte[] bytes = Files.readAllBytes(filePath);
        return bytesToString(bytes);
    }
    public static String bytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
