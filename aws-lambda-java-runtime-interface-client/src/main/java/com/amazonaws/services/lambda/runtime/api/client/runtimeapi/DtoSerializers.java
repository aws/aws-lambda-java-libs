/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.LambdaError;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayErrorCause;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.factories.GsonFactory;
import com.amazonaws.services.lambda.runtime.serialization.factories.JacksonFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DtoSerializers {
    /**
     * Implementation of
     * <a href="https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">Initialization-on-demand holder idiom</a>
     * This way the serializers will be loaded lazily
     */
    private static class SingletonHelper {
        private static final PojoSerializer<LambdaError> LAMBDA_ERROR_SERIALIZER = GsonFactory.getInstance().getSerializer(LambdaError.class);
        private static final PojoSerializer<XRayErrorCause> X_RAY_ERROR_CAUSE_SERIALIZER = GsonFactory.getInstance().getSerializer(XRayErrorCause.class);
    }

    public static byte[] serialize(LambdaError error) {
        return serialize(error, SingletonHelper.LAMBDA_ERROR_SERIALIZER);
    }

    public static byte[] serialize(XRayErrorCause xRayErrorCause) {
        return serialize(xRayErrorCause, SingletonHelper.X_RAY_ERROR_CAUSE_SERIALIZER);
    }

    private static <T> byte[] serialize(T pojo, PojoSerializer<T> serializer) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            serializer.toJson(pojo, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
}
