/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LambdaOutputStreamTest {

    @Mock
    private OutputStream mockInnerStream;

    private LambdaOutputStream lambdaOutputStream;

    @BeforeEach
    void setUp() {
        lambdaOutputStream = new LambdaOutputStream(mockInnerStream);
    }

    @Test
    void writeSingleByte() throws IOException {
        int testByte = 65; // 'A'
        lambdaOutputStream.write(testByte);
        verify(mockInnerStream).write(new byte[]{(byte) testByte}, 0, 1);
    }

    @Test
    void writeByteArray() throws IOException {
        byte[] testBytes = "test".getBytes();
        lambdaOutputStream.write(testBytes);
        verify(mockInnerStream).write(testBytes, 0, testBytes.length);
    }

    @Test
    void writeOffsetLength() throws IOException {
        byte[] testBytes = "test".getBytes();
        int offset = 1;
        int length = 2;
        lambdaOutputStream.write(testBytes, offset, length);
        verify(mockInnerStream).write(testBytes, offset, length);
    }

    @Test
    void throwWriteSingleByte() throws IOException {
        doThrow(new IOException("Test exception"))
            .when(mockInnerStream)
            .write(any(byte[].class), anyInt(), anyInt());
        assertThrows(IOException.class, () -> lambdaOutputStream.write(65));
    }

    @Test
    void throwWriteByteArray() throws IOException {
        byte[] testBytes = "test".getBytes();
        doThrow(new IOException("Test exception"))
            .when(mockInnerStream)
            .write(any(byte[].class), anyInt(), anyInt());
        assertThrows(IOException.class, () -> lambdaOutputStream.write(testBytes));
    }

    @Test
    void throwWriteOffsetLength() throws IOException {
        byte[] testBytes = "test".getBytes();
        doThrow(new IOException("Test exception"))
            .when(mockInnerStream)
            .write(any(byte[].class), anyInt(), anyInt());
        assertThrows(IOException.class, () -> lambdaOutputStream.write(testBytes, 1, 2));
    }
}
