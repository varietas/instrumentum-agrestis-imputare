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
package io.varietas.agrestis.imputare.utils.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>ClassLoadUtils</h1>
 *
 * @author Michael RhÃ¶se
 * @since Mo, Jun 6, 2016
 */
public class ClassLoadUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoadUtils.class);

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Static methods to do loading operations
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static List<Class<?>> visitPackage(final String scannedPackage, final Path packagePath) throws IOException {
        LOGGER.trace("[TRAVERSE PACKAGE] {}", packagePath.toString());
        final List<Class<?>> clazzList = new ArrayList<>();

        Files.walkFileTree(packagePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!file.toString().endsWith(".class")) {
                    LOGGER.trace("[SKIPPED] {}", file.getFileName().toString());
                    return FileVisitResult.CONTINUE;
                }

                try {
                    LOGGER.trace("[ACCEPTED] {}", file.getFileName().toString());
                    AgrestisImpitareClassVisiter classVisiter = new AgrestisImpitareClassVisiter();
                    new ClassReader(new FileInputStream(file.toFile())).accept(classVisiter, 0);

                    if (!classVisiter.isAnnotated()) {
                        LOGGER.trace("[SKIPPED] No annotation located.");
                        return FileVisitResult.CONTINUE;
                    }

                    LOGGER.trace("[ACCEPTED] Annotation located.");
                    clazzList.add(Class.forName(ClassLoadUtils.clazzName(scannedPackage, file.toString())));
                } catch (ClassNotFoundException ex) {
                    LOGGER.error(ex.getLocalizedMessage(), ex);
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return clazzList;
    }

    public static List<Class<?>> visitJar(URL url) throws IOException, URISyntaxException, ClassNotFoundException {
        LOGGER.trace("[TRAVERSE URL] {}", url.toString());
        ClassLoader jarClassLoader = URLClassLoader.newInstance(new URL[]{url}, Thread.currentThread().getContextClassLoader());
        List<Class<?>> clazzList = new ArrayList<>();

        try (InputStream urlIn = url.openStream(); JarInputStream jarIn = new JarInputStream(urlIn)) {
            JarEntry entry;
            JarFile jarFile = new JarFile(URLDecoder.decode(url.getPath(), "UTF8"));
            while ((entry = jarIn.getNextJarEntry()) != null) {
                if (entry.getName().endsWith(".class")) {
                    LOGGER.trace("[ACCEPTED] {}", entry.getName());
                    AgrestisImpitareClassVisiter classVisiter = new AgrestisImpitareClassVisiter();
                    new ClassReader(jarFile.getInputStream(entry)).accept(classVisiter, 0);

                    if (!classVisiter.isAnnotated()) {
                        LOGGER.trace("[SKIPPED] No annotation located.");
                        continue;
                    }

                    LOGGER.trace("[ACCEPTED] Annotation located.");
                    clazzList.add(jarClassLoader.loadClass(entry.getName().replace(".class", "").replace('/', '.')));
                }
            }
        }
        return clazzList;
    }

    private static class AgrestisImpitareClassVisiter extends ClassVisitor {

        private boolean annotated = false;

        public AgrestisImpitareClassVisiter() {
            super(Opcodes.ASM5);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String annotationAsString, boolean visible) {
            this.annotated = (ClassMetaDataExtractionUtils.getPresentAnnotationCodeForAnnotationAsString(annotationAsString.replace('/', '.')) > ClassMetaDataExtractionUtils.AnnotationCodes.NONE);
            return null;
        }

        public boolean isAnnotated() {
            return annotated;
        }
    }

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

    public static List<URL> getResourceUrls(final List<URL> urls, final ClassLoader classLoader, final String path) throws IOException{
        Enumeration<URL> resources = classLoader.getResources(path);
        List<URL> tempResourceUrls = new ArrayList<>();
        while (resources.hasMoreElements()) {
            tempResourceUrls.add(resources.nextElement());
        }
        
        return tempResourceUrls.stream().filter(resourceUrl -> !urls.stream().filter(url -> resourceUrl.toString().contains(url.toString())).findFirst().isPresent()).collect(Collectors.toList());
    }
}
