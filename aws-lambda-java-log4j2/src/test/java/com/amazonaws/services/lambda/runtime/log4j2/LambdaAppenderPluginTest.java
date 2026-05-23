/* Copyright 2026 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LambdaAppenderPluginTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream captured;

    @BeforeEach
    void redirectStdout() throws UnsupportedEncodingException {
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8.name()));
    }

    @AfterEach
    void restoreStdout() {
        System.setOut(originalOut);
    }

    @Test
    void lambdaAppenderEmitsLogsAtVariousLevels() throws UnsupportedEncodingException {
        Logger logger = LogManager.getLogger(LambdaAppenderPluginTest.class);

        logger.debug("debug-msg");
        logger.info("info-msg");
        logger.warn("warn-msg");
        logger.error("error-msg");

        String output = captured.toString(StandardCharsets.UTF_8.name());

        // The PatternLayout in src/test/resources/log4j2.xml is "%-5p %c{1} - %m%n",
        // so each event should appear as "<LEVEL> LambdaAppenderPluginTest - <message>".
        assertTrue(output.contains("DEBUG LambdaAppenderPluginTest - debug-msg"),
                "expected DEBUG line in output but got:\n" + output);
        assertTrue(output.contains("INFO  LambdaAppenderPluginTest - info-msg"),
                "expected INFO line in output but got:\n" + output);
        assertTrue(output.contains("WARN  LambdaAppenderPluginTest - warn-msg"),
                "expected WARN line in output but got:\n" + output);
        assertTrue(output.contains("ERROR LambdaAppenderPluginTest - error-msg"),
                "expected ERROR line in output but got:\n" + output);

        // Sanity check: log4j should not have fallen back to its default
        // ConsoleAppender / status logger error message.
        assertFalse(output.contains("ERROR StatusLogger"),
                "log4j status logger reported an error, output was:\n" + output);
    }

    @Test
    void lambdaAppenderEmitsJsonForJsonFormatLogger() throws UnsupportedEncodingException {
        // The "json-test" logger is configured in src/test/resources/log4j2.xml
        // with additivity=false to a second LambdaAppender using format="JSON"
        // and JsonTemplateLayout backed by LambdaLayout.json.
        Logger logger = LogManager.getLogger("json-test");

        logger.info("json-info-msg");
        logger.error("json-error-msg");

        String output = captured.toString(StandardCharsets.UTF_8.name());

        assertTrue(output.contains("json-info-msg"),
                "expected json-info-msg in output but got:\n" + output);
        assertTrue(output.contains("json-error-msg"),
                "expected json-error-msg in output but got:\n" + output);

        // Output should look like JSON, not the text PatternLayout from the
        // root logger — so it must contain JSON field punctuation around the
        // message rather than the "INFO  json-test - ..." text pattern.
        assertTrue(output.contains("\"message\":\"json-info-msg\""),
                "expected JSON-encoded message field but got:\n" + output);
    }
}
