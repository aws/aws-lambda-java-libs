/* Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved. */
package com.amazonaws.services.lambda.runtime.tests;

import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.Record;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.time.Instant.ofEpochSecond;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.from;

import com.amazonaws.services.lambda.runtime.events.*;

public class EventLoaderTest {

    @Test
    public void testLoadApiGatewayRestEvent() {
        APIGatewayProxyRequestEvent event = EventLoader.loadApiGatewayRestEvent("apigw_rest_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getBody()).isEqualTo("Hello from Lambda!");
        assertThat(event.getHeaders()).hasSize(2);
    }

    @Test
    public void testLoadApiGatewayHttpEvent() {
        APIGatewayV2HTTPEvent event = EventLoader.loadApiGatewayHttpEvent("apigw_http_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getBody()).isEqualTo("Hello from Lambda!!");
    }

    @Test
    public void testLoadAPIGatewayCustomAuthorizerEvent() {
        APIGatewayCustomAuthorizerEvent event = EventLoader.loadAPIGatewayCustomAuthorizerEvent("apigw_auth.json");

        assertThat(event).isNotNull();
        assertThat(event.getRequestContext().getHttpMethod()).isEqualTo("GET");
        assertThat(event.getHeaders()).hasSize(8);
    }

    @Test
    public void testLoadAPIGatewayV2CustomAuthorizerEvent() {
        APIGatewayV2CustomAuthorizerEvent event = EventLoader.loadAPIGatewayV2CustomAuthorizerEvent("apigw_auth_v2.json");

        assertThat(event).isNotNull();
        assertThat(event.getRequestContext().getHttp().getMethod()).isEqualTo("POST");
        assertThat(event.getRequestContext().getTimeEpoch()).isEqualTo(Instant.ofEpochMilli(1583348638390L));
    }

    @Test
    public void testLoadApplicationLoadBalancerRequestEvent() {
        ApplicationLoadBalancerRequestEvent event = EventLoader.loadApplicationLoadBalancerRequestEvent("elb_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getBody()).isEqualTo("Hello from ELB");
    }

    @Test
    public void testLoadConfigEvent() {
        ConfigEvent event = EventLoader.loadConfigEvent("config_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getConfigRuleArn()).isEqualTo("arn:aws:config:eu-central-1:123456789012:config-rule/config-rule-0123456");
        assertThat(event.getConfigRuleName()).isEqualTo("change-triggered-config-rule");
    }

    @Test
    public void testLoadKafkaEvent() {
        KafkaEvent event = EventLoader.loadKafkaEvent("kafka_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getEventSourceArn()).isEqualTo("arn:aws:kafka:us-east-1:123456789012:cluster/vpc-3432434/4834-3547-3455-9872-7929");
        assertThat(event.getBootstrapServers()).isEqualTo("b-2.demo-cluster-1.a1bcde.c1.kafka.us-east-1.amazonaws.com:9092,b-1.demo-cluster-1.a1bcde.c1.kafka.us-east-1.amazonaws.com:9092");

        KafkaEvent.KafkaEventRecord record = event.getRecords().get("mytopic-01").get(0);
        assertThat(record.getValue()).decodedAsBase64().asString().isEqualTo("Hello from Kafka !!");
         
        String headerValue = new String(record.getHeaders().get(0).get("headerKey"));    
        assertThat(headerValue).isEqualTo("headerValue");
    }

    @Test
    public void testLoadLambdaDestinationEvent() {
        LambdaDestinationEvent event = EventLoader.loadLambdaDestinationEvent("lambda_destination_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getTimestamp()).isEqualTo(DateTime.parse("2019-11-24T21:52:47.333Z"));
        assertThat(event.getRequestContext().getFunctionArn()).isEqualTo("arn:aws:lambda:sa-east-1:123456678912:function:event-destinations:$LATEST");
        assertThat(event.getRequestPayload().get("Success")).isEqualTo(false);
    }

    @Test
    public void testLoadLexEvent() {
        LexEvent event = EventLoader.loadLexEvent("lex_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getInvocationSource()).isEqualTo("DialogCodeHook");
        assertThat(event.getSessionAttributes()).hasSize(1);
        assertThat(event.getCurrentIntent().getName()).isEqualTo("BookHotel");
        assertThat(event.getCurrentIntent().getSlots()).hasSize(4);
        assertThat(event.getBot().getName()).isEqualTo("BookTrip");
    }

    @Test
    public void testLoadKinesisFirehoseEvent() {
        KinesisFirehoseEvent event = EventLoader.loadKinesisFirehoseEvent("firehose_event.json");

        assertThat(event).isNotNull();
        assertThat(event.getDeliveryStreamArn()).isEqualTo("arn:aws:kinesis:EXAMPLE");
        assertThat(event.getRecords()).hasSize(1);
        assertThat(event.getRecords().get(0).getData().array()).asString().isEqualTo("Hello, this is a test 123.");
    }

    @Test
    public void testLoadS3Event() {
        S3Event event = EventLoader.loadS3Event("s3_event.json");
        assertThat(event).isNotNull();
        assertThat(event.getRecords()).hasSize(1);
    }

    @Test
    public void testLoadSQSEvent() {
        SQSEvent event = EventLoader.loadSQSEvent("sqs/sqs_event_nobody.json");
        assertThat(event).isNotNull();
        assertThat(event.getRecords()).hasSize(1);
        assertThat(event.getRecords().get(0).getEventSourceArn()).isEqualTo("arn:aws:sqs:eu-central-1:123456789012:TestLambda");
    }

    @Test
    public void testLoadSNSEvent() {
        SNSEvent event = EventLoader.loadSNSEvent("sns_event.json");
        assertThat(event).isNotNull();
        assertThat(event.getRecords()).hasSize(1);

        SNSEvent.SNSRecord record = event.getRecords().get(0);
        assertThat(record.getEventSource()).isEqualTo("aws:sns");
        assertThat(record.getEventVersion()).isEqualTo("1.0");
        assertThat(record.getEventSubscriptionArn()).isEqualTo("arn:aws:sns:eu-central-1:123456789012:TopicSendToMe:e3ddc7d5-2f86-40b8-a13d-3362f94fd8dd");

        SNSEvent.SNS sns = record.getSNS();
        assertThat(sns)
                .returns("Test sns message", from(SNSEvent.SNS::getSubject))
                .returns("{\n  \"id\": 42,\n  \"name\": \"Bob\"\n}", from(SNSEvent.SNS::getMessage))
                .returns("arn:aws:sns:eu-central-1:123456789012:TopicSendToMe", from(SNSEvent.SNS::getTopicArn))
                .returns("dc918f50-80c6-56a2-ba33-d8a9bbf013ab", from(SNSEvent.SNS::getMessageId))
                .returns(DateTime.parse("2020-10-08T16:06:14.656Z"), from(SNSEvent.SNS::getTimestamp))
                .returns("https://sns.eu-central-1.amazonaws.com/?Action=Unsubscribe", SNSEvent.SNS::getUnsubscribeUrl);
        assertThat(sns.getMessageAttributes()).containsKey("name");
        assertThat(sns.getMessageAttributes().get("name").getValue()).isEqualTo("Bob");
        assertThat(sns.getMessageAttributes().get("name").getType()).isEqualTo("String");
    }

    @Test
    public void testLoadDynamoEvent() {
        DynamodbEvent event = EventLoader.loadDynamoDbEvent("dynamo_event.json");
        assertThat(event).isNotNull();
        assertThat(event.getRecords()).hasSize(3);

        DynamodbEvent.DynamodbStreamRecord record = event.getRecords().get(0);
        assertThat(record)
                .returns("arn:aws:dynamodb:eu-central-1:123456789012:table/ExampleTableWithStream/stream/2015-06-27T00:48:05.899", from(DynamodbEvent.DynamodbStreamRecord::getEventSourceARN))
                .returns("INSERT", from(Record::getEventName));

        StreamRecord streamRecord = record.getDynamodb();
        assertThat(streamRecord)
                .returns("4421584500000000017450439091", StreamRecord::getSequenceNumber)
                .returns(26L, StreamRecord::getSizeBytes)
                .returns("NEW_AND_OLD_IMAGES", StreamRecord::getStreamViewType)
                .returns(Date.from(ofEpochSecond(1428537600)), StreamRecord::getApproximateCreationDateTime);

        assertThat(streamRecord.getKeys()).contains(entry("Id", new AttributeValue().withN("101")));
        assertThat(streamRecord.getNewImage()).containsAnyOf(
                entry("Message", new AttributeValue("New item!")),
                entry("Id", new AttributeValue().withN("101"))
        );
    }

    @Test
    public void testLoadKinesisEvent() {
        KinesisEvent event = EventLoader.loadKinesisEvent("kinesis_event.json");
        assertThat(event).isNotNull();
        assertThat(event.getRecords()).hasSize(1);

        KinesisEvent.Record record = event.getRecords().get(0).getKinesis();
        assertThat(record.getEncryptionType()).isEqualTo("NONE");
        assertThat(record.getApproximateArrivalTimestamp()).isEqualTo(Date.from(ofEpochSecond(1428537600)));
        assertThat(new String(record.getData().array())).isEqualTo("Hello, this is a test 123.");
    }

    @Test
    public void testLoadActiveMQEvent() {
        ActiveMQEvent event = EventLoader.loadActiveMQEvent("mq_event.json");
        assertThat(event).isNotNull();
        assertThat(event.getMessages()).hasSize(2);

        assertThat(event.getMessages().get(0).getMessageID()).isEqualTo("ID:b-9bcfa592-423a-4942-879d-eb284b418fc8-1.mq.us-west-2.amazonaws.com-37557-1234520418293-4:1:1:1:1");
        assertThat(event.getMessages().get(1).getMessageID()).isEqualTo("ID:b-8bcfa572-428a-4642-879d-eb284b418fc8-1.mq.us-west-2.amazonaws.com-37557-1234520418293-4:1:1:1:1");
    }

    @Test
    public void testLoadCodeCommitEvent() {
        CodeCommitEvent event = EventLoader.loadCodeCommitEvent("codecommit_event.json");
        assertThat(event).isNotNull();
        assertThat(event.getRecords()).hasSize(1);

        CodeCommitEvent.Record record = event.getRecords().get(0);
        assertThat(record.getEventSourceArn()).isEqualTo("arn:aws:codecommit:eu-central-1:123456789012:my-repo");
        assertThat(record.getUserIdentityArn()).isEqualTo("arn:aws:iam::123456789012:root");
        assertThat(record.getEventTime()).isEqualTo(DateTime.parse("2016-01-01T23:59:59.000+0000"));

        assertThat(record.getCodeCommit().getReferences()).hasSize(1);
        CodeCommitEvent.Reference reference = record.getCodeCommit().getReferences().get(0);
        assertThat(reference.getCommit()).isEqualTo("5c4ef1049f1d27deadbeeff313e0730018be182b");
        assertThat(reference.getRef()).isEqualTo("refs/heads/master");
    }

    @Test
    public void testLoadCloudWatchLogsEvent() {
        CloudWatchLogsEvent cloudWatchLogsEvent = EventLoader.loadCloudWatchLogsEvent("cloudwatchlogs_event.json");
        assertThat(cloudWatchLogsEvent).isNotNull();
        assertThat(cloudWatchLogsEvent.getAwsLogs().getData()).isEqualTo("H4sIAAAAAAAAAHWPwQqCQBCGX0Xm7EFtK+smZBEUgXoLCdMhFtKV3akI8d0bLYmibvPPN3wz00CJxmQnTO41whwWQRIctmEcB6sQbFC3CjW3XW8kxpOpP+OC22d1Wml1qZkQGtoMsScxaczKN3plG8zlaHIta5KqWsozoTYw3/djzwhpLwivWFGHGpAFe7DL68JlBUk+l7KSN7tCOEJ4M3/qOI49vMHj+zCKdlFqLaU2ZHV2a4Ct/an0/ivdX8oYc1UVX860fQDQiMdxRQEAAA==");
    }

    @Test
    public void testLoadCloudFrontEvent() {
        CloudFrontEvent event = EventLoader.loadCloudFrontEvent("cloudfront_event.json");
        assertThat(event).isNotNull();
        assertThat(event.getRecords()).hasSize(1);
        assertThat(event.getRecords().get(0).getCf().getConfig().getDistributionId()).isEqualTo("EXAMPLE");
    }

    @Test
    public void testLoadScheduledEvent() {
        ScheduledEvent event = EventLoader.loadScheduledEvent("cloudwatch_event.json");
        assertThat(event).isNotNull();
        assertThat(event.getDetailType()).isEqualTo("Scheduled Event");
        assertThat(event.getTime()).isEqualTo(DateTime.parse("2020-09-30T15:58:34Z"));
    }

    @Test
    public void testLoadConnectEvent() {
        ConnectEvent event = EventLoader.loadConnectEvent("connect_event.json");
        assertThat(event).isNotNull();

        ConnectEvent.ContactData contactData = event.getDetails().getContactData();
        assertThat(contactData)
                .returns("VOICE", from(ConnectEvent.ContactData::getChannel))
                .returns("5ca32fbd-8f92-46af-92a5-6b0f970f0efe", from(ConnectEvent.ContactData::getContactId))
                .returns("6ca32fbd-8f92-46af-92a5-6b0f970f0efe", from(ConnectEvent.ContactData::getInitialContactId))
                .returns("API", from(ConnectEvent.ContactData::getInitiationMethod))
                .returns("arn:aws:connect:eu-central-1:123456789012:instance/9308c2a1-9bc6-4cea-8290-6c0b4a6d38fa", from(ConnectEvent.ContactData::getInstanceArn))
                .returns("4ca32fbd-8f92-46af-92a5-6b0f970f0efe", from(ConnectEvent.ContactData::getPreviousContactId));

        assertThat(contactData.getCustomerEndpoint())
                .returns("+11234567890",from(ConnectEvent.CustomerEndpoint::getAddress))
                .returns("TELEPHONE_NUMBER",from(ConnectEvent.CustomerEndpoint::getType));

        assertThat(contactData.getSystemEndpoint())
                .returns("+21234567890",from(ConnectEvent.SystemEndpoint::getAddress))
                .returns("TELEPHONE_NUMBER",from(ConnectEvent.SystemEndpoint::getType));
    }

    @Test
    public void testLoadCloudFormationCustomResourceEvent() {
        CloudFormationCustomResourceEvent event = EventLoader.loadCloudFormationCustomResourceEvent("cloudformation_event.json");
        assertThat(event).isNotNull();
        assertThat(event)
                .returns("Update", from(CloudFormationCustomResourceEvent::getRequestType))
                .returns("http://pre-signed-S3-url-for-response", from(CloudFormationCustomResourceEvent::getResponseUrl))
                .returns("arn:aws:cloudformation:eu-central-1:123456789012:stack/MyStack/guid", from(CloudFormationCustomResourceEvent::getStackId))
                .returns("unique id for this create request", from(CloudFormationCustomResourceEvent::getRequestId))
                .returns("Custom::TestResource", from(CloudFormationCustomResourceEvent::getResourceType))
                .returns("MyTestResource", from(CloudFormationCustomResourceEvent::getLogicalResourceId))
                .returns("MyTestResourceId", from(CloudFormationCustomResourceEvent::getPhysicalResourceId))
                .returns("abcd", from(CloudFormationCustomResourceEvent::getServiceToken));

        Map<String, Object> resourceProperties = event.getResourceProperties();
        assertThat(resourceProperties).hasSize(2);
        assertThat(resourceProperties.get("StackName")).isEqualTo("MyStack");
        assertThat(resourceProperties.get("List")).isInstanceOf(List.class);
        assertThat(resourceProperties.get("List")).asList().hasSize(3);

        Map<String, Object> oldResourceProperties = event.getOldResourceProperties();
        assertThat(oldResourceProperties).hasSize(2);
        assertThat(oldResourceProperties.get("StackName")).isEqualTo("MyStack");
        assertThat(oldResourceProperties.get("List")).isInstanceOf(List.class);
        assertThat(oldResourceProperties.get("List")).asList().hasSize(1);
    }

    @Test
    public void testLoadSecretsManagerRotationEvent() {
        SecretsManagerRotationEvent event = EventLoader.loadSecretsManagerRotationEvent("secrets_rotation_event.json");
        assertThat(event).isNotNull();
        assertThat(event)
                .returns("123e4567-e89b-12d3-a456-426614174000", from(SecretsManagerRotationEvent::getClientRequestToken))
                .returns("arn:aws:secretsmanager:eu-central-1:123456789012:secret:/powertools/secretparam-xBPaJ5", from(SecretsManagerRotationEvent::getSecretId))
                .returns("CreateSecret", from(SecretsManagerRotationEvent::getStep));
    }

    @Test
    public void testLoadRabbitMQEvent() {
        RabbitMQEvent event = EventLoader.loadRabbitMQEvent("rabbitmq_event.json");
        assertThat(event).isNotNull();
        assertThat(event)
                .returns("aws:rmq", from(RabbitMQEvent::getEventSource))
                .returns("arn:aws:mq:us-west-2:112556298976:broker:test:b-9bcfa592-423a-4942-879d-eb284b418fc8", from(RabbitMQEvent::getEventSourceArn));

        Map<String, List<RabbitMQEvent.RabbitMessage>> messagesByQueue = event.getRmqMessagesByQueue();
        assertThat(messagesByQueue).isNotEmpty();
        List<RabbitMQEvent.RabbitMessage> messages = messagesByQueue.get("test::/");
        assertThat(messages).hasSize(1);
        RabbitMQEvent.RabbitMessage firstMessage = messages.get(0);
        assertThat(firstMessage)
                .returns(false, RabbitMQEvent.RabbitMessage::getRedelivered)
                .returns("eyJ0aW1lb3V0IjowLCJkYXRhIjoiQ1pybWYwR3c4T3Y0YnFMUXhENEUifQ==", RabbitMQEvent.RabbitMessage::getData);

        RabbitMQEvent.BasicProperties basicProperties = firstMessage.getBasicProperties();
        assertThat(basicProperties)
                .returns("text/plain", from(RabbitMQEvent.BasicProperties::getContentType))
                .returns(1, from(RabbitMQEvent.BasicProperties::getDeliveryMode))
                .returns(34, from(RabbitMQEvent.BasicProperties::getPriority))
                .returns(60000, from(RabbitMQEvent.BasicProperties::getExpiration))
                .returns("AIDACKCEVSQ6C2EXAMPLE", from(RabbitMQEvent.BasicProperties::getUserId))
                .returns(80, from(RabbitMQEvent.BasicProperties::getBodySize))
                .returns("Jan 1, 1970, 12:33:41 AM", from(RabbitMQEvent.BasicProperties::getTimestamp));

        Map<String, Object> headers = basicProperties.getHeaders();
        assertThat(headers).hasSize(3);
        Map<String, List<Integer>> header1 = (Map<String, List<Integer>>) headers.get("header1");
        assertThat(header1.get("bytes")).contains(118, 97, 108, 117, 101, 49);
        assertThat((Integer) headers.get("numberInHeader")).isEqualTo(10);
    }
}
