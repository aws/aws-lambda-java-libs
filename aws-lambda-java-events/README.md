# AWS Lambda Java Events v3

### Event Models Supported
* `ActiveMQEvent`
* `APIGatewayCustomAuthorizerEvent`
* `APIGatewayProxyRequestEvent`
* `APIGatewayProxyResponseEvent`
* `APIGatewayV2CustomAuthorizerEvent`
* `APIGatewayV2HTTPEvent`
* `APIGatewayV2HTTPResponse`
* `APIGatewayV2WebSocketEvent`
* `APIGatewayV2WebSocketResponse`
* `ApplicationLoadBalancerRequestEvent`
* `ApplicationLoadBalancerResponseEvent`
* `AppSyncLambdaAuthorizerEvent`
* `AppSyncLambdaAuthorizerResponse`
* `CloudFormationCustomResourceEvent`
* `CloudFrontEvent`
* `CloudWatchCompositeAlarmEvent`
* `CloudWatchLogsEvent`
* `CloudWatchMetricAlarmEvent`
* `CodeCommitEvent`
* `CognitoEvent`
* `CognitoUserPoolCreateAuthChallengeEvent`
* `CognitoUserPoolCustomMessageEvent`
* `CognitoUserPoolCustomSMSSenderEvent`
* `CognitoUserPoolDefineAuthChallengeEvent`
* `CognitoUserPoolEvent`
* `CognitoUserPoolMigrateUserEvent`
* `CognitoUserPoolPostAuthenticationEvent`
* `CognitoUserPoolPostConfirmationEvent`
* `CognitoUserPoolPreAuthenticationEvent`
* `CognitoUserPoolPreSignUpEvent`
* `CognitoUserPoolPreTokenGenerationEvent`
* `CognitoUserPoolPreTokenGenerationEventV2`
* `CognitoUserPoolVerifyAuthChallengeResponseEvent`
* `ConfigEvent`
* `ConnectEvent`
* `DynamodbEvent`
* `IoTButtonEvent`
* `KafkaEvent`
* `KinesisAnalyticsFirehoseInputPreprocessingEvent`
* `KinesisAnalyticsInputPreprocessingResponse`
* `KinesisAnalyticsOutputDeliveryEvent`
* `KinesisAnalyticsOutputDeliveryResponse`
* `KinesisAnalyticsStreamsInputPreprocessingEvent`
* `KinesisEvent`
* `KinesisFirehoseEvent`
* `LambdaDestinationEvent`
* `LexEvent`
* `MSKFirehoseEvent`
* `MSKFirehoseResponse`
* `RabbitMQEvent`
* `S3BatchEvent`
* `S3BatchResponse`
* `S3Event`
* `ScheduledEvent`
* `SecretsManagerRotationEvent`
* `SimpleIAMPolicyResponse`
* `SNSEvent`
* `SQSBatchResponse`
* `SQSEvent`


### Usage

```xml
<dependencies>
    ...
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-core</artifactId>
        <version>1.2.3</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-events</artifactId>
        <version>3.16.0</version>
    </dependency>
    ...
</dependencies>
```
