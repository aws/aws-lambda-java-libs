/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.amazonaws.services.lambda.runtime.events;

import com.amazonaws.services.lambda.runtime.events.apigateway.*;
import com.amazonaws.services.lambda.runtime.events.cognito.*;
import com.amazonaws.services.lambda.runtime.events.dynamodb.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.dynamodb.DynamodbTimeWindowEvent;
import com.amazonaws.services.lambda.runtime.events.kinesis.*;
import com.amazonaws.services.lambda.runtime.events.s3.S3BatchEvent;
import com.amazonaws.services.lambda.runtime.events.s3.S3BatchResponse;
import com.amazonaws.services.lambda.runtime.events.s3.S3Event;
import com.amazonaws.services.lambda.runtime.events.s3.S3ObjectLambdaEvent;
import com.amazonaws.services.lambda.serialization.JacksonPojoSerializer;
import com.amazonaws.services.lambda.serialization.CustomPojoSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ServiceLoader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LambdaEventSerializersTest {

    private static final ObjectMapper OBJECT_MAPPER = JacksonPojoSerializer.getInstance().getMapper();
    private static final CustomPojoSerializer CUSTOM_POJO_SERIALIZER = loadSerializer();

    static public CustomPojoSerializer loadSerializer() {
        ServiceLoader<CustomPojoSerializer> loader = ServiceLoader.load(CustomPojoSerializer.class);

        return loader.iterator().next();
    }

    @Test
    public void testActiveMQEvent() throws IOException {
        String expected = EventUtils.readEvent("active_mq_event.json");
        String actual = deserializeSerializeJsonToString(expected, ActiveMQEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testAPIGatewayCustomAuthorizerEvent() throws IOException {
        String expected = EventUtils.readEvent("api_gateway_custom_authorizer_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayCustomAuthorizerEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testAPIGatewayProxyRequestEvent() throws IOException {
        String expected = EventUtils.readEvent("api_gateway_proxy_request_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayProxyRequestEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testAPIGatewayProxyResponseEvent() throws IOException {
        String expected = EventUtils.readEvent("api_gateway_proxy_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayProxyResponseEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testAPIGatewayV2CustomAuthorizerEvent() throws IOException {
        String expected = EventUtils.readEvent("api_gateway_v2_custom_authorizer_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayV2CustomAuthorizerEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testAPIGatewayV2HTTPEvent() throws IOException {
        String expected = EventUtils.readEvent("api_gateway_v2_http_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayV2HTTPEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testAPIGatewayV2HTTPResponse() throws IOException {
        String expected = EventUtils.readEvent("api_gateway_v2_http_response.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayV2HTTPResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testAPIGatewayV2WebSocketEvent() throws IOException {
        String expected = EventUtils.readEvent("api_gateway_v2_web_socket_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayV2WebSocketEvent.class);

        assertJsonEqual(expected, actual);
    }

        @Test
    public void testAPIGatewayV2WebSocketResponse() throws IOException {
        String expected = EventUtils.readEvent("api_gateway_v2_web_socket_response.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayV2WebSocketResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testApplicationLoadBalancerRequestEvent() throws IOException {
        String expected = EventUtils.readEvent("application_load_balancer_request_event.json");
        String actual = deserializeSerializeJsonToString(expected, ApplicationLoadBalancerRequestEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testApplicationLoadBalancerResponseEvent() throws IOException {
        String expected = EventUtils.readEvent("application_load_balancer_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, ApplicationLoadBalancerResponseEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testAppSyncLambdaAuthorizerEvent() throws IOException {
        String expected = EventUtils.readEvent("app_sync_lambda_authorizer_event.json");
        String actual = deserializeSerializeJsonToString(expected, AppSyncLambdaAuthorizerEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testAppSyncLambdaAuthorizerResponse() throws IOException {
        String expected = EventUtils.readEvent("app_sync_lambda_authorizer_response.json");
        String actual = deserializeSerializeJsonToString(expected, AppSyncLambdaAuthorizerResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCloudFormationCustomResourceEvent() throws IOException {
        String expected = EventUtils.readEvent("cloud_formation_custom_resource_event.json");
        String actual = deserializeSerializeJsonToString(expected, CloudFormationCustomResourceEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCloudFrontEvent() throws IOException {
        String expected = EventUtils.readEvent("cloud_front_event.json");
        String actual = deserializeSerializeJsonToString(expected, CloudFrontEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCloudWatchLogsEvent() throws IOException {
        String expected = EventUtils.readEvent("cloud_watch_logs_event.json");
        String actual = deserializeSerializeJsonToString(expected, CloudWatchLogsEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCodeCommitEvent() throws IOException {
        String expected = EventUtils.readEvent("code_commit_event.json");
        String actual = deserializeSerializeJsonToString(expected, CodeCommitEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoEvent() throws IOException {
        String expected = EventUtils.readEvent("cognito_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoUserPoolCreateAuthChallengeEvent() throws IOException {
        String expected = EventUtils.readEvent("cognito_user_pool_create_auth_challenge_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolCreateAuthChallengeEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoUserPoolCustomMessageEvent() throws IOException {
        String expected = EventUtils.readEvent("cognito_user_pool_custom_message_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolCustomMessageEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoUserPoolDefineAuthChallengeEvent() throws IOException {
        String expected = EventUtils.readEvent("cognito_user_pool_define_auth_challenge_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolDefineAuthChallengeEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoUserPoolMigrateUserEvent() throws IOException {
        String expected = EventUtils.readEvent("cognito_user_pool_migrate_user_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolMigrateUserEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoUserPoolPostAuthenticationEvent() throws IOException {
        String expected = EventUtils.readEvent("cognito_user_pool_post_authentication_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolPostAuthenticationEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoUserPoolPostConfirmationEvent() throws IOException {
        String expected = EventUtils.readEvent("cognito_user_pool_post_confirmation_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolPostConfirmationEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoUserPoolPreAuthenticationEvent() throws IOException {
        String expected = EventUtils.readEvent("cognito_user_pool_pre_authentication_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolPreAuthenticationEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoUserPoolPreSignUpEvent() throws IOException {
        String expected = EventUtils.readEvent("cognito_user_pool_pre_sign_up_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolPreSignUpEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoUserPoolPreTokenGenerationEvent() throws IOException {
        String expected = EventUtils.readEvent("cognito_user_pool_pre_token_generation_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolPreTokenGenerationEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testCognitoUserPoolVerifyAuthChallengeResponseEvent() throws IOException {
        String expected = EventUtils.readEvent("cognito_user_pool_verify_auth_challenge_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolVerifyAuthChallengeResponseEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testConfigEvent() throws IOException {
        String expected = EventUtils.readEvent("config_event.json");
        String actual = deserializeSerializeJsonToString(expected, ConfigEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testConnectEvent() throws IOException {
        String expected = EventUtils.readEvent("connect_event.json");
        String actual = deserializeSerializeJsonToString(expected, ConnectEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testDynamodbEvent() throws IOException {
        String expected = EventUtils.readEvent("dynamodb_event.json");
        String actual = deserializeSerializeJsonToString(expected, DynamodbEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testDynamodbTimeWindowEvent() throws IOException {
        String expected = EventUtils.readEvent("dynamodb_time_window_event.json");
        String actual = deserializeSerializeJsonToString(expected, DynamodbTimeWindowEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testIamPolicyResponse() throws IOException {
        String expected = EventUtils.readEvent("iam_policy_response.json");
        String actual = deserializeSerializeJsonToString(expected, IamPolicyResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testIamPolicyResponseV1() throws IOException {
        String expected = EventUtils.readEvent("iam_policy_response_v1.json");
        String actual = deserializeSerializeJsonToString(expected, IamPolicyResponseV1.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testIoTButtonEvent() throws IOException {
        String expected = EventUtils.readEvent("iot_button_event.json");
        String actual = deserializeSerializeJsonToString(expected, IoTButtonEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKafkaEvent() throws IOException {
        String expected = EventUtils.readEvent("kafka_event.json");
        String actual = deserializeSerializeJsonToString(expected, KafkaEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisAnalyticsFirehoseInputPreprocessingEvent() throws IOException {
        String expected = EventUtils.readEvent("kinesis_analytics_firehose_input_preprocessing_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsFirehoseInputPreprocessingEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisAnalyticsInputPreprocessingResponse() throws IOException {
        String expected = EventUtils.readEvent("kinesis_analytics_input_preprocessing_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsInputPreprocessingResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisAnalyticsOutputDeliveryEvent() throws IOException {
        String expected = EventUtils.readEvent("kinesis_analytics_output_delivery_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsOutputDeliveryEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisAnalyticsOutputDeliveryResponse() throws IOException {
        String expected = EventUtils.readEvent("kinesis_analytics_output_delivery_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsOutputDeliveryResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisAnalyticsStreamsInputPreprocessingEvent() throws IOException {
        String expected = EventUtils.readEvent("kinesis_analytics_streams_input_preprocessing_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsStreamsInputPreprocessingEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisEvent() throws IOException {
        String expected = EventUtils.readEvent("kinesis_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisFirehoseEvent() throws IOException {
        String expected = EventUtils.readEvent("kinesis_firehose_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisFirehoseEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testKinesisTimeWindowEvent() throws IOException {
        String expected = EventUtils.readEvent("kinesis_time_window_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisTimeWindowEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testLambdaDestinationEvent() throws IOException {
        String expected = EventUtils.readEvent("lambda_destination_event.json");
        String actual = deserializeSerializeJsonToString(expected, LambdaDestinationEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testLexEvent() throws IOException {
        String expected = EventUtils.readEvent("lex_event.json");
        String actual = deserializeSerializeJsonToString(expected, LexEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testRabbitMQ() throws IOException {
        String expected = EventUtils.readEvent("rabbit_mq.json");
        String actual = deserializeSerializeJsonToString(expected, RabbitMQEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testS3BatchEvent() throws IOException {
        String expected = EventUtils.readEvent("s3_batch_event.json");
        String actual = deserializeSerializeJsonToString(expected, S3BatchEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testS3BatchResponse() throws IOException {
        String expected = EventUtils.readEvent("s3_batch_response.json");
        String actual = deserializeSerializeJsonToString(expected, S3BatchResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testS3Event() throws IOException {
        String expected = EventUtils.readEvent("s3_event.json");
        String actual = deserializeSerializeJsonToString(expected, S3Event.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testS3ObjectLambdaEvent() throws IOException {
        String expected = EventUtils.readEvent("s3_object_lambda_event.json");
        String actual = deserializeSerializeJsonToString(expected, S3ObjectLambdaEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testScheduledEvent() throws IOException {
        String expected = EventUtils.readEvent("scheduled_event.json");
        String actual = deserializeSerializeJsonToString(expected, ScheduledEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testSecretsManagerRotationEvent() throws IOException {
        String expected = EventUtils.readEvent("secrets_manager_rotation_event.json");
        String actual = deserializeSerializeJsonToString(expected, SecretsManagerRotationEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testSimpleIAMPolicyResponse() throws IOException {
        String expected = EventUtils.readEvent("simple_iam_policy_response.json");
        String actual = deserializeSerializeJsonToString(expected, SimpleIAMPolicyResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testSNSEvent() throws IOException {
        String expected = EventUtils.readEvent("sns_event.json");
        String actual = deserializeSerializeJsonToString(expected, SNSEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testSQSBatchResponse() throws IOException {
        String expected = EventUtils.readEvent("sqs_batch_response.json");
        String actual = deserializeSerializeJsonToString(expected, SQSBatchResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testSQSEvent() throws IOException {
        String expected = EventUtils.readEvent("sqs_event.json");
        String actual = deserializeSerializeJsonToString(expected, SQSEvent.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testStreamsEventResponse() throws IOException {
        String expected = EventUtils.readEvent("streams_event_response.json");
        String actual = deserializeSerializeJsonToString(expected, StreamsEventResponse.class);

        assertJsonEqual(expected, actual);
    }

    @Test
    public void testTimeWindowEventResponse() throws IOException {
        String expected = EventUtils.readEvent("time_window_event_response.json");
        String actual = deserializeSerializeJsonToString(expected, TimeWindowEventResponse.class);

        assertJsonEqual(expected, actual);
    }

    private <T> String deserializeSerializeJsonToString(String expected, Class<T> modelClass) throws IOException {
        T event = CUSTOM_POJO_SERIALIZER.fromJson(expected, modelClass);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        CUSTOM_POJO_SERIALIZER.toJson(event, baos, modelClass);
        return EventUtils.bytesToString(baos.toByteArray());
    }

    private void assertJsonEqual(String expected, String actual) throws IOException {
        Assertions.assertEquals(OBJECT_MAPPER.readTree(expected), OBJECT_MAPPER.readTree(actual));
    }

}
