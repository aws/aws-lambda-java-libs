package com.amazonaws.services.lambda.serialization;

import com.amazonaws.services.lambda.runtime.CustomPojoSerializer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class ServiceLoaderTest {

    @Test
    public void checkNumberOfImplementations() throws ClassNotFoundException {
        ServiceLoader<CustomPojoSerializer> loader = ServiceLoader.load(CustomPojoSerializer.class);

        List<CustomPojoSerializer> providers = new ArrayList<>();
        loader.iterator().forEachRemaining(providers::add);

        Assertions.assertFalse(providers.isEmpty());
        Assertions.assertEquals(1, providers.size());
    }
}
