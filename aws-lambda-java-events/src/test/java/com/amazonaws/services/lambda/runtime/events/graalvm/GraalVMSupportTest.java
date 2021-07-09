package com.amazonaws.services.lambda.runtime.events.graalvm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.ClassPath;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * The aws-lambda-java-events library supports GraalVM by containing a _reflect-config.json file. This is located
 * src/main/resources/META-INF/native-image/com.amazonaws/aws-lambda-java-events/_reflect-config.json
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

    private static final File CONFIG_LOCATION = FileUtils.getFile("src", "main", "resources", "META-INF", "native-image", "com.amazonaws", "aws-lambda-java-events", "_reflect-config.json");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testThatAllEventClassesExistWithinGraalVMReflectConfig() throws IOException, ClassNotFoundException {
        ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
        List<String> nonTestClassesInPackage = GraalVMConfigMaker.getNonTestClassesInPackage(classPath, GraalVMConfigMaker.EVENTS_PACKAGE);
        List<ReflectConfigEntry> actualReflectConfigEntries = Arrays.asList(objectMapper.readValue(CONFIG_LOCATION, ReflectConfigEntry[].class));

        Assertions.assertEquals(nonTestClassesInPackage.size(), actualReflectConfigEntries.size(), "Please add the new event / response to the _reflect-config.json");
    }
}
