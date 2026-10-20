/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ResponseBufferViewerTest {

    /** Exposes the backing array so the test can check that the view holds the same instance. */
    private static final class InspectableByteArrayOutputStream extends ByteArrayOutputStream {
        InspectableByteArrayOutputStream(int size) {
            super(size);
        }

        byte[] backingArray() {
            return buf;
        }
    }

    @Test
    void capturesTheBackingArrayWithoutCopying() throws IOException {
        InspectableByteArrayOutputStream payload = new InspectableByteArrayOutputStream(64);
        byte[] content = "response".getBytes(StandardCharsets.UTF_8);
        payload.write(content);

        ResponseBufferViewer view = ResponseBufferViewer.of(payload);

        assertSame(payload.backingArray(), view.array());
        assertEquals(content.length, view.length());
        assertArrayEquals(content, Arrays.copyOf(view.array(), view.length()));
    }

    @Test
    void capturesAnEmptyStream() throws IOException {
        InspectableByteArrayOutputStream payload = new InspectableByteArrayOutputStream(16);

        ResponseBufferViewer view = ResponseBufferViewer.of(payload);

        assertSame(payload.backingArray(), view.array());
        assertEquals(0, view.length());
    }

    @Test
    void capturesTheCurrentArrayAfterTheStreamGrew() throws IOException {
        InspectableByteArrayOutputStream payload = new InspectableByteArrayOutputStream(4);
        payload.write(new byte[1000]);

        ResponseBufferViewer view = ResponseBufferViewer.of(payload);

        assertSame(payload.backingArray(), view.array());
        assertEquals(1000, view.length());
    }

    @Test
    void throwsWhenWriteToWritesSingleBytes() throws IOException {
        ByteArrayOutputStream payload = new ByteArrayOutputStream() {
            @Override
            public synchronized void writeTo(OutputStream out) throws IOException {
                out.write('a');
            }
        };

        assertThrows(IOException.class, () -> ResponseBufferViewer.of(payload));
    }

    @Test
    void throwsWhenWriteToWritesMoreThanOneArray() throws IOException {
        ByteArrayOutputStream payload = new ByteArrayOutputStream() {
            @Override
            public synchronized void writeTo(OutputStream out) throws IOException {
                out.write(new byte[] {'a'}, 0, 1);
                out.write(new byte[] {'b'}, 0, 1);
            }
        };

        assertThrows(IOException.class, () -> ResponseBufferViewer.of(payload));
    }

    @Test
    void throwsWhenWriteToWritesFromAnOffset() throws IOException {
        ByteArrayOutputStream payload = new ByteArrayOutputStream() {
            @Override
            public synchronized void writeTo(OutputStream out) throws IOException {
                out.write(new byte[] {'a', 'b'}, 1, 1);
            }
        };

        assertThrows(IOException.class, () -> ResponseBufferViewer.of(payload));
    }

    @Test
    void isClosedAfterItsWriteTo() throws IOException {
        InspectableByteArrayOutputStream payload = new InspectableByteArrayOutputStream(16);
        payload.write("response".getBytes(StandardCharsets.UTF_8));
        ResponseBufferViewer view = ResponseBufferViewer.of(payload);

        assertThrows(IOException.class, () -> payload.writeTo(view));
        assertThrows(IOException.class, () -> view.write('a'));

        // The rejected writes did not replace what the viewer holds.
        assertSame(payload.backingArray(), view.array());
        assertEquals(8, view.length());
    }
}
