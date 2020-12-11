/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.stream.Stream;

/**
 * Used to process @{@link Event} com.amazonaws.services.lambda.runtime.tests.annotations
 */
public class EventArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<Event> {

    private Event event;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        Object o = EventLoader.loadEvent(event.value(), event.type());
        return Stream.of(Arguments.of(o));
    }

    @Override
    public void accept(Event event) {
        this.event = event;
    }
}
