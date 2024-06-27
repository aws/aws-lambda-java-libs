/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.util;

import java.io.IOException;
import java.io.OutputStream;

public class LambdaOutputStream extends OutputStream {
    private final OutputStream inner;

    public LambdaOutputStream(OutputStream inner) {
        this.inner = inner;
    }

    @Override
    public void write(int b) throws IOException {
        write(new byte[]{(byte) b});
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        write(bytes, 0, bytes.length);

    }

    @Override
    public void write(byte[] bytes, int offset, int length) throws IOException {
        inner.write(bytes, offset, length);
    }
}
