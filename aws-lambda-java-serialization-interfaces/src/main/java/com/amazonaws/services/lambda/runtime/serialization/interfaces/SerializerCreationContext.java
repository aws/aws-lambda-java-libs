/* Copyright 2025 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.interfaces;

import java.util.Optional;

/**
 * Context interface for passing custom metadata to serializer factory implementations.
 *
 * <p>This allows customers to implement custom logic for selecting different serializers
 * based on runtime conditions, configuration, or request-specific data.
 *
 * @since 1.0
 */
public interface SerializerCreationContext {

    /**
     * Retrieves a context attribute by key.
     *
     * @param key the attribute key; must not be null
     * @return an Optional containing the attribute value if present
     * @throws IllegalArgumentException if key is null
     */
    Optional<Object> getAttribute(String key);

    /**
     * Retrieves a context attribute by key with type casting.
     *
     * @param <T> the expected type of the attribute
     * @param key the attribute key; must not be null
     * @param type the expected class of the attribute
     * @return an Optional containing the typed attribute value if present and of correct type
     * @throws IllegalArgumentException if key or type is null
     */
    default <T> Optional<T> getAttribute(String key, Class<T> type) {
        return getAttribute(key)
                .filter(type::isInstance)
                .map(type::cast);
    }
}