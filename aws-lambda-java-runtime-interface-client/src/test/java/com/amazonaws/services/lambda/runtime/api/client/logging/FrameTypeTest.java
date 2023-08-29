package com.amazonaws.services.lambda.runtime.api.client.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.amazonaws.services.lambda.runtime.logging.LogFormat;

public class FrameTypeTest {

    @Test
    public void logFrames() {
        assertHexEquals(
                0xa55a0003,
                FrameType.getValue(LogLevel.UNDEFINED, LogFormat.TEXT)
        );

        assertHexEquals(
                0xa55a001b,
                FrameType.getValue(LogLevel.FATAL, LogFormat.TEXT)
        );
    }


    /**
     * Helper function to make it easier to debug failing test.
     *
     * @param expected Expected value as int
     * @param actual   Actual value as int
     */
    private void assertHexEquals(int expected, int actual) {
        assertEquals(
                Integer.toHexString(expected),
                Integer.toHexString(actual)
        );
    }

}
