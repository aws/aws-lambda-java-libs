package com.amazonaws.services.lambda.runtime.events.graalvm;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.lambda.runtime.graalvm.LambdaEventsFeature;
import com.google.common.reflect.ClassPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * The aws-lambda-java-events library supports GraalVM by containing a reflect-config.json file. This is located
 * src/main/resources/META-INF/native-image/com.amazonaws/aws-lambda-java-events/reflect-config.json
 *
 * This config is used my the GraalVM native-image tool in order to load the required classes and methods into the
 * native binary it creates.
 *
 * Any event or response class added to this library needs to be added to this config file.
 *
 * The standalone class GraalVMConfigMaker located in this package while will generate the file for you.
 *
 */
public class GraalVMSupportTest {

    @Test
    public void testClassIsFound() throws IOException, ClassNotFoundException {
        Class<S3Event> s3EventClass = S3Event.class;
        List<Class<?>> classes = LambdaEventsFeature.getClasses(s3EventClass.getPackage().getName());
        Assertions.assertTrue(classes.contains(s3EventClass));
    }

    @Test
    public void testInnerClassIsFound() throws IOException, ClassNotFoundException {
        Class<S3EventNotification.S3EventNotificationRecord> s3EventClass = S3EventNotification.S3EventNotificationRecord.class;
        List<Class<?>> classes = LambdaEventsFeature.getClasses(s3EventClass.getPackage().getName());
        Assertions.assertTrue(classes.contains(s3EventClass));
    }
}
