# AWS Lambda Java Events v2.0

### New Event Models Supported
* APIGatewayProxyRequestEvent
* APIGatewayProxyResponseEvent
* CloudFrontEvent
* CloudWatchLogsEvent
* CodeCommitEvent
* IoTButtonEvent
* KinesisFirehoseEvent
* LexEvent
* ScheduledEvent

### New package inclusion model
The old package inclusion model required users to pull unused dependencies into
their package. We have removed this inclusion so that users' jars will be
smaller, which will results in reduced latency times. Customers using older
versions do not need to make any changes to their existing code.

The following event models do not require any SDK dependencies
* APIGatewayProxyRequestEvent
* APIGatewayProxyResponseEvent
* CloudFrontEvent
* CloudWatchLogsEvent
* CodeCommitEvent
* CognitoEvent
* ConfigEvent
* IoTButtonEvent
* KinesisFirehoseEvent
* LexEvent
* ScheduledEvent
* SNSEvent

so the dependencies section in the pom.xml file would like this

```xml
<dependencies>
    ...
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-core</artifactId>
        <version>1.1.0</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-events</artifactId>
        <version>2.2.6</version>
    </dependency>
    ...
</dependencies>
```

#### S3 Event

For the S3 event the pom would look like this:

```xml
<dependencies>
    ...
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-core</artifactId>
        <version>1.1.0</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-events</artifactId>
        <version>2.2.6</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-java-sdk-s3</artifactId>
        <version>1.11.163</version>
    </dependency>
    ...
</dependencies>
```

#### Kinesis Event

For the Kinesis event

```xml
<dependencies>
    ....
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-core</artifactId>
        <version>1.1.0</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-events</artifactId>
        <version>2.2.6</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-java-sdk-kinesis</artifactId>
        <version>1.11.163</version>
    </dependency>
    ...
</dependencies>
```

#### Dynamodb Event

For the Dynamodb event

```xml
<dependencies>
    ...
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-core</artifactId>
        <version>1.1.0</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-events</artifactId>
        <version>2.2.6</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-java-sdk-dynamodb</artifactId>
        <version>1.11.163</version>
    </dependency>
    ...
</dependencies>
```
