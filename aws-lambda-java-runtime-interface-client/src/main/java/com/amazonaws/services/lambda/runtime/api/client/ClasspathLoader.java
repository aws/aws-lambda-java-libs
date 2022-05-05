/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This class loads all of the classes that are in jars on the classpath.
 *
 * It is used to generate a class list and Application CDS archive that includes all the possible classes that could be
 * loaded by the runtime. This simplifies the process of generating the Application CDS archive.
 */
public class ClasspathLoader {

    private static final Set<String> BLOCKLIST = new HashSet<>();
    private static final ClassLoader SYSTEM_CLASS_LOADER = ClassLoader.getSystemClassLoader();
    private static final int CLASS_SUFFIX_LEN = ".class".length();

    static {
        // NativeClient loads a native library and crashes if loaded here so just exclude it
        BLOCKLIST.add("com.amazonaws.services.lambda.runtime.api.client.runtimeapi.NativeClient");
    }

    private static String pathToClassName(final String path) {
        return path.substring(0, path.length() - CLASS_SUFFIX_LEN).replaceAll("/|\\\\", "\\.");
    }

    private static void loadClass(String name) {
        try {
            Class.forName(name, true, SYSTEM_CLASS_LOADER);
        } catch (ClassNotFoundException e) {
            System.err.println("[WARN] Failed to load " +  name + ": " + e.getMessage());
        }
    }

    private static void loadClassesInJar(File file) throws IOException {
        JarFile jar = new JarFile(file);
        Enumeration<JarEntry> en = jar.entries();
        while (en.hasMoreElements()) {
            JarEntry entry = en.nextElement();

            if(!entry.getName().endsWith(".class")) {
                continue;
            }

            String name = pathToClassName(entry.getName());

            if(BLOCKLIST.contains(name)) {
                continue;
            }

            loadClass(name);
        }
    }

    private static void loadClassesInClasspathEntry(String entry) throws IOException {
        File file = new File(entry);

        if(!file.exists()) {
            throw new FileNotFoundException("Classpath entry does not exist: " + file.getPath());
        }

        if(file.isDirectory() || !file.getPath().endsWith(".jar")) {
            System.err.println("[WARN] Only jar classpath entries are supported. Skipping " + file.getPath());
            return;
        }

        loadClassesInJar(file);
    }

    private static void loadAllClasses() throws IOException {
        final String classPath = System.getProperty("java.class.path");
        if(classPath == null) {
            return;
        }
        for(String classPathEntry : classPath.split(File.pathSeparator)) {
            loadClassesInClasspathEntry(classPathEntry);
        }
    }

    public static void main(String[] args) throws IOException {
        loadAllClasses();
    }
}
