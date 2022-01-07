# Using log4j2 with AWS Lambda

### 1. Pull in log4j2 dependencies

Example for Maven pom.xml

```xml
<dependencies>
  ...
  <dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-lambda-java-log4j2</artifactId>
    <version>1.5.1</version>
  </dependency>
  <dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.17.1</version>
  </dependency>
  <dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.17.1</version>
  </dependency>
  ....
</dependencies>
```

If using maven shade plugin, set the plugin configuration as follows

```xml
<plugins>
  ...
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>2.4.3</version>
    <executions>
      <execution>
        <phase>package</phase>
        <goals>
          <goal>shade</goal>
        </goals>
        <configuration>
          <transformers>
            <transformer
                    implementation="com.github.edwgiz.mavenShadePlugin.log4j2CacheTransformer.PluginsCacheFileTransformer">
            </transformer>
          </transformers>
        </configuration>
      </execution>
    </executions>
    <dependencies>
      <dependency>
        <groupId>com.github.edwgiz</groupId>
        <artifactId>maven-shade-plugin.log4j2-cachefile-transformer</artifactId>
        <version>2.8.1</version>
      </dependency>
    </dependencies>
  </plugin>
  ...
</plugins>
```

If you are using the [John Rengelman](https://github.com/johnrengelman/shadow) Gradle shadow plugin, then the plugin configuration is as follows:

```groovy
 
dependencies{
  ...
    implementation group: 'com.amazonaws', name: 'aws-lambda-java-log4j2', version: '1.5.1'
    implementation group: 'org.apache.logging.log4j', name: 'log4j-core', version: log4jVersion
    implementation group: 'org.apache.logging.log4j', name: 'log4j-api', version: log4jVersion
}

jar {
    enabled = false
}
shadowJar {
    transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer)
}

build.dependsOn(shadowJar)

```
 
If you are using the `sam build` and `sam deploy` commands to deploy your lambda function, then you don't 
need to use the shadow jar plugin. The `sam` cli-tool merges itself the `Log4j2Plugins.dat`
files.

### 2. Configure log4j2 using log4j2.xml file

Add the following file `<project-dir>/src/main/resources/log4j2.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration packages="com.amazonaws.services.lambda.runtime.log4j2">
  <Appenders>
    <Lambda name="Lambda">
      <PatternLayout>
          <pattern>%d{yyyy-MM-dd HH:mm:ss} %X{AWSRequestId} %-5p %c{1}:%L - %m%n</pattern>
      </PatternLayout>
    </Lambda>
  </Appenders>
  <Loggers>
    <Root level="info">
      <AppenderRef ref="Lambda" />
    </Root>
  </Loggers>
</Configuration>
```

### 3. Example code

```java
package example;

import com.amazonaws.services.lambda.runtime.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hello {
    // Initialize the Log4j logger.
    static final Logger logger = LogManager.getLogger(Hello.class);

    public String myHandler(String name, Context context) {
        // System.out: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
        System.out.println("log data from stdout \n this is continuation of system.out");

       // System.err: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
        System.err.println("log data from stderr. \n this is a continuation of system.err");

        // Use log4j to log the same thing as above and AWS Lambda will log only one event in CloudWatch.
        logger.debug("log data from log4j debug \n this is continuation of log4j debug");

        logger.error("log data from log4j err. \n this is a continuation of log4j.err");

        // Return will include the log stream name so you can look
        // up the log later.
        return String.format("Hello %s. log stream = %s", name, context.getLogStreamName());
    }
}
```
