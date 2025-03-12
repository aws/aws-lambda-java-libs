/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HandlerInfoTest {

    @Test
    void testConstructor() {
        Class<?> testClass = String.class;
        String methodName = "testMethod";
        
        HandlerInfo info = new HandlerInfo(testClass, methodName);
        
        assertNotNull(info);
        assertEquals(testClass, info.clazz);
        assertEquals(methodName, info.methodName);
    }

    @Test
    void testFromStringWithoutMethod() throws Exception {
        String handler = "java.lang.String";
        HandlerInfo info = HandlerInfo.fromString(handler, ClassLoader.getSystemClassLoader());
        
        assertEquals(String.class, info.clazz);
        assertNull(info.methodName);
    }

    @Test
    void testFromStringWithMethod() throws Exception {
        String handler = "java.lang.String::length";
        HandlerInfo info = HandlerInfo.fromString(handler, ClassLoader.getSystemClassLoader());
        
        assertEquals(String.class, info.clazz);
        assertEquals("length", info.methodName);
    }

    @Test
    void testFromStringWithEmptyClass() {
        String handler = "::method";
        
        assertThrows(HandlerInfo.InvalidHandlerException.class, () ->
            HandlerInfo.fromString(handler, ClassLoader.getSystemClassLoader())
        );
    }

    @Test
    void testFromStringWithEmptyMethod() {
        String handler = "java.lang.String::";
        
        assertThrows(HandlerInfo.InvalidHandlerException.class, () ->
            HandlerInfo.fromString(handler, ClassLoader.getSystemClassLoader())
        );
    }

    @Test
    void testFromStringWithNonexistentClass() {
        String handler = "com.nonexistent.TestClass::method";
        
        assertThrows(ClassNotFoundException.class, () ->
            HandlerInfo.fromString(handler, ClassLoader.getSystemClassLoader())
        );
    }

    @Test
    void testFromStringWithNullHandler() {
        assertThrows(NullPointerException.class, () ->
            HandlerInfo.fromString(null, ClassLoader.getSystemClassLoader())
        );
    }

    @Test
    void testClassNameWithoutMethod() {
        String handler = "java.lang.String";
        String className = HandlerInfo.className(handler);
        
        assertEquals("java.lang.String", className);
    }

    @Test
    void testClassNameWithMethod() {
        String handler = "java.lang.String::length";
        String className = HandlerInfo.className(handler);
        
        assertEquals("java.lang.String", className);
    }

    @Test
    void testClassNameWithEmptyString() {
        String handler = "";
        String className = HandlerInfo.className(handler);
        
        assertEquals("", className);
    }

    @Test
    void testClassNameWithOnlyDelimiter() {
        String handler = "::";
        String className = HandlerInfo.className(handler);
        
        assertEquals("", className);
    }

    @Test
    void testInvalidHandlerExceptionSerialVersionUID() {
        assertEquals(-1L, HandlerInfo.InvalidHandlerException.serialVersionUID);
    }

    @Test
    void testFromStringWithInnerClass() throws Exception {
        // Create a custom class loader that can load our test class
        ClassLoader cl = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                if (name.equals("com.test.OuterClass$InnerClass")) {
                    throw new ClassNotFoundException("Test class not found");
                }
                return super.loadClass(name);
            }
        };

        String handler = "com.test.OuterClass$InnerClass::method";
        assertThrows(ClassNotFoundException.class, () ->
            HandlerInfo.fromString(handler, cl)
        );
    }
}
