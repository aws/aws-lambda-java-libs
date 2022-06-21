/* Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.amazonaws.services.lambda.runtime.api.client;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class CustomerClassLoader extends URLClassLoader {
    /**
     * This Comparator is used to ensure that jars added to this classloader are added in a deterministic order which
     * does not depend on the underlying filesystem.
     */
    private final static Comparator<String> LEXICAL_SORT_ORDER = Comparator.comparing(String::toString);
    private final static FilenameFilter JAR_FILE_NAME_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
            int offset = name.length() - 4;
            if (offset <= 0) { /* must be at least A.jar */
                return false;
            } else {
                return name.startsWith(".jar", offset);
            }
        }
    };

    CustomerClassLoader(String taskRoot, String optRoot, ClassLoader parent) throws IOException {
        super(getUrls(taskRoot, optRoot), parent);
    }

    private static URL[] getUrls(String taskRoot, String optRoot) throws MalformedURLException {
        File taskDir = new File(taskRoot + "/");
        List<URL> res = new ArrayList<>();
        res.add(newURL(taskDir, ""));
        appendJars(new File(taskRoot + "/lib"), res);
        appendJars(new File(optRoot + "/lib"), res);
        return res.toArray(new URL[res.size()]);
    }

    private static void appendJars(File dir, List<URL> result) throws MalformedURLException {
        if (!dir.isDirectory()) {
            return;
        }
        String[] names = dir.list(CustomerClassLoader.JAR_FILE_NAME_FILTER);
        if (names == null) {
            return;
        }
        Arrays.sort(names, CustomerClassLoader.LEXICAL_SORT_ORDER);

        for (String path : names) {
            result.add(newURL(dir, path));
        }
    }

    private static URL newURL(File parent, String path) throws MalformedURLException {
        return new URL("file", null, -1, parent.getPath() + "/" + path);
    }
}
