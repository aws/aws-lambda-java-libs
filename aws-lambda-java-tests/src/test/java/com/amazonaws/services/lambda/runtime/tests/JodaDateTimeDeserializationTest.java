/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.CodeCommitEvent;
import com.amazonaws.services.lambda.runtime.events.LambdaDestinationEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Bug condition exploration tests for Joda DateTime deserialization.
 *
 * Validates: Requirements 1.1, 1.2, 1.3, 1.4, 1.5
 *
 * These tests verify that event types with org.joda.time.DateTime fields
 * deserialize correctly in an unshaded environment. Prior to the fix,
 * Jackson 2.18.6 would throw MismatchedInputException because the
 * DateTimeModule registered for the shaded class path rather than
 * org.joda.time.DateTime.
 */
public class JodaDateTimeDeserializationTest {

    @Test
    public void testLambdaDestinationEventDateTimeDeserialization() {
        LambdaDestinationEvent event = EventLoader.loadLambdaDestinationEvent("lambda_destination_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getTimestamp()).isEqualTo(DateTime.parse("2019-11-24T21:52:47.333Z"));
    }

    @Test
    public void testScheduledEventDateTimeDeserialization() {
        ScheduledEvent event = EventLoader.loadScheduledEvent("cloudwatch_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getTime()).isEqualTo(DateTime.parse("2020-09-30T15:58:34Z"));
    }

    @Test
    public void testCodeCommitEventDateTimeDeserialization() {
        CodeCommitEvent event = EventLoader.loadCodeCommitEvent("codecommit_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getRecords()).isNotEmpty();

        CodeCommitEvent.Record record = event.getRecords().get(0);
        assertThat(record.getEventTime()).isEqualTo(DateTime.parse("2016-01-01T23:59:59.000+0000"));
    }

    @Test
    public void testSNSEventDateTimeDeserialization() {
        SNSEvent event = EventLoader.loadSNSEvent("sns_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getRecords()).isNotEmpty();

        SNSEvent.SNS sns = event.getRecords().get(0).getSNS();
        assertThat(sns.getTimestamp()).isEqualTo(DateTime.parse("2020-10-08T16:06:14.656Z"));
    }
}
