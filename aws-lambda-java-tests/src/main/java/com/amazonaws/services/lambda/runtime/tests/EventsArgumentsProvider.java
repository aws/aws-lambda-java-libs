/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.tests.annotations.Events;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Used to process @{@link Events} com.amazonaws.services.lambda.runtime.tests.annotations
 */
public class EventsArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<Events> {

    private Events events;

    @Override
    public void accept(Events events) {
        this.events = events;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        if (ArrayUtils.isNotEmpty(events.events())) {
            return Arrays.stream(events.events())
                    .map(event -> {
                        Class<?> clazz = event.type() == Void.class ? events.type() : event.type();
                        return Arguments.of(EventLoader.loadEvent(event.value(), clazz));
                    });
        } else {
            URL folderUrl = getClass().getResource(events.folder());
            if (folderUrl == null) {
                folderUrl = getClass().getClassLoader().getResource(events.folder());
            }
            if (folderUrl == null) {
                throw new IllegalArgumentException("Path " + events.folder() + " cannot be found");
            }
            Stream<Path> files = Files.list(Paths.get(folderUrl.toURI())).sorted();
            return files
                    .filter(Files::isRegularFile)
                    .map(path -> Arguments.of(EventLoader.loadEvent(path.toString(), events.type())));
        }
    }
}
