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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java8.util.Optional;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * <h2>SortedStorageImpl</h2>
 *
 * This class represents a container to store all classes sorted by {@link ClassMetaDataExtractionUtils.AnnotationCodes}. Additionally there are a number of useful methods.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 6/30/2016
 */
public class SortedStorageImpl implements SortedStorage<Integer, Class<?>> {

    private final Map<Integer, List<Class<?>>> clazzes;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public SortedStorageImpl() throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        this.clazzes = new HashMap<>();

        this.initialiseStorage();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Optional<Class<?>> next() {

        final Optional<List<Class<?>>> nextList = StreamSupport.stream(this.clazzes.values()).filter(list -> !list.isEmpty()).findFirst();

        if (!nextList.isPresent()) {
            return Optional.empty();
        }

        Class<?> res = nextList.get().get(nextList.get().size() - 1);
        nextList.get().remove(res);

        return Optional.of(res);
    }

    @Override
    public Optional<Class<?>> next(final Integer code) {

        final List<Class<?>> nextList = this.clazzes.get(code);

        if (nextList.isEmpty()) {
            return Optional.empty();
        }

        Class<?> res = nextList.get(nextList.size() - 1);
        nextList.remove(res);

        return Optional.of(res);
    }

    /**
     * Searches for a given class all available classes. If is no class available an empty list will returned. Internally the {@link ClassMetaDataExtractionUtils.AnnotationCodes} will searched.
     *
     * @param entry Equal classes searched for.
     * @return
     */
    @Override
    public List<Class<?>> findByTypes(final Class<?> entry) {
        List<Class<?>> res = new ArrayList<>();

        this.findByTypesAndAnnotationCode(entry, ClassMetaDataExtractionUtils.getPresentAnnotationCode(entry));

        return res;
    }

    /**
     * Searches for a given class and {@link ClassMetaDataExtractionUtils.AnnotationCodes} all available classes. If is no class available an empty list will returned.
     *
     * @param entry Equal classes searched for.
     * @param code Annotation code.
     * @return
     */
    @Override
    public List<Class<?>> findByTypesAndAnnotationCode(final Class<?> entry, final Integer code) {

        List<Class<?>> res = new ArrayList<>();

        if (code == ClassMetaDataExtractionUtils.AnnotationCodes.NONE) {
            return res;
        }

        res.addAll(StreamSupport.stream(this.clazzes.get(code)).filter(clazz -> clazz.equals(entry)).collect(Collectors.toList()));

        return res;
    }

    /**
     * Stores a class in the storage. Returns -1 if the class is not stored otherwise the current number of stored classes will be returned.
     *
     * @param entry Class to be stored.
     * @param code Annotation type code where the class should be stored for.
     * @return Number of stored classes or -1 for an error.
     */
    @Override
    public int store(final Class<?> entry, final Integer code) {
        if (code == ClassMetaDataExtractionUtils.AnnotationCodes.NONE) {
            return -1;
        }

        if (!this.clazzes.get(code).add(entry)) {
            return -1;
        }

        return this.clazzes.get(code).size();
    }

    /**
     * Stores all classes from a given collection in the storage. Returns -1 if the classes are not stored otherwise the current number of stored classes will be returned.
     *
     * @param entries Classes to be stored.
     * @param code Annotation type code where the class should be stored for.
     * @return Number of stored classes or -1 for an error.
     */
    @Override
    public int storeAll(Collection<Class<?>> entries, final Integer code) {

        for (Class<?> clazz : entries) {
            if (this.store(clazz, code) == -1) {
                return -1;
            }
        }

        return this.clazzes.get(code).size();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Map<Integer, List<Class<?>>> getStorage() {
        return this.clazzes;
    }

    @Override
    public Boolean isEmpty(Integer code) {
        return this.clazzes.get(code).isEmpty();
    }

    @Override
    public Boolean isEmpty() {
        return StreamSupport.stream(this.getStorage().keySet()).filter(key -> this.isEmpty(key)).findFirst().isPresent();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void initialiseStorage() throws IllegalArgumentException, IllegalAccessException, InstantiationException {

        Object annotationCodesInstance = ClassMetaDataExtractionUtils.AnnotationCodes.class.newInstance();
        Field[] annotationCodesFields = ClassMetaDataExtractionUtils.AnnotationCodes.class.getDeclaredFields();

        for (int index = 1; index < annotationCodesFields.length; ++index) {
            Integer code = (Integer) annotationCodesFields[index].get(annotationCodesInstance);
            this.clazzes.put(code, new ArrayList<>(0));
        }
    }
}
