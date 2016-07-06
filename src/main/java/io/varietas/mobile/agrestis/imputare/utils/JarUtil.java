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

import io.varietas.agrestis.imputare.contant.ExtractionFileType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * <h1>JarUtil</h1>
 *
 * @author Michael Rhöse
 * @since So, Jan 24, 2016
 */
public class JarUtil {

    /**
     * Scans the given jar file for all classes and returns a list of class URLs. The URLs could used for load the classes later with the main class loader.
     *
     * @param jar Given jar with which contains the classes.
     * @return A list of class URLs.
     * @throws IOException Thrown if there is a problem with the jar file itself.
     */
    public static List<String> loadClassUrlsFromJar(JarFile jar) throws IOException {
        List<ZipEntry> entries = JarUtil.loadObjectsUrlFromJar(jar, ExtractionFileType.TYPE_CLASS, false);
        List<String> urlList = new ArrayList<>(entries.size());
        entries.forEach(entry -> urlList.add(entry.getName().replace(ExtractionFileType.TYPE_CLASS, "").replace("/", ".")));
        return urlList;
    }

    /**
     * Collects all elements from a given jar file. The are a number of possible options:
     *
     * <ol>
     * <li><b>criteria {@link ExtractionFileType}.TYPE_ALL:</b> Collect every file contains in the jar. The isFindFirst flag will be ignored.</li>
     * <li><b>criteria {@link ExtractionFileType}.TYPE_JSON:</b> Collect Json files. If you want the first Json file in the jar you can use the isFindFirat flag (TRUE). Normally the first Json after
     * the jar root will returned.</li>
     * <li><b>criteria {@link ExtractionFileType}.TYPE_Class:</b> Collect class files. If you want the first class in the jar you can use the isFindFirat flag (TRUE). Normally the first class after
     * the jar root will returned.</li>
     * <li><b>Custom criteria (char sequence):</b> This option allows to search one or more file contains the given string. If the isFindFirst flag is set the first matching file will returned.
     * Otherwise all located files will returned.</li>
     * </ol>
     *
     * @param jar
     * @param criteria
     * @param isFindFirst
     * @return
     * @throws IOException
     */
    public static List<ZipEntry> loadObjectsUrlFromJar(JarFile jar, String criteria, boolean isFindFirst) throws IOException {
        List<ZipEntry> locatedObjects = new ArrayList<>();

//        log.trace(String.format("Plugin file '%s' loaded.", jar.getName()));
//        log.trace(String.format("List all objects for type '%s'-----------------", criteria));
        Enumeration<? extends ZipEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            ZipEntry nextElement = entries.nextElement();

            // If the current element is a folder, return to the next element
            if (nextElement.isDirectory()) {
                continue;
            }

//            log.trace(String.format("- %s", nextElement.getName()));
            // If the option "all" is used every element will be collected.
            if (criteria.equals(ExtractionFileType.TYPE_ALL)) {
                locatedObjects.add(nextElement);
                continue;
            }

            // If the name of the current file contains the criteria string.
            if (nextElement.getName().endsWith(criteria)) {
                locatedObjects.add(nextElement);

                // If the isFindFirst flag is true the while loop will breaked.
                if (isFindFirst) {
                    break;
                }
            }
        }
        return locatedObjects;
    }
}
