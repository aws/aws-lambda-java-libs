/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Gets the backing array of a ByteArrayOutputStream without copying it. ByteArrayOutputStream.writeTo(out) is
 * specified to call out.write(buf, 0, count) with its own array, under the stream's lock, so this stream keeps that
 * reference instead of writing anywhere. The RIC's response buffers are plain ByteArrayOutputStreams
 * (EventHandlerLoaderTest checks it); a subclass that overrides writeTo to write differently makes the viewer throw.
 * A viewer is only made by of(), serves exactly one writeTo, and is closed afterwards.
 */
final class ResponseBufferViewer extends OutputStream {
    private byte[] array;
    private int length;
    private boolean closed;

    private ResponseBufferViewer() {
    }

    /** Runs payload.writeTo on a new viewer, then closes it. */
    static ResponseBufferViewer of(ByteArrayOutputStream payload) throws IOException {
        ResponseBufferViewer view = new ResponseBufferViewer();
        payload.writeTo(view);
        view.close();
        return view;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (closed || array != null || off != 0) {
            throw new IOException("ResponseBufferViewer takes a single write(buf, 0, count) from writeTo");
        }
        array = b;
        length = len;
    }

    @Override
    public void write(int b) throws IOException {
        throw new IOException("ResponseBufferViewer takes a single write(buf, 0, count) from writeTo");
    }

    @Override
    public void close() {
        closed = true;
    }

    /** The stream's backing array. Only the first length() bytes are the content. */
    byte[] array() {
        return array;
    }

    int length() {
        return length;
    }
}
