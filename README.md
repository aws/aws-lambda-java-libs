# AWS Lambda Java Support Libraries
Interface definitions for Java code running on the AWS Lambda platform.

For issues and questions, you can start with our [FAQ](https://aws.amazon.com/lambda/faqs/) and the [AWS forums](https://forums.aws.amazon.com/forum.jspa?forumID=186)

To get started writing AWS Lambda functions in Java, check out the [official documentation] (http://docs.aws.amazon.com/lambda/latest/dg/java-gs.html).

# Disclaimer of use

Each of the supplied packages should be used without modification. Removing
dependencies, adding conflicting dependencies, or selectively including classes
from the packages can result in unexpected behavior.

# Recent Updates!

* ### [SQS Support](https://github.com/aws/aws-lambda-java-libs/commit/9a74fdc9d92b5d7f73ae05660090e65cbd098360)
* ### [Kinesis Analytics Support](https://github.com/aws/aws-lambda-java-libs/commit/943352c7f0256afe82773e664e887e1593303508)
* ### [2017 Java Events Update](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-events)
* ### [Log4j2 Support](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-log4j2)

# Where to get packages
___

[Maven](https://maven.apache.org)

```xml
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-core</artifactId>
  <version>1.2.0</version>
</dependency>
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-events</artifactId>
  <version>2.2.6</version>
</dependency>
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-log4j</artifactId>
  <version>1.0.0</version>
</dependency>
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-log4j2</artifactId>
  <version>1.0.0</version>
</dependency>
```

[Gradle](https://gradle.org)

```groovy
'com.amazonaws:aws-lambda-java-core:1.2.0'
'com.amazonaws:aws-lambda-java-events:2.2.6'
'com.amazonaws:aws-lambda-java-log4j:1.0.0'
'com.amazonaws:aws-lambda-java-log4j2:1.0.0'
```

[Leiningen](http://leiningen.org) and [Boot](http://boot-clj.com)

```clojure
[com.amazonaws/aws-lambda-java-core "1.2.0"]
[com.amazonaws/aws-lambda-java-events "2.2.6"]
[com.amazonaws/aws-lambda-java-log4j "1.0.0"]
[com.amazonaws/aws-lambda-java-log4j2 "1.0.0"]
```

[sbt](http://www.scala-sbt.org)

```scala
"com.amazonaws" % "aws-lambda-java-core" % "1.2.0"
"com.amazonaws" % "aws-lambda-java-events" % "2.2.6"
"com.amazonaws" % "aws-lambda-java-log4j" % "1.0.0"
"com.amazonaws" % "aws-lambda-java-log4j2" % "1.0.0"
```

# Using aws-lambda-java-core

This package defines the Lambda [Context](http://docs.aws.amazon.com/lambda/latest/dg/java-context-object.html)
object as well as [interfaces](http://docs.aws.amazon.com/lambda/latest/dg/java-handler-using-predefined-interfaces.html) that Lambda accepts.

# Using aws-lambda-java-events

This package defines [event sources](http://docs.aws.amazon.com/lambda/latest/dg/intro-invocation-modes.html) that AWS Lambda natively accepts. See the [documentation](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-events) for more information.

# Using aws-lambda-java-log4j2

This package defines the Lambda adapter to use with log4j version 2. See
[documentation](https://github.com/aws/aws-lambda-java-libs/tree/master/aws-lambda-java-log4j2) for how to use the adapter.

# Using aws-lambda-java-log4j (Not recommended)

This package defines the Lambda adapter to use with log4j version 1. See
the [official documentation](http://docs.aws.amazon.com/lambda/latest/dg/java-logging.html#java-wt-logging-using-log4j) for how to use this adapter.
