/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.example.vehicles.serialization;

import com.amazonaws.services.lambda.runtime.CustomPojoSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GsonSerializer implements CustomPojoSerializer {
    private static final Charset utf8 = StandardCharsets.UTF_8;
    private static Gson gson;

    public GsonSerializer() {
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .serializeSpecialFloatingPointValues()
                .create();
    }

    @Override
    public <T> T fromJson(InputStream input, Type type) {
        try (JsonReader reader = new JsonReader(new InputStreamReader(input, utf8))) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public <T> T fromJson(String input, Type type) {
        try (JsonReader reader = new JsonReader(new StringReader(input))) {
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public <T> void toJson(T value, OutputStream output, Type type) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(output, utf8)))) {
            writer.write(gson.toJson(value));
        }
    }
}
