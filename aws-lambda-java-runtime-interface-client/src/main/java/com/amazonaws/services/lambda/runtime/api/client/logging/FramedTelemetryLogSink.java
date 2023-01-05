/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;

/**
 * FramedTelemetryLogSink implements the logging contract between runtimes and the platform. It implements a simple
 * framing protocol so message boundaries can be determined. Each frame can be visualized as follows:
 *
 *  <pre>
 * {@code
 * +----------------------+------------------------+---------------------+-----------------------+
 * | Frame Type - 4 bytes | Length (len) - 4 bytes | Timestamp - 8 bytes | Message - 'len' bytes |
 * +----------------------+------------------------+---------------------+-----------------------+
 * }
 * </pre>
 *
 * The first 4 bytes indicate the type of the frame - log frames have a type defined as the hex value 0xa55a0001. The
 * second 4 bytes should indicate the message's length. The next 8 bytes contain UNIX timestamp of the message in 
 * microsecond accuracy. The next 'len' bytes contain the message. The byte order is big-endian.
 */
public class FramedTelemetryLogSink implements LogSink {

    private static final int HEADER_LENGTH = 16;

    private final FileOutputStream logOutputStream;
    private final ByteBuffer headerBuf;

    public FramedTelemetryLogSink(FileDescriptor fd) throws IOException {
        this.logOutputStream = new FileOutputStream(fd);
        this.headerBuf = ByteBuffer.allocate(HEADER_LENGTH).order(ByteOrder.BIG_ENDIAN);
    }

    @Override
    public synchronized void log(byte[] message) {
        try {
            writeFrame(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFrame(byte[] message) throws IOException {
        updateHeader(message.length);
        this.logOutputStream.write(this.headerBuf.array());
        this.logOutputStream.write(message);
    }

    private long timestamp() {
        Instant instant = Instant.now();
        return instant.getEpochSecond() * 1_000_000 + instant.getNano() / 1000; // microsecond precision
    }

    /**
     * Updates the header ByteBuffer with the provided length. The header comprises the frame type and message length.
     */
    private void updateHeader(int length) {
        this.headerBuf.clear();
        this.headerBuf.putInt(FrameType.LOG.getValue());
        this.headerBuf.putInt(length);
        this.headerBuf.putLong(timestamp());
        this.headerBuf.flip();
    }

    @Override
    public void close() throws IOException {
        this.logOutputStream.close();
    }

}
