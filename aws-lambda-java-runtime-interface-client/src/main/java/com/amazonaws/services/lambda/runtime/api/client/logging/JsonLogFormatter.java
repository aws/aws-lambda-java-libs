/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client.logging;

import com.amazonaws.services.lambda.runtime.api.client.api.LambdaContext;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.factories.GsonFactory;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class JsonLogFormatter implements LogFormatter {
    private static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.
                ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").
                withZone(ZoneId.of("UTC"));
    private final PojoSerializer<StructuredLogMessage> serializer = GsonFactory.getInstance().getSerializer(StructuredLogMessage.class);

    private ThreadLocal<LambdaContext> lambdaContext = new ThreadLocal<>();

    @Override
    public String format(String message, LogLevel logLevel) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        StructuredLogMessage msg = createLogMessage(message, logLevel);
        serializer.toJson(msg, stream);
        stream.write('\n');
        return new String(stream.toByteArray(), StandardCharsets.UTF_8);
    }

    private StructuredLogMessage createLogMessage(String message, LogLevel logLevel) {
        StructuredLogMessage msg = new StructuredLogMessage();
        msg.timestamp = dateFormatter.format(LocalDateTime.now());
        msg.message = message;
        msg.level = logLevel;

        LambdaContext lambdaContextForCurrentThread = lambdaContext.get();
        if (lambdaContextForCurrentThread != null) {
            msg.AWSRequestId = lambdaContextForCurrentThread.getAwsRequestId();
            msg.tenantId = lambdaContextForCurrentThread.getTenantId();
        }

        return msg;
    }


    /**
     * Function to set the context for every invocation.
     * This way the logger will be able to attach additional information to the log packet.
     */
    @Override
    public void setLambdaContext(LambdaContext context) {
        if (context == null) {
            lambdaContext.remove();
        } else {
            lambdaContext.set(context);
        }
    }
}
