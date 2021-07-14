package com.amazonaws.services.lambda.runtime.graalvm;

import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.amazonaws.services.lambda.runtime.LambdaRuntimeInternal;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class LambdaCoreGraalVMFeature implements Feature {

    private static final Set<Class> classesForReflectConfig = new HashSet<>();

    static {
        classesForReflectConfig.add(LambdaRuntime.class);
        classesForReflectConfig.add(LambdaRuntimeInternal.class);
    }

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        for (Class aClass : classesForReflectConfig) {
            registerClass(aClass);
            registerMethods(aClass);
            registerFields(aClass);
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
