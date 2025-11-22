package com.amazonaws.services.lambda.runtime.serialization.factories;

import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializer;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.LambdaSerializerFactory;
import com.amazonaws.services.lambda.runtime.serialization.interfaces.SerializerCreationContext;

import java.lang.reflect.Type;
import java.util.Optional;

public class DefaultLambdaSerializerFactory implements LambdaSerializerFactory {

    @Override
    public <T> LambdaSerializer<T> getLambdaSerializer(Class<T> clazz, SerializerCreationContext context) {

        Optional<Object> platform = context.getAttribute("platform");
        
        if (platform.isPresent() && "android".equalsIgnoreCase(platform.get().toString())) {
            return GsonFactoryAdapter.getInstance().getLambdaSerializer(clazz, context);
        }
        return JacksonFactoryAdapter.getInstance().getLambdaSerializer(clazz, context);

    }

    @Override
    public <T> LambdaSerializer<T> getLambdaSerializer(Type type, SerializerCreationContext context) {
        Optional<Object> platform = context.getAttribute("platform");
        
        if (platform.isPresent() && "android".equalsIgnoreCase(platform.get().toString())) {
            return GsonFactoryAdapter.getInstance().getLambdaSerializer(type, context);
        }
        return JacksonFactoryAdapter.getInstance().getLambdaSerializer(type, context);
    }
}
