/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.*;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    private static Stream<Arguments> passingCases() {
        return Stream.of(
                args(CloudFormationCustomResourceEvent.class, "cloudformation_event.json"),
                args(CloudWatchLogsEvent.class, "cloudwatchlogs_event.json"),
                args(CodeCommitEvent.class, "codecommit_event.json"),
                args(ConfigEvent.class, "config_event.json"),
                args(DynamodbEvent.class, "ddb/dynamo_event_roundtrip.json"),
                args(KinesisEvent.class, "kinesis/kinesis_event_roundtrip.json"),
                args(KinesisFirehoseEvent.class, "firehose_event.json"),
                args(LambdaDestinationEvent.class, "lambda_destination_event.json"),
                args(ScheduledEvent.class, "cloudwatch_event.json"),
                args(SecretsManagerRotationEvent.class, "secrets_rotation_event.json"),
                args(SNSEvent.class, "sns_event.json"),
                args(LexEvent.class, "lex_event.json"),
                args(ConnectEvent.class, "connect_event.json"),
                args(SQSEvent.class, "sqs/sqs_event_nobody.json"),
                args(APIGatewayProxyRequestEvent.class, "apigw_rest_event.json"),
                args(CloudFrontEvent.class, "cloudfront_event.json"),
                args(S3Event.class, "s3_event.json"),
                args(S3EventNotification.class, "s3_event.json"));
    }

    private static Arguments args(Class<?> clazz, String fixture) {
        return Arguments.of(clazz.getSimpleName(), fixture, clazz);
    }
}
