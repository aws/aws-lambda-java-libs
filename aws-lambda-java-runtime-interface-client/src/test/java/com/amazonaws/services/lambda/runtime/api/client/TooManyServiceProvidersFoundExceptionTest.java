/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.api.client.TooManyServiceProvidersFoundException;

import static org.junit.jupiter.api.Assertions.*;

class TooManyServiceProvidersFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        TooManyServiceProvidersFoundException exception = new TooManyServiceProvidersFoundException();
        
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testMessageConstructor() {
        String errorMessage = "Too many service providers found";
        TooManyServiceProvidersFoundException exception = 
            new TooManyServiceProvidersFoundException(errorMessage);
        
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

        @Test
    void testCauseConstructor() {
        Throwable cause = new IllegalStateException("Original error");
        TooManyServiceProvidersFoundException exception = 
            new TooManyServiceProvidersFoundException(cause);
        
        assertNotNull(exception);
        assertEquals(cause.toString(), exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    void testMessageAndCauseConstructor() {
        String errorMessage = "Too many service providers found";
        Throwable cause = new IllegalStateException("Original error");
        TooManyServiceProvidersFoundException exception = 
            new TooManyServiceProvidersFoundException(errorMessage, cause);
        
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    
}
