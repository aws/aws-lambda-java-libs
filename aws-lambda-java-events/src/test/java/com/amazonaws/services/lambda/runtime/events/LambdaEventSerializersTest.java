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
import com.amazonaws.services.lambda.runtime.CustomPojoSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ServiceLoader;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

public class LambdaEventSerializersTest {

    private static final CustomPojoSerializer CUSTOM_POJO_SERIALIZER = loadSerializer();

    static public CustomPojoSerializer loadSerializer() {
        ServiceLoader<CustomPojoSerializer> loader = ServiceLoader.load(CustomPojoSerializer.class);

        return loader.iterator().next();
    }

    @Test
    public void testActiveMQEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("active_mq_event.json");
        String actual = deserializeSerializeJsonToString(expected, ActiveMQEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testAPIGatewayCustomAuthorizerEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("api_gateway_custom_authorizer_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayCustomAuthorizerEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testAPIGatewayProxyRequestEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("api_gateway_proxy_request_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayProxyRequestEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testAPIGatewayProxyResponseEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("api_gateway_proxy_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayProxyResponseEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testAPIGatewayV2CustomAuthorizerEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("api_gateway_v2_custom_authorizer_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayV2CustomAuthorizerEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testAPIGatewayV2HTTPEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("api_gateway_v2_http_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayV2HTTPEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testAPIGatewayV2HTTPResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("api_gateway_v2_http_response.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayV2HTTPResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testAPIGatewayV2WebSocketEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("api_gateway_v2_web_socket_event.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayV2WebSocketEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testAPIGatewayV2WebSocketResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("api_gateway_v2_web_socket_response.json");
        String actual = deserializeSerializeJsonToString(expected, APIGatewayV2WebSocketResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testApplicationLoadBalancerRequestEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("application_load_balancer_request_event.json");
        String actual = deserializeSerializeJsonToString(expected, ApplicationLoadBalancerRequestEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testApplicationLoadBalancerResponseEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("application_load_balancer_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, ApplicationLoadBalancerResponseEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testAppSyncLambdaAuthorizerEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("app_sync_lambda_authorizer_event.json");
        String actual = deserializeSerializeJsonToString(expected, AppSyncLambdaAuthorizerEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testAppSyncLambdaAuthorizerResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("app_sync_lambda_authorizer_response.json");
        String actual = deserializeSerializeJsonToString(expected, AppSyncLambdaAuthorizerResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCloudFormationCustomResourceEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cloud_formation_custom_resource_event.json");
        String actual = deserializeSerializeJsonToString(expected, CloudFormationCustomResourceEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCloudFrontEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cloud_front_event.json");
        String actual = deserializeSerializeJsonToString(expected, CloudFrontEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCloudWatchLogsEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cloud_watch_logs_event.json");
        String actual = deserializeSerializeJsonToString(expected, CloudWatchLogsEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCodeCommitEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("code_commit_event.json");
        String actual = deserializeSerializeJsonToString(expected, CodeCommitEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCodePipelineEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("code_pipeline_event.json");
        String actual = deserializeSerializeJsonToString(expected, CodePipelineEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCognitoEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cognito_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCognitoUserPoolCreateAuthChallengeEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cognito_user_pool_create_auth_challenge_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolCreateAuthChallengeEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCognitoUserPoolCustomMessageEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cognito_user_pool_custom_message_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolCustomMessageEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCognitoUserPoolDefineAuthChallengeEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cognito_user_pool_define_auth_challenge_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolDefineAuthChallengeEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCognitoUserPoolMigrateUserEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cognito_user_pool_migrate_user_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolMigrateUserEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCognitoUserPoolPostAuthenticationEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cognito_user_pool_post_authentication_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolPostAuthenticationEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCognitoUserPoolPostConfirmationEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cognito_user_pool_post_confirmation_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolPostConfirmationEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCognitoUserPoolPreAuthenticationEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cognito_user_pool_pre_authentication_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolPreAuthenticationEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCognitoUserPoolPreSignUpEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cognito_user_pool_pre_sign_up_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolPreSignUpEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCognitoUserPoolPreTokenGenerationEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cognito_user_pool_pre_token_generation_event.json");
        String actual = deserializeSerializeJsonToString(expected, CognitoUserPoolPreTokenGenerationEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testCognitoUserPoolVerifyAuthChallengeResponseEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("cognito_user_pool_verify_auth_challenge_response_event.json");
        String actual = deserializeSerializeJsonToString(expected,
                CognitoUserPoolVerifyAuthChallengeResponseEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testConfigEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("config_event.json");
        String actual = deserializeSerializeJsonToString(expected, ConfigEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testConnectEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("connect_event.json");
        String actual = deserializeSerializeJsonToString(expected, ConnectEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testDynamodbEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("dynamodb_event.json");
        String actual = deserializeSerializeJsonToString(expected, DynamodbEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testDynamodbTimeWindowEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("dynamodb_time_window_event.json");
        String actual = deserializeSerializeJsonToString(expected, DynamodbTimeWindowEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testEventBridgeEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("eventbridge_event.json");
        String actual = deserializeSerializeJsonToString(expected, EventBridgeEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testIamPolicyResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("iam_policy_response.json");
        String actual = deserializeSerializeJsonToString(expected, IamPolicyResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testIamPolicyResponseV1() throws IOException, JSONException {
        String expected = EventUtils.readEvent("iam_policy_response_v1.json");
        String actual = deserializeSerializeJsonToString(expected, IamPolicyResponseV1.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testIoTButtonEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("iot_button_event.json");
        String actual = deserializeSerializeJsonToString(expected, IoTButtonEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testIoTCustomAuthorizerEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("iot_custom_authorizer_event.json");
        String actual = deserializeSerializeJsonToString(expected, IoTCustomAuthorizerEvent.class);

        assertEquals(expected, actual, STRICT);
    }
    
    @Test
    public void testIoTCustomAuthorizerResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("iot_custom_authorizer_response.json");
        String actual = deserializeSerializeJsonToString(expected, IoTCustomAuthorizerResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testKafkaEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("kafka_event.json");
        String actual = deserializeSerializeJsonToString(expected, KafkaEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testKinesisAnalyticsFirehoseInputPreprocessingEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("kinesis_analytics_firehose_input_preprocessing_event.json");
        String actual = deserializeSerializeJsonToString(expected,
                KinesisAnalyticsFirehoseInputPreprocessingEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testKinesisAnalyticsInputPreprocessingResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("kinesis_analytics_input_preprocessing_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsInputPreprocessingResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testKinesisAnalyticsOutputDeliveryEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("kinesis_analytics_output_delivery_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsOutputDeliveryEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testKinesisAnalyticsOutputDeliveryResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("kinesis_analytics_output_delivery_response_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisAnalyticsOutputDeliveryResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testKinesisAnalyticsStreamsInputPreprocessingEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("kinesis_analytics_streams_input_preprocessing_event.json");
        String actual = deserializeSerializeJsonToString(expected,
                KinesisAnalyticsStreamsInputPreprocessingEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testKinesisEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("kinesis_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testKinesisFirehoseEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("kinesis_firehose_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisFirehoseEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testKinesisTimeWindowEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("kinesis_time_window_event.json");
        String actual = deserializeSerializeJsonToString(expected, KinesisTimeWindowEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testLambdaDestinationEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("lambda_destination_event.json");
        String actual = deserializeSerializeJsonToString(expected, LambdaDestinationEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testLexEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("lex_event.json");
        String actual = deserializeSerializeJsonToString(expected, LexEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testLexV2Event() throws IOException, JSONException {
        String expected = EventUtils.readEvent("lex_v2_event.json");
        String actual = deserializeSerializeJsonToString(expected, LexV2Event.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testLexV2KendraSentimentEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("lex_v2_kendra_sentiment_event.json");
        String actual = deserializeSerializeJsonToString(expected, LexV2Event.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testLexV2Response() throws IOException, JSONException {
        String expected = EventUtils.readEvent("lex_v2_response.json");
        String actual = deserializeSerializeJsonToString(expected, LexV2Response.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testRabbitMQ() throws IOException, JSONException {
        String expected = EventUtils.readEvent("rabbit_mq.json");
        String actual = deserializeSerializeJsonToString(expected, RabbitMQEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testS3BatchEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("s3_batch_event.json");
        String actual = deserializeSerializeJsonToString(expected, S3BatchEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testS3BatchResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("s3_batch_response.json");
        String actual = deserializeSerializeJsonToString(expected, S3BatchResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testS3Event() throws IOException, JSONException {
        String expected = EventUtils.readEvent("s3_event.json");
        String actual = deserializeSerializeJsonToString(expected, S3Event.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testS3ObjectLambdaEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("s3_object_lambda_event.json");
        String actual = deserializeSerializeJsonToString(expected, S3ObjectLambdaEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testScheduledEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("scheduled_event.json");
        String actual = deserializeSerializeJsonToString(expected, ScheduledEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testSecretsManagerRotationEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("secrets_manager_rotation_event.json");
        String actual = deserializeSerializeJsonToString(expected, SecretsManagerRotationEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testSimpleIAMPolicyResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("simple_iam_policy_response.json");
        String actual = deserializeSerializeJsonToString(expected, SimpleIAMPolicyResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testSNSEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("sns_event.json");
        String actual = deserializeSerializeJsonToString(expected, SNSEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testSQSBatchResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("sqs_batch_response.json");
        String actual = deserializeSerializeJsonToString(expected, SQSBatchResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testSQSEvent() throws IOException, JSONException {
        String expected = EventUtils.readEvent("sqs_event.json");
        String actual = deserializeSerializeJsonToString(expected, SQSEvent.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testStreamsEventResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("streams_event_response.json");
        String actual = deserializeSerializeJsonToString(expected, StreamsEventResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    @Test
    public void testTimeWindowEventResponse() throws IOException, JSONException {
        String expected = EventUtils.readEvent("time_window_event_response.json");
        String actual = deserializeSerializeJsonToString(expected, TimeWindowEventResponse.class);

        assertEquals(expected, actual, STRICT);
    }

    private <T> String deserializeSerializeJsonToString(String expected, Class<T> modelClass) {
        T event = CUSTOM_POJO_SERIALIZER.fromJson(expected, modelClass);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        CUSTOM_POJO_SERIALIZER.toJson(event, baos, modelClass);
        return EventUtils.bytesToString(baos.toByteArray());
    }
}
