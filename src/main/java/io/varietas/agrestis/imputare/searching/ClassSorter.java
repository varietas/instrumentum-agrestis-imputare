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

import io.varietas.agrestis.imputare.error.IllegalAnnotationException;
import io.varietas.instrumentum.simul.storage.SortedStorage;
import io.varietas.agrestis.imputare.storage.impl.SortedStorageImpl;
import io.varietas.instrumentum.simul.storage.UnsortedStorage;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import java8.util.Optional;

/**
 * <h2>ClassSorter</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 6/30/2016
 */
public class ClassSorter {

    private final SortedStorageImpl sortedClassStorage;
    private final UnsortedStorage classStorage;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassSorter(UnsortedStorage classStorage) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        this.classStorage = classStorage;
        this.sortedClassStorage = new SortedStorageImpl();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Sorts the located classes for present annotation.
     *
     * @throws IllegalAnnotationException
     */
    public ClassSorter sortLocatedClazzes() throws IllegalAnnotationException {

        Optional<Class<?>> next = this.classStorage.next();

        while (next.isPresent()) {

            Integer code = ClassMetaDataExtractionUtils.getPresentAnnotationCode(next.get());

            if (!ClassMetaDataExtractionUtils.isValidAnnotation(code)) {
                throw new IllegalAnnotationException(code, "Illegal annotation located.");
            }

            this.sortedClassStorage.store(next.get(), code);

            next = this.classStorage.next();
        }

        return this;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public SortedStorage<Integer, Class<?>> getStorage() {
        return this.sortedClassStorage;
    }
}
