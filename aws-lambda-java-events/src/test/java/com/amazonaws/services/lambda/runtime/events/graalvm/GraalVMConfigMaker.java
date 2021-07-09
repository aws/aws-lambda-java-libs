package com.amazonaws.services.lambda.runtime.events.graalvm;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2CustomAuthorizerEventTest;
import com.amazonaws.services.lambda.runtime.events.HttpUtils;
import com.amazonaws.services.lambda.runtime.events.IamPolicyResponseTest;
import com.amazonaws.services.lambda.runtime.events.IamPolicyResponseV1Test;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotificationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Example Application to generate GraalVM _reflect-config.json files from all the classes and subclasses in a package.
 *
 */
public class GraalVMConfigMaker {

    public static final String EVENTS_PACKAGE = "com.amazonaws.services.lambda.runtime.events";
    public static final List<String> testClasses = Arrays.asList(GraalVMConfigMaker.class.getName(),
            GraalVMSupportTest.class.getName(),
            ReflectConfigEntry.class.getName(),
            S3EventNotificationTest.class.getName(),
            APIGatewayV2CustomAuthorizerEventTest.class.getName(),
            HttpUtils.class.getName(),
            IamPolicyResponseTest.class.getName(),
            IamPolicyResponseV1Test.class.getName());

    public static void main(String[] args )
    {
        GraalVMConfigMaker app = new GraalVMConfigMaker();
        try {
            app.run(EVENTS_PACKAGE, "aws-lambda-java-events/src/main/resources/META-INF/native-image/com.amazonaws/aws-lambda-java-events/_reflect-config.json");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void run(String packageName, String pathName) throws IOException, ClassNotFoundException {
        ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
        List<String> allEventClasses = getNonTestClassesInPackage(classPath, packageName);
        List<ReflectConfigEntry> reflectConfigEntryList = allEventClasses.stream()
                .map(ReflectConfigEntry::allTrue)
                .collect(Collectors.toList());
        String reflectConfig = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(reflectConfigEntryList);
        System.out.println(reflectConfig);
        FileUtils.writeStringToFile(new File(pathName), reflectConfig, UTF_8);
    }

    public static List<String> getNonTestClassesInPackage(ClassPath classPath, String packageName) throws ClassNotFoundException {
        ImmutableSet<ClassPath.ClassInfo> topLevelClassesRecursive = classPath.getTopLevelClassesRecursive(packageName);

        List<String> reflectConfigEntries = new ArrayList<>();

        for (ClassPath.ClassInfo classInfo : topLevelClassesRecursive) {
            if (notTestClass(classInfo.getName())) {
                reflectConfigEntries.add(classInfo.getName());
            }
            Class<?> aClass = Class.forName(classInfo.getName());
            Class<?>[] declaredClasses = aClass.getDeclaredClasses();
            for (Class<?> declaredClass : declaredClasses) {
                if (notTestClass(classInfo.getName())) {
                    reflectConfigEntries.add(declaredClass.getName());
                }
            }
        }
        return reflectConfigEntries;
    }

    private static boolean notTestClass(String name) {
        return !testClasses.contains(name);
    }
}
