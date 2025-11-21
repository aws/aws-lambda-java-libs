/* Copyright 2025 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.interfaces.factories;

import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializer;

import java.lang.reflect.Type;

/**
 * Factory interface for creating LambdaSerializer instances.
 * Provides methods to obtain serializers for specific types.
 */
public interface LambdaSerializerFactory {
    /**
     * Creates a serializer for the specified class type.
     *
     * @param <T> the type to serialize
     * @param clazz the class of the type to serialize
     * @return a serializer for the specified class
     */
    <T> LambdaSerializer<T> getSerializer(Class<T> clazz);
    
    /**
     * Creates a serializer for the specified generic type.
     *
     * @param type the generic type to serialize
     * @return a serializer for the specified type
     */
    LambdaSerializer<Object> getSerializer(Type type);
}