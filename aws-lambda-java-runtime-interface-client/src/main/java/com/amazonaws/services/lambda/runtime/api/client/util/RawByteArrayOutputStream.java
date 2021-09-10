package com.amazonaws.services.lambda.runtime.api.client.util;

import java.io.ByteArrayOutputStream;

public class RawByteArrayOutputStream extends ByteArrayOutputStream {
    public RawByteArrayOutputStream() { super(); }
    public RawByteArrayOutputStream(int size) { super(size); }

    public byte[] getRawByteArray() {
        return this.buf; // avoids toByteArray's copy
    }
}