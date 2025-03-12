/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/

package com.amazonaws.services.lambda.runtime.api.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class ClasspathLoaderTest {

    @Test
    void testLoadAllClassesWithNoClasspath() throws IOException {
        String originalClasspath = System.getProperty("java.class.path");
        try {
            System.clearProperty("java.class.path");
            ClasspathLoader.main(new String[]{});
        } finally {
            if (originalClasspath != null) {
                System.setProperty("java.class.path", originalClasspath);
            }
        }
    }

    @Test
    void testLoadAllClassesWithEmptyClasspath() {
        String originalClasspath = System.getProperty("java.class.path");
        try {
            System.setProperty("java.class.path", "");
            assertThrows(FileNotFoundException.class, () -> 
                ClasspathLoader.main(new String[]{}));
        } finally {
            if (originalClasspath != null) {
                System.setProperty("java.class.path", originalClasspath);
            }
        }
    }

    @Test
    void testLoadAllClassesWithInvalidPath() {
        String originalClasspath = System.getProperty("java.class.path");
        try {
            System.setProperty("java.class.path", "nonexistent/path");
            assertThrows(FileNotFoundException.class, () -> 
                ClasspathLoader.main(new String[]{}));
        } finally {
            if (originalClasspath != null) {
                System.setProperty("java.class.path", originalClasspath);
            }
        }
    }

    @Test
    void testLoadAllClassesWithValidJar(@TempDir Path tempDir) throws IOException {
        File jarFile = createSimpleJar(tempDir, "test.jar", "TestClass");
        String originalClasspath = System.getProperty("java.class.path");
        try {
            System.setProperty("java.class.path", jarFile.getAbsolutePath());
            ClasspathLoader.main(new String[]{});
        } finally {
            if (originalClasspath != null) {
                System.setProperty("java.class.path", originalClasspath);
            }
        }
    }

    @Test
    void testLoadAllClassesWithDirectory(@TempDir Path tempDir) throws IOException {
        String originalClasspath = System.getProperty("java.class.path");
        try {
            System.setProperty("java.class.path", tempDir.toString());
            ClasspathLoader.main(new String[]{});
        } finally {
            if (originalClasspath != null) {
                System.setProperty("java.class.path", originalClasspath);
            }
        }
    }

    @Test
    void testLoadAllClassesWithMultipleEntries(@TempDir Path tempDir) throws IOException {
        File jarFile1 = createSimpleJar(tempDir, "test1.jar", "TestClass1");
        File jarFile2 = createSimpleJar(tempDir, "test2.jar", "TestClass2");
        
        String originalClasspath = System.getProperty("java.class.path");
        try {
            String newClasspath = jarFile1.getAbsolutePath() + 
                                File.pathSeparator + 
                                jarFile2.getAbsolutePath();
            System.setProperty("java.class.path", newClasspath);
            ClasspathLoader.main(new String[]{});
        } finally {
            if (originalClasspath != null) {
                System.setProperty("java.class.path", originalClasspath);
            }
        }
    }

    private File createSimpleJar(Path tempDir, String jarName, String className) throws IOException {
        File jarFile = tempDir.resolve(jarName).toFile();
        
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile))) {
            // Add a simple non-class file to make it a valid jar
            JarEntry entry = new JarEntry("com/test/" + className + ".txt");
            jos.putNextEntry(entry);
            jos.write("test content".getBytes());
            jos.closeEntry();
        }
        
        return jarFile;
    }
}
