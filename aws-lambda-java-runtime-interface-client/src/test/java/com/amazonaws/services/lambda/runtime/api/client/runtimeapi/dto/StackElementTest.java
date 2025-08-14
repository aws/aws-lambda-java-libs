package com.amazonaws.services.lambda.runtime.api.client.runtimeapi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StackElementTest {

    @Test
    void testDefaultConstructor() {
        StackElement stackElement = new StackElement();
        assertNotNull(stackElement);
        assertNull(stackElement.label);
        assertNull(stackElement.path);
        assertEquals(0, stackElement.line);
    }

    @Test
    void testParameterizedConstructor() {
        String label = "testMethod";
        String path = "/test/path";
        int line = 42;

        StackElement stackElement = new StackElement(label, path, line);

        assertEquals(label, stackElement.label);
        assertEquals(path, stackElement.path);
        assertEquals(line, stackElement.line);
    }
}