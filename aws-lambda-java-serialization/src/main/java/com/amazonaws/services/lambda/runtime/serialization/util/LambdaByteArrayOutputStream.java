/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * java.io.ByteArrayOutputStream that gives raw access to underlying byte array
 */
final class LambdaByteArrayOutputStream extends ByteArrayOutputStream {

    public LambdaByteArrayOutputStream(int size) {
        super(size);
    }

    public byte[] getRawBuf() {
        return super.buf;
    }

    public int getValidByteCount() {
        return super.count;
    }

    public void readAll(InputStream input) throws IOException {
        while(true) {
            int numToRead = Math.max(input.available(), 1024);
            ensureSpaceAvailable(numToRead);
            int rc = input.read(this.buf, this.count, numToRead);
            if(rc < 0) {
                break;
            } else {
                this.count += rc;
            }
        }
    }

    private void ensureSpaceAvailable(int space) {
        if(space <= 0) {
            return;
        }
        int remaining = count - buf.length;
        if(remaining < space) {
            int newSize = buf.length * 2;
            if(newSize < buf.length) {
                newSize = Integer.MAX_VALUE;
            }
            byte[] newBuf = new byte[newSize];
            System.arraycopy(this.buf, 0, newBuf, 0, count);
            this.buf = newBuf;
        }
    }
}
