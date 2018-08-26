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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>ClassLoadUtils</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 6/6/2016
 */
@Slf4j
public class ClassLoadUtils {

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // Static methods to do loading operations
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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
}
