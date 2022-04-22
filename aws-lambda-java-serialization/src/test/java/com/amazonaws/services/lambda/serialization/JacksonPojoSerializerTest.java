package com.amazonaws.services.lambda.serialization;

import com.amazonaws.services.lambda.serialization.JacksonPojoSerializer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class JacksonPojoSerializerTest {

    @Test
    public void deserializeVoidAsNonNull() throws Exception {
        JacksonPojoSerializer instance = JacksonPojoSerializer.getInstance();
        Void actual = instance.getMapper().readValue("{}", Void.class);
        Assertions.assertNotNull(actual);
    }
}
