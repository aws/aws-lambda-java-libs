FROM public.ecr.aws/lambda/java:21 as builder
WORKDIR /tmpBuild
RUN dnf -y update
RUN dnf -y install zip
RUN dnf -y install findutils
RUN  curl -k -L -v -X GET https://services.gradle.org/distributions/gradle-8.3-bin.zip > gradle-8.3-bin.zip
RUN unzip -d /opt/gradle gradle-8.3-bin.zip

COPY src ./src
COPY build.gradle .
COPY aws-lambda-java-serialization-1.1.6.jar .

RUN JAVA_HOME=/var/lang /opt/gradle/gradle-8.3/bin/gradle :buildZip

# Replace runtime JARs with new versions (keep names that bootstrap expects: 2.8.7)
RUN rm -f /var/runtime/lib/aws-lambda-java-runtime-interface-client-*.jar
RUN rm -f /var/runtime/lib/aws-lambda-java-serialization-*.jar
COPY aws-lambda-java-runtime-interface-client-2.8.4.jar /var/runtime/lib/aws-lambda-java-runtime-interface-client-2.8.7-linux-x86_64.jar
COPY aws-lambda-java-serialization-1.1.6.jar /var/runtime/lib/aws-lambda-java-serialization-1.1.5.jar
COPY aws-lambda-java-serialization-interfaces-1.0.0.jar /var/runtime/lib/aws-lambda-java-serialization-interfaces-1.0.0.jar
COPY bootstrap /var/runtime

WORKDIR /var/task
RUN unzip /tmpBuild/build/distributions/code.zip



CMD ["io.github.maxday.Handler::handleRequest"]