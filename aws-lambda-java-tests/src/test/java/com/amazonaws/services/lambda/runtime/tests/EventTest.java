/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @ParameterizedTest
    @Event(value = "sqs/sqs_event_nobody.json", type = SQSEvent.class)
    public void testInjectEvent(SQSEvent event) {
        assertThat(event).isNotNull();
        assertThat(event.getRecords()).hasSize(1);
    }
}
