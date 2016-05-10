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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <h1>DIUtils</h1>
 *
 * @author Michael Rhöse
 * @since Di, Mai 10, 2016
 */
public class DIUtils {

    private static final Logger LOGGER = Logger.getLogger(DIUtils.class.getName());

    /**
     * Is searching all classes from a given package. This method walks the complete file tree down and collects all classes.
     *
     * @param packagePath
     * @return
     * @throws IOException
     */
    public static List<Class<?>> searchClassesFromPackage(final Path packagePath) throws IOException {
        final List<Class<?>> clazzList = new ArrayList<>(0);
        Files.walkFileTree(packagePath, new FileVisitor<Path>() {
            ///< Called before a directory visit.
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                LOGGER.log(Level.FINEST, String.format("=== %s", dir.getFileName().toString()));
                return FileVisitResult.CONTINUE;
            }

            ///< Called for each file visited. The basic attributes of the files are also available.
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!file.toString().endsWith(".class")) {
                    LOGGER.log(Level.FINEST, String.format("  -> [SKIPPED] %s", file.getFileName().toString()));
                    return FileVisitResult.CONTINUE;
                }
                try {
                    LOGGER.log(Level.FINEST, String.format("  -> [ACCEPTED] %s", file.getFileName().toString()));
                    clazzList.add(Class.forName(file.toFile().getName()));
                } catch (ClassNotFoundException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.CONTINUE;
            }

            ///< Called if the file visit fails for any reason.
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                LOGGER.log(Level.FINEST, String.format("  -> [FAILURE] %s", file.toString()));
                return FileVisitResult.CONTINUE;
            }

            ///< Called after a directory visit is complete.
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        return clazzList;
    }
}
