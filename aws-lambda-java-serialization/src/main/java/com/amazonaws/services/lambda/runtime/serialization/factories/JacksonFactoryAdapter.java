package com.amazonaws.services.lambda.runtime.serialization.factories;

import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializer;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializerFactory;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.SerializerCreationContext;

import java.lang.reflect.Type;

class JacksonFactoryAdapter implements LambdaSerializerFactory {

    private static final JacksonFactoryAdapter instance = new JacksonFactoryAdapter();
    private static final JacksonFactory jacksonFactory = JacksonFactory.getInstance();

    public static JacksonFactoryAdapter getInstance() {
        return instance;
    }

    @Override
    public <T> LambdaSerializer<T> getLambdaSerializer(Class<T> clazz, SerializerCreationContext context) {
        return new PojoSerializerAdapter<>(jacksonFactory.getSerializer(clazz));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LambdaSerializer<T> getLambdaSerializer(Type type, SerializerCreationContext context) {
        return new PojoSerializerAdapter<T>((PojoSerializer<T>) jacksonFactory.getSerializer(type));
    }
}
