package com.amazonaws.services.lambda.runtime.serialization.interfaces;

/**
 * Enumeration of available serializer types for AWS Lambda functions.
 */
public enum SerializerType {
    Custom("Custom"),
    Event("AWSEvent"),
    Default("Default");

    private final String value;

    SerializerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SerializerType fromString(String value) {
        for (SerializerType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown SerializerType: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}