/*
 * Copyright 2016 Michael RhÃ¶se.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.varietas.agrestis.imputare.utils.analysis.classes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>ClassLoadUtils</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 6/6/2016
 */
@Slf4j
public class ClassLoadUtils {

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Static methods to do loading operations
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Static methods to do string operations
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String modifyPackageName(final String packageName) {
        if (!packageName.contains(" ")) {
            return packageName;
        }

        String temp = packageName;

        if (temp.contains("package ")) {
            temp = temp.replace("package ", "");
        }

        return temp;
    }

    public static String fullModifyPackageName(final String packageName) {
        return ClassLoadUtils.modifyPackageName(packageName).replace('.', '/');
    }

    public static String cleanFileName(final String fileName) {
        String temp = fileName;
        return temp.replace(File.separatorChar, '.').replace(".class", "");
    }

    public static String clazzName(final String scannedPackage, String relativeFilePath) {
        String convertedFileName = ClassLoadUtils.cleanFileName(relativeFilePath);
        int index = convertedFileName.indexOf(scannedPackage.replace('/', '.'));

        if (index < 0) {
            return "";
        }

        return convertedFileName.substring(index);
    }

    // ----------------------------------------
    public static List<URL> getRootUrls(ClassLoader clazzLoader) {
        List<URL> result = new ArrayList<>();

        while (clazzLoader != null) {
            if (clazzLoader instanceof URLClassLoader) {
                URL[] urls = ((URLClassLoader) clazzLoader).getURLs();
                result.addAll(Arrays.asList(urls));
            }
            clazzLoader = clazzLoader.getParent();
        }
        return result;
    }

    public static List<URL> getResourceUrls(final List<URL> urls, final ClassLoader classLoader, final String path) throws IOException {
        Enumeration<URL> resources = classLoader.getResources(path);
        List<URL> tempResourceUrls = new ArrayList<>();
        while (resources.hasMoreElements()) {
            tempResourceUrls.add(resources.nextElement());
        }

        return java8.util.stream.StreamSupport.stream(tempResourceUrls).filter(resourceUrl -> !StreamSupport.stream(urls).filter(url -> resourceUrl.toString().contains(url.toString())).findFirst().isPresent()).collect(Collectors.toList());
    }
}
