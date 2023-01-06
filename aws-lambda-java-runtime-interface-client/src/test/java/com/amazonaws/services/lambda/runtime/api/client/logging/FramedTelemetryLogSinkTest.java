/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FramedTelemetryLogSinkTest {

    private static final int DEFAULT_BUFFER_SIZE = 256;
    private static final byte ZERO_BYTE = (byte) 0;

    private long timestamp() {
        Instant instant = Instant.now();
        return instant.getEpochSecond() * 1_000_000 + instant.getNano() / 1000;
    }

    @TempDir
    public Path tmpFolder;

    @Test
    public void logSingleFrame() throws IOException {
        byte[] message = "hello world\nsomething on a new line!\n".getBytes();
        File tmpFile = tmpFolder.resolve("pipe").toFile();
        FileOutputStream fos = new FileOutputStream(tmpFile);
        FileDescriptor fd = fos.getFD();
        long before = timestamp();
        try (FramedTelemetryLogSink logSink = new FramedTelemetryLogSink(fd)) {
            logSink.log(message);
        }
        long after = timestamp();

        ByteBuffer buf = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        ReadableByteChannel readChannel = new FileInputStream(tmpFile).getChannel();
        readChannel.read(buf);

        // reset the position to the start
        buf.position(0);

        // first 4 bytes indicate the type
        int type = buf.getInt();
        assertEquals(FrameType.LOG.getValue(), type);

        // next 4 bytes indicate the length of the message
        int len = buf.getInt();
        assertEquals(message.length, len);

        // next 8 bytes should indicate the timestamp
        long timestamp = buf.getLong();
        assertTrue(before <= timestamp);
        assertTrue(timestamp <= after);

        // use `len` to allocate a byte array to read the logged message into
        byte[] actual = new byte[len];
        buf.get(actual);
        assertArrayEquals(message, actual);

        // rest of buffer should be empty
        while(buf.hasRemaining())
            assertEquals(ZERO_BYTE, buf.get());
    }

    @Test
    public void logMultipleFrames() throws IOException {
        byte[] firstMessage = "hello world\nsomething on a new line!".getBytes();
        byte[] secondMessage = "hello again\nhere's another message\n".getBytes();
        File tmpFile = tmpFolder.resolve("pipe").toFile();
        FileOutputStream fos = new FileOutputStream(tmpFile);
        FileDescriptor fd = fos.getFD();
        long before = timestamp();
        try (FramedTelemetryLogSink logSink = new FramedTelemetryLogSink(fd)) {
            logSink.log(firstMessage);
            logSink.log(secondMessage);
        }
        long after = timestamp();

        ByteBuffer buf = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        ReadableByteChannel readChannel = new FileInputStream(tmpFile).getChannel();
        readChannel.read(buf);

        // reset the position to the start
        buf.position(0);

        for(byte[] message : Arrays.asList(firstMessage, secondMessage)) {
            // first 4 bytes indicate the type
            int type = buf.getInt();
            assertEquals(FrameType.LOG.getValue(), type);

            // next 4 bytes indicate the length of the message
            int len = buf.getInt();
            assertEquals(message.length, len);

            // next 8 bytes should indicate the timestamp
            long timestamp = buf.getLong();
            assertTrue(before <= timestamp);
            assertTrue(timestamp <= after);

            // use `len` to allocate a byte array to read the logged message into
            byte[] actual = new byte[len];
            buf.get(actual);
            assertArrayEquals(message, actual);
        }

        // rest of buffer should be empty
        while(buf.hasRemaining())
            assertEquals(ZERO_BYTE, buf.get());
    }

    /**
     * The implementation of FramedTelemetryLogSink was based on java.nio.channels.WritableByteChannel which would
     * throw ClosedByInterruptException if Thread.currentThread.interrupt() was called. The implementation was changed
     * and this test ensures that logging works even if the current thread was interrupted.
     *
     * https://t.corp.amazon.com/0304370986/
     */
    @Test
    public void interruptedThread() throws IOException {
        try {
            byte[] message = "hello world\nsomething on a new line!\n".getBytes();
            File tmpFile = tmpFolder.resolve("pipe").toFile();
            FileOutputStream fos = new FileOutputStream(tmpFile);
            FileDescriptor fd = fos.getFD();
            try (FramedTelemetryLogSink logSink = new FramedTelemetryLogSink(fd)) {
                Thread.currentThread().interrupt();

                logSink.log(message);
            }

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream logInputStream = new FileInputStream(tmpFile);
            int readBytes = logInputStream.read(buffer);

            int headerSizeBytes = 16; // message type (4 bytes) + len (4 bytes) + timestamp (8 bytes)
            int expectedBytes = headerSizeBytes + message.length;

            assertEquals(expectedBytes, readBytes);

            for(int i = 0; i < message.length; i++) {
                assertEquals(buffer[i + headerSizeBytes], message[i]);
            }
        } finally {
            // clear interrupted status of the current thread
            assertTrue(Thread.interrupted());
        }
    }
}
