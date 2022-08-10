package com.amazonaws.services.lambda.serialization;

import com.amazonaws.services.lambda.serialization.factories.GsonFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class GsonFactoryTest {

    @Test
    public void deserializeVoidAsNonNull() throws Exception {
        GsonFactory instance = GsonFactory.getInstance();
        Void actual = instance.getSerializer(Void.class).fromJson("{}");
        Assertions.assertNotNull(actual);
    }
}

