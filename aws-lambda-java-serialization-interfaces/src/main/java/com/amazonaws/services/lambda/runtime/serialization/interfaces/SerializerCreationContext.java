/* Copyright 2025 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.interfaces;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Object for passing custom metadata to serializer factory implementations.
 *
 * <p>This allows customers to implement custom logic for selecting different serializers
 * based on runtime conditions, configuration, or request-specific data.
 *
 * @since 1.0
 */
public class SerializerCreationContext {

    private final Map<String, Object> attributes;

    /**
     * Creates a new SerializerCreationContext with an empty attribute map.
     */
    public SerializerCreationContext() {
        this.attributes = new HashMap<>();
    }

    /**
     * Creates a new SerializerCreationContext with the provided attributes.
     *
     * @param attributes the initial attributes map
     */
    public SerializerCreationContext(Map<String, Object> attributes) {
        this.attributes = new HashMap<>(attributes);
    }

    /**
     * Sets a context attribute.
     *
     * @param key the attribute key; must not be null
     * @param value the attribute value
     * @throws IllegalArgumentException if key is null
     */
    public void setAttribute(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Attribute key must not be null");
        }
        attributes.put(key, value);
    }

    /**
     * Retrieves a context attribute by key.
     *
     * @param key the attribute key; must not be null
     * @return an Optional containing the attribute value if present
     * @throws IllegalArgumentException if key is null
     */
    public Optional<Object> getAttribute(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Attribute key must not be null");
        }
        return Optional.ofNullable(attributes.get(key));
    }

    /**
     * Retrieves a context attribute by key with type casting.
     *
     * @param <T> the expected type of the attribute
     * @param key the attribute key; must not be null
     * @param type the expected class of the attribute
     * @return an Optional containing the typed attribute value if present and of correct type
     * @throws IllegalArgumentException if key or type is null
     */
    public <T> Optional<T> getAttribute(String key, Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Attribute type must not be null");
        }
        return getAttribute(key)
                .filter(type::isInstance)
                .map(type::cast);
    }
}