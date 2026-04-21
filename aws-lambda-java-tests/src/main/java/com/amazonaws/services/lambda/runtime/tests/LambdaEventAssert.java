package com.amazonaws.services.lambda.runtime.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.events.LambdaEventSerializers;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.JsonNode;
import com.amazonaws.lambda.thirdparty.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Framework-agnostic assertion utilities for verifying Lambda event
 * serialization.
 *
 * <p>
 * When opentest4j is on the classpath (e.g. JUnit 5.x / JUnit Platform),
 * assertion failures are reported as
 * {@code org.opentest4j.AssertionFailedError}
 * which enables rich diff support in IDEs. Otherwise, falls back to plain
 * {@link AssertionError}.
 * </p>
 *
 */
public class LambdaEventAssert {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Round-trip using the registered {@link LambdaEventSerializers} path
     * (Jackson + mixins + DateModule + DateTimeModule + naming strategies).
     *
     * <p>
     * The check performs two consecutive round-trips
     * (JSON &rarr; POJO &rarr; JSON &rarr; POJO &rarr; JSON) and compares the
     * original JSON tree against the final output tree. A single structural
     * comparison catches both:
     * </p>
     * <ul>
     * <li>Fields silently dropped during deserialization</li>
     * <li>Non-idempotent serialization (output changes across round-trips)</li>
     * </ul>
     *
     * @param fileName    classpath resource name (must end with {@code .json})
     * @param targetClass the event class to deserialize into
     * @throws AssertionError if the original and final JSON trees differ
     */
    public static <T> void assertSerializationRoundTrip(String fileName, Class<T> targetClass) {
        PojoSerializer<T> serializer = LambdaEventSerializers.serializerFor(targetClass,
                ClassLoader.getSystemClassLoader());

        if (!fileName.endsWith(".json")) {
            throw new IllegalArgumentException("File " + fileName + " must have json extension");
        }

        byte[] originalBytes;
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (stream == null) {
                throw new IllegalArgumentException("Could not load resource '" + fileName + "' from classpath");
            }
            originalBytes = toBytes(stream);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read resource " + fileName, e);
        }

        // Two round-trips: original → POJO → JSON → POJO → JSON
        // We are doing 2 passes so we can check instability problems
        // like UnstablePojo in LambdaEventAssertTest
        ByteArrayOutputStream firstOutput = roundTrip(new ByteArrayInputStream(originalBytes), serializer);
        ByteArrayOutputStream secondOutput = roundTrip(
                new ByteArrayInputStream(firstOutput.toByteArray()), serializer);

        // Compare original tree against final tree.
        // Strip explicit nulls from the original because the serializer is
        // configured with Include.NON_NULL — null fields are intentionally
        // omitted and that is not a data-loss bug.
        try {
            JsonNode originalTree = JsonNodeUtils.stripNulls(MAPPER.readTree(originalBytes));
            JsonNode finalTree = MAPPER.readTree(secondOutput.toByteArray());

            if (!originalTree.equals(finalTree)) {
                List<String> diffs = new ArrayList<>();
                JsonNodeUtils.diffNodes("", originalTree, finalTree, diffs);

                if (!diffs.isEmpty()) {
                    StringBuilder msg = new StringBuilder();
                    msg.append("Serialization round-trip failure for ")
                            .append(targetClass.getSimpleName())
                            .append(" (").append(diffs.size()).append(" difference(s)):\n");
                    for (String diff : diffs) {
                        msg.append("  ").append(diff).append('\n');
                    }

                    String expected = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(originalTree);
                    String actual = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(finalTree);
                    throw buildAssertionError(msg.toString(), expected, actual);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to parse JSON for tree comparison", e);
        }
    }

    private static <T> ByteArrayOutputStream roundTrip(InputStream stream, PojoSerializer<T> serializer) {
        T event = serializer.fromJson(stream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serializer.toJson(event, outputStream);
        return outputStream;
    }

    private static byte[] toBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int n;
        while ((n = stream.read(chunk)) != -1) {
            buffer.write(chunk, 0, n);
        }
        return buffer.toByteArray();
    }

    /**
     * Tries to create an opentest4j AssertionFailedError for rich IDE diff
     * support. Falls back to plain AssertionError if opentest4j is not on
     * the classpath.
     */
    private static AssertionError buildAssertionError(String message, String expected, String actual) {
        try {
            // opentest4j is provided by JUnit Platform (5.x) and enables
            // IDE diff viewers to show expected vs actual side-by-side.
            Class<?> cls = Class.forName("org.opentest4j.AssertionFailedError");
            return (AssertionError) cls
                    .getConstructor(String.class, Object.class, Object.class)
                    .newInstance(message, expected, actual);
        } catch (ReflectiveOperationException e) {
            return new AssertionError(message + "\nExpected:\n" + expected + "\nActual:\n" + actual);
        }
    }
}
