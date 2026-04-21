/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies serialization round-trip fidelity for events that are NOT registered
 * in {@code LambdaEventSerializers.SUPPORTED_EVENTS}.
 *
 * <p>In the Runtime Interface Client (RIC), when a handler's event type is not
 * in {@code SUPPORTED_EVENTS}, {@code EventHandlerLoader.getSerializer()} falls
 * through to {@code JacksonFactory.getInstance().getSerializer(type)} — a bare
 * {@code PojoSerializer} backed by Jackson without any mixins or naming
 * strategies. However, {@code LambdaEventSerializers.serializerFor()} (used by
 * this test) unconditionally registers {@code DateModule} and
 * {@code DateTimeModule}, so Joda/java.time types are still handled. For most
 * unregistered events this makes no practical difference because they don't
 * contain Joda DateTime fields.</p>
 *
 * @see SerializationRoundTripTest for events registered in SUPPORTED_EVENTS
 */
@SuppressWarnings("deprecation") // APIGatewayV2ProxyRequestEvent is deprecated
public class UnregisteredEventSerializationRoundTripTest {

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
                // S3 Batch
                args(S3BatchEvent.class, "s3_batch_event.json"),
                // AppSync
                args(AppSyncLambdaAuthorizerEvent.class, "appsync_authorizer_event.json"),
                args(AppSyncLambdaAuthorizerResponse.class, "appsync_authorizer_response.json"),
                // TimeWindow response
                args(TimeWindowEventResponse.class, "time_window_event_response.json"),
                // Cognito UserPool triggers
                args(CognitoUserPoolPreSignUpEvent.class, "cognito/cognito_userpool_presignup.json"),
                args(CognitoUserPoolPostConfirmationEvent.class, "cognito/cognito_userpool_postconfirmation.json"),
                args(CognitoUserPoolPreAuthenticationEvent.class, "cognito/cognito_userpool_preauthentication.json"),
                args(CognitoUserPoolPostAuthenticationEvent.class, "cognito/cognito_userpool_postauthentication.json"),
                args(CognitoUserPoolDefineAuthChallengeEvent.class, "cognito/cognito_userpool_define_auth_challenge.json"),
                args(CognitoUserPoolCreateAuthChallengeEvent.class, "cognito/cognito_userpool_create_auth_challenge.json"),
                args(CognitoUserPoolVerifyAuthChallengeResponseEvent.class, "cognito/cognito_userpool_verify_auth_challenge.json"),
                args(CognitoUserPoolMigrateUserEvent.class, "cognito/cognito_userpool_migrate_user.json"),
                args(CognitoUserPoolCustomMessageEvent.class, "cognito/cognito_userpool_custom_message.json"),
                args(CognitoUserPoolPreTokenGenerationEvent.class, "cognito/cognito_userpool_pre_token_generation.json"),
                // Kinesis Analytics
                args(KinesisAnalyticsFirehoseInputPreprocessingEvent.class, "kinesis/kinesis_analytics_firehose_input_preprocessing.json"),
                args(KinesisAnalyticsStreamsInputPreprocessingEvent.class, "kinesis/kinesis_analytics_streams_input_preprocessing.json"),
                args(KinesisAnalyticsInputPreprocessingResponse.class, "kinesis/kinesis_analytics_input_preprocessing_response.json"),
                args(KinesisAnalyticsOutputDeliveryEvent.class, "kinesis/kinesis_analytics_output_delivery.json"),
                args(KinesisAnalyticsOutputDeliveryResponse.class, "kinesis/kinesis_analytics_output_delivery_response.json"),
                // API Gateway V2 WebSocket
                args(APIGatewayV2WebSocketEvent.class, "apigw_websocket_event.json"),
                args(APIGatewayV2ProxyRequestEvent.class, "apigw_websocket_event.json"));
    }

    private static Stream<Arguments> knownFailureCases() {
        return Stream.of(
                // S3ObjectLambdaEvent: Lombok generates getXAmzRequestId() for field
                // "xAmzRequestId". With USE_STD_BEAN_NAMING, Jackson derives the property
                // name as "XAmzRequestId" (capital X), so the original "xAmzRequestId" key
                // is silently dropped during deserialization.
                // Fix: add @JsonProperty("xAmzRequestId") on the field or getter.
                args(S3ObjectLambdaEvent.class, "s3_object_lambda_event.json"));
    }

    private static Arguments args(Class<?> clazz, String fixture) {
        return Arguments.of(clazz.getSimpleName(), fixture, clazz);
    }
}
