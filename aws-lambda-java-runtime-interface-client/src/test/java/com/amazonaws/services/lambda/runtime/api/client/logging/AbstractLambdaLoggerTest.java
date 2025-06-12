package com.amazonaws.services.lambda.runtime.api.client.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.amazonaws.services.lambda.runtime.logging.LogFormat;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.amazonaws.lambda.thirdparty.org.json.JSONObject;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.api.client.api.LambdaContext;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;


public class AbstractLambdaLoggerTest {
    class TestSink implements LogSink {
        private List<byte[]> messages = new LinkedList<>();

        public TestSink() {
        }

        @Override
        public synchronized void log(byte[] message) {
            messages.add(message);
        }

        @Override
        public synchronized void log(LogLevel logLevel, LogFormat logFormat, byte[] message) {
            messages.add(message);
        }

        @Override
        public void close() {
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
    public void testLoggingNullValuesWithoutLogLevelInText() {
        TestSink sink = new TestSink();
        LambdaLogger logger = new LambdaContextLogger(sink, LogLevel.INFO, LogFormat.TEXT);

        String isNullString = null;
        byte[] isNullBytes = null;

        logger.log(isNullString);
        logger.log(isNullBytes);

        assertEquals("null", new String(sink.getMessages().get(0)));
        assertEquals("null", new String(sink.getMessages().get(1)));
    }

    // Makes Sure Logging Contexts are thread local.
    @Test
    public void testMultiConcurrentLoggingWithoutLogLevelInJSON() {
        TestSink sink = new TestSink();
        LambdaContextLogger logger = new LambdaContextLogger(sink, LogLevel.INFO, LogFormat.JSON);

        String someMessagePrefix = "Some Message from ";
        String reqIDPrefix = "Thread ID as request# ";

        final int nThreads = 5;
        ExecutorService es = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            es.submit(() -> logger.setLambdaContext(new LambdaContext(Integer.MAX_VALUE, Long.MAX_VALUE, reqIDPrefix + Thread.currentThread().getName(), "", "", "", null, "", "", "", null)));
        }

        final int nMessages = 100_000;
        for (int i = 0; i < nMessages; i++) {
            es.submit(() -> logger.log(someMessagePrefix + Thread.currentThread().getName()));
        }

        es.shutdown();
        while (!es.isTerminated()) {
            ;
        }

        assertEquals(nMessages, sink.getMessages().size());  
        for (byte[] message : sink.getMessages()) {
            JSONObject parsedLog = new JSONObject(new String(message, StandardCharsets.UTF_8));
            String parsedMessage = parsedLog.getString("message");
            String parsedReqID = parsedLog.getString("AWSRequestId");
            assertEquals(parsedMessage.substring(someMessagePrefix.length()), parsedReqID.substring(reqIDPrefix.length()));
        }
    }

    @Test
    public void testLoggingNullValuesWithoutLogLevelInJSON() {
        TestSink sink = new TestSink();
        LambdaLogger logger = new LambdaContextLogger(sink, LogLevel.INFO, LogFormat.JSON);

        String isNullString = null;
        byte[] isNullBytes = null;

        logger.log(isNullString);
        logger.log(isNullBytes);

        assertEquals(2, sink.getMessages().size());
    }

    @Test
    public void testLoggingNullValuesWithLogLevelInText() {
        TestSink sink = new TestSink();
        LambdaLogger logger = new LambdaContextLogger(sink, LogLevel.INFO, LogFormat.TEXT);

        String isNullString = null;
        byte[] isNullBytes = null;

        logger.log(isNullString, LogLevel.ERROR);
        logger.log(isNullBytes, LogLevel.ERROR);

        assertEquals("[ERROR] null", new String(sink.getMessages().get(0)));
        assertEquals("null", new String(sink.getMessages().get(1)));
    }

    @Test
    public void testLoggingNullValuesWithLogLevelInJSON() {
        TestSink sink = new TestSink();
        LambdaLogger logger = new LambdaContextLogger(sink, LogLevel.INFO, LogFormat.JSON);

        String isNullString = null;
        byte[] isNullBytes = null;

        logger.log(isNullString, LogLevel.ERROR);
        logger.log(isNullBytes, LogLevel.ERROR);

        assertEquals(2, sink.getMessages().size());
    }
    @Test
    public void testWithoutFiltering() {
        TestSink sink = new TestSink();
        LambdaLogger logger = new LambdaContextLogger(sink, LogLevel.UNDEFINED, LogFormat.TEXT);
        logMessages(logger);

        assertEquals(6, sink.getMessages().size());
    }

    @Test
    public void testWithFiltering() {
        TestSink sink = new TestSink();
        LambdaLogger logger = new LambdaContextLogger(sink, LogLevel.WARN, LogFormat.TEXT);
        logMessages(logger);

        assertEquals(3, sink.getMessages().size());
    }

    @Test
    public void testUndefinedLogLevelWithFiltering() {
        TestSink sink = new TestSink();
        LambdaLogger logger = new LambdaContextLogger(sink, LogLevel.WARN, LogFormat.TEXT);
        logger.log("undefined");

        assertEquals(1, sink.getMessages().size());
    }

    @Test
    public void testFormattingLogMessages() {
        TestSink sink = new TestSink();
        LambdaLogger logger = new LambdaContextLogger(sink, LogLevel.INFO, LogFormat.TEXT);
        logger.log("test message", LogLevel.INFO);

        assertEquals(1, sink.getMessages().size());
        assertEquals("[INFO] test message", new String(sink.getMessages().get(0)));
    }
}
