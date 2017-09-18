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
package io.varietas.agrestis.imputare.storage.impl;

import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import io.varietas.instrumentum.simul.storage.SortedStorage;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <h2>SortedStorageImpl</h2>
 *
 * This class represents a container to store all classes sorted by {@link ClassMetaDataExtractionUtils.AnnotationCodes}. Additionally there are a number of useful methods.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 6/30/2016
 * @param <CODE> Generic code type.
 * @param <TYPE> Generic type which is stored.
 */
public class SortedStorageImpl<CODE extends Serializable, TYPE> implements SortedStorage<CODE, TYPE> {

    protected final Map<CODE, List<TYPE>> storage;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public SortedStorageImpl() {
        this.storage = new HashMap<>();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Optional<TYPE> next() {

        final Optional<List<TYPE>> nextList = this.storage.values()
            .stream()
            .filter(list -> !list.isEmpty())
            .findFirst();

        if (!nextList.isPresent()) {
            return Optional.empty();
        }

        TYPE res = nextList.get().get(nextList.get().size() - 1);
        nextList.get().remove(res);

        return Optional.of(res);
    }

    @Override
    public Optional<TYPE> next(final CODE code) {

        final List<TYPE> nextList = this.storage.get(code);

        if (nextList.isEmpty()) {
            return Optional.empty();
        }

        TYPE res = nextList.get(nextList.size() - 1);
        nextList.remove(res);

        return Optional.of(res);
    }

    /**
     * Stores a class in the storage. Returns -1 if the class is not stored otherwise the current number of stored classes will be returned.
     *
     * @param entry Class to be stored.
     * @param code Annotation type code where the class should be stored for.
     * @return Number of stored classes or -1 for an error.
     */
    @Override
    public int store(final TYPE entry, final CODE code) {
        if (Objects.equals(code, ClassMetaDataExtractionUtils.AnnotationCodes.NONE)) {
            return -1;
        }

        if (!this.storage.get(code).add(entry)) {
            return -1;
        }

        return this.storage.get(code).size();
    }

    /**
     * Stores all classes from a given collection in the storage. Returns -1 if the classes are not stored otherwise the current number of stored classes will be returned.
     *
     * @param entries Classes to be stored.
     * @param code Annotation type code where the class should be stored for.
     * @return Number of stored classes or -1 for an error.
     */
    @Override
    public int storeAll(Collection<TYPE> entries, final CODE code) {

        for (TYPE clazz : entries) {
            if (this.store(clazz, code) == -1) {
                return -1;
            }
        }

        return this.storage.get(code).size();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Map<CODE, List<TYPE>> getStorage() {
        return this.storage;
    }

    @Override
    public Boolean isEmpty(CODE code) {
        return this.storage.get(code).isEmpty();
    }

    @Override
    public Boolean isEmpty() {
        return this.getStorage().keySet().stream().filter(key -> this.isEmpty(key)).findFirst().isPresent();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public SortedStorage initialiseStorage() {
        return this;
    }
}
