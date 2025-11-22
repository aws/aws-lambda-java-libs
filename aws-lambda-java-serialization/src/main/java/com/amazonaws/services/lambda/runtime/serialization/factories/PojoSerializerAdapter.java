/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.factories;

import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializer;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Adapter class to bridge between PojoSerializer and LambdaSerializer interfaces.
 * Package-private for internal use within the serialization package.
 */
class PojoSerializerAdapter<T> implements LambdaSerializer<T> {
    private final PojoSerializer<T> pojoSerializer;

    public PojoSerializerAdapter(PojoSerializer<T> pojoSerializer) {
        this.pojoSerializer = pojoSerializer;
    }

    @Override
    public T deserialize(InputStream input) {
        return pojoSerializer.fromJson(input);
    }

    @Override
    public T deserialize(String input) {
        return pojoSerializer.fromJson(input);
    }

    @Override
    public void serialize(T value, OutputStream output) {
        pojoSerializer.toJson(value, output);
    }
}