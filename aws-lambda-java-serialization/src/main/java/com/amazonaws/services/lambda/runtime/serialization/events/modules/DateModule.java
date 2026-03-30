/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.modules;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * The AWS API represents a date as a double (fractional seconds since epoch).
 * Java's Date uses a long (milliseconds since epoch). This module translates
 * between the two formats.
 *
 * <p>
 * <b>Round-trip caveats:</b> The serializer always writes via
 * {@link JsonGenerator#writeNumber(double)}, so integer epochs
 * (e.g. {@code 1428537600}) round-trip as decimal ({@code 1.4285376E9}).
 * Sub-millisecond precision is lost because {@link java.util.Date}
 * has milliseconds precision.
 * </p>
 * 
 * This class is copied from LambdaEventBridgeservice
 * com.amazon.aws.lambda.stream.ddb.DateModule
 */
public class DateModule extends SimpleModule {
    private static final long serialVersionUID = 1L;

    public static final class Serializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator generator, SerializerProvider serializers) throws IOException {
            if (date != null) {
                generator.writeNumber(millisToSeconds(date.getTime()));
            }
        }
    }

    public static final class Deserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            double dateSeconds = parser.getValueAsDouble();
            if (dateSeconds == 0.0) {
                return null;
            } else {
                return new Date((long) secondsToMillis(dateSeconds));
            }
        }
    }

    private static double millisToSeconds(double millis) {
        return millis / 1000.0;
    }

    private static double secondsToMillis(double seconds) {
        return seconds * 1000.0;
    }

    public DateModule() {
        super(PackageVersion.VERSION);
        addSerializer(Date.class, new Serializer());
        addDeserializer(Date.class, new Deserializer());
    }
}
