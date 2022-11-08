/* Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime;

import java.io.InputStream;
import java.io.OutputStream;

import java.lang.reflect.Type;

/**
 * Interface required to implement a custom plain old java objects serializer
 */
public interface CustomPojoSerializer {

    /**
     * Deserializes from input stream to plain old java object
     * @param input input stream
     * @param type plain old java object type
     * @return deserialized plain old java object of type T
     */
    <T> T fromJson(InputStream input, Type type);

    /**
     * Deserializes from String to plain old java object
     * @param input input string
     * @param type plain old java object type
     * @return deserialized plain old java object of type T
     */
    <T> T fromJson(String input, Type type);

    /**
     * Serializes plain old java object to output stream
     * @param value instance of type T to be serialized
     * @param output OutputStream to serialize plain old java object to
     * @param type plain old java object type
     */
    <T> void toJson(T value, OutputStream output, Type type);
}
