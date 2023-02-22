/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.events.modules;

import com.amazonaws.services.lambda.runtime.serialization.util.SerializeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.io.IOException;

/**
 * Class that is used to load customer DateTime class
 */
public class DateTimeModule extends JodaModule {

    /**
     * creates a DateTimeModule using customer class loader to pull org.joda.time.DateTime
     */
    public DateTimeModule(ClassLoader classLoader) {
        Class dateTimeClass = SerializeUtil.loadCustomerClass("org.joda.time.DateTime", classLoader);
        this.addSerializer(dateTimeClass, getSerializer(dateTimeClass, classLoader));
        this.addDeserializer(dateTimeClass, getDeserializer(dateTimeClass));
    }

    /**
     * @param <T> refers to type org.joda.time.DateTime
     * @param dateTimeClass org.joda.time.DateTime class of the customer
     * @param classLoader classLoader that's used to load any DateTime classes
     * @return JsonSerializer with generic DateTime
     */
    private <T> JsonSerializer<T> getSerializer(Class<T> dateTimeClass, ClassLoader classLoader) {
        return new JsonSerializer<T>() {

            /**
             * @param dateTime customer DateTime class
             * @param jsonGenerator json generator
             * @param serializerProvider serializer provider
             * @throws IOException when unable to write
             * @throws JsonProcessingException when unable to parse
             */
            @Override
            public void serialize(T dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException, JsonProcessingException {
                jsonGenerator.writeString(SerializeUtil.serializeDateTime(dateTime, classLoader));
            }
        };
    }

    /**
     * @param dateTimeClass org.joda.time.DateTime class of the customer
     * @param <T> refers to type org.joda.time.DateTime
     * @return JsonDeserializer with generic DateTime
     */
    private <T> JsonDeserializer<T> getDeserializer(Class<T> dateTimeClass) {
        return new JsonDeserializer<T>() {

            /**
             * @param jsonParser json parser
             * @param deserializationContext deserialization context
             * @return DateTime instance
             * @throws IOException error when reading
             * @throws JsonProcessingException error when processing json
             */
            @Override
            public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException, JsonProcessingException {
                return SerializeUtil.deserializeDateTime(dateTimeClass, jsonParser.getValueAsString());
            }
        };
    }
}
