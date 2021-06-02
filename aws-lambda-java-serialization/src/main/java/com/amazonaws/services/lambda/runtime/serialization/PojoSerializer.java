/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization;

import java.io.InputStream;
import java.io.OutputStream;

public interface PojoSerializer<T> {
    T fromJson(InputStream input);
    T fromJson(String input);
    void toJson(T value, OutputStream output);
}
