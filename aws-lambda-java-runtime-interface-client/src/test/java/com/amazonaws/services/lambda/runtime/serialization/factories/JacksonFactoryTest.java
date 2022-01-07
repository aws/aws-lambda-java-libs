/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.serialization.factories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JacksonFactoryTest {

    private JacksonFactory jacksonFactory = JacksonFactory.getInstance();
    private ObjectMapper mapper = jacksonFactory.getMapper();

    @Test
    public void deserializeVoidAsNonNull() throws Exception {
        Void actual = mapper.readValue("{}", Void.class);
        assertNotNull(actual);
    }

    @Test
    public void testOptionalAsEmpty() throws JsonProcessingException {
        Contact emptyEmail = new Contact("Example Co.", Optional.empty());
        String emptyEmailJson = mapper.writeValueAsString(emptyEmail);
        assertEquals("{\"name\":\"Example Co.\",\"email\":null}", emptyEmailJson);
    }

    @Test
    public void testOptionalWithValue() throws JsonProcessingException {
        Contact withEmail = new Contact("Example Co.", Optional.of("info@example.com"));
        String withEmailJson = mapper.writeValueAsString(withEmail);
        assertEquals("{\"name\":\"Example Co.\",\"email\":\"info@example.com\"}", withEmailJson);
    }

    static class Contact {
        private final String name;
        private final Optional<String> email;

        public Contact(String name, Optional<String> email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public Optional<String> getEmail() {
            return email;
        }
    }
}
