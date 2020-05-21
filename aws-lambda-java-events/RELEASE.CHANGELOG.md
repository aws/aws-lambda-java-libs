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

### Apr 28, 2020
`2.2.8`:
- Added missing XML namespace declarations to `pom.xml` file ([#97](https://github.com/aws/aws-lambda-java-libs/issues/97))
- Updated `nexusUrl` in `pom.xml` file ([#108](https://github.com/aws/aws-lambda-java-libs/issues/108))

### Aug 13, 2019
`2.2.7`:
- Added support for APIGatewayV2 (Web Sockets) ([#92](https://github.com/aws/aws-lambda-java-libs/issues/92))
  - `APIGatewayV2ProxyRequestEvent`
  - `APIGatewayV2ProxyResponseEvent`
- Fixed typo in `CognitoEvent` javadoc ([#87](https://github.com/aws/aws-lambda-java-libs/issues/87))

### Mar 11, 2019
`2.2.6`:
- Added field `customData` to `CommitEvent.Record` ([#79](https://github.com/aws/aws-lambda-java-libs/issues/79))
- Added field `isBase64Encoded` to `APIGatewayProxyResponseEvent` ([#48](https://github.com/aws/aws-lambda-java-libs/issues/48))
- Added field `authorizer` to `APIGatewayProxyRequestEvent` ([#77](https://github.com/aws/aws-lambda-java-libs/issues/77))

### Jan 03, 2019
`2.2.5`:
- Fixed "Paramters" typo in `APIGatewayProxyRequestEvent` and `ConfigEvent` ([#65](https://github.com/aws/aws-lambda-java-libs/issues/65))

### Nov 14, 2018
`2.2.4`:
- Added default constructor for `S3Event` for easier deserialization

### Nov 05, 2018
`2.2.3`:
- Added support for Multi-Value Headers and Query String Parameters to `APIGatewayProxyRequestEvent` ([#60](https://github.com/aws/aws-lambda-java-libs/issues/60))

### Jul 02, 2018
`2.2.2`:
- Made `SQSEvent.SQSMessage` default constructor public ([#51](https://github.com/aws/aws-lambda-java-libs/issues/51))

### Jun 29, 2018
`2.2.1`:
- Made `SQSEvent.SQSMessage` public ([#51](https://github.com/aws/aws-lambda-java-libs/issues/51))

### Jun 28, 2018
`2.2.0`:
- Added `SQSEvent`

### Mar 09, 2018
`2.1.0`:
- Added Kinesis Analytics events
  - `KinesisAnalyticsFirehoseInputPreprocessingEvent`
  - `KinesisAnalyticsInputPreprocessingResponse`
  - `KinesisAnalyticsOutputDeliveryEvent`
  - `KinesisAnalyticsOutputDeliveryResponse`
  - `KinesisAnalyticsStreamsInputPreprocessingEvent`

### Nov 21, 2017
`2.0.2`:
- Added missing fields to `APIGatewayProxyRequestEvent` ([#46](https://github.com/aws/aws-lambda-java-libs/issues/46))

### Oct 07, 2017
`2.0.1`:
- Updated KinesisFirehose event schema.
  - `approximateArrivalTimestamp` is now represented as a millisecond epoch instead of an `org.joda.time.DateTime` object.

### Sep 20, 2017
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

### Apr 22, 2016
`1.2.0`:
- Added `ConfigEvent`

### Aug 21, 2015
`1.1.0`:
- Added `DynamodbEvent`

### Jun 15, 2015
`1.0.0`:
- Initial support for java in AWS Lambda, includes the following events:
  - `CognitoEvent`
  - `KinesisEvent`
  - `S3Event`
  - `SNSEvent`
