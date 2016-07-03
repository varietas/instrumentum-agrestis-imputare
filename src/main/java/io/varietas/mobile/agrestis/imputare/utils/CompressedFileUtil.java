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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

/**
 * <h1>CompressedFileUtil</h1>
 *
 * @author Michael Rhöse
 * @since Di, Feb 9, 2016
 */
public class CompressedFileUtil {

    public static final String FILE_PATH_PATTERN = "%s" + File.separator + "%s";

    /**
     * Loads a file from the file system and check for 'zip' or 'jar' extention. If it is no compressed file the process will skipped. This functionality is only for local installed plugins. The
     * supported types are ZIP and JAR at the moment.
     *
     * @param filePath
     * @param folderPath Path to the watched folder.
     * @return
     * @throws IOException
     */
    public static Optional<ZipFile> loadZipFile(Path filePath, String folderPath) throws IOException {
        String fileName = filePath.getFileName().toString();

//        log.debug(String.format("File %s located.", String.format(FILE_PATH_PATTERN, CompressedFileUtil.cleanFolderPath(folderPath), fileName)));
        if (fileName.contains(".zip")) {
            return Optional.of(new ZipFile(new File(String.format("%s", String.format(FILE_PATH_PATTERN, folderPath, fileName)))));
        }

        if (fileName.contains(".jar")) {
            return Optional.of(new JarFile(new File(String.format("%s", String.format(FILE_PATH_PATTERN, folderPath, fileName)))));
        }

        return Optional.empty();
    }

    public static Optional<ZipFile> loadZipFile(Path filePath) throws IOException {

//        log.debug("File {} located.", filePath.getFileName().toString());
        if (filePath.toString().contains(".zip")) {
            return Optional.of(new ZipFile(filePath.toFile()));
        }

        if (filePath.toString().contains(".jar")) {
            return Optional.of(new JarFile(filePath.toFile()));
        }

        return Optional.empty();
    }

    private static String cleanFolderPath(String folderPath) {
        return folderPath.replace("file:///", "");
    }
}
