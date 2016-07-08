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
package io.varietas.agrestis.imputare.searching;

import io.varietas.agrestis.imputare.storage.UnsortedStorageImpl;
import io.varietas.agrestis.imputare.storage.UnsortedStorage;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassLoadUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * <h1>ClassCollector</h1>
 *
 * This class is the entry point to the collection of all annotated classes of the current application. All classes contained in the packages and used libraries will scanned and loaded.
 *
 * @author Michael Rhöse
 * @since Di, Jun 28, 2016
 */
public class ClassCollector {

    private final String applicationPackage;
    private final UnsortedStorage clazzStorage;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassCollector(String applicationPackage) {
        this.applicationPackage = applicationPackage;
        this.clazzStorage = new UnsortedStorageImpl();
    }

    public ClassCollector(Package applicationPackage) {
        this(applicationPackage.toString());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassCollector collectAnnotatedClazzes() throws IOException, ClassNotFoundException, URISyntaxException {

        String path = ClassLoadUtils.fullModifyPackageName(this.applicationPackage);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<URL> urls = ClassLoadUtils.getRootUrls(classLoader);

        urls.addAll(ClassLoadUtils.getResourceUrls(urls, classLoader, path));

        urls.stream().forEach(url -> this.doClazzLoading(url, path));

        return this;
    }

    public UnsortedStorage getClazzStorage() {
        return this.clazzStorage;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void doClazzLoading(URL url, final String path) throws RuntimeException {

        try {
            if (!url.toString().contains("jar")) {
                Path root = Paths.get(url.toURI());
                ///< Load all classes from the given package
                this.clazzStorage.storeAll(ClassLoadUtils.visitPackage(ClassLoadUtils.modifyPackageName(path), root));

                return;
            }

            ///< Load all classes from required jar
            this.clazzStorage.storeAll(ClassLoadUtils.visitJar(url));

        } catch (URISyntaxException | IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
