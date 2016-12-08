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
import io.varietas.instrumentum.simul.storage.UnsortedStorage;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassLoadUtils;
import io.varietas.agrestis.imputare.utils.searching.ClazzCollectorUtils;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java8.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>ClassCollector</h2>
 *
 * This class is the entry point to the collection of all annotated classes of the current application. All classes contained in the packages and used libraries will scanned and loaded.
 *
 * TODO: Remove NIO classes
 *
 * @author Michael Rhöse
 * @since Di, Jun 28, 2016
 */
@Slf4j
public class ClassCollector {

    private final String applicationPackage;
    private final UnsortedStorage clazzStorage;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassCollector(String applicationPackage) {
        this.applicationPackage = applicationPackage;
        this.clazzStorage = new UnsortedStorageImpl();
    }

    public ClassCollector(final Package applicationPackage) {
        this(applicationPackage.toString());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassCollector collectAnnotatedClazzes() throws IOException, ClassNotFoundException, URISyntaxException {

        this.doClazzCollection();

        return this;
    }

    public UnsortedStorage getStorage() {
        return this.clazzStorage;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void doClazzCollection() throws IOException {
        String path = ClassLoadUtils.fullModifyPackageName(this.applicationPackage);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<URL> urls = ClassLoadUtils.getRootUrls(classLoader);

        urls.addAll(ClassLoadUtils.getResourceUrls(urls, classLoader, path));

        StreamSupport.stream(urls).forEach(url -> this.clazzStorage.storeAll(ClazzCollectorUtils.loadClazzes(url, path)));

        String projectPath = System.getProperty("user.dir");

        this.clazzStorage.storeAll(ClazzCollectorUtils.loadClazzes(Paths.get(projectPath), projectPath));
    }
}
