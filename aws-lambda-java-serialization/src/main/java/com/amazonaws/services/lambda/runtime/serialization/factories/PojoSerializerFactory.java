/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.factories;

import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;

import java.lang.reflect.Type;

public interface PojoSerializerFactory {
    <T> PojoSerializer<T> getSerializer(Class<T> clazz);
    PojoSerializer<Object> getSerializer(Type type);
}
