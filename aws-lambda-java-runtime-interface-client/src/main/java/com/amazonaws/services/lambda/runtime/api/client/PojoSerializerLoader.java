/* Copyright 2023 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.CustomPojoSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

public class PojoSerializerLoader {
    // The serializer obtained from the provider will always be the same so we can cache it as a filed.
    private static CustomPojoSerializer customPojoSerializer;
    // If Input and Output type are different, the runtime will try to search for a serializer twice due to
    // the getSerializerCached method. Save the initialization state in order to search for the provider only once.
    private static boolean initialized = false;

    private static CustomPojoSerializer loadSerializer()
            throws ServiceConfigurationError, TooManyServiceProvidersFoundException {

        if (customPojoSerializer != null) {
            return customPojoSerializer;
        }

        ServiceLoader<CustomPojoSerializer> loader = ServiceLoader.load(CustomPojoSerializer.class, AWSLambda.customerClassLoader);
        Iterator<CustomPojoSerializer> serializers = loader.iterator();

        if (!serializers.hasNext()) {
            initialized = true;
            return null;
        }

        customPojoSerializer = serializers.next();

        if (serializers.hasNext()) {
            throw new TooManyServiceProvidersFoundException(
                    "Too many serializers provided inside the META-INF/services folder, only one is allowed"
            );
        }

        initialized = true;
        return customPojoSerializer;
    }

    public static PojoSerializer<Object> getCustomerSerializer(Type type) {
        if (!initialized) {
            customPojoSerializer = loadSerializer();
        }

        if (customPojoSerializer == null) {
            return null;
        }

        return new PojoSerializer<Object>() {
            @Override
            public Object fromJson(InputStream input) {
                return customPojoSerializer.fromJson(input, type);
            }

            @Override
            public Object fromJson(String input) {
                return customPojoSerializer.fromJson(input, type);
            }

            @Override
            public void toJson(Object value, OutputStream output) {
                customPojoSerializer.toJson(value, output, type);
            }
        };
    }
}
