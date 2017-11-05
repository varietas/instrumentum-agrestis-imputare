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
package io.varietas.agrestis.imputare.storage;

import java.io.Serializable;
import java.util.List;

/**
 * <h2>SortedTypedStorage</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 09/13/2017
 * @param <CODE> Generic code type.
 * @param <TYPE> Generic type which is stored.
 */
public interface SortedTypedStorage<CODE extends Serializable, TYPE> {

    /**
     * Searches for a given entry all available entries. If is no class available an empty list will returned.
     *
     * @param entry Equal entries searched for.
     * @return
     */
    public List<TYPE> findByTypes(final TYPE entry);

    /**
     * Searches for a given entry and {@link ClassMetaDataExtractionUtils.AnnotationCodes} all available entries. If is no class available an empty list will returned.
     *
     * @param entry Equal entries searched for.
     * @param code Annotation code.
     * @return
     */
    public List<TYPE> findByTypesAndAnnotationCode(final TYPE entry, final CODE code);
}
