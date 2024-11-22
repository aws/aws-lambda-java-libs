/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.example.vehicles.serialization;

import com.amazonaws.services.lambda.runtime.CustomPojoSerializer;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import okio.BufferedSink;
import okio.Okio;

public class MoshiSerializer implements CustomPojoSerializer {

    private static final Moshi globalMoshi = createMoshi();

    private final Moshi moshi;

    /**
     * ServiceLoader class requires that the single exposed provider type has a
     * default constructor
     * to easily instantiate the service providers that it finds
     */
    public MoshiSerializer() {
        this.moshi = globalMoshi;
    }

    private static Moshi createMoshi() {
        return new Moshi.Builder().build();
    }

    @Override
    public <T> T fromJson(InputStream input, Type type) {
        JsonAdapter<T> jsonAdapter = moshi.adapter(type);
        try {
            return jsonAdapter.fromJson(Okio.buffer(Okio.source(input)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public <T> T fromJson(String input, Type type) {
        JsonAdapter<T> jsonAdapter = moshi.adapter(type);
        try {
            return jsonAdapter.fromJson(input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public <T> void toJson(T value, OutputStream output, Type type) {
        JsonAdapter<T> jsonAdapter = moshi.adapter(type);
        BufferedSink out = Okio.buffer(Okio.sink(output));
        try {
            jsonAdapter.toJson(out, value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
