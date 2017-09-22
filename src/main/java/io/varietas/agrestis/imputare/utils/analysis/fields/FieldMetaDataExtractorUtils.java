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
package io.varietas.agrestis.imputare.utils.analysis.fields;

import io.varietas.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.agrestis.imputare.annotation.injections.Value;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h2>FieldMetaDataExtractorUtils</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/6/2016
 */
public class FieldMetaDataExtractorUtils {

    public static Boolean isDependenciesExist(final Class<?> clazz) {

        return Objects.equals(FieldMetaDataExtractorUtils.getAnnotationPosition(clazz), ClassMetaDataExtractionUtils.AnnotationPosition.FIELD);
    }

    /**
     * Searches for {@link Autowire} annotation on a given class. Codes could be:
     *
     * <ul>
     * <li>NONE = 0</li>
     * <li>FIELD = 1</li>
     * </ul>
     *
     * A full list of available codes in general could be found on the {@link ClassMetaDataExtractionUtils.AnnotationPosition}.
     *
     * @param clazz Class where the annotation will searched on.
     * @return
     */
    public static Integer getAnnotationPosition(final Class<?> clazz) {

        if (Stream.of(clazz.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Autowire.class) || field.isAnnotationPresent(Value.class))
            .findFirst().isPresent()) {
            return ClassMetaDataExtractionUtils.AnnotationPosition.FIELD;
        }

        return ClassMetaDataExtractionUtils.AnnotationPosition.NONE;
    }
}
