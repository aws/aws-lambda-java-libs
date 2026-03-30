package com.amazonaws.services.lambda.runtime.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LambdaEventAssertTest {
    /**
     * Demonstrates the completeness check: the fixture has a field
     * ({@code unknownField}) that {@code PartialPojo} does not capture,
     * so it gets silently dropped during deserialization.
     */
    @Test
    void shouldFailWhenFieldIsDropped() {
        AssertionError error = assertThrows(AssertionError.class,
                () -> LambdaEventAssert.assertSerializationRoundTrip(
                        "partial_pojo.json", PartialPojo.class));

        assertTrue(error.getMessage().contains("PartialPojo"),
                "Error message should name the failing class");
    }

    /**
     * Demonstrates the stability check: the getter mutates state on each
     * call, so the first and second round-trips produce different JSON.
     */
    @Test
    void shouldFailWhenSerializationIsUnstable() {
        AssertionError error = assertThrows(AssertionError.class,
                () -> LambdaEventAssert.assertSerializationRoundTrip(
                        "unstable_pojo.json", UnstablePojo.class));

        assertTrue(error.getMessage().contains("UnstablePojo"),
                "Error message should name the failing class");
    }

    /** POJO that only captures {@code name}, silently dropping any other fields. */
    public static class PartialPojo {
        private String name;

        public PartialPojo() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * POJO with a getter that appends a suffix, making serialization
     * non-idempotent.
     */
    public static class UnstablePojo {
        private String name;

        public UnstablePojo() {
        }

        public String getName() {
            return name == null ? null : name + "_x";
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
