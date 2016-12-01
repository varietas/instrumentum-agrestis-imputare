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
package io.varietas.agrestis.imputare.utils.searching;

import io.varietas.agrestis.imputare.utils.analysis.classes.ClassLoadUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>ClazzCollectorUtils</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 12/1/2016
 */
@Slf4j
public class ClazzCollectorUtils {

    public static List<Class<?>> doClazzLoading(final Path root, final String path) throws IOException {
        if (!Files.exists(root)) {
            LOGGER.debug("File or folder {} not exists.", root.toString());
            return new ArrayList<>();
        }
        ///< Load all classes from the given package
        return ClassLoadUtils.visitPackage(ClassLoadUtils.modifyPackageName(path), root);
    }

    public static List<Class<?>> doClazzLoading(URL url, final String path) throws RuntimeException {

        try {
            if (!url.toString().contains("jar")) {
                Path root = Paths.get(url.toURI());
                return ClazzCollectorUtils.doClazzLoading(root, path);
            }

            ///< Load all classes from required jar
            return ClassLoadUtils.visitJar(url);

        } catch (URISyntaxException | IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
