/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializerFactory;
import java.util.Iterator;
import java.util.ServiceLoader;

public class LambdaSerializerFactoryLoader {
    private static LambdaSerializerFactory factory;
    private static boolean initialized = false;

    public static LambdaSerializerFactory load() {
        if (!initialized) {
            factory = loadFactory();
            initialized = true;
        }

        return factory;
    }

    private static LambdaSerializerFactory loadFactory() {
        Iterator<LambdaSerializerFactory> serializers = ServiceLoader.load(LambdaSerializerFactory.class, AWSLambda.customerClassLoader).iterator();

        if (!serializers.hasNext()) {
            return null;
        }

        LambdaSerializerFactory factory = serializers.next();

        if (serializers.hasNext()) {
            throw new TooManyServiceProvidersFoundException(
                    "Too many serializers provided inside the META-INF/services folder, only one is allowed"
            );
        }

        return factory;
    }
}
