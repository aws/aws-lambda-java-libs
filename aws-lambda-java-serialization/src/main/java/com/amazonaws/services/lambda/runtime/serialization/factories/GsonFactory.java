/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.factories;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.EOFException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;

import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GsonFactory implements PojoSerializerFactory {
    private static final Charset utf8 = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder()
        .disableHtmlEscaping()
        .serializeSpecialFloatingPointValues()
        .create();
        
    private static final GsonFactory instance = new GsonFactory();

    public static GsonFactory getInstance() {
        return instance;
    }

    private GsonFactory() {
    }

    private static class InternalSerializer<T> implements PojoSerializer<T> {
        private final TypeAdapter<T> adapter;

        public InternalSerializer(TypeAdapter<T> adapter) {
            this.adapter = adapter.nullSafe();
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public static <T> InternalSerializer<T> create(TypeToken<T> token) {
            if(Void.TYPE.equals(token.getRawType())) {
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
            return create((TypeToken<Object>)TypeToken.get(type));
        }

        private T fromJson(JsonReader reader) {
            reader.setLenient(true);
            try {
                try {
                    reader.peek();
                } catch(EOFException e) {
                    return null;
                }
                return adapter.read(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }


        @Override
        public T fromJson(InputStream input) {
            try(JsonReader reader = new JsonReader(new InputStreamReader(input, utf8))) {
                return fromJson(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public T fromJson(String input) {
            try(JsonReader reader = new JsonReader(new StringReader(input))) {
                return fromJson(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public void toJson(T value, OutputStream output) {
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

    
    @Override
    public <T> PojoSerializer<T> getSerializer(Class<T> clazz) {
        return InternalSerializer.create(clazz);
    }

    @Override
    public PojoSerializer<Object> getSerializer(Type type) {
        return InternalSerializer.create(type);
    }
}
