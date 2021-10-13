# AWS Lambda Java Support Libraries
Interface definitions for Java code running on the AWS Lambda platform.

For issues and questions, you can start with our [FAQ](https://aws.amazon.com/lambda/faqs/)
 and the [AWS forums](https://forums.aws.amazon.com/forum.jspa?forumID=186)

To get started writing AWS Lambda functions in Java, check out the [official documentation](http://docs.aws.amazon.com/lambda/latest/dg/java-gs.html).

# Disclaimer of use

Each of the supplied packages should be used without modification. Removing
dependencies, adding conflicting dependencies, or selectively including classes
from the packages can result in unexpected behavior.

# Release Notes

Check out the per-module release notes:
- [aws-lambda-java-core](aws-lambda-java-core/RELEASE.CHANGELOG.md)
- [aws-lambda-java-events](aws-lambda-java-events/RELEASE.CHANGELOG.md)
- [aws-lambda-java-events-sdk-transformer](aws-lambda-java-events-sdk-transformer/RELEASE.CHANGELOG.md)
- [aws-lambda-java-log4j2](aws-lambda-java-log4j2/RELEASE.CHANGELOG.md)
- [aws-lambda-java-runtime-interface-client](aws-lambda-java-runtime-interface-client/RELEASE.CHANGELOG.md)
- [aws-lambda-java-serialization](aws-lambda-java-serialization/RELEASE.CHANGELOG.md)
- [aws-lambda-java-test](aws-lambda-java-tests/RELEASE.CHANGELOG.md)

# Where to get packages
___

[Maven](https://maven.apache.org)

```xml
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-core</artifactId>
  <version>1.2.1</version>
</dependency>
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-events</artifactId>
  <version>3.10.0</version>
</dependency>
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-events-sdk-transformer</artifactId>
  <version>3.0.6</version>
</dependency>
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-log4j2</artifactId>
  <version>1.2.0</version>
</dependency>
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-runtime-interface-client</artifactId>
  <version>2.0.0</version>
</dependency>
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-tests</artifactId>
  <version>1.1.0</version>
  <scope>test</scope>
</dependency>
```

[Gradle](https://gradle.org)

```groovy
'com.amazonaws:aws-lambda-java-core:1.2.1'
'com.amazonaws:aws-lambda-java-events:3.10.0'
'com.amazonaws:aws-lambda-java-events-sdk-transformer:3.0.6'
'com.amazonaws:aws-lambda-java-log4j2:1.2.0'
'com.amazonaws:aws-lambda-java-runtime-interface-client:2.0.0'
'com.amazonaws:aws-lambda-java-tests:1.1.0'
```

[Leiningen](http://leiningen.org) and [Boot](http://boot-clj.com)

```clojure
[com.amazonaws/aws-lambda-java-core "1.2.1"]
[com.amazonaws/aws-lambda-java-events "3.10.0"]
[com.amazonaws/aws-lambda-java-events-sdk-transformer "3.0.6"]
[com.amazonaws/aws-lambda-java-log4j2 "1.2.0"]
[com.amazonaws/aws-lambda-java-runtime-interface-client "2.0.0"]
[com.amazonaws/aws-lambda-java-tests "1.1.0"]
```

[sbt](http://www.scala-sbt.org)

```scala
"com.amazonaws" % "aws-lambda-java-core" % "1.2.1"
"com.amazonaws" % "aws-lambda-java-events" % "3.10.0"
"com.amazonaws" % "aws-lambda-java-events-sdk-transformer" % "3.0.6"
"com.amazonaws" % "aws-lambda-java-log4j2" % "1.2.0"
"com.amazonaws" % "aws-lambda-java-runtime-interface-client" % "2.0.0"
"com.amazonaws" % "aws-lambda-java-tests" % "1.1.0"
```

# Using aws-lambda-java-core

This package defines the Lambda [Context](http://docs.aws.amazon.com/lambda/latest/dg/java-context-object.html) object
 as well as [interfaces](http://docs.aws.amazon.com/lambda/latest/dg/java-handler-using-predefined-interfaces.html) that Lambda accepts.

# Using aws-lambda-java-events

This package defines [event sources](http://docs.aws.amazon.com/lambda/latest/dg/intro-invocation-modes.html) that AWS Lambda natively accepts. 
See the [documentation](aws-lambda-java-events/README.md) for more information.

# Using aws-lambda-java-events-sdk-transformer

This package provides helper classes/methods to use alongside `aws-lambda-java-events` in order to transform
 Lambda input event model objects into SDK-compatible output model objects.  
See the [documentation](aws-lambda-java-events-sdk-transformer/README.md) for more information.

# Using aws-lambda-java-log4j2

This package defines the Lambda adapter to use with log4j version 2. 
See the [README](aws-lambda-java-log4j2/README.md) or the [official documentation](http://docs.aws.amazon.com/lambda/latest/dg/java-logging.html#java-wt-logging-using-log4j) for information on how to use the adapter.

# Using aws-lambda-java-runtime-interface-client

This package defines the Lambda Java Runtime Interface Client package, a Lambda Runtime component that starts the runtime and interacts with the Runtime API - i.e., it calls the API for invocation events, starts the function code, calls the API to return the response.
The purpose of this package is to allow developers to deploy their applications in Lambda under the form of Container Images. See the [README](aws-lambda-java-runtime-interface-client/README.md) for information on how to use the library.

# Using aws-lambda-java-serialization

This package defines the Lambda serialization logic using in the aws-lambda-java-runtime-client library. It has no current standalone usage.

# Using aws-lambda-java-tests

This package provides utils to ease Lambda Java testing. Used with `aws-lambda-java-serialization` and `aws-lambda-java-events` to inject events in your JUnit tests.
