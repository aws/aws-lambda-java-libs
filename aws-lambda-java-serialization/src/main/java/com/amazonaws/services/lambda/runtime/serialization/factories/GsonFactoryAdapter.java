package com.amazonaws.services.lambda.runtime.serialization.factories;

import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializer;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializerFactory;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.SerializerCreationContext;

import java.lang.reflect.Type;

class GsonFactoryAdapter implements LambdaSerializerFactory {

    private static final GsonFactoryAdapter instance = new GsonFactoryAdapter();
    private static final GsonFactory gsonFactory = GsonFactory.getInstance();

    public static GsonFactoryAdapter getInstance() {
        return instance;
    }

    @Override
    public <T> LambdaSerializer<T> getLambdaSerializer(Class<T> clazz, SerializerCreationContext context) {
        return new PojoSerializerAdapter<>(gsonFactory.getSerializer(clazz));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LambdaSerializer<T> getLambdaSerializer(Type type, SerializerCreationContext context) {
        return new PojoSerializerAdapter<T>((PojoSerializer<T>) gsonFactory.getSerializer(type));
    }
}
