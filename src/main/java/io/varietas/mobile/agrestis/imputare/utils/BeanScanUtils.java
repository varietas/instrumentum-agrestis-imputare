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

import io.varietas.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.agrestis.imputare.annotation.Bean;
import io.varietas.agrestis.imputare.annotation.Component;
import io.varietas.agrestis.imputare.annotation.Configuration;
import io.varietas.agrestis.imputare.annotation.Controller;
import io.varietas.agrestis.imputare.annotation.Repository;
import io.varietas.agrestis.imputare.annotation.Service;
import io.varietas.agrestis.imputare.enumeration.BeanScope;
import io.varietas.agrestis.imputare.contant.AnnotationConstants;
import io.varietas.agrestis.imputare.contant.AnnotationMethodIndices;
import io.varietas.agrestis.imputare.enumeration.ConstructorTypes;
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

    public static final BeanScope getBeanScope(final Annotation annotation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Method[] methods = annotation.annotationType().getDeclaredMethods();
        return ((BeanScope) methods[AnnotationMethodIndices.SCOPE].invoke(annotation));
    }

    public static final Annotation getBeanAnnotation(final Class<?> clazz) throws IOException, URISyntaxException, ClassNotFoundException {
        List<Class<?>> annotationClazzes = ReflectionUtils.searchClasses(Bean.class.getPackage());
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

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Scans the given class for annotations and return a status code. The code will represent the number of annotations and the type.
     *
     * <ul>
     * <li>[0] No agrestis imputare annotation present</li>
     * <li>[1] Repository</li>
     * <li>[2] Service</li>
     * <li>[4] Controller</li>
     * <li>[8] Component</li>
     * <li>[16] Configuration</li>
     * </ul>
     *
     * All other numbers are additions of the types above.
     *
     * @param clazz
     * @return
     */
    public static Integer getPresentAnnotationCode(Class<?> clazz) {
        Integer status = BeanScanUtils.AnnotationCodes.NONE;

        boolean isRepository = clazz.isAnnotationPresent(Repository.class);
        boolean isService = clazz.isAnnotationPresent(Service.class);
        boolean isController = clazz.isAnnotationPresent(Controller.class);
        boolean isComponent = clazz.isAnnotationPresent(Component.class);

        boolean isConfiguration = clazz.isAnnotationPresent(Configuration.class);

        if (isRepository) {
            status += BeanScanUtils.AnnotationCodes.REPOSITORY;
        }

        if (isService) {
            status += BeanScanUtils.AnnotationCodes.SERVICE;
        }

        if (isController) {
            status += BeanScanUtils.AnnotationCodes.CONTROLLER;
        }

        if (isComponent) {
            status += BeanScanUtils.AnnotationCodes.COMPONENT;
        }

        if (isConfiguration) {
            status += BeanScanUtils.AnnotationCodes.CONFIGURATION;
        }

        return status;
    }

    public static Optional<Annotation> convertAnnotationCodeToAnnotation(Integer code) throws InstantiationException, IllegalAccessException {

        ///< TODO: Refector this methode to an dynamic solution, think about an annotation order annotation
        if (code.equals(BeanScanUtils.AnnotationCodes.REPOSITORY)) {
            return Optional.of(Repository.class.newInstance());
        }

        if (code.equals(BeanScanUtils.AnnotationCodes.SERVICE)) {
            return Optional.of(Repository.class.newInstance());
        }

        if (code.equals(BeanScanUtils.AnnotationCodes.CONTROLLER)) {
            return Optional.of(Repository.class.newInstance());
        }

        if (code.equals(BeanScanUtils.AnnotationCodes.COMPONENT)) {
            return Optional.of(Repository.class.newInstance());
        }

        if (code.equals(BeanScanUtils.AnnotationCodes.CONFIGURATION)) {
            return Optional.of(Repository.class.newInstance());
        }

        return Optional.empty();
    }

    /**
     * Compares annotations of a class with the allowed strategy. If more than one annotation of agrestis imputare is present the method will return false.
     *
     * @param clazz
     * @return
     */
    public static Boolean isIllegalAnnotated(Class<?> clazz) {
        Integer status = BeanScanUtils.getPresentAnnotationCode(clazz);
        return status.equals(BeanScanUtils.AnnotationCodes.REPOSITORY) || status.equals(BeanScanUtils.AnnotationCodes.SERVICE) || status.equals(BeanScanUtils.AnnotationCodes.CONTROLLER) || status.equals(BeanScanUtils.AnnotationCodes.COMPONENT) || status.equals(BeanScanUtils.AnnotationCodes.CONFIGURATION);
    }

    public static boolean isNotAnnotated(Class<?> clazz) {
        return BeanScanUtils.getPresentAnnotationCode(clazz).equals(BeanScanUtils.AnnotationCodes.NONE);
    }

    public static class AnnotationCodes {

        public static final Integer //
                NONE = 0,
                REPOSITORY = 1,
                SERVICE = 2,
                CONTROLLER = 4,
                COMPONENT = 8,
                CONFIGURATION = 16;
    }
}
