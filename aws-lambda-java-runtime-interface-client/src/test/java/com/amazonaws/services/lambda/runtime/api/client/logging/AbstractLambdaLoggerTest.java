package com.amazonaws.services.lambda.runtime.api.client.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amazonaws.services.lambda.runtime.logging.LogFormat;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;


public class AbstractLambdaLoggerTest {
    class TestLogger extends AbstractLambdaLogger {
        private List<byte[]> messages = new LinkedList<>();

        public TestLogger(LogLevel logLevel, LogFormat logFormat) {
            super(logLevel, logFormat);
        }

        @Override
        protected void logMessage(byte[] message, LogLevel logLevel) {
            messages.add(message);
        }

        List<byte[]> getMessages() {
            return messages;
        }
    }

    private void logMessages(LambdaLogger logger) {
        logger.log("trace", LogLevel.TRACE);
        logger.log("debug", LogLevel.DEBUG);
        logger.log("info", LogLevel.INFO);
        logger.log("warn", LogLevel.WARN);
        logger.log("error", LogLevel.ERROR);
        logger.log("fatal", LogLevel.FATAL);
    }

    @Test
    public void testWithoutFiltering() {
        TestLogger logger = new TestLogger(LogLevel.UNDEFINED, LogFormat.TEXT);
        logMessages(logger);

        assertEquals(6, logger.getMessages().size());
    }

    @Test
    public void testWithFiltering() {
        TestLogger logger = new TestLogger(LogLevel.WARN, LogFormat.TEXT);
        logMessages(logger);

        assertEquals(3, logger.getMessages().size());
    }

    @Test
    public void testUndefinedLogLevelWithFiltering() {
        TestLogger logger = new TestLogger(LogLevel.WARN, LogFormat.TEXT);
        logger.log("undefined");

        assertEquals(1, logger.getMessages().size());
    }

    @Test
    public void testFormattingLogMessages() {
        TestLogger logger = new TestLogger(LogLevel.INFO, LogFormat.TEXT);
        logger.log("test message", LogLevel.INFO);

        assertEquals(1, logger.getMessages().size());
        assertEquals("[INFO] test message", new String(logger.getMessages().get(0)));
    }
}
