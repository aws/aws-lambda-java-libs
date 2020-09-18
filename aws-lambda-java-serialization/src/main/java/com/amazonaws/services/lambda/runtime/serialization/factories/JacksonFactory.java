/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.factories;

import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
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
import com.fasterxml.jackson.databind.module.SimpleModule;

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
        ObjectMapper mapper = new ObjectMapper(createJsonFactory());
        SerializationConfig scfg = mapper.getSerializationConfig();
        scfg = scfg.withFeatures(
                SerializationFeature.FAIL_ON_SELF_REFERENCES,
                SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS,
                SerializationFeature.WRAP_EXCEPTIONS
                );
        scfg = scfg.withoutFeatures(
                SerializationFeature.CLOSE_CLOSEABLE, 
                SerializationFeature.EAGER_SERIALIZER_FETCH,
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                SerializationFeature.FLUSH_AFTER_WRITE_VALUE,
                SerializationFeature.INDENT_OUTPUT,
                SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,
                SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID,
                SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS,
                SerializationFeature.WRAP_ROOT_VALUE
                );
        mapper.setConfig(scfg);

        DeserializationConfig dcfg = mapper.getDeserializationConfig();
        dcfg = dcfg.withFeatures(
                DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,
                DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                DeserializationFeature.FAIL_ON_INVALID_SUBTYPE,
                DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS,
                DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL,
                DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS,
                DeserializationFeature.WRAP_EXCEPTIONS
                );
        dcfg = dcfg.withoutFeatures(
                DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
                DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS,
                DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY,
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
                );
        mapper.setConfig(dcfg);
        mapper.setSerializationInclusion(Include.NON_NULL); //NON_EMPTY?

        mapper.enable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS);
        mapper.enable(MapperFeature.AUTO_DETECT_FIELDS);
        mapper.enable(MapperFeature.AUTO_DETECT_GETTERS);
        mapper.enable(MapperFeature.AUTO_DETECT_IS_GETTERS);
        mapper.enable(MapperFeature.AUTO_DETECT_SETTERS);
        mapper.enable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        mapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
        mapper.enable(MapperFeature.USE_ANNOTATIONS);

        mapper.disable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        mapper.disable(MapperFeature.AUTO_DETECT_CREATORS);
        mapper.disable(MapperFeature.INFER_PROPERTY_MUTATORS);
        mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        mapper.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
        mapper.disable(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME);
        mapper.disable(MapperFeature.USE_STATIC_TYPING);
        mapper.disable(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Void.class, new VoidDeserializer());
        mapper.registerModule(module);

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
        JsonFactory factory = new JsonFactory();
        //Json Parser enabled
        factory.enable(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS);
        factory.enable(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS);
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        factory.enable(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);
        factory.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        factory.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);

        //Json Parser disabled
        factory.disable(JsonParser.Feature.ALLOW_COMMENTS);
        factory.disable(JsonParser.Feature.ALLOW_YAML_COMMENTS);
        factory.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        factory.disable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);

        //Json generator enabled
        factory.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        factory.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        factory.enable(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS);
        //Json generator disabled
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        factory.disable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        factory.disable(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM);
        factory.disable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION);
        factory.disable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        factory.disable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
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
            super(mapper.reader(type), mapper.writerFor(type));
        }

        public TypeSerializer(ObjectMapper mapper, Type type) {
            this(mapper, mapper.constructType(type));
        }
    }

    private static final class ClassSerializer<T> extends InternalSerializer<T> {
        public ClassSerializer(ObjectMapper mapper, Class<T> clazz) {
            super(mapper.reader(clazz), mapper.writerFor(clazz));
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
