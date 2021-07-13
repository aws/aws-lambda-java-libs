package com.amazonaws.services.lambda.runtime.events.graalvm;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.lambda.runtime.graalvm.LambdaEventsGraalVMFeature;
import io.github.classgraph.ClassGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.amazonaws.services.lambda.runtime.graalvm.LambdaEventsGraalVMFeature.EVENTS_PACKAGE_NAME;

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
public class LambdaEventsGraalVMFeatureTest {

    @TestFactory
    public Stream<DynamicTest> testEventClassesAreFound() throws IOException, ClassNotFoundException {

        Set<Class<?>> eventClassNames = new HashSet<>(LambdaEventsGraalVMFeature.getClasses(EVENTS_PACKAGE_NAME));

        return new ClassGraph()
                .enableClassInfo()
                .acceptPackages(EVENTS_PACKAGE_NAME)
                .scan().getAllClasses().stream()
                .map(classInfo -> DynamicTest.dynamicTest("Test " + classInfo.getSimpleName(), () -> {
                    Class<?> eventClass = classInfo.loadClass();
                    Assertions.assertTrue(eventClassNames.contains(eventClass));
                }));
    }
}
