/*
 * Copyright 2017 Michael Rhöse.
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

import io.varietas.agrestis.imputare.error.StorageInitialisingException;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import io.varietas.agrestis.imputare.storage.SortedTypedStorage;
import java.lang.reflect.Field;

/**
 * <h2>SortedClazzStorageImpl</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 09/13/2017
 */
public class SortedClazzStorageImpl extends SortedStorageImpl<Integer, Class<?>> implements SortedTypedStorage<Integer, Class<?>> {

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

        if (Objects.equals(code, ClassMetaDataExtractionUtils.AnnotationCodes.NONE)) {
            return res;
        }

        res.addAll(this.storage.get(code).stream().filter(clazz -> clazz.equals(entry)).collect(Collectors.toList()));

        return res;
    }

    @Override
    protected void initialiseStorage() {
        try {
            Object annotationCodesInstance = ClassMetaDataExtractionUtils.AnnotationCodes.class.newInstance();
            Field[] annotationCodesFields = ClassMetaDataExtractionUtils.AnnotationCodes.class.getDeclaredFields();

            for (int index = 1; index < annotationCodesFields.length; ++index) {
                Integer code = (Integer) annotationCodesFields[index].get(annotationCodesInstance);
                this.storage.put(code, new ArrayList<>(0));
            }
        } catch (IllegalArgumentException | IllegalAccessException | InstantiationException | SecurityException ex) {
            throw new StorageInitialisingException("Initialising of sorted storage not possible.", ex);
        }
    }
}
