/*
 * Copyright 2016 Michael Rhöse.
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
package io.varietas.mobile.agrestis.imputare.utils;

import io.varietas.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.agrestis.imputare.error.ToManyInjectedConstructorsException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <h1>DIUtils</h1>
 *
 * @author Michael Rhöse
 * @since Di, Mai 10, 2016
 */
@Deprecated
public class DIUtils {

    private static final Logger LOGGER = Logger.getLogger(DIUtils.class.getName());

    /**
     * It is searching all classes from a given package. This method walks the complete file tree down and collects all classes.
     *
     * @param packagePath
     * @return
     * @throws IOException
     * @throws java.net.URISyntaxException
     */
    public static final List<Class<?>> searchClassesFromPackage(final String packagePath) throws IOException, URISyntaxException {
        final List<Class<?>> clazzList = new ArrayList<>(0);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = DIUtils.fullModifyPackageName(packagePath);

        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {

            URL res = resources.nextElement();
            Path root;

            ///< TODO: iterate jar/zip file
            if (res.toString().contains("jar!")) {
                ///< TODO: not a good way. build a example project to fix the issues
                try (FileSystem zipFileSystem = DIUtils.createZipFileSystem(res.toString(), false)) {
                    root = zipFileSystem.getPath(DIUtils.extractJarFileRootNode(res.toString()));
                };
            } else {
                root = Paths.get(res.toURI());
            }

            clazzList.addAll(DIUtils.walkFileTree(DIUtils.modifyPackageName(packagePath), root));

        }

        return clazzList;
    }

    /**
     * It is searching all classes from a given package. This method walks the complete file tree down and collects all classes.
     *
     * @param packagePath
     * @return
     * @throws IOException
     * @throws java.net.URISyntaxException
     */
    public static final List<Class<?>> searchClassesFromPackage(final Package packagePath) throws IOException, URISyntaxException {
        return DIUtils.searchClassesFromPackage(packagePath.toString());
    }

    public static final Constructor getConstructor(Class<?> clazz) throws ToManyInjectedConstructorsException, NoSuchMethodException {
        ///< Constructor dependencies
        List<Constructor> injectedConstructors = Arrays.asList(clazz.getConstructors()).stream().filter(constructor -> constructor.isAnnotationPresent(Autowire.class)).collect(Collectors.toList());

        if (injectedConstructors.size() > 1) {
            throw new ToManyInjectedConstructorsException(String.format("There are %d constructors injected. Only one is allowed.", injectedConstructors.size()));
        }

        if (!(injectedConstructors.isEmpty())) {
            return injectedConstructors.get(0);
        }

        List<Constructor> annotatedParamsConstructor = Arrays.asList(clazz.getConstructors()).stream().filter(constructor -> Arrays.asList(constructor.getParameters()).stream().filter(parameter -> parameter.isAnnotationPresent(Autowire.class)).findFirst().isPresent()).collect(Collectors.toList());

        if (annotatedParamsConstructor.size() > 1) {
            throw new ToManyInjectedConstructorsException(String.format("There are %d constructors with injected parameters. Only one is allowed.", injectedConstructors.size()));
        }

        if (!(annotatedParamsConstructor.isEmpty())) {
            return annotatedParamsConstructor.get(0);
        }

        return clazz.getConstructor();
    }

    private static List<Class<?>> walkFileTree(final String scannedPackage, final Path packagePath) throws IOException {
        final List<Class<?>> clazzList = new ArrayList<>();

        Files.walkFileTree(packagePath, new SimpleFileVisitor<Path>() {
            ///< Called for each file visited. The basic attributes of the files are also available.
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!file.toString().endsWith(".class")) {
                    LOGGER.log(Level.FINEST, String.format("  -> [SKIPPED] %s", file.getFileName().toString()));
                    return FileVisitResult.CONTINUE;
                }
                try {
                    LOGGER.log(Level.FINEST, String.format("  -> [ACCEPTED] %s", file.getFileName().toString()));
                    clazzList.add(Class.forName(DIUtils.clazzName(scannedPackage, file.toString())));
                } catch (ClassNotFoundException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return clazzList;
    }

    private static String fullModifyPackageName(final String packageName) {
        return DIUtils.modifyPackageName(packageName).replace('.', '/');
    }

    private static String modifyPackageName(final String packageName) {
        if (!packageName.contains(" ")) {
            return packageName;
        }

        String temp = packageName;

        if (temp.contains("package ")) {
            temp = temp.replace("package ", "");
        }

        return temp;
    }

    private static String cleanFileName(final String fileName) {
        String temp = fileName;
        return temp.replace(File.separatorChar, '.').replace(".class", "");
    }

    private static String clazzName(final String scannedPackage, String relativeFilePath) {
        String convertedFileName = DIUtils.cleanFileName(relativeFilePath);
        int index = convertedFileName.indexOf(scannedPackage);

        return convertedFileName.substring(index);
    }

    public static String extractJarFileRootNode(String res) {
        Integer index = res.indexOf(".jar!") + 5;
        return res.substring(index).replace('\\', '/');
    }

    private static FileSystem createZipFileSystem(String res, boolean create) throws IOException {
        // convert the filename to a URI
        Integer index = res.indexOf(".jar!") + 4;
        String externalFilePath = res.substring(0, index).replace('\\', '/').replace("jar:file:/", "");

        Path zipfile = Paths.get(externalFilePath);

//        final URI uri = URI.create(externalFilePath);
        final Map<String, String> env = new HashMap<>();
        env.put("create", Boolean.toString(create));

        return FileSystems.newFileSystem(zipfile, null);
    }
}
