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
package io.varietas.mobile.agrestis.imputare.utils;

import io.varietas.mobile.agrestis.imputare.annotation.Autowire;
import io.varietas.mobile.agrestis.imputare.annotation.Bean;
import io.varietas.mobile.agrestis.imputare.contant.AnnotationConstants;
import io.varietas.mobile.agrestis.imputare.contant.AnnotationMethodIndices;
import io.varietas.mobile.agrestis.imputare.enumeration.BeanScopes;
import io.varietas.mobile.agrestis.imputare.enumeration.ConstructorTypes;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <h1>BeanScanUtils</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
public class BeanScanUtils {

    public static final String getBeanIdentifier(Field field) {
        Autowire autowire = (Autowire) field.getAnnotation(Autowire.class);
        String name = autowire.value()[0];

        if (!name.equals(AnnotationConstants.ANNOTATION_BEAN_NAME_DEFAULT)) {
            return name;
        }

        return field.getName();
    }

    public static final String getBeanIdentifier(Parameter parameter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Autowire autowireAnnotation = (Autowire) parameter.getAnnotation(Autowire.class);
        return autowireAnnotation.value()[0];
    }

    public static final String getBeanIdentifier(final Class<?> beanClazz, final Annotation annotation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Method[] methods = annotation.annotationType().getDeclaredMethods();
        return BeanDefinitionUtils.formatIdentifier((String) methods[AnnotationMethodIndices.NAME].invoke(annotation), beanClazz.getSimpleName());
    }

    public static final String[] getBeanIdentifiers(Constructor constructor) {
        Autowire constructorAnnotation = (Autowire) constructor.getAnnotation(Autowire.class);
        return constructorAnnotation.value();
    }

    public static final BeanScopes getBeanScope(final Annotation annotation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Method[] methods = annotation.annotationType().getDeclaredMethods();
        return ((BeanScopes) methods[AnnotationMethodIndices.SCOPE].invoke(annotation));
    }

    public static final Annotation getBeanAnnotation(final Class<?> clazz) throws IOException, URISyntaxException {
        List<Class<?>> annotationClazzes = DIUtils.searchClassesFromPackage(Bean.class.getPackage());
        Optional<Class<?>> res = annotationClazzes.stream().filter(annotationClazz -> clazz.isAnnotationPresent((Class<? extends Annotation>) annotationClazz)).findFirst();
        return clazz.getAnnotation((Class<? extends Annotation>) res.get());
    }

    public static Optional<Constructor> getSpecifiedConstructor(Class<?> clazz, ConstructorTypes constructorType) {
        List<Constructor> constructors = Arrays.asList(clazz.getConstructors());

        if (constructorType.equals(ConstructorTypes.PARAMETERISED)) {
            return Optional.empty();
        }

        switch (constructorType) {
            case STANDARD:
                ///< Standard
                return constructors.stream().filter(constructor -> constructor.getParameterCount() == 0).findFirst();
            case COPY:
                ///< Copy
                return constructors.stream().filter(constructor -> constructor.getParameterCount() == 1 && constructor.getParameters()[0].getType().equals(clazz)).findFirst();
            default:
                ///< Injected
                return constructors.stream().filter(constructor -> constructor.isAnnotationPresent(Autowire.class)).findFirst();
        }
    }
}
