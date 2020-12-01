/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StdOutLogSinkTest {

    private final PrintStream originalOutPrintStream = System.out;
    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private final PrintStream capturedOutPrintStream = new PrintStream(bos);

    @BeforeEach
    public void setup() {
        bos.reset();
    }

    @Test
    public void testSingleLog() {
        System.setOut(capturedOutPrintStream);
        try {
            try (StdOutLogSink logSink = new StdOutLogSink()) {
                logSink.log("hello\nworld".getBytes());
            }
        } finally {
            System.setOut(originalOutPrintStream);
        }

        assertEquals("hello\nworld", bos.toString());
    }

    @Test
    public void testContextLoggerWithStdoutLogSink_logBytes() {
        System.setOut(capturedOutPrintStream);
        try {
            try (StdOutLogSink logSink = new StdOutLogSink()) {
                logSink.log("hello\nworld".getBytes());
                logSink.log("hello again".getBytes());
            }
        } finally {
            System.setOut(originalOutPrintStream);
        }

        assertEquals("hello\nworldhello again", bos.toString());
    }

}
