package com.amazonaws.services.lambda.runtime.api.client.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;

class TextLogFormatterTest {
    @Test
    void testFormattingStringWithLogLevel() {
        assertFormatsString("test log", LogLevel.WARN, "[WARN] test log");
    }

    @Test
    void testFormattingStringWithoutLogLevel() {
        assertFormatsString("test log", LogLevel.UNDEFINED, "test log");
    }

    void assertFormatsString(String input, LogLevel logLevel, String expected) {
        LogFormatter logFormatter = new TextLogFormatter();
        String output = logFormatter.format(input, logLevel);
        assertEquals(expected, output);
    }
}