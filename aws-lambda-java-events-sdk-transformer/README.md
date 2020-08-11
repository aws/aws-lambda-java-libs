# AWS Lambda Java Events SDK Transformer Library

### About

Provides helper classes/methods to use alongside `aws-lambda-java-events` in order to transform Lambda input event model
 objects into SDK-compatible output model objects 
 (eg. DynamodbEvent to a List of records writable back to DynamoDB through the AWS DynamoDB SDK v2).
 

### Getting started

Add the following Apache Maven dependencies to your `pom.xml` file:

```xml
<dependencies>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-events-sdk-transformer</artifactId>
        <version>2.0.1</version>
    </dependency>
    <dependency>
        <groupId>com.amazonaws</groupId>
        <artifactId>aws-lambda-java-events</artifactId>
        <version>3.2.0</version>
    </dependency>
</dependencies>
```

To use this library as a transformer to the AWS DynamoDB Java SDK v2, also add the following dependency to your `pom.xml` file:

```xml
<dependencies>
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>dynamodb</artifactId>
        <version>2.13.18</version>
    </dependency>
</dependencies>
```


### Example Usage

To convert a full `DynamodbEvent` object to an SDK v2 compatible `List<Record>`:
```java
import com.amazonaws.services.lambda.runtime.events.transformers.DynamodbEventTransformer;

public class DDBEventProcessor implements RequestHandler<DynamodbEvent, String> {

    public String handleRequest(DynamodbEvent ddbEvent, Context context) {
        // Process input event
        List<Record> convertedRecords = DynamodbEventTransformer.toRecordsV2(ddbEvent);
        // Modify records as needed and write back to DynamoDB using the DynamoDB AWS SDK for Java 2.0
    }
}
```

To convert a single `DynamodbEvent.DynamodbStreamRecord` object to an SDK v2 compatible `Record`:
```java
import com.amazonaws.services.lambda.runtime.events.transformers.dynamodb.DynamodbRecordTransformer;

public class MyClass {

    public void myMethod(DynamodbEvent.DynamodbStreamRecord record) {
        // ...
        Record convertedRecord = DynamodbRecordTransformer.toRecordV2(record);
        // ...
    }
}
```

To convert a `StreamRecord` object originating from a `DynamodbEvent` to an SDK v2 compatible `StreamRecord`:
```java
import com.amazonaws.services.lambda.runtime.events.transformers.dynamodb.DynamodbStreamRecordTransformer;

public class MyClass {

    public void myMethod(StreamRecord streamRecord) {
        // ...
        software.amazon.awssdk.services.dynamodb.model.StreamRecord convertedStreamRecord = 
                DynamodbStreamRecordTransformer.toStreamRecordV2(streamRecord);
        // ...
    }
}
```

To convert an `AttributeValue` object originating from a `DynamodbEvent` to an SDK v2 compatible `AttributeValue`:
```java
import com.amazonaws.services.lambda.runtime.events.transformers.dynamodb.DynamodbAttributeValueTransformer;

public class MyClass {

    public void myMethod(AttributeValue attributeValue) {
        // ...
        software.amazon.awssdk.services.dynamodb.model.AttributeValue convertedAttributeValue = 
                DynamodbAttributeValueTransformer.toAttributeValueV2(attributeValue);
        // ...
    }
}
```

To convert an `Identity` object originating from a `DynamodbEvent` to an SDK v2 compatible `Identity`:
```java
import com.amazonaws.services.lambda.runtime.events.transformers.dynamodb.DynamodbIdentityTransformer;

public class MyClass {

    public void myMethod(Identity identity) {
        // ...
        software.amazon.awssdk.services.dynamodb.model.Identity convertedIdentity =
                DynamodbIdentityTransformer.toIdentityV2(identity);
        // ...
    }
}
```