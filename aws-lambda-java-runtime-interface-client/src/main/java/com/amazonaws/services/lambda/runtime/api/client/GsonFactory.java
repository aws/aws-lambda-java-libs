/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializer;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializerFactory;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.SerializerCreationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class GsonFactory implements LambdaSerializerFactory {
    private static final Charset utf8 = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().serializeSpecialFloatingPointValues().create();
        
    private static final GsonFactory instance = new GsonFactory();
    private GsonFactory() { }
    public static GsonFactory getInstance() {
        return instance;
    }

    

    @Override
    public <T> LambdaSerializer<T> getLambdaSerializer(Class<T> clazz, SerializerCreationContext context) {
        return InternalSerializer.create(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LambdaSerializer<T> getLambdaSerializer(Type type, SerializerCreationContext context) {
        return (LambdaSerializer<T>) InternalSerializer.create(type);
    }

    private static class InternalSerializer<T> implements LambdaSerializer<T> {
        private final TypeAdapter<T> adapter;

        public InternalSerializer(TypeAdapter<T> adapter) {
            this.adapter = adapter.nullSafe();
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public static <T> InternalSerializer<T> create(TypeToken<T> token) {
            if (Void.TYPE.equals(token.getRawType())) {
                return new InternalSerializer(gson.getAdapter(Object.class));
            } else {
                return new InternalSerializer<T>(gson.getAdapter(token));
            }
        }

        public static <T> InternalSerializer<T> create(Class<T> clazz) {
            return create(TypeToken.get(clazz));
        }

        @SuppressWarnings("unchecked") 
        public static <T> InternalSerializer<Object> create(Type type) {
            return create((TypeToken<Object>) TypeToken.get(type));
        }

        private T fromJson(JsonReader reader) {
            reader.setLenient(true);
            try {
                try {
                    reader.peek();
                } catch (EOFException e) {
                    return null;
                }
                return adapter.read(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public T deserialize(InputStream input) {
            try (JsonReader reader = new JsonReader(new InputStreamReader(input, utf8))) {
                return fromJson(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public T deserialize(String input) {
            try (JsonReader reader = new JsonReader(new StringReader(input))) {
                return fromJson(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public void serialize(T value, OutputStream output) {
            try {
                try (JsonWriter writer = new JsonWriter(new OutputStreamWriter((output), utf8))) {
                    writer.setLenient(true);
                    writer.setSerializeNulls(false);
                    writer.setHtmlSafe(false);
                    adapter.write(writer, value);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
