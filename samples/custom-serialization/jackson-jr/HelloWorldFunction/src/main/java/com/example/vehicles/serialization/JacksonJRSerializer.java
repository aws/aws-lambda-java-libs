/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.example.vehicles.serialization;

import com.amazonaws.services.lambda.runtime.CustomPojoSerializer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.jr.annotationsupport.JacksonAnnotationExtension;
import com.fasterxml.jackson.jr.ob.JSON;
import com.fasterxml.jackson.jr.ob.JSON.Feature;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;

public class JacksonJRSerializer implements CustomPojoSerializer {

    private static final JSON globalJson = createJson();

    private static final JacksonJRSerializer instance = new JacksonJRSerializer(globalJson);

    private final JSON json;

    private JacksonJRSerializer(JSON json) {
        this.json = json;
    }

    /**
     * ServiceLoader class requires that the single exposed provider type has a default constructor
     * to easily instantiate the service providers that it finds
     */
    public JacksonJRSerializer() {
        this.json = globalJson;
    }

    public static JacksonJRSerializer getInstance() {
        return instance;
    }

    public JSON getJson() {
        return json;
    }

    private static JSON createJson() {
        JSON json = JSON.builder(createJsonFactory())
            .register(JacksonAnnotationExtension.std)
            .build();

        json.with(Feature.FLUSH_AFTER_WRITE_VALUE, false);

        return json;
    }

    private static JsonFactory createJsonFactory() {
        return JsonFactory.builder().build();
    }
    
    @Override
    public <T> T fromJson(InputStream input, Type type) {
        try {
            return json.beanFrom((Class<T>) type, input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public <T> T fromJson(String input, Type type) {
        try {
            return json.beanFrom((Class<T>) type, input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public <T> void toJson(T value, OutputStream output, Type type) {
        try {
            json.write(value, output);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
