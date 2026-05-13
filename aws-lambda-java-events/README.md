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

### API Gateway WebSocket connection context

API Gateway manages the WebSocket connection. Lambda receives event payloads instead of a native Java WebSocket session.

For `APIGatewayV2WebSocketEvent`, use the request context's connection metadata:

```java
APIGatewayV2WebSocketEvent.WebSocketConnectionContext connection = event.getConnectionContext();
if (connection != null) {
    String connectionId = connection.getConnectionId();
    String endpoint = connection.getManagementApiEndpoint(); // https://{domainName}/{stage}
    // endpoint is null when domainName/stage is missing or empty
}
```

You can pass this object through your handlers as a lightweight session-like context and use it with the API Gateway Management API to send messages.


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
