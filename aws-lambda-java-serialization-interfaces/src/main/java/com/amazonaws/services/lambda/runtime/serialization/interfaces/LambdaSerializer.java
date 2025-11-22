/* Copyright 2025 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.interfaces;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for serializing and deserializing objects in AWS Lambda functions.
 * Provides methods to convert between Java objects and serialized format.
 *
 * @param <T> the type of object to serialize/deserialize
 */
public interface LambdaSerializer<T> {
    /**
     * Deserializes an object from input stream.
     *
     * @param input the input stream
     * @return the deserialized object
     */
    T deserialize(InputStream input);
    
    /**
     * Deserializes an object from string.
     *
     * @param input the input string
     * @return the deserialized object
     */
    T deserialize(String input);
    
    /**
     * Serializes an object to output stream.
     *
     * @param value the object to serialize
     * @param output the output stream
     */
    void serialize(T value, OutputStream output);
}