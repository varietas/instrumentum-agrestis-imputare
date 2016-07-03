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
package io.varietas.agrestis.imputare.storage;

import io.varietas.agrestis.imputare.utils.classes.ClassMetaDataExtractionUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <h1>SortedStorageImpl</h1>
 *
 * This class represents a container to store all classes sorted by {@link ClassMetaDataExtractionUtils.AnnotationCodes}. Additionally there are a number of useful methods.
 *
 * @author Michael Rhöse
 * @since Do, Jun 30, 2016
 */
public class SortedStorageImpl implements SortedStorage<Integer, Class<?>> {

    private final Map<Integer, List<Class<?>>> clazzes;
    private final Map<Integer, Boolean> emptyFlags;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public SortedStorageImpl() throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        this.clazzes = new HashMap<>();
        this.emptyFlags = new HashMap<>();

        this.initialiseStorage();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Optional<Class<?>> next() {

        Optional<Map.Entry<Integer, Boolean>> nextListIndex = this.emptyFlags.entrySet().stream().filter(entry -> !entry.getValue()).findFirst();

        if (!nextListIndex.isPresent()) {
            return Optional.empty();
        }

        if (nextListIndex.get().getValue()) {
            return Optional.empty();
        }

        final List<Class<?>> nextList = this.clazzes.get(nextListIndex.get().getKey());
        Class<?> res = nextList.get(nextList.size() - 1);
        nextList.remove(res);

        nextListIndex.get().setValue(nextList.isEmpty());

        return Optional.of(res);
    }

    @Override
    public Optional<Class<?>> next(final Integer code) {

        Boolean listFlag = this.emptyFlags.get(code);

        if (listFlag) {
            return Optional.empty();
        }

        final List<Class<?>> nextList = this.clazzes.get(code);
        Class<?> res = nextList.get(nextList.size() - 1);
        nextList.remove(res);

        this.emptyFlags.entrySet().stream().filter(entry -> Objects.equals(entry.getKey(), code)).findFirst().get().setValue(nextList.isEmpty());

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

        res.addAll(this.clazzes.get(code).stream().filter(clazz -> clazz.equals(entry)).collect(Collectors.toList()));

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

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void initialiseStorage() throws IllegalArgumentException, IllegalAccessException, InstantiationException {

        Object annotationCodesInstance = ClassMetaDataExtractionUtils.AnnotationCodes.class.newInstance();
        Field[] annotationCodesFields = ClassMetaDataExtractionUtils.AnnotationCodes.class.getDeclaredFields();

        for (int index = 1; index < annotationCodesFields.length - 1; ++index) {
            this.clazzes.put(annotationCodesFields[index].getInt(annotationCodesInstance), new ArrayList<>(0));
            this.emptyFlags.put(annotationCodesFields[index].getInt(annotationCodesInstance), true);
        }
    }
}