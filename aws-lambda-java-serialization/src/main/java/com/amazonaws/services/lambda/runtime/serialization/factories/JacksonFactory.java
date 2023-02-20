/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.factories;

import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public class JacksonFactory implements PojoSerializerFactory {

    private static final ObjectMapper globalMapper = createObjectMapper();
    
    private static final JacksonFactory instance = new JacksonFactory(globalMapper);

    public static JacksonFactory getInstance() {
        return instance;
    }

    private final ObjectMapper mapper;

    private JacksonFactory(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }


    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = JsonMapper.builder(createJsonFactory())
                .enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)                       // this is default as of 2.2.0
                .enable(MapperFeature.AUTO_DETECT_FIELDS)                                   // this is default as of 2.0.0
                .enable(MapperFeature.AUTO_DETECT_GETTERS)                                  // this is default as of 2.0.0
                .enable(MapperFeature.AUTO_DETECT_IS_GETTERS)                               // this is default as of 2.0.0
                .enable(MapperFeature.AUTO_DETECT_SETTERS)                                  // this is default as of 2.0.0
                .enable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)                        // this is default as of 2.0.0
                .enable(MapperFeature.USE_STD_BEAN_NAMING)
                .enable(MapperFeature.USE_ANNOTATIONS)                                      // this is default as of 2.0.0
                .disable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)                  // this is default as of 2.5.0
                .disable(MapperFeature.AUTO_DETECT_CREATORS)
                .disable(MapperFeature.INFER_PROPERTY_MUTATORS)
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)                      // this is default as of 2.0.0
                .disable(MapperFeature.USE_GETTERS_AS_SETTERS)
                .disable(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME)                   // this is default as of 2.1.0
                .disable(MapperFeature.USE_STATIC_TYPING)                                   // this is default as of 2.0.0
                .disable(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS)                         // this is default as of 2.0.0
                .build();

        SerializationConfig scfg = mapper.getSerializationConfig();
        scfg = scfg.withFeatures(
                SerializationFeature.FAIL_ON_SELF_REFERENCES,                               // this is default as of 2.4.0
                SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS,                    // this is default as of 2.4.0
                SerializationFeature.WRAP_EXCEPTIONS                                        // this is default as of 2.0.0
        );
        scfg = scfg.withoutFeatures(
                SerializationFeature.CLOSE_CLOSEABLE,                                       // this is default as of 2.0.0
                SerializationFeature.EAGER_SERIALIZER_FETCH,
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                SerializationFeature.FLUSH_AFTER_WRITE_VALUE,
                SerializationFeature.INDENT_OUTPUT,                                         // this is default as of 2.5.0
                SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,                             // this is default as of 2.0.0
                SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID,                            // this is default as of 2.3.0
                SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS,                      // this is default as of 2.0.0
                SerializationFeature.WRAP_ROOT_VALUE                                        // this is default as of 2.2.0
        );
        mapper.setConfig(scfg);

        DeserializationConfig dcfg = mapper.getDeserializationConfig();
        dcfg = dcfg.withFeatures(
                DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,
                DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                DeserializationFeature.FAIL_ON_INVALID_SUBTYPE,                             // this is default as of 2.2.0
                DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS,                       // this is default as of 2.5.0
                DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL,
                DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS,
                DeserializationFeature.WRAP_EXCEPTIONS                                      // this is default as of 2.0.0
        );
        dcfg = dcfg.withoutFeatures(
                DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,                          // this is default as of 2.3.0
                DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,                         // this is default as of 2.0.0
                DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS,                           // this is default as of 2.0.0
                DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY,                        // this is default as of 2.3.0
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
        );
        mapper.setConfig(dcfg);
        mapper.setSerializationInclusion(Include.NON_NULL);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Void.class, new VoidDeserializer());
        mapper.registerModule(module);

        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());

        return mapper;
    }

    public static final class VoidDeserializer extends JsonDeserializer<Void> {

        private final static Void VOID = createVoid();

        private static Void createVoid() {
            try {
                Constructor<Void> constructor = Void.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch(Exception e) {
                return null;
            }
        }

        @Override
        public Void deserialize(JsonParser parser, DeserializationContext ctx) {
            return VOID;
        }

    }

    private static JsonFactory createJsonFactory() {
        JsonFactory factory = JsonFactory.builder()
                //Json Read enabled
                .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
                .enable(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS)
                .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)

                //Json Read disabled
                .disable(JsonReadFeature.ALLOW_JAVA_COMMENTS)                               // this is default as of 2.10.0
                .disable(JsonReadFeature.ALLOW_YAML_COMMENTS)                               // this is default as of 2.10.0

                //Json Write enabled
                .enable(JsonWriteFeature.QUOTE_FIELD_NAMES)                                 // this is default as of 2.10.0
                .enable(JsonWriteFeature.WRITE_NAN_AS_STRINGS)                              // this is default as of 2.10.0

                //Json Write disabled
                .disable(JsonWriteFeature.ESCAPE_NON_ASCII)                                 // this is default as of 2.10.0
                .disable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS)                         // this is default as of 2.10.0
                .build();

        //Json Parser disabled
        factory.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        factory.disable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);

        //Json Generator enabled
        factory.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);

        //Json Generator disabled
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        factory.disable(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM);
        factory.disable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION);                  // this is default as of 2.3.0
        factory.disable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);                   // this is default as of 2.3.0

        return factory;
    }

    private static class InternalSerializer<T> implements PojoSerializer<T> {
        private final ObjectReader reader;
        private final ObjectWriter writer;
        public InternalSerializer(ObjectReader reader, ObjectWriter writer) {
            this.reader = reader;
            this.writer = writer;
        }

        @Override
        public T fromJson(InputStream input) {
            try {
                return reader.readValue(input);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public T fromJson(String input) {
            try {
                return reader.readValue(input);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public void toJson(T value, OutputStream output) {
            try {
                writer.writeValue(output, value);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private static final class TypeSerializer extends InternalSerializer<Object> {
        public TypeSerializer(ObjectMapper mapper, JavaType type) {
            super(mapper.readerFor(type), mapper.writerFor(type));
        }

        public TypeSerializer(ObjectMapper mapper, Type type) {
            this(mapper, mapper.constructType(type));
        }
    }

    private static final class ClassSerializer<T> extends InternalSerializer<T> {
        public ClassSerializer(ObjectMapper mapper, Class<T> clazz) {
            super(mapper.readerFor(clazz), mapper.writerFor(clazz));
        }
    }

    public <T> PojoSerializer<T> getSerializer(Class<T> clazz) {
        return new ClassSerializer<T>(this.mapper, clazz);
    }
    public PojoSerializer<Object> getSerializer(Type type) {
        return new TypeSerializer(this.mapper, type);
    }

    public JacksonFactory withNamingStrategy(PropertyNamingStrategy strategy) {
        return new JacksonFactory(this.mapper.copy().setPropertyNamingStrategy(strategy));
    }

    public JacksonFactory withMixin(Class<?> clazz, Class<?> mixin) {
        return new JacksonFactory(this.mapper.copy().addMixIn(clazz, mixin));
    }
    
}
