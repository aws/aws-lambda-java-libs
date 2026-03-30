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
 * Verifies serialization round-trip fidelity for events that are registered in
 * {@code LambdaEventSerializers.SUPPORTED_EVENTS}.
 *
 * <p>Registered events go through the full customized serialization path in the
 * Runtime Interface Client (RIC): {@code EventHandlerLoader.getSerializer()}
 * detects them via {@code isLambdaSupportedEvent()} and delegates to
 * {@code LambdaEventSerializers.serializerFor()}, which applies Jackson mixins,
 * {@code DateModule}/{@code DateTimeModule}, and naming strategies.</p>
 *
 * <p>Each case feeds a JSON fixture through
 * {@link LambdaEventAssert#assertSerializationRoundTrip} which performs two
 * consecutive round-trips and compares the original JSON tree against the
 * final output.</p>
 *
 * @see UnregisteredEventSerializationRoundTripTest for events not in SUPPORTED_EVENTS
 */
public class SerializationRoundTripTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("passingCases")
    void roundTrip(String displayName, String fixture, Class<?> eventClass) {
        LambdaEventAssert.assertSerializationRoundTrip(fixture, eventClass);
    }

    @ParameterizedTest(name = "{0} (known failure)")
    @MethodSource("knownFailureCases")
    void roundTripKnownFailures(String displayName, String fixture, Class<?> eventClass) {
        assertThrows(Throwable.class,
                () -> LambdaEventAssert.assertSerializationRoundTrip(fixture, eventClass),
                displayName + " was expected to fail but passed — move it to passingCases()");
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
                args(LexEvent.class, "lex_event_roundtrip.json"),
                args(ConnectEvent.class, "connect_event.json"),
                args(SQSEvent.class, "sqs/sqs_event_nobody.json"),
                args(APIGatewayProxyRequestEvent.class, "apigw_rest_event.json"),
                args(CloudFrontEvent.class, "cloudfront_event.json"),
                args(S3Event.class, "s3_event.json"),
                args(S3EventNotification.class, "s3_event.json"),
                args(APIGatewayV2HTTPEvent.class, "apigw_http_event.json"),
                args(APIGatewayCustomAuthorizerEvent.class, "apigw_auth.json"),
                args(ApplicationLoadBalancerRequestEvent.class, "elb_event.json"),
                args(CloudWatchCompositeAlarmEvent.class, "cloudwatch_composite_alarm.json"),
                args(CloudWatchMetricAlarmEvent.class, "cloudwatch_metric_alarm.json"),
                args(CognitoUserPoolPreTokenGenerationEventV2.class, "cognito_user_pool_pre_token_generation_event_v2.json"),
                args(KafkaEvent.class, "kafka_event_roundtrip.json"),
                args(MSKFirehoseEvent.class, "msk_firehose_event_roundtrip.json"),
                args(RabbitMQEvent.class, "rabbitmq_event_roundtrip.json"),
                args(S3BatchEventV2.class, "s3_batch_event_v2.json"),
                args(IoTButtonEvent.class, "iot_button_event.json"),
                args(CognitoEvent.class, "cognito_sync_event.json"),
                args(DynamodbTimeWindowEvent.class, "ddb/dynamo_time_window_event.json"),
                args(KinesisTimeWindowEvent.class, "kinesis/kinesis_time_window_event.json"));
    }

    private static Stream<Arguments> knownFailureCases() {
        return Stream.of(
                // APIGatewayV2CustomAuthorizerEvent has two serialization issues:
                // 1. getTime() parses the raw string "12/Mar/2020:19:03:58 +0000" into a
                //    DateTime via dd/MMM/yyyy formatter. Jackson serializes as ISO-8601, but
                //    the formatter cannot parse ISO-8601 back on the second round-trip.
                //    The time field is effectively mandatory (getTime() throws NPE if null),
                //    and the date format change is inherent to how the serialization works.
                // 2. getTimeEpoch() converts long to Instant; Jackson serializes as decimal
                //    seconds (e.g. 1583348638.390000000) instead of the original long.
                // Both transformations are lossy; coercion captured in EventLoaderTest.
                args(APIGatewayV2CustomAuthorizerEvent.class, "apigw_auth_v2.json"),
                // ActiveMQEvent has one serialization issue:
                // Destination.physicalName (camelCase) vs JSON "physicalname" (lowercase) —
                // ACCEPT_CASE_INSENSITIVE_PROPERTIES is disabled in JacksonFactory so the
                // field is silently dropped during deserialization.
                // Fix: create an ActiveMQEventMixin with a DestinationMixin that maps
                // @JsonProperty("physicalname") to getPhysicalName()/setPhysicalName(),
                // then register it in LambdaEventSerializers MIXIN_MAP and NESTED_CLASS_MAP.
                args(ActiveMQEvent.class, "mq_event.json"));
    }

    private static Arguments args(Class<?> clazz, String fixture) {
        return Arguments.of(clazz.getSimpleName(), fixture, clazz);
    }
}
