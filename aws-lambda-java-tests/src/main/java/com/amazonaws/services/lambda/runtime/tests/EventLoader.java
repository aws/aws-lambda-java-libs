/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.apigateway.APIGatewayCustomAuthorizerEvent;
import com.amazonaws.services.lambda.runtime.events.apigateway.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.apigateway.APIGatewayV2CustomAuthorizerEvent;
import com.amazonaws.services.lambda.runtime.events.apigateway.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.dynamodb.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.kinesis.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.kinesis.KinesisFirehoseEvent;
import com.amazonaws.services.lambda.runtime.events.s3.S3Event;

import java.io.*;

import com.amazonaws.services.lambda.runtime.events.*;
import com.amazonaws.services.lambda.serialization.JacksonPojoSerializer;
import com.amazonaws.services.lambda.serialization.PojoSerializer;

/**
 * Load events from json files and serialize them in Events
 */
public class EventLoader {

    public static ActiveMQEvent loadActiveMQEvent(String filename) {
        return loadEvent(filename, ActiveMQEvent.class);
    }

    public static APIGatewayV2HTTPEvent loadApiGatewayHttpEvent(String filename) {
        return loadEvent(filename, APIGatewayV2HTTPEvent.class);
    }

    public static APIGatewayProxyRequestEvent loadApiGatewayRestEvent(String filename) {
        return loadEvent(filename, APIGatewayProxyRequestEvent.class);
    }

    public static APIGatewayCustomAuthorizerEvent loadAPIGatewayCustomAuthorizerEvent(String filename) {
        return loadEvent(filename, APIGatewayCustomAuthorizerEvent.class);
    }

    public static APIGatewayV2CustomAuthorizerEvent loadAPIGatewayV2CustomAuthorizerEvent(String filename) {
        return loadEvent(filename, APIGatewayV2CustomAuthorizerEvent.class);
    }

    public static ApplicationLoadBalancerRequestEvent loadApplicationLoadBalancerRequestEvent(String filename) {
        return loadEvent(filename, ApplicationLoadBalancerRequestEvent.class);
    }

    public static CloudFormationCustomResourceEvent loadCloudFormationCustomResourceEvent(String filename) {
        return loadEvent(filename, CloudFormationCustomResourceEvent.class);
    }

    public static CloudFrontEvent loadCloudFrontEvent(String filename) {
        return loadEvent(filename, CloudFrontEvent.class);
    }

    public static CloudWatchLogsEvent loadCloudWatchLogsEvent(String filename) {
        return loadEvent(filename, CloudWatchLogsEvent.class);
    }

    public static CodeCommitEvent loadCodeCommitEvent(String filename) {
        return loadEvent(filename, CodeCommitEvent.class);
    }

    public static CodePipelineEvent loadCodePipelineEvent(String filename) {
        return loadEvent(filename, CodePipelineEvent.class);
    }

    public static ConfigEvent loadConfigEvent(String filename) {
        return loadEvent(filename, ConfigEvent.class);
    }

    public static ConnectEvent loadConnectEvent(String filename) {
        return loadEvent(filename, ConnectEvent.class);
    }

    public static DynamodbEvent loadDynamoDbEvent(String filename) {
        return loadEvent(filename, DynamodbEvent.class);
    }

    public static KafkaEvent loadKafkaEvent(String filename) {
        return loadEvent(filename, KafkaEvent.class);
    }

    public static KinesisEvent loadKinesisEvent(String filename) {
        return loadEvent(filename, KinesisEvent.class);
    }

    public static KinesisFirehoseEvent loadKinesisFirehoseEvent(String filename) {
        return loadEvent(filename, KinesisFirehoseEvent.class);
    }

    public static LambdaDestinationEvent loadLambdaDestinationEvent(String filename) {
        return loadEvent(filename, LambdaDestinationEvent.class);
    }

    public static LexEvent loadLexEvent(String filename) {
        return loadEvent(filename, LexEvent.class);
    }

    public static S3Event loadS3Event(String filename) {
        return loadEvent(filename, S3Event.class);
    }

    public static SecretsManagerRotationEvent loadSecretsManagerRotationEvent(String filename) {
        return loadEvent(filename, SecretsManagerRotationEvent.class);
    }

    public static ScheduledEvent loadScheduledEvent(String filename) {
        return loadEvent(filename, ScheduledEvent.class);
    }

    public static SESEvent loadSESEvent(String filename) {
        return loadEvent(filename, SESEvent.class);
    }

    public static SNSEvent loadSNSEvent(String filename) {
        return loadEvent(filename, SNSEvent.class);
    }

    public static SQSEvent loadSQSEvent(String filename) {
        return loadEvent(filename, SQSEvent.class);
    }

    public static RabbitMQEvent loadRabbitMQEvent(String filename) {
        return loadEvent(filename, RabbitMQEvent.class);
    }

    public static <T> T loadEvent(String filename, Class<T> targetClass) {

        if (!filename.endsWith("json")) {
            throw new IllegalArgumentException("File " + filename + " must have json extension");
        }

        JacksonPojoSerializer serializer = JacksonPojoSerializer.getInstance();

        InputStream stream = serializer.getClass().getResourceAsStream(filename);
        if (stream == null) {
            stream = serializer.getClass().getClassLoader().getResourceAsStream(filename);
        }
        if (stream == null) {
            try {
                stream = new FileInputStream(filename);
            } catch (FileNotFoundException e) {
                throw new EventLoadingException("Cannot load " + filename, e);
            }
        }
        try {
            return serializer.fromJson(stream, targetClass);
        } finally {
            try {
                stream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
