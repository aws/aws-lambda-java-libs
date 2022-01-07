### November 24, 2021
`3.11.0`:
- Added support for SQSaaES Partial Batch Feature ([#279](https://github.com/aws/aws-lambda-java-libs/pull/279))
  - `SQSBatchResponse`

### August 26, 2021
`3.10.0`:
- Added headers in `KafkaEventRecord` ([#260](https://github.com/aws/aws-lambda-java-libs/pull/260))
- Added support for AppSync Lambda Authorizer ([#263](https://github.com/aws/aws-lambda-java-libs/pull/263))
  - `AppSyncLambdaAuthorizerEvent`
  - `AppSyncLambdaAuthorizerResponse`
- Added support for RabbitMQ Event ([#256](https://github.com/aws/aws-lambda-java-libs/pull/256))
  - `RabbitMQEvent`
- Added missing `version` field to `APIGatewayProxyRequestEvent` ([#258](https://github.com/aws/aws-lambda-java-libs/pull/258))

### June 2, 2021
`3.9.0`:
- Added support for Cognito User Pool events ([#175](https://github.com/aws/aws-lambda-java-libs/pull/175))
  - `CognitoUserPoolCreateAuthChallengeEvent`
  - `CognitoUserPoolCustomMessageEvent`
  - `CognitoUserPoolDefineAuthChallengeEvent`
  - `CognitoUserPoolEvent`
  - `CognitoUserPoolMigrateUserEvent`
  - `CognitoUserPoolPostAuthenticationEvent`
  - `CognitoUserPoolPostConfirmationEvent`
  - `CognitoUserPoolPreAuthenticationEvent`
  - `CognitoUserPoolPreSignUpEvent`
  - `CognitoUserPoolPreTokenGenerationEvent`
  - `CognitoUserPoolVerifyAuthChallengeResponseEvent`
- Added support for IAM Policy Responses for API Gateway REST APIs ([#213](https://github.com/aws/aws-lambda-java-libs/pull/213))
 - `IamPolicyResponseV1`
- Added default IntelliJ equals, hashCode and toString methods to `APIGatewayV2WebSocketEvent` ([#248](https://github.com/aws/aws-lambda-java-libs/pull/248))
- Fixed toString method in `KinesisEvent` ([#245](https://github.com/aws/aws-lambda-java-libs/pull/245))
- Changed `body` field to lowercase in `APIGatewayV2HTTPEvent` ([#236](https://github.com/aws/aws-lambda-java-libs/pull/236))
- Added `principalOrgId` field to `APIGatewayProxyRequestEvent` ([#247](https://github.com/aws/aws-lambda-java-libs/pull/247))

### March 24, 2021
`3.8.0`:
- Added support for S3 Object Lambda event ([#229](https://github.com/aws/aws-lambda-java-libs/pull/229))
  - `S3ObjectLambdaEvent`
- Added support for IAM Policy response ([#213](https://github.com/aws/aws-lambda-java-libs/pull/213))
  - `IamPolicyResponse`
- Added `bootstrapServers` to `KafkaEvent` ([#216](https://github.com/aws/aws-lambda-java-libs/pull/216))
- Added `requestId` to `APIGatewayV2HTTPEvent` ([#230](https://github.com/aws/aws-lambda-java-libs/pull/230))
- Added `multiValueHeaders` to `APIGatewayProxyResponseEvent` ([#228](https://github.com/aws/aws-lambda-java-libs/pull/228))
- Bumped `jackson-databind` from `2.10.4` to `2.10.5.1` ([#231](https://github.com/aws/aws-lambda-java-libs/pull/231))

### December 16, 2020
`3.7.0`:
- Added support for StreamAnalytics and CustomCheckpointing ([#202](https://github.com/aws/aws-lambda-java-libs/pull/202))
  - `DynamodbTimeWindowEvent`
  - `KinesisTimeWindowEvent`
  - `StreamsEventResponse`
  - `TimeWindowEventResponse`

### November 06, 2020
`3.6.0`:
- Added support for Amazon `ActiveMQ` event: ([#185](https://github.com/aws/aws-lambda-java-libs/pull/185))
- Bumped `junit-jupiter-engine` to version `5.7.0`
- Bumped `lombok` to version `1.18.16`

### October 28, 2020
`3.5.0`:
- Added support for S3 Batch events: ([#179](https://github.com/aws/aws-lambda-java-libs/pull/179))
  - `S3BatchEvent`
  - `S3BatchResponse`
  
### October 07, 2020
`3.4.0`:
- Added Lambda and IAM authorizers to `APIGatewayV2HTTPEvent` request context ([#167](https://github.com/aws/aws-lambda-java-libs/pull/167))
- Added support for API Gateway custom authorizer: ([#166](https://github.com/aws/aws-lambda-java-libs/pull/166))
  - `APIGatewayCustomAuthorizerEvent`
  - `APIGatewayV2CustomAuthorizerEvent`
  - `SimpleIAMPolicyResponse`

### September 23, 2020
`3.3.1`:
- Added `multiValueQueryStringParameters` to `ApplicationLoadBalancerRequestEvent` ([#163](https://github.com/aws/aws-lambda-java-libs/pull/163))

### September 14, 2020
`3.3.0`:
- Added support for Secrets Manager Rotation Event ([#130](https://github.com/aws/aws-lambda-java-libs/pull/130))
- Added support for CloudFormation Custom Resource Event ([#138](https://github.com/aws/aws-lambda-java-libs/pull/138))
- Added support for Lambda Destination Event ([#139](https://github.com/aws/aws-lambda-java-libs/pull/139))
- Added support for Amazon Connect Event ([#140](https://github.com/aws/aws-lambda-java-libs/pull/140))

### August 11, 2020
`3.2.0`:
- Added support for Kafka Events ([#154](https://github.com/aws/aws-lambda-java-libs/pull/154))

### July 31, 2020
`3.1.1`:
- Fixed Base64 encoding for ALB and API Gateway HTTP events ([#150](https://github.com/aws/aws-lambda-java-libs/pull/131))

### May 20, 2020
`3.1.0`:
- Added support for Application Load Balancer Target Events ([#131](https://github.com/aws/aws-lambda-java-libs/pull/131))
  - `ApplicationLoadBalancerRequestEvent`
  - `ApplicationLoadBalancerResponseEvent`
- Added support for API Gateway HTTP API Events ([#123](https://github.com/aws/aws-lambda-java-libs/pull/123))
  - `APIGatewayV2HTTPEvent`
  - `APIGatewayV2HTTPResponse`
- Aliased the existing APIGatewayV2Proxy classes as `APIGatewayV2WebSocketEvent`/`APIGatewayV2WebSocketResponse` ([#125](https://github.com/aws/aws-lambda-java-libs/pull/125))

### May 18, 2020
`3.0.0`:
- Removed AWS SDK v1 dependencies ([#74](https://github.com/aws/aws-lambda-java-libs/issues/74))
  - Copied relevant S3, Kinesis and DynamoDB model classes under namespace `com.amazonaws.services.lambda.runtime.events.models`
  - S3:
    - `S3EventNotification`
  - Kinesis:
    - `EncryptionType`
    - `Record`
  - DynamoDB:
    - `AttributeValue`
    - `Identity`
    - `OperationType`
    - `Record`
    - `StreamRecord`
    - `StreamViewType`

### May 13, 2020
`2.2.9`:
- Added field `operationName` to `APIGatewayProxyRequestEvent` ([#126](https://github.com/aws/aws-lambda-java-libs/pull/126))

### April 28, 2020
`2.2.8`:
- Added missing XML namespace declarations to `pom.xml` file ([#97](https://github.com/aws/aws-lambda-java-libs/issues/97))
- Updated `nexusUrl` in `pom.xml` file ([#108](https://github.com/aws/aws-lambda-java-libs/issues/108))

### August 13, 2019
`2.2.7`:
- Added support for APIGatewayV2 (Web Sockets) ([#92](https://github.com/aws/aws-lambda-java-libs/issues/92))
  - `APIGatewayV2ProxyRequestEvent`
  - `APIGatewayV2ProxyResponseEvent`
- Fixed typo in `CognitoEvent` javadoc ([#87](https://github.com/aws/aws-lambda-java-libs/issues/87))

### March 11, 2019
`2.2.6`:
- Added field `customData` to `CommitEvent.Record` ([#79](https://github.com/aws/aws-lambda-java-libs/issues/79))
- Added field `isBase64Encoded` to `APIGatewayProxyResponseEvent` ([#48](https://github.com/aws/aws-lambda-java-libs/issues/48))
- Added field `authorizer` to `APIGatewayProxyRequestEvent` ([#77](https://github.com/aws/aws-lambda-java-libs/issues/77))

### January 03, 2019
`2.2.5`:
- Fixed "Paramters" typo in `APIGatewayProxyRequestEvent` and `ConfigEvent` ([#65](https://github.com/aws/aws-lambda-java-libs/issues/65))

### November 14, 2018
`2.2.4`:
- Added default constructor for `S3Event` for easier deserialization

### November 05, 2018
`2.2.3`:
- Added support for Multi-Value Headers and Query String Parameters to `APIGatewayProxyRequestEvent` ([#60](https://github.com/aws/aws-lambda-java-libs/issues/60))

### July 02, 2018
`2.2.2`:
- Made `SQSEvent.SQSMessage` default constructor public ([#51](https://github.com/aws/aws-lambda-java-libs/issues/51))

### June 29, 2018
`2.2.1`:
- Made `SQSEvent.SQSMessage` public ([#51](https://github.com/aws/aws-lambda-java-libs/issues/51))

### June 28, 2018
`2.2.0`:
- Added `SQSEvent`

### March 09, 2018
`2.1.0`:
- Added Kinesis Analytics events
  - `KinesisAnalyticsFirehoseInputPreprocessingEvent`
  - `KinesisAnalyticsInputPreprocessingResponse`
  - `KinesisAnalyticsOutputDeliveryEvent`
  - `KinesisAnalyticsOutputDeliveryResponse`
  - `KinesisAnalyticsStreamsInputPreprocessingEvent`

### November 21, 2017
`2.0.2`:
- Added missing fields to `APIGatewayProxyRequestEvent` ([#46](https://github.com/aws/aws-lambda-java-libs/issues/46))

### October 07, 2017
`2.0.1`:
- Updated KinesisFirehose event schema.
  - `approximateArrivalTimestamp` is now represented as a millisecond epoch instead of an `org.joda.time.DateTime` object.

### September 20, 2017
`2.0`:
- Added the following events:
  - `APIGatewayProxyRequestEvent`
  - `APIGatewayProxyResponseEvent`
  - `CloudFrontEvent`
  - `CloudWatchLogsEvent`
  - `CodeCommitEvent`
  - `IoTButtonEvent`
  - `KinesisFirehoseEvent`
  - `LexEvent`
  - `ScheduledEvent`
- Changed dependency management; Users must now supply the SDK package if they are using an event that is connected to an SDK library.
  - These events are `S3Event`, `KinesisEvemt`, and `DynamodbEvent`.
- Bumped AWS SDK versions to `1.11.163`


### May 16, 2016
`1.3.0`:
- Bumped AWS SDK versions to `1.11.0`

### May 16, 2016
`1.2.1`:
- Bumped AWS SDK versions to `1.10.77`

### April 22, 2016
`1.2.0`:
- Added `ConfigEvent`

### August 21, 2015
`1.1.0`:
- Added `DynamodbEvent`

### June 15, 2015
`1.0.0`:
- Initial support for java in AWS Lambda, includes the following events:
  - `CognitoEvent`
  - `KinesisEvent`
  - `S3Event`
  - `SNSEvent`
