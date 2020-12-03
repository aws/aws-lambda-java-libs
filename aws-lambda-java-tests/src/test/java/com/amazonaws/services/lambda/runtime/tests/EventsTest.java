/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import com.amazonaws.services.lambda.runtime.tests.annotations.Events;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;


public class EventsTest {

    @ParameterizedTest
    @Events(
            events = {
                    @Event("sqs/sqs_event_nobody.json"),
                    @Event("sqs/sqs_event_product.json"),
            },
            type = SQSEvent.class
    )
    public void testInjectEvents(SQSEvent event) {
        assertThat(event).isNotNull();
        assertThat(event.getRecords()).hasSize(1);
    }

    @ParameterizedTest
    @Events(
            events = {
                    @Event(value = "sqs/sqs_event_nobody.json", type = SQSEvent.class),
                    @Event(value = "sqs/sqs_event_product.json", type = SQSEvent.class),
            }
    )
    public void testInjectEvents2(SQSEvent event) {
        assertThat(event).isNotNull();
        assertThat(event.getRecords()).hasSize(1);
    }

    @ParameterizedTest
    @Events(folder = "sqs", type = SQSEvent.class)
    public void testInjectEventsFromFolder(SQSEvent event) {
        assertThat(event).isNotNull();
        assertThat(event.getRecords()).hasSize(1);
    }
}
