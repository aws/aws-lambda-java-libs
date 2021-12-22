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
* `CloudWatchLogsEvent`
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

*As of version `3.0.0`, users are no longer required to pull in SDK dependencies in order to use this library.*


### Getting Started

[Maven](https://maven.apache.org)

```xml
<dependencies>
    ...
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-core</artifactId>
        <version>1.2.1</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-events</artifactId>
        <version>3.11.0</version>
    </dependency>
    ...
</dependencies>
```

[Gradle](https://gradle.org)

```groovy
'com.amazonaws:aws-lambda-java-core:1.2.1'
'com.amazonaws:aws-lambda-java-events:3.11.0'
```

[Leiningen](http://leiningen.org) and [Boot](http://boot-clj.com)

```clojure
[com.amazonaws/aws-lambda-java-core "1.2.1"]
[com.amazonaws/aws-lambda-java-events "3.11.0"]
```

[sbt](http://www.scala-sbt.org)

```scala
"com.amazonaws" % "aws-lambda-java-core" % "1.2.1"
"com.amazonaws" % "aws-lambda-java-events" % "3.11.0"
```
