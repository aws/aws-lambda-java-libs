# AWS Lambda Java Support Libraries 
Interface definitions for Java code running on the AWS Lambda platform.

For issues and questions, you can start with our [FAQ](https://aws.amazon.com/lambda/faqs/) and the [AWS forums](https://forums.aws.amazon.com/forum.jspa?forumID=186)

To get started writing AWS Lambda functions in Java, check out the [official documentation] (http://docs.aws.amazon.com/lambda/latest/dg/java-gs.html).

___

[Maven](https://maven.apache.org)

```xml
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-core</artifactId>
  <version>1.1.0</version>
</dependency>
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-lambda-java-events</artifactId>
  <version>1.3.0</version>
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
'com.amazonaws:aws-lambda-java-core:1.1.0'
'com.amazonaws:aws-lambda-java-events:1.3.0'
'com.amazonaws:aws-lambda-java-log4j:1.0.0'
'com.amazonaws:aws-lambda-java-log4j2:1.0.0'
```

[Leiningen](http://leiningen.org) and [Boot](http://boot-clj.com)

```clojure
[com.amazonaws/aws-lambda-java-core "1.1.0"]
[com.amazonaws/aws-lambda-java-events "1.3.0"]
[com.amazonaws/aws-lambda-java-log4j "1.0.0"]
[com.amazonaws/aws-lambda-java-log4j2 "1.0.0"]
```

[sbt](http://www.scala-sbt.org)

```scala
"com.amazonaws" % "aws-lambda-java-core" % "1.1.0"
"com.amazonaws" % "aws-lambda-java-events" % "1.3.0"
"com.amazonaws" % "aws-lambda-java-log4j" % "1.0.0"
"com.amazonaws" % "aws-lambda-java-log4j2" % "1.0.0"
```
