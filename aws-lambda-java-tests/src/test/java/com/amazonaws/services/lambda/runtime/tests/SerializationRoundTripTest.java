/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.*;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies serialization round-trip fidelity for every Lambda-supported event
 * that has a test fixture. Each case feeds the JSON fixture through
 * {@link LambdaEventAssert#assertSerializationRoundTrip} which performs two
 * consecutive round-trips and compares the original JSON tree against the
 * final output.
 */
public class SerializationRoundTripTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("passingCases")
    void roundTrip(String displayName, String fixture, Class<?> eventClass) {
        LambdaEventAssert.assertSerializationRoundTrip(fixture, eventClass);
    }

    /**
     * Documents event classes whose serialization round-trip is currently broken.
     * Each case is expected to throw {@link AssertionError}. When a fix lands in
     * the event model or serializer, the corresponding case should be moved to
     * {@link #passingCases()}.
     */
    @ParameterizedTest(name = "{0} (known failure)")
    @MethodSource("knownFailureCases")
    void roundTripKnownFailures(String displayName, String fixture, Class<?> eventClass) {
        assertThrows(AssertionError.class,
                () -> LambdaEventAssert.assertSerializationRoundTrip(fixture, eventClass),
                displayName + " was expected to fail but passed — move it to passingCases()");
    }

    static Stream<Arguments> passingCases() {
        return Stream.of(
                args("CloudFormationCustomResourceEvent", "cloudformation_event.json",
                        CloudFormationCustomResourceEvent.class),
                args("CloudWatchLogsEvent", "cloudwatchlogs_event.json", CloudWatchLogsEvent.class),
                args("CodeCommitEvent", "codecommit_event.json", CodeCommitEvent.class),
                args("ConfigEvent", "config_event.json", ConfigEvent.class),
                args("DynamodbEvent", "ddb/dynamo_event_roundtrip.json", DynamodbEvent.class),
                args("KinesisEvent", "kinesis/kinesis_event_roundtrip.json", KinesisEvent.class),
                args("KinesisFirehoseEvent", "firehose_event.json", KinesisFirehoseEvent.class),
                args("LambdaDestinationEvent", "lambda_destination_event.json", LambdaDestinationEvent.class),
                args("ScheduledEvent", "cloudwatch_event.json", ScheduledEvent.class),
                args("SecretsManagerRotationEvent", "secrets_rotation_event.json", SecretsManagerRotationEvent.class),
                args("SNSEvent", "sns_event.json", SNSEvent.class),
                args("LexEvent", "lex_event.json", LexEvent.class),
                args("SQSEvent", "sqs/sqs_event_nobody.json", SQSEvent.class));
    }

    static Stream<Arguments> knownFailureCases() {
        return Stream.of(
                // Dropped fields: clientCert lost during deserialization
                args("APIGatewayProxyRequestEvent", "apigw_rest_event.json", APIGatewayProxyRequestEvent.class),
                // Dropped fields: querystring lost during deserialization
                args("CloudFrontEvent", "cloudfront_event.json", CloudFrontEvent.class),
                // Dropped fields: MediaStreams lost during deserialization
                args("ConnectEvent", "connect_event.json", ConnectEvent.class),
                // Extra fields: urlDecodedKey and versionId added by getters
                args("S3Event", "s3_event.json", S3Event.class),
                args("S3EventNotification", "s3_event.json", S3EventNotification.class));
    }

    private static Arguments args(String name, String fixture, Class<?> clazz) {
        return Arguments.of(name, fixture, clazz);
    }
}
