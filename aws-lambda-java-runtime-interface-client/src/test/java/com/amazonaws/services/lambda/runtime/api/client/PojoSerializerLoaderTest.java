/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import com.amazonaws.services.lambda.runtime.CustomPojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PojoSerializerLoaderTest {

    @Mock
    private CustomPojoSerializer mockSerializer;

    @AfterEach
    @BeforeEach
    void setUp() throws Exception {
        resetStaticFields();
    }

    private void resetStaticFields() throws Exception {
        Field serializerField = PojoSerializerLoader.class.getDeclaredField("customPojoSerializer");
        serializerField.setAccessible(true);
        serializerField.set(null, null);

        Field initializedField = PojoSerializerLoader.class.getDeclaredField("initialized");
        initializedField.setAccessible(true);
        initializedField.set(null, false);
    }


    private void setMockSerializer(CustomPojoSerializer serializer) throws Exception {
        Field serializerField = PojoSerializerLoader.class.getDeclaredField("customPojoSerializer");
        serializerField.setAccessible(true);
        serializerField.set(null, serializer);
    }

    @Test
    void testGetCustomerSerializerNoSerializerAvailable() throws Exception {
        PojoSerializer<Object> serializer = PojoSerializerLoader.getCustomerSerializer(String.class);
        assertNull(serializer);
        Field initializedField = PojoSerializerLoader.class.getDeclaredField("initialized");
        initializedField.setAccessible(true);
        assert((Boolean) initializedField.get(null));
    }

    @Test
    void testGetCustomerSerializerWithValidSerializer() throws Exception {
        setMockSerializer(mockSerializer);
        String testInput = "test input";
        String testOutput = "test output";
        Type testType = String.class;
        when(mockSerializer.fromJson(any(InputStream.class), eq(testType))).thenReturn(testOutput);
        when(mockSerializer.fromJson(eq(testInput), eq(testType))).thenReturn(testOutput);

        PojoSerializer<Object> serializer = PojoSerializerLoader.getCustomerSerializer(testType);
        assertNotNull(serializer);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testInput.getBytes());
        Object result1 = serializer.fromJson(inputStream);
        assertEquals(testOutput, result1);
    
        Object result2 = serializer.fromJson(testInput);
        assertEquals(testOutput, result2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serializer.toJson(testInput, outputStream);
        verify(mockSerializer).toJson(eq(testInput), any(OutputStream.class), eq(testType));
    }

    @Test
    void testGetCustomerSerializerCachingBehavior() throws Exception {
        setMockSerializer(mockSerializer);

        Type testType = String.class;
        PojoSerializer<Object> serializer1 = PojoSerializerLoader.getCustomerSerializer(testType);
        PojoSerializer<Object> serializer2 = PojoSerializerLoader.getCustomerSerializer(testType);

        assertNotNull(serializer1);
        assertNotNull(serializer2);
        
        String testInput = "test";
        serializer1.fromJson(testInput);
        serializer2.fromJson(testInput);
        
        verify(mockSerializer, times(2)).fromJson(eq(testInput), eq(testType));
    }

    @Test
    void testGetCustomerSerializerDifferentTypes() throws Exception {
        setMockSerializer(mockSerializer);

        PojoSerializer<Object> stringSerializer = PojoSerializerLoader.getCustomerSerializer(String.class);
        PojoSerializer<Object> integerSerializer = PojoSerializerLoader.getCustomerSerializer(Integer.class);

        assertNotNull(stringSerializer);
        assertNotNull(integerSerializer);

        String testString = "test";
        Integer testInt = 123;

        stringSerializer.fromJson(testString);
        integerSerializer.fromJson(testInt.toString());

        verify(mockSerializer).fromJson(eq(testString), eq(String.class));
        verify(mockSerializer).fromJson(eq(testInt.toString()), eq(Integer.class));
    }

    @Test
    void testGetCustomerSerializerNullType() throws Exception {
        setMockSerializer(mockSerializer);

        PojoSerializer<Object> serializer = PojoSerializerLoader.getCustomerSerializer(null);
        assertNotNull(serializer);

        String testInput = "test";
        serializer.fromJson(testInput);
        verify(mockSerializer).fromJson(eq(testInput), eq(null));
    }

    @Test
    void testGetCustomerSerializerExceptionHandling() throws Exception {
        setMockSerializer(mockSerializer);

        doThrow(new RuntimeException("Test exception"))
            .when(mockSerializer)
            .fromJson(any(String.class), any(Type.class));

        PojoSerializer<Object> serializer = PojoSerializerLoader.getCustomerSerializer(String.class);
        assertNotNull(serializer);
        assertThrows(RuntimeException.class, () -> serializer.fromJson("test"));
    }
}
