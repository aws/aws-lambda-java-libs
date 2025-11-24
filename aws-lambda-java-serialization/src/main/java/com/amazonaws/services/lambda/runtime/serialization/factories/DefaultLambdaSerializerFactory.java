package com.amazonaws.services.lambda.runtime.serialization.factories;

import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.events.LambdaEventSerializers;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializer;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializerFactory;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.SerializerCreationContext;

import java.lang.reflect.Type;
import java.util.Objects;

public class DefaultLambdaSerializerFactory implements LambdaSerializerFactory {

    @Override
    public <T> LambdaSerializer<T> getLambdaSerializer(Class<T> clazz, SerializerCreationContext context) {
        String platform = (String) context.getAttribute("platform").get();
        ClassLoader classLoader = (ClassLoader) context.getAttribute("classLoader").get();

        // We are removing GSON dependency from the layer because that is
        if (Objects.requireNonNull(platform).equals("ANDROID")) {
            return null;
        }

        if (LambdaEventSerializers.isLambdaSupportedEvent(clazz.getName())) {
            return new PojoSerializerAdapter<>(LambdaEventSerializers.serializerFor(clazz, classLoader));
        }

        return JacksonFactoryAdapter.getInstance().getLambdaSerializer(clazz, context);

    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LambdaSerializer<T> getLambdaSerializer(Type type, SerializerCreationContext context) {
        String platform = (String) context.getAttribute("platform").get();
        ClassLoader classLoader = (ClassLoader) context.getAttribute("classLoader").get();

        if (Objects.requireNonNull(platform).equals("ANDROID")) {
            return null;
        }

        // if serializing a Class that is a Lambda supported event, use Jackson with customizations
        if (type instanceof Class) {
            Class<Object> clazz = ((Class) type);
            if (LambdaEventSerializers.isLambdaSupportedEvent(clazz.getName())) {
                return new PojoSerializerAdapter<>((PojoSerializer<T>) LambdaEventSerializers.serializerFor(clazz, classLoader));
            }
        }

        return JacksonFactoryAdapter.getInstance().getLambdaSerializer(type, context);
    }
}
