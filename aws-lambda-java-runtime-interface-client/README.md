## AWS Lambda Java Runtime Interface Client

We have open-sourced a set of software packages, Runtime Interface Clients (RIC), that implement the Lambda
 [Runtime API](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-api.html), allowing you to seamlessly extend your preferred
  base images to be Lambda compatible or to implement your own custom Lambda runtime.
The Lambda Runtime Interface Client is a lightweight interface that allows your runtime to receive requests from and send requests to the Lambda service.

## Usage 1: Deploy your Lambda as a Container Image without native compilation

You can include this package in your preferred base image to make that base image Lambda compatible.

### Creating a Docker Image for Lambda with the Runtime Interface Client

Choose a preferred base image. The Runtime Interface Client is tested on Amazon Linux, Alpine, Ubuntu, Debian, and CentOS. The requirements are that the image is:

* built for x86_64
* contains Java >= 8
* contains glibc >= 2.17 or musl

### Example

The Runtime Interface Client library can be installed into the image separate from the function code, but the simplest approach to keeping the Dockerfile simple is to include the library as a part of the function's dependencies!

Dockerfile
```dockerfile
# we'll use Amazon Linux 2 + Corretto 11 as our base
FROM amazoncorretto:11 as base

# configure the build environment
FROM base as build
RUN yum install -y maven
WORKDIR /src

# cache and copy dependencies
ADD pom.xml .
RUN mvn dependency:go-offline dependency:copy-dependencies

# compile the function
ADD . .
RUN mvn package 

# copy the function artifact and dependencies onto a clean base
FROM base
WORKDIR /function

COPY --from=build /src/target/dependency/*.jar ./
COPY --from=build /src/target/*.jar ./

# configure the runtime startup as main
ENTRYPOINT [ "/usr/bin/java", "-cp", "./*", "com.amazonaws.services.lambda.runtime.api.client.AWSLambda" ]
# pass the name of the function handler as an argument to the runtime
CMD [ "example.App::sayHello" ]
```
pom.xml
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>example</groupId>
  <artifactId>hello-lambda</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>hello-lambda</name>
  <url>http://maven.apache.org</url>
  <properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>
  <dependencies>
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-lambda-java-runtime-interface-client</artifactId>
      <version>1.0.0</version>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <version>3.1.2</version>
        <executions>
          <execution>
            <id>copy-dependencies</id>
            <phase>package</phase>
            <goals>
              <goal>copy-dependencies</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
```
src/main/java/example/App.java
```java
package example;

public class App {
    public static String sayHello() {
        return "Hello λ!";
    }
}
```

### Local Testing

To make it easy to locally test Lambda functions packaged as container images we open-sourced a lightweight web-server, Lambda Runtime Interface Emulator (RIE), which allows your function packaged as a container image to accept HTTP requests. You can install the [AWS Lambda Runtime Interface Emulator](https://github.com/aws/aws-lambda-runtime-interface-emulator) on your local machine to test your function. Then when you run the image function, you set the entrypoint to be the emulator. 

*To install the emulator and test your Lambda function*

1) Run the following command to download the RIE from GitHub and install it on your local machine. 

```shell script
mkdir -p ~/.aws-lambda-rie && \
    curl -Lo ~/.aws-lambda-rie/aws-lambda-rie https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie && \
    chmod +x ~/.aws-lambda-rie/aws-lambda-rie
```
2) Run your Lambda image function using the docker run command. 

```shell script
docker run -d -v ~/.aws-lambda-rie:/aws-lambda -p 9000:8080 \
    --entrypoint /aws-lambda/aws-lambda-rie \
    myfunction:latest \
    /usr/bin/java -cp './*' com.amazonaws.services.lambda.runtime.api.client.AWSLambda example.App::sayHello
```

This runs the image as a container and starts up an endpoint locally at `http://localhost:9000/2015-03-31/functions/function/invocations`. 

3) Post an event to the following endpoint using a curl command: 

```shell script
curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'
```

This command invokes the function running in the container image and returns a response.

*Alternately, you can also include RIE as a part of your base image. See the AWS documentation on how to [Build RIE into your base image](https://docs.aws.amazon.com/lambda/latest/dg/images-test.html#images-test-alternative).*

### Troubleshooting

While running integration tests, you might encounter the Docker Hub rate limit error with the following body:
```
You have reached your pull rate limit. You may increase the limit by authenticating and upgrading: https://www.docker.com/increase-rate-limits
```
To fix the above issue, consider authenticating to a Docker Hub account by setting the Docker Hub credentials as below CodeBuild environment variables.
```shell script
DOCKERHUB_USERNAME=<dockerhub username>
DOCKERHUB_PASSWORD=<dockerhub password>
```
Recommended way is to set the Docker Hub credentials in CodeBuild job by retrieving them from AWS Secrets Manager.

## Usage 2: Deploy your Lambda with Native Compilation

You can include this package as a dependency to build your Custom Runtime or to run your Lambda as a Container without any Java Runtime.

The idea here is to compile natively your java bytecode with [GraalVM](https://www.graalvm.org) [native-image](https://www.graalvm.org/reference-manual/native-image/) to a linux executable file.
In order to ease this native compilation of the Java Runtime Interface Client, the Java RIC jar artifact is now including special configuration files for GraalVM native-image.

Note: In this example the docker image provided may be used 
* For cross compiling only to linux x86-64 and extract the Custom Runtime zip to deploy 
* For deploying your Lambda as a Container Image
* For local testing of your Container Image or your native executable.

### Example: hello-lambda-native project

Let's take the same Lambda as before:

src/main/java/example/App.java
```java
package example;

public class App {
    public static String sayHello() {
        return "Hello λ!";
    }
}
```

This is the needed configuration for native-image for finding the Lambda code:

src/main/resources/META-INF/native-image/reflect-config.json
```json
[
{
  "name":"example.App",
  "allPublicMethods":true
}
]
```

When building a Custom Runtime you need a zip archive containing at root a file named bootstrap that will be the entry point for running your Lambda. In a Container, the same bootstrap file is used as ENTRYPOINT. And finally, it contains also the logic to identify the case of local testing: 

bootstrap
```bash
#!/bin/sh
if [ -z "${AWS_LAMBDA_RUNTIME_API}" ]; then
    exec /usr/bin/aws-lambda-rie ./func $1
else
	set -euo pipefail
    exec ./func $_HANDLER
fi
```

The Dockerfile is slightly more complex to support the three use cases described above:

Dockerfile
```dockerfile
FROM amazonlinux:2 as base

FROM base as build
ENV LANG=en_US.UTF-8
RUN yum update -y && yum install -y gcc gcc-c++ zlib-devel zip tar gzip && yum clean all
RUN curl -4 -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-20.3.0/graalvm-ce-java11-linux-amd64-20.3.0.tar.gz -o /tmp/graalvm.tar.gz \
    && tar -zxf /tmp/graalvm.tar.gz -C /tmp \
    && mv /tmp/graalvm-ce-java11-20.3.0 /usr/lib/graalvm \
    && rm -rf /tmp/*
RUN /usr/lib/graalvm/bin/gu install native-image
ENV PATH=/usr/lib/graalvm/bin:${PATH}
ENV JAVA_HOME=/usr/lib/graalvm
RUN yum install -y maven

# compile the function
WORKDIR /home/app
ADD . .
RUN mvn package 

COPY bootstrap /home/app/
# (Optional) Add Lambda Runtime Interface Emulator and use a script in the ENTRYPOINT for simpler local runs
ADD https://github.com/aws/aws-lambda-runtime-interface-emulator/releases/latest/download/aws-lambda-rie /home/app/aws-lambda-rie
RUN native-image -jar target/hello-lambda-native-1.0-SNAPSHOT.jar -H:Name=func --no-fallback
RUN yum install -y zip && yum clean all
RUN chmod 755 aws-lambda-rie
RUN chmod 755 bootstrap
RUN chmod 755 func
RUN zip -j function.zip bootstrap func

FROM base
WORKDIR /function
COPY --from=build /home/app/func /function/func
COPY --from=build /home/app/bootstrap /function/bootstrap
COPY --from=build /home/app/function.zip /function/function.zip
COPY --from=build /home/app/aws-lambda-rie /usr/bin/aws-lambda-rie
ENTRYPOINT [ "/function/bootstrap" ]
CMD [ "example.App::sayHello" ]
```

pom.xml
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>example</groupId>
  <artifactId>hello-lambda-native</artifactId>
  <version>1.0-SNAPSHOT</version>
  
	<properties>
	   <jdk.version>11</jdk.version>
	   <release.version>11</release.version>
	   <exec.mainClass>com.amazonaws.services.lambda.runtime.api.client.AWSLambda</exec.mainClass>
	</properties>

	<dependencies>
		<dependency>
	      <groupId>com.amazonaws</groupId>
	      <artifactId>aws-lambda-java-runtime-interface-client</artifactId>
	      <version>1.0.0</version>
	    </dependency>    
	</dependencies>
 
<build>
	  <plugins>
	  	   <plugin>
	        <groupId>org.apache.maven.plugins</groupId>
	        <artifactId>maven-compiler-plugin</artifactId>
	        <version>3.6.0</version>
	        <configuration>
	          <source>${jdk.version}</source>
	          <target>${release.version}</target>
	          <encoding>UTF-8</encoding>
	        </configuration>
	      </plugin>
	      
	      <plugin>
		    <groupId>org.apache.maven.plugins</groupId>
		    <artifactId>maven-dependency-plugin</artifactId>
		    <version>3.1.2</version>
		    <executions>
		        <execution>
		            <id>copy-dependencies</id>
		            <phase>prepare-package</phase>
		            <goals>
		                <goal>copy-dependencies</goal>
		            </goals>
		            <configuration>
		                <outputDirectory>
		                    ${project.build.directory}/libs
		                </outputDirectory>
		            </configuration>
		        </execution>
		    </executions>
		</plugin>
		
		<plugin>
		    <groupId>org.apache.maven.plugins</groupId>
		    <artifactId>maven-jar-plugin</artifactId>
		    <version>3.2.0</version>
		    <configuration>
		        <archive>
		            <manifest>
		                <addClasspath>true</addClasspath>
		                <classpathPrefix>libs/</classpathPrefix>
		                <mainClass>${exec.mainClass}</mainClass>
		            </manifest>
		        </archive>
		    </configuration>
		</plugin>
		
	  </plugins>
	  
  </build>
  
</project>
```

### Custom Runtime

just build the image with Docker:

```bash
docker build -t hello-lambda-native:latest .
```

Then extract the Custom Runtime deployable zip bundle with this command:

```bash
docker cp $(docker create hello-lambda-native:latest):/function/function.zip .
```

### Container with native compilation

just build the image with Docker:

```bash
docker build -t hello-lambda-native:latest .
```

This image can be deployed directly.

### Local Testing

just build the image with Docker:

```bash
docker build -t hello-lambda-native:latest .
```

Then you can run the image locally:

```bash
docker run -p 9000:8080 hello-lambda-native:latest
```

And test it from another Terminal/Console with:

```bash
curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'
```

## Security

If you discover a potential security issue in this project we ask that you notify AWS/Amazon Security via our [vulnerability reporting page](http://aws.amazon.com/security/vulnerability-reporting/). Please do **not** create a public github issue.

## License

This project is licensed under the Apache-2.0 License.

