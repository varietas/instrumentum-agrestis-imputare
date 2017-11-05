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
package io.varietas.agrestis.imputare.storage;

import io.varietas.agrestis.imputare.analysis.containers.Information;
import io.varietas.agrestis.imputare.error.StorageInitialisingException;
import io.varietas.agrestis.imputare.storage.impl.SortedStorageImpl;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.analysis.methods.MethodMetaDataExtractionUtils;
import io.varietas.instrumentum.simul.storage.SortedStorage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>SortedInformationStorage</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/1/2016
 */
@Slf4j
public class SortedInformationStorage extends SortedStorageImpl<Integer, Information> implements SortedTypedStorage<Integer, Information> {

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Searches for a given entry all available {@link Information}. If is no {@link Information} available an empty list will returned.
     *
     * @param entry Equal entries searched for.
     * @return
     */
    @Override
    public List<Information> findByTypes(Information entry) {
        final List<Information> res = new ArrayList<>();

        this.storage.entrySet().stream().forEach(storageEntry -> {
            res.addAll(storageEntry.getValue().stream().filter(storageEntryEntry -> Objects.equals(storageEntryEntry.getType(), entry.getType())).collect(Collectors.toList()));
        });

        return res;
    }

    /**
     * Searches for a given entry and {@link ClassMetaDataExtractionUtils.AnnotationCodes} all available {@link Information}. If is no {@link Information} available an empty list will returned.
     *
     * @param entry Equal entries searched for.
     * @param code Annotation code.
     * @return
     */
    @Override
    public List<Information> findByTypesAndAnnotationCode(Information entry, Integer code) {
        final List<Information> res = new ArrayList<>();

        res.addAll(this.storage.get(code).stream().filter(storageEntry -> Objects.equals(storageEntry.getType(), entry.getType())).collect(Collectors.toList()));

        return res;
    }

    public Optional<Information> findByIdentifier(final String identifier) {

        for (Map.Entry<Integer, List<Information>> set : this.storage.entrySet()) {
            Optional<Information> res = set.getValue().stream()
                .filter(entry -> Objects.equals(entry.getIdentifier(), identifier))
                .findFirst();

            if (res.isPresent()) {
                return res;
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Information> next() {

        final Optional<List<Information>> nextList = this.storage.values().stream()
            .filter(list -> !list.isEmpty())
            .findFirst();

        if (!nextList.isPresent()) {
            return Optional.empty();
        }

        Information res = nextList.get().get(nextList.get().size() - 1);
        nextList.get().remove(res);

        return Optional.of(res);
    }

    @Override
    public Optional<Information> next(final Integer code) {

        final List<Information> nextList = this.storage.get(code);

        if (nextList.isEmpty()) {
            return Optional.empty();
        }

        Information res = nextList.get(nextList.size() - 1);
        nextList.remove(res);

        return Optional.of(res);
    }

    /**
     * Stores a class in the storage. The following list shows the information the return value represents:
     * <ul>
     * <li><b>>= 0:</b> Number of currently stored entries.</li>
     * <li><b>-1:</b> New entry not stored.</li>
     * <li><b>-2:</b> An bean information with this getIdentifier already exists.</li>
     * </ul>
     *
     * @param entry Bean information to be stored.
     * @param code Annotation getType code where the class should be stored for.
     * @return Number of stored classes or -1 for an error.
     */
    @Override
    public int store(final Information entry, final Integer code) {
        if (Objects.equals(code, MethodMetaDataExtractionUtils.AnnotationCodes.NONE)) {
            return -1;
        }

        if (this.isContainsBeanWithIdentifier(entry, code)) {
            return -2;
        }

        if (!this.storage.get(code).add(entry)) {
            return -1;
        }

        return this.storage.get(code).size();
    }

    /**
     * Stores all classes from a given collection in the storage. The following list shows the information the return value represents:
     * <ul>
     * <li><b>>= 0:</b> Number of currently stored entries.</li>
     * <li><b>-1:</b> New entry not stored.</li>
     * <li><b>-2:</b> An bean information with this getIdentifier already exists.</li>
     * </ul>
     *
     * @param entries bean information to be stored.
     * @param code Annotation getType code where the class should be stored for.
     * @return Number of stored classes or -1 for an error.
     */
    @Override
    public int storeAll(final Collection<Information> entries, Integer code) {
        for (Information entry : entries) {

            int status = this.store(entry, code);

            if (status < 0) {
                return status;
            }
        }

        return this.storage.get(code).size();
    }

    @Override
    public Boolean isEmpty(Integer code) {
        if (!this.storage.keySet().contains(code)) {
            return true;
        }
        return this.storage.get(code).isEmpty();
    }

    @Override
    public Boolean isEmpty() {
        return this.storage.keySet().stream()
            .filter(code -> this.isEmpty(code))
            .findFirst()
            .isPresent();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Map<Integer, List<Information>> getStorage() {
        return this.storage;
    }

    public Boolean contains(final Information beanInformation) {
        Integer code = ClassMetaDataExtractionUtils.getPresentAnnotationCode(beanInformation.getType());
        return this.isContainsBeanWithIdentifier(beanInformation, code);
    }

    public Boolean contains(final String identifier) {
        return this.storage.entrySet().stream()
            .anyMatch((set) -> (set.getValue().stream().filter(entry -> Objects.equals(entry.getIdentifier(), identifier)).findFirst().isPresent()));
    }

    public Boolean isContainsBeanWithIdentifier(final Information beanInformation, Integer code) {

        if (this.isEmpty(code)) {
            return Boolean.FALSE;
        }

        if (this.storage.get(code).stream().filter(entry -> Objects.equals(entry.getIdentifier(), beanInformation.getIdentifier())).findFirst().isPresent()) {
            LOGGER.info("Bean information for identifier {} already exists.", beanInformation.getIdentifier());
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public final SortedStorage initialiseStorage() {

        try {
            Object annotationCodesInstance = MethodMetaDataExtractionUtils.AnnotationCodes.class.newInstance();
            Field[] annotationCodesFields = MethodMetaDataExtractionUtils.AnnotationCodes.class.getFields();

            for (int index = 0; index < annotationCodesFields.length; ++index) {
                if (Objects.equals(annotationCodesFields[index].getName(), ClassMetaDataExtractionUtils.AnnotationCodes.NONE)) {
                    continue;
                }
                Integer code = (Integer) annotationCodesFields[index].get(annotationCodesInstance);
                this.storage.put(code, new ArrayList<>(0));
            }
        } catch (IllegalArgumentException | IllegalAccessException | InstantiationException | SecurityException ex) {
            throw new StorageInitialisingException("Initialising of sorted storage not possible.", ex);
        }

        return this;
    }
}
