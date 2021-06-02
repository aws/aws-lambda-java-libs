/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.tests.annotations.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Used to process @{@link HandlerParams} com.amazonaws.services.lambda.runtime.tests.annotations
 */
public class HandlerParamsArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<HandlerParams> {

    private Event event;
    private Response response;

    private Events events;
    private Responses responses;

    @Override
    public void accept(HandlerParams handlerParams) {
        this.event = handlerParams.event();
        this.response = handlerParams.response();
        this.events = handlerParams.events();
        this.responses = handlerParams.responses();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        if ((!event.value().isEmpty() && response.value().isEmpty()) ||
                (event.value().isEmpty() && !response.value().isEmpty())) {
            throw new IllegalStateException("You must use either Event & Response (singular) or Events & Responses (plural) annotations together, you cannot mix them");
        }
        if (((ArrayUtils.isEmpty(events.events()) && StringUtils.isEmpty(events.folder()))
                && (StringUtils.isNotEmpty(responses.folder()) || ArrayUtils.isNotEmpty(responses.responses())))
                ||
                ((ArrayUtils.isEmpty(responses.responses()) && StringUtils.isEmpty(responses.folder()))
                        && (StringUtils.isNotEmpty(events.folder()) || ArrayUtils.isNotEmpty(events.events())))) {
            throw new IllegalStateException("You must use either Event & Response (singular) or Events & Responses (plural) annotations together, you cannot mix them");
        }

        // deal with one element
        if (!event.value().isEmpty() && !response.value().isEmpty()) {
            return Stream.of(
                    Arguments.of(
                            EventLoader.loadEvent(event.value(), event.type()),
                            EventLoader.loadEvent(response.value(), response.type())
                    )
            );
        }

        // deal with many elements
        List<Object> eventList = getEvents();
        List<Object> responseList = getResponses();
        if (eventList == null || eventList.size() == 0 || responseList == null || responseList.size() == 0 || eventList.size() != responseList.size()) {
            throw new IllegalStateException("At least one event and one response should be provided, and you should have the exact same number of events and responses.");
        }

        Stream.Builder<Arguments> streamBuilder = Stream.builder();
        for (int i = 0; i < eventList.size(); i++) {
            streamBuilder.add(Arguments.of(eventList.get(i), responseList.get(i)));
        }
        return streamBuilder.build();
    }

    private List<Object> getResponses() throws IOException, URISyntaxException {
        List<Object> responseList;
        if (ArrayUtils.isNotEmpty(responses.responses())) {
            responseList = Arrays.stream(responses.responses()).map(
                    response -> {
                        Class<?> clazz = response.type() == Void.class ? responses.type() : response.type();
                        return EventLoader.loadEvent(response.value(), clazz);
                    }
            ).collect(Collectors.toList());
        } else {
            Stream<Path> files = listFiles(responses.folder());

            responseList = files
                    .filter(Files::isRegularFile)
                    .map(path -> EventLoader.loadEvent(path.toString(), responses.type()))
                    .collect(Collectors.toList());
        }
        return responseList;
    }

    private List<Object> getEvents() throws IOException, URISyntaxException {
        List<Object> eventList;
        if (ArrayUtils.isNotEmpty(events.events())) {
            eventList = Arrays.stream(events.events()).map(
                    event -> {
                        Class<?> clazz = event.type() == Void.class ? events.type() : event.type();
                        return EventLoader.loadEvent(event.value(), clazz);
                    }
            ).collect(Collectors.toList());
        } else {
            Stream<Path> files = listFiles(events.folder());

            eventList = files
                    .filter(Files::isRegularFile)
                    .map(path -> EventLoader.loadEvent(path.toString(), events.type()))
                    .collect(Collectors.toList());
        }
        return eventList;
    }

    private Stream<Path> listFiles(String folder) throws IOException, URISyntaxException {
        URL folderUrl = getClass().getResource(folder);
        if (folderUrl == null) {
            folderUrl = getClass().getClassLoader().getResource(folder);
        }
        if (folderUrl == null) {
            throw new IllegalArgumentException("Path " + folder + " cannot be found");
        }
        return Files.list(Paths.get(folderUrl.toURI())).sorted();
    }
}
