/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.condition.OS.MAC;

public class CustomerClassLoaderTest {

    final static String[] EXAMPLE_FUNCTION = new String[]{
            "var/runtime/lib/LambdaJavaRTEntry-1.0.jar",
            "user/path/Hello.class",
            "user/path/example/Hello.class",
            "user/path/hidden.jar",
            "user/path/lib/b.jar",
            "user/path/lib/z.jar",
            "user/path/lib/A.jar",
            "user/path/lib/4.jar",
            "user/path/lib/λ.jar",
            "user/path/lib/a.jar",
            "user/path/lib/hidden/c.jar"
    };

    final static String[] EXAMPLE_FUNCTION_AND_LAYERS = new String[]{
            "var/runtime/lib/LambdaJavaRTEntry-1.0.jar",
            "user/path/Hello.class",
            "user/path/example/Hello.class",
            "user/path/hidden.jar",
            "user/path/lib/a.jar",
            "user/path/lib/b.jar",
            "user/path/lib/hidden/c.jar",
            "opt/hidden.jar",
            "opt/java/hidden.jar",
            "opt/java/lib/b.jar",
            "opt/java/lib/c.jar",
            "opt/java/lib/a-2.0.jar",
            "opt/java/lib/a-1.0.jar",
            "opt/java/lib/a-1.12.jar",
            "opt/java/lib/a-1.21.jar",
            "user/path/lib/hidden/d.jar"
    };

    /**
     * Generate a fake file-system with the provided list of paths
     */
    private Path fakeFileSystem(String[] paths) throws IOException {
        Path dir = Files.createTempDirectory("rtentry");

        for (String path : paths) {
            Path stub = dir.resolve(path);
            Files.createDirectories(stub.getParent());
            Files.write(stub, "fake-data".getBytes());
        }

        return dir;
    }

    /**
     * Strip the base URL from list
     */
    private List<String> strip(String base, URL[] urls) {
        return Arrays.stream(urls)
                .map(URL::toExternalForm)
                .filter(s -> s.startsWith(base))
                .map(s -> s.substring(base.length()))
                .collect(Collectors.toList());
    }

    @Test
    @DisabledOnOs(MAC) // test fails on systems with case-insensitive volumes
    public void customerClassLoaderFunction() throws IOException {
        try {
            Path rootDir = fakeFileSystem(EXAMPLE_FUNCTION);

            URLClassLoader customerClassLoader = new CustomerClassLoader(
                    rootDir.resolve("user/path").toString(),
                    rootDir.resolve("opt/java").toString(),
                    ClassLoader.getSystemClassLoader());

            List<String> res = strip("file:" + rootDir.toString(), customerClassLoader.getURLs());

            Assertions.assertEquals(Arrays.asList(
                    "/user/path/",
                    "/user/path/lib/4.jar",
                    "/user/path/lib/A.jar",
                    "/user/path/lib/a.jar",
                    "/user/path/lib/b.jar",
                    "/user/path/lib/z.jar",
                    "/user/path/lib/λ.jar"),
                    res);
        } catch(Throwable t) {
            // this system property is the name of the charset used when encoding/decoding file paths
            // exception is expected if it is not set to a UTF variant or not set at all
            String systemEncoding = System.getProperty("sun.jnu.encoding");

            if (systemEncoding != null && !systemEncoding.toLowerCase().contains("utf")){
                Assertions.assertTrue(t.getMessage().contains("Malformed input or input contains unmappable characters"));
            }
            else {
                throw t;
            }
        }
    }

    @Test
    @DisabledOnOs(MAC) // test fails on systems with case-insensitive volumes
    public void customerClassLoaderLayer() throws IOException {

        Path rootDir = fakeFileSystem(EXAMPLE_FUNCTION_AND_LAYERS);

        URLClassLoader customerClassLoader = new CustomerClassLoader(
                rootDir.resolve("user/path").toString(),
                rootDir.resolve("opt/java").toString(),
                ClassLoader.getSystemClassLoader());

        List<String> res = strip("file:" + rootDir.toString(), customerClassLoader.getURLs());

        // Layer order is fixed (unicode value)
        Assertions.assertEquals(Arrays.asList(
                "/user/path/",
                "/user/path/lib/a.jar",
                "/user/path/lib/b.jar",
                "/opt/java/lib/a-1.0.jar",
                "/opt/java/lib/a-1.12.jar",
                "/opt/java/lib/a-1.21.jar",
                "/opt/java/lib/a-2.0.jar",
                "/opt/java/lib/b.jar",
                "/opt/java/lib/c.jar"
        ), res);
    }
}
