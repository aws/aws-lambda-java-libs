package com.amazonaws.services.lambda.runtime.serialization.interfaces.exceptions;

/**
 * Exception thrown when a serializer cannot be created or initialized.
 * This is a runtime exception that indicates a failure in the serializer factory.
 */
public class SerializerCreationException extends RuntimeException {
    /**
     * Constructs a new serializer creation exception with the specified detail message.
     *
     * @param message the detail message
     */
    public SerializerCreationException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new serializer creation exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public SerializerCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
