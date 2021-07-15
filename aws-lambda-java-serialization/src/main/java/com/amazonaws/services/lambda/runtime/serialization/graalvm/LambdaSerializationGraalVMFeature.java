package com.amazonaws.services.lambda.runtime.serialization.graalvm;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.ext.Java7Handlers;
import com.fasterxml.jackson.databind.ext.Java7HandlersImpl;
import com.fasterxml.jackson.databind.ext.Java7SupportImpl;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * This class programmatically registers classes, methods and fields
 * to be added to an application when used with GraalVM native-image.
 *
 * These specific classes are registered because GraalVM can't detect
 * them during static analysis.
 *
 * @see <a href="https://www.graalvm.org/reference-manual/native-image/">GraalVM native-image</a>
 */
public class LambdaSerializationGraalVMFeature implements Feature {

    private static final Set<Class> classesForReflectConfig = new HashSet<>();

    static {
        classesForReflectConfig.add(Deserializers.class);
        classesForReflectConfig.add(Serializers.class);
        classesForReflectConfig.add(Java7SupportImpl.class);
        classesForReflectConfig.add(Java7HandlersImpl.class);
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
    }
}
