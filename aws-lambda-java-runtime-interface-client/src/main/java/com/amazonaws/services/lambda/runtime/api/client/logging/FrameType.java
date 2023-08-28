/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.amazonaws.services.lambda.runtime.logging.LogFormat;

/**
 * The first 4 bytes of the framing protocol is the Frame Type, that's made of a magic number (3 bytes) and 1 byte of flags.
 * +-----------------------+
 * | Frame Type - 4 bytes  |
 * +-----------------------+
 * | a5   5a    00  | flgs |
 * + - - - - - + - - - - - +
 *                  \  bit |
 *                   | view|
 *         +---------+     +
 *         |               |
 *         v     byte 3    v  F - free
 *         +-+-+-+-+-+-+-+-+  J - { JsonLog = 0, PlainTextLog = 1 }
 *         |F|F|F|L|l|l|T|J|  T - { NoTimeStamp = 0, TimeStampPresent = 1 }
 *         +-+-+-+-+-+-+-+-+  Lll -> Log Level in 3-bit binary (L-> most significant bit)
 */
public class FrameType {
    private static final int LOG_MAGIC = 0xa55a0000;
    private static final int OFFSET_LOG_FORMAT = 0;
    private static final int OFFSET_TIMESTAMP_PRESENT = 1;
    private static final int OFFSET_LOG_LEVEL = 2;

    private final int val;

    public static int getValue(LogLevel logLevel, LogFormat logFormat) {
        return LOG_MAGIC |
                (logLevel.ordinal() << OFFSET_LOG_LEVEL) |
                (1 << OFFSET_TIMESTAMP_PRESENT) |
                (logFormat.ordinal() << OFFSET_LOG_FORMAT);
    }

    FrameType(int val) {
        this.val = val;
    }

    public int getValue() {
        return this.val;
    }
}
