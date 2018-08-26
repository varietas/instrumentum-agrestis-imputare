/*
 * Copyright 2016 varietas.io
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

import io.varietas.agrestis.imputare.storage.impl.UnsortedStorageImpl;
import io.varietas.instrumentum.simul.storage.UnsortedStorage;
import io.varietas.agrestis.imputare.utils.searching.ClazzCollectorUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>ClassCollector</h2>
 *
 * This class is the entry point to the collection of all annotated classes of the current application. All classes contained in the packages and used libraries will scanned and loaded.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 6/28/2016
 */
@Slf4j
public final class ClassCollector {

    private final List<String> sources;
    private final UnsortedStorage clazzStorage;
    private final List<ClassLoader> additionalClassLoaders;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassCollector(final String... sources) {
        this.sources = new ArrayList<>();
        this.clazzStorage = new UnsortedStorageImpl();
        this.additionalClassLoaders = new ArrayList<>();
    }

    public ClassCollector additionalClassLoader(final ClassLoader classLoader) {
        this.additionalClassLoaders.add(classLoader);
        return this;
    }

    public ClassCollector addApplicationPackage(final Package appPackage) {
        if (!this.sources.contains(appPackage.toString())) {
            this.sources.add(appPackage.toString());
        }
        return this;
    }

    public ClassCollector addApplicationPackage(final String appPackage) {
        return this.addOther(appPackage);
    }

    public ClassCollector addOther(final String other) {
        if (!this.sources.contains(other)) {
            this.sources.add(other);
        }
        return this;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Filters all classes of current class path for annotated classes and stores them in an unsorted storage.
     *
     * @return Storage of all annotated classes.
     */
    public final ClassCollector collectAnnotatedClazzes() {

        this.doClazzCollection();

        return this;
    }

    public UnsortedStorage getStorage() {
        return this.clazzStorage;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void doClazzCollection() {

        this.clazzStorage.storeAll(ClazzCollectorUtils.loadClazzes(this.additionalClassLoaders, this.sources));
    }
}
