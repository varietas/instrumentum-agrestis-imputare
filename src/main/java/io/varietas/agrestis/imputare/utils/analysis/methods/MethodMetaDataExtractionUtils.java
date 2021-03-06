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
package io.varietas.agrestis.imputare.utils.analysis.methods;

import io.varietas.agrestis.imputare.annotation.Bean;
import io.varietas.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.agrestis.imputare.annotation.injections.Value;
import io.varietas.agrestis.imputare.annotation.resources.Resource;
import io.varietas.agrestis.imputare.enumerations.BeanScopes;
import io.varietas.agrestis.imputare.enumerations.ResourceType;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.common.NamingUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h2>MethodMetaDataExtractionUtils</h2>
 *
 * This class is a collection of useful methods to collect information from methods.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 7/1/2016
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
        return Stream.of(clazz.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(annotationType))
            .collect(Collectors.toList());
    }

    public static BeanScopes getBeanScope(final Method method) {
        Bean annotation = method.getAnnotation(Bean.class);
        return annotation.scope();
    }

    public static String getBeanIdentifier(final Method method) {
        Bean annotation = method.getAnnotation(Bean.class);
        return NamingUtils.formatIdentifier(annotation.name(), method.getName());
    }

    public static String getResourceIdentifier(final Method method) {
        Resource annotation = method.getAnnotation(Resource.class);
        return NamingUtils.formatIdentifier(annotation.name(), method.getName());
    }

    public static ResourceType getResourceType(final Method method) {
        Resource annotation = method.getAnnotation(Resource.class);
        return annotation.type();
    }

    /**
     * Searches for {@link Autowire} annotation on a given method. If is the annotation on the method or on any parameter true will returned.
     *
     * @param method Method where the annotation will searched on.
     * @return
     */
    public static Boolean isDependenciesExist(final Executable method) {

        if (MethodMetaDataExtractionUtils.getAnnotationPosition(method) > ClassMetaDataExtractionUtils.AnnotationPosition.NONE) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    /**
     * Searches for {@link Autowire} annotation on a given method. Codes could be:
     *
     * <ul>
     * <li>NONE = 0</li>
     * <li>METHOD_PARAMETER = 2</li>
     * <li>METHOD = 3</li>
     * </ul>
     *
     * A full list of available codes in general could be found on the {@link ClassMetaDataExtractionUtils.AnnotationPosition}.
     *
     * @param method Method where the annotation will searched on.
     * @return
     */
    public static Integer getAnnotationPosition(Executable method) {
        if (method.isAnnotationPresent(Autowire.class) || method.isAnnotationPresent(Value.class)) {
            return ClassMetaDataExtractionUtils.AnnotationPosition.METHOD;
        }

        if (Stream.of(method.getParameters()).filter(param -> param.isAnnotationPresent(Autowire.class) || param.isAnnotationPresent(Value.class)).findFirst().isPresent()) {
            return ClassMetaDataExtractionUtils.AnnotationPosition.METHOD_PARAMETER;
        }

        return ClassMetaDataExtractionUtils.AnnotationPosition.NONE;
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
    public static class AnnotationCodes extends ClassMetaDataExtractionUtils.AnnotationCodes {

        public static final Integer //
            BEAN = 32;
    }
}
