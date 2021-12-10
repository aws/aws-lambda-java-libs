/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.serialization.events.tck;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.serialization.PojoSerializer;
import com.amazonaws.services.lambda.runtime.serialization.events.mixins.SQSEventMixin;
import com.amazonaws.services.lambda.runtime.serialization.factories.JacksonFactory;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SQSEventTest {

    @Test
    void testDeserializationOfSqsRecieveMessageEvent() throws IOException {
        //given:
        JacksonFactory factory = JacksonFactory.getInstance().withMixin(SQSEvent.class, SQSEventMixin.class);
        PojoSerializer<SQSEvent> serializer = factory.getSerializer(SQSEvent.class);

        //when:
        String json = EventUtils.readEvent("sqs-receive-message.json");
        SQSEvent sqsEvent = serializer.fromJson(json);

        //then:
        assertNotNull(sqsEvent);
        assertNotNull(sqsEvent.getRecords());
        assertEquals(1, sqsEvent.getRecords().size());

        //when:
        SQSEvent.SQSMessage sqsMessage = sqsEvent.getRecords().get(0);

        //then:
        assertEquals("19dd0b57-b21e-4ac1-bd88-01bbb068cb78", sqsMessage.getMessageId());
        assertEquals("MessageReceiptHandle", sqsMessage.getReceiptHandle());
        assertEquals("Hello from SQS!", sqsMessage.getBody());
        assertEquals(Collections.emptyMap(), sqsMessage.getMessageAttributes());
        assertEquals("{{{md5_of_body}}}", sqsMessage.getMd5OfBody());
        assertEquals("aws:sqs", sqsMessage.getEventSource());
        //TODO this fails
        // assertEquals("arn:aws:sqs:us-east-1:123456789012:MyQueue", sqsMessage.getEventSourceArn());
        assertEquals("us-east-1", sqsMessage.getAwsRegion());
        assertNotNull(sqsMessage.getAttributes());
        assertEquals("1", sqsMessage.getAttributes().get("ApproximateReceiveCount"));
        assertEquals("1523232000000", sqsMessage.getAttributes().get("SentTimestamp"));
        assertEquals("123456789012", sqsMessage.getAttributes().get("SenderId"));
        assertEquals("1523232000001", sqsMessage.getAttributes().get("ApproximateFirstReceiveTimestamp"));
    }
}
