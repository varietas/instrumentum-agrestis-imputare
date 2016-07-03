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
package io.varietas.agrestis.imputare.utils.methods;

import io.varietas.agrestis.imputare.utils.classes.ClassMetaDataExtractionUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>MethodMetaDataExtractionUtils</h1>
 *
 * This class is a collection of useful methods to collect information from methods.
 *
 * @author Michael Rhöse
 * @since Fr, Jul 1, 2016
 */
public class MethodMetaDataExtractionUtils {

    /**
     * Collects methods annotated with an annotation from a given class.
     *
     * @param clazz From which the methods will be collected.
     * @param annotationType For which the methods will be collected.
     * @return
     */
    public static List<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationType) {
        return Arrays.asList(clazz.getDeclaredMethods()).stream().filter(method -> method.isAnnotationPresent(annotationType)).collect(Collectors.toList());
    }
    
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * <h2>MethodMetaDataExtractionUtils.AnnotationCodes</h2>
     * This class is a constants container for the codes which are available on a method. The following codes are available:
     * <ul>
     * <li>NONE = 0</li>
     * <li>REPOSITORY = 1</li>
     * <li>SERVICE = 2</li>
     * <li>CONTROLLER = 4</li>
     * <li>COMPONENT = 8</li>
     * <li>CONFIGURATION = 16</li>
     * <li>BEAN = 32</li>
     * </ul>
     *
     * @author Michael Rhöse
     * @since Fr, Jul 01, 2016
     */
    public static class AnnotationCodes extends ClassMetaDataExtractionUtils.AnnotationCodes{

        public static final Integer //
                BEAN = 32;
    }
}
