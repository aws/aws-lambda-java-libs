/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.factories;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JacksonFactoryTest {

    @Test
    public void deserializeVoidAsNonNull() throws Exception {
        JacksonFactory instance = JacksonFactory.getInstance();
        Void actual = instance.getMapper().readValue("{}", Void.class);
        assertNotNull(actual);
    }

}
