/* Copyright 2025 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.interfaces;

import com.amazonaws.services.lambda.runtime.serialization.interfaces.exceptions.SerializerCreationException;

import java.lang.reflect.Type;

/**
 * Factory interface for creating type-safe LambdaSerializer instances.
 *
 * <p>Implementations of this interface should be thread-safe and may cache
 * serializer instances for performance optimization.
 *
 * @since 1.0
 */
public interface LambdaSerializerFactory {

    /**
     * Creates or retrieves a serializer for the specified class type.
     *
     * @param <T> the type to serialize
     * @param clazz the class of the type to serialize; must not be null
     * @return a serializer for the specified class, never null
     * @throws IllegalArgumentException if clazz is null
     * @throws SerializerCreationException if a serializer cannot be created for the type
     */
    default <T> LambdaSerializer<T> getSerializer(Class<T> clazz) {
        return getSerializer(clazz, null);
    }

    /**
     * Creates or retrieves a serializer for the specified class type with custom context.
     *
     * <p>The context allows implementations to customize serializer selection based on
     * runtime information such as request metadata, configuration, or feature flags.
     *
     * @param <T> the type to serialize
     * @param clazz the class of the type to serialize; must not be null
     * @param context optional context for custom serializer selection logic; may be null
     * @return a serializer for the specified class, never null
     * @throws IllegalArgumentException if clazz is null
     * @throws SerializerCreationException if a serializer cannot be created for the type
     */
    <T> LambdaSerializer<T> getSerializer(Class<T> clazz, SerializerCreationContext context);

    /**
     * Creates or retrieves a serializer for the specified generic type.
     *
     * <p>Use this method for parameterized types such as {@code List<String>}.
     *
     * @param <T> the type to serialize
     * @param type the generic type to serialize; must not be null
     * @return a serializer for the specified type, never null
     * @throws IllegalArgumentException if type is null
     * @throws SerializerCreationException if a serializer cannot be created for the type
     */
    default <T> LambdaSerializer<T> getSerializer(Type type) {
        return getSerializer(type, null);
    }

    /**
     * Creates or retrieves a serializer for the specified generic type with custom context.
     *
     * <p>Use this method for parameterized types such as {@code List<String>}.
     * The context allows implementations to customize serializer selection based on
     * runtime information such as request metadata, configuration, or feature flags.
     *
     * @param <T> the type to serialize
     * @param type the generic type to serialize; must not be null
     * @param context optional context for custom serializer selection logic; may be null
     * @return a serializer for the specified type, never null
     * @throws IllegalArgumentException if type is null
     * @throws SerializerCreationException if a serializer cannot be created for the type
     */
    <T> LambdaSerializer<T> getSerializer(Type type, SerializerCreationContext context);
}