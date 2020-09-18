/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.serializers;

import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for event serializers that use org json
 */
public interface OrgJsonSerializer<T> extends PojoSerializer<T> {

    /**
     * @param eventClass event class object
     * @return OrgJsonSerializer with event type
     */
    OrgJsonSerializer<T> withClass(Class<T> eventClass);

    /**
     * @param classLoader to use if the implementation needs to load any classes
     * @return OrgJsonSerializer with the supplied classLoader
     */
    OrgJsonSerializer<T> withClassLoader(ClassLoader classLoader);

    /**
     * defined in PojoSerializer
     * @param input input stream
     * @return deserialized object of type T
     */
    T fromJson(InputStream input);

    /**
     * defined in PojoSerializer
     * @param input String input
     * @return deserialized object of type T
     */
    T fromJson(String input);

    /**
     * defined in PojoSerializer
     * @param value instance of type T to be serialized
     * @param output OutputStream to serialize object to
     */
    void toJson(T value, OutputStream output);
}
