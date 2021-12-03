
# Tests utility

The `aws-lambda-java-tests` module provides opinionated tools to ease Java Lambda testing. This is a test dependency.

**Key features**

* Load events from json files and get them deserialized into Java Events.
* Inject Events directly in JUnit 5 tests, using the `@ParameterizedTest` annotation.


## Background

When using Java for a Lambda function, you must implement the RequestHandler interface and provide input and output types:

```java
public interface RequestHandler<I, O> {
    public O handleRequest(I input, Context context);
}
```

The input is automatically deserialized by the Lambda Java Runtime from a json event into the type you define,
and the output is serialized into JSON from the output type. More info in the [docs](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html).

When you want to test your Lambda function and your handleRequest method, you cannot simply use JSON events files
as some of the event fields may not be deserialized correctly.

For example, an SQS JSON event contains a list of "Records", while the [`SQSEvent`](https://github.com/aws/aws-lambda-java-libs/blob/master/aws-lambda-java-events/src/main/java/com/amazonaws/services/lambda/runtime/events/SQSEvent.java) use "records" with a lowercase.
You can choose to modify the JSON input but it can be tedious and you generally want to keep the JSON event as you get it
in the doc, the Lambda console or in your logs.

Now you can use the [aws-lambda-java-serialization](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-serialization) module to deserialize events. And this test library is using this module as a dependency to ease tests of lambda function handlers.

## Installation

To install this utility, add the following dependency to your project. Note that it's a test dependency.

```xml
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-lambda-java-tests</artifactId>
    <version>1.1.1</version>
    <scope>test</scope>
</dependency>
```

Also have surefire in your plugins:

```xml
<build>
  <plugins>
	<plugin>
	    <groupId>org.apache.maven.plugins</groupId>
	    <artifactId>maven-surefire-plugin</artifactId>
	    <version>2.22.2</version>
	</plugin>
  </plugins>
</build>
```

## Usage

### Events injection

A set of annotations can be used to inject Events and/or to validate handler responses against those Events.
**All those annotations must be used in conjunction with the [`@ParameterizedTest`](https://junit.org/junit5/docs/current/api/org.junit.jupiter.params/org/junit/jupiter/params/ParameterizedTest.html) annotation from Junit 5.**

`ParameterizedTest` enables to inject arguments into a unit test, so you can run the same test one or more time with different parameters.
See [the doc](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests) for more details on this.

**Event**

The `@Event` annotation permits to inject one Event into a Junit test.

Example:

```java
// the json file must be in the classpath (most often in src/test/resources)
@ParameterizedTest
@Event(value = "sqs/sqs_event.json", type = SQSEvent.class)
public void testInjectSQSEvent(SQSEvent event) {
    // test your handleRequest method with this event as parameter
}
```

**Events**

The `@Events` annotation permits to inject multiple Events into a Junit test

Examples:

```java
@ParameterizedTest
@Events(
        events = {
                @Event("sqs/sqs_event.json"),
                @Event("sqs/sqs_event2.json"),
        },
        type = SQSEvent.class
)
public void testInjectEvents(SQSEvent event) {
    // test your handleRequest method with all the JSON events available in the sqs folder
}

// OR simpler

// sqs folder must be in the classpath (most often in src/test/resources)
@ParameterizedTest
@Events(folder = "sqs", type = SQSEvent.class)
public void testInjectEventsFromFolder(SQSEvent event) {
    // test your handleRequest method with all the JSON events available in the sqs folder
}
```

**HandlerParams**

The `@HandlerParams` is the most advanced one as it permits to provide both input and output as arguments to your tests.
Thus you can validate your `handlerRequest` method by providing the output and asserting on the expected output.

```java

// Single event
@ParameterizedTest
@HandlerParams(
   event = @Event(value = "apigw/events/apigw_event.json", type = APIGatewayProxyRequestEvent.class),
   response = @Response(value = "apigw/responses/apigw_response.json", type = APIGatewayProxyResponseEvent.class))
public void testSingleEventResponse(APIGatewayProxyRequestEvent event, APIGatewayProxyResponseEvent response) {
}

// Multiple events in folder
@ParameterizedTest
@HandlerParams(
   events = @Events(folder = "apigw/events/", type = APIGatewayProxyRequestEvent.class),
   responses = @Responses(folder = "apigw/responses/", type = APIGatewayProxyResponseEvent.class))
public void testMultipleEventsResponsesInFolder(APIGatewayProxyRequestEvent event, APIGatewayProxyResponseEvent response) {
}

// Multiple events
@HandlerParams(
        events = @Events(
                events = {
                        @Event("apigw/events/apigw_event.json"),
                        @Event("apigw/events/apigw_event2.json"),
                },
                type = APIGatewayProxyRequestEvent.class
        ),
        responses = @Responses(
                responses = {
                        @Response("apigw/responses/apigw_response.json"),
                        @Response("apigw/responses/apigw_response2.json")
                },
                type = APIGatewayProxyResponseEvent.class
        )
)
public void testMultipleEventsResponses(APIGatewayProxyRequestEvent event, APIGatewayProxyResponseEvent response) {
}
```

If you cannot use those annotations (for example if you use TestNG), or if you want to load the events on your own, you can directly use the `EventLoader`, which is the underlying class that load the json events.

### EventLoader

`EventLoader` enables to load any Event from a JSON file and deserialize it into a Java Object.
Either one from the [aws-lambda-java-events](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-events) library
or your own Event.

EventLoader provides a load method for most of the pre-defined events:

```java
APIGatewayV2HTTPEvent httpEvent = 
    EventLoader.loadApiGatewayHttpEvent("apigw_http_event.json");

APIGatewayProxyRequestEvent restEvent = 
    EventLoader.loadApiGatewayRestEvent("apigw_rest_event.json");

DynamodbEvent ddbEvent = EventLoader.loadDynamoDbEvent("ddb_event.json");

KinesisEvent kinesisEvent = 
    EventLoader.loadKinesisEvent("kinesis_event.json");

ScheduledEvent eventBridgeEvent = 
    EventLoader.loadScheduledEvent("eb_event.json");

S3Event s3Event = EventLoader.loadS3Event("s3_event.json");

SNSEvent snsEvent = EventLoader.loadSNSEvent("sns_event.json");

SQSEvent sqsEvent = EventLoader.loadSQSEvent("sqs_event.json");

// ... and many others
```

Or you can load what you prefer with the generic method:

```java
MyEvent myEvent = EventLoader.loadEvent("my_event.json", MyEvent.class);
```

