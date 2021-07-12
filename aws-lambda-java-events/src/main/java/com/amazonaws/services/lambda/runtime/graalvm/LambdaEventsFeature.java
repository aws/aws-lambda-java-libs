package com.amazonaws.services.lambda.runtime.graalvm;

import com.amazonaws.services.lambda.runtime.events.S3Event;
//import com.oracle.svm.core.annotate.AutomaticFeature;
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

public class LambdaEventsFeature implements Feature {

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
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        try {
            List<Class<?>> classes = getClasses(S3Event.class.getPackage().getName());
            for (Class<?> cl : classes) {
                System.out.println("Registering class:"+cl.getName());
                registerClass(cl);
                registerMethods(cl);
                registerFields(cl);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.err.println("Failed to automatically load classes from "+LambdaEventsFeature.class.getName());
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
        System.out.println(String.format("\tAdding constructors for %s", cl.getName()));
    }
}
