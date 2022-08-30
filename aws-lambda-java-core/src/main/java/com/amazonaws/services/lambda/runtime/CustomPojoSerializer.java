/* Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime;

import java.io.InputStream;
import java.io.OutputStream;

import java.lang.reflect.Type;

public interface CustomPojoSerializer {
    <T> T fromJson(InputStream input, Type type);
    <T> T fromJson(String input, Type type);
    <T> void toJson(T value, OutputStream output, Type type);
} 