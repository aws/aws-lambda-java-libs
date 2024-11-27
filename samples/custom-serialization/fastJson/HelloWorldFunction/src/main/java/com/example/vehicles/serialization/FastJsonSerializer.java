/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.example.vehicles.serialization;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.amazonaws.services.lambda.runtime.CustomPojoSerializer;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

public class FastJsonSerializer implements CustomPojoSerializer {
    /**
     * ServiceLoader class requires that the single exposed provider type has a default constructor
     * to easily instantiate the service providers that it finds
     */
    public FastJsonSerializer() {
    }

    @Override
    public <T> T fromJson(InputStream input, Type type) {
        try {
            return JSON.parseObject(input, type);
        } catch (JSONException e) {
            throw (e);
        }
    }

    @Override
    public <T> T fromJson(String input, Type type) {
        try {
            return JSON.parseObject(input, type);
        } catch (JSONException e) {
            throw (e);
        }
    }

    @Override
    public <T> void toJson(T value, OutputStream output, Type type) {
        try {
            JSON.writeTo(output, value);
        } catch (JSONException e) {
            throw (e);
        }
    }

}
