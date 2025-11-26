/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
package com.amazonaws.services.lambda.runtime.api.client.runtimeapi;

import com.amazonaws.services.lambda.runtime.api.client.GsonFactory;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.ErrorRequest;
import com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto.XRayErrorCause;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DtoSerializers {

    public static byte[] serialize(ErrorRequest error) {
        return serialize(error, SingletonHelper.LAMBDA_ERROR_SERIALIZER);
    }

    public static byte[] serialize(XRayErrorCause xRayErrorCause) {
        return serialize(xRayErrorCause, SingletonHelper.X_RAY_ERROR_CAUSE_SERIALIZER);
    }

    private static <T> byte[] serialize(T pojo, LambdaSerializer<T> serializer) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            serializer.serialize(pojo, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Implementation of
     * <a href="https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">Initialization-on-demand holder idiom</a>
     * This way the serializers will be loaded lazily
     */
    private static class SingletonHelper {
        private static final LambdaSerializer<ErrorRequest> LAMBDA_ERROR_SERIALIZER = GsonFactory.getInstance().getLambdaSerializer(ErrorRequest.class);
        private static final LambdaSerializer<XRayErrorCause> X_RAY_ERROR_CAUSE_SERIALIZER = GsonFactory.getInstance().getLambdaSerializer(XRayErrorCause.class);
    }
}
