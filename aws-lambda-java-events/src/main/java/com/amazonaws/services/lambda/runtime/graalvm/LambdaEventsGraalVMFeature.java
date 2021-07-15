package com.amazonaws.services.lambda.runtime.graalvm;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * The aws-lambda-java-events library supports GraalVM by containing a reflect-config.json file. This is located
 * src/main/resources/META-INF/native-image/com.amazonaws/aws-lambda-java-events/reflect-config.json
 *
 * This config is used by the GraalVM native-image tool in order to load the required classes and methods into the
 * native binary it creates.
 *
 * Any event or response class added to this library needs to be added to this config file.
 *
 * This implementation of Feature does that by iterating through the events package and registering
 * them for runtime reflection.
 */
public class LambdaEventsGraalVMFeature implements Feature {

    public static final String EVENTS_PACKAGE_NAME = "com.amazonaws.services.lambda.runtime.events";

    public static List<Class<?>> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if(files == null){
            return classes;
        }
        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                assert !fileName.contains(".");
                classes.addAll(findClasses(file, packageName + "." + fileName));
            } else if (fileName.endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + fileName.substring(0, fileName.length() - ".class".length())));
            }
        }
        return classes;
    }

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        try {
            List<Class<?>> classes = getClasses(EVENTS_PACKAGE_NAME);
            for (Class<?> cl : classes) {
                System.out.println("Registering class:"+cl.getName());
                registerClass(cl);
                registerMethods(cl);
                registerFields(cl);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.err.println("Failed to automatically load classes from "+ LambdaEventsGraalVMFeature.class.getName());
            System.exit(1);
        }
    }

    private void registerMethods(Class<?> cl) {
        for (Method method : cl.getDeclaredMethods()) {
            RuntimeReflection.register(method);
        }
    }

    private void registerFields(Class<?> cl) {
        for (Field field : cl.getDeclaredFields()) {
            RuntimeReflection.register(field);
        }
    }

    private void registerClass(Class<?> cl) {
        RuntimeReflection.register(cl);
        for (Constructor<?> constructor : cl.getDeclaredConstructors()) {
            RuntimeReflection.register(constructor);
        }
        System.out.printf("\tAdding constructors for %s%n", cl.getName());
    }
}