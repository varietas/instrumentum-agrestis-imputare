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
package io.varietas.agrestis.imputare.utils.analysis.classes;

import io.varietas.agrestis.imputare.annotation.Component;
import io.varietas.agrestis.imputare.annotation.Configuration;
import io.varietas.agrestis.imputare.annotation.Controller;
import io.varietas.agrestis.imputare.annotation.Repository;
import io.varietas.agrestis.imputare.annotation.Service;
import io.varietas.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.agrestis.imputare.contant.AnnotationConstants;
import io.varietas.agrestis.imputare.contant.AnnotationMethodIndices;
import io.varietas.agrestis.imputare.enumeration.BeanScope;
import io.varietas.agrestis.imputare.error.InvokationException;
import io.varietas.agrestis.imputare.utils.common.NamingUtils;
import io.varietas.agrestis.imputare.error.ToManyInjectedConstructorsException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <h1>ClassMetaDataExtractionUtils</h1>
 *
 * This class is a collection of useful methods to collect information from classes.
 *
 * @author Michael Rhöse
 * @since Di, Jun 28, 2016
 */
public class ClassMetaDataExtractionUtils {

    /**
     * Returns the constructor of a given class. The constructor could be:
     * <ol>
     * <li>Annotated constructor if is {@link Autowire} is present on parameters or constructor itself.</li>
     * <li>Standard constructor if is no annotation is located.</li>
     * </ol>
     *
     * @param clazz Where the constructor is for.
     * @return
     * @throws ToManyInjectedConstructorsException
     * @throws NoSuchMethodException
     */
    public static final Constructor getConstructor(Class<?> clazz) throws ToManyInjectedConstructorsException, NoSuchMethodException {
        ///< Constructor dependencies
        List<Constructor> injectedConstructors = Arrays.asList(clazz.getConstructors()).stream().filter(constructor -> constructor.isAnnotationPresent(Autowire.class)).collect(Collectors.toList());

        if (injectedConstructors.size() > 1) {
            throw new ToManyInjectedConstructorsException(String.format("There are %d constructors injected. Only one is allowed.", injectedConstructors.size()));
        }

        if (!(injectedConstructors.isEmpty())) {
            return injectedConstructors.get(0);
        }

        List<Constructor> annotatedParamsConstructor = Arrays.asList(clazz.getConstructors())
                .stream().filter(constructor -> Arrays.asList(constructor.getParameters())
                        .stream().filter(parameter -> parameter.isAnnotationPresent(Autowire.class)).findFirst().isPresent())
                .collect(Collectors.toList());

        if (annotatedParamsConstructor.size() > 1) {
            throw new ToManyInjectedConstructorsException(String.format("There are %d constructors with injected parameters. Only one is allowed.", injectedConstructors.size()));
        }

        if (!(annotatedParamsConstructor.isEmpty())) {
            return annotatedParamsConstructor.get(0);
        }

        return clazz.getConstructor();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static Integer getPresentAnnotationCodeSum(Class<?> clazz) {
        Integer status = ClassMetaDataExtractionUtils.AnnotationCodes.NONE;

        boolean isRepository = clazz.isAnnotationPresent(Repository.class);
        boolean isService = clazz.isAnnotationPresent(Service.class);
        boolean isController = clazz.isAnnotationPresent(Controller.class);
        boolean isComponent = clazz.isAnnotationPresent(Component.class);

        boolean isConfiguration = clazz.isAnnotationPresent(Configuration.class);

        if (isRepository) {
            status += ClassMetaDataExtractionUtils.AnnotationCodes.REPOSITORY;
        }

        if (isService) {
            status += ClassMetaDataExtractionUtils.AnnotationCodes.SERVICE;
        }

        if (isController) {
            status += ClassMetaDataExtractionUtils.AnnotationCodes.CONTROLLER;
        }

        if (isComponent) {
            status += ClassMetaDataExtractionUtils.AnnotationCodes.COMPONENT;
        }

        if (isConfiguration) {
            status += ClassMetaDataExtractionUtils.AnnotationCodes.CONFIGURATION;
        }

        return status;
    }
    
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
     * All other numbers are additions of the types above. For better use the available codes are stored in the {@link AnnotationCodes}.
     *
     * @param clazz Where annotations are searched.
     * @return
     */
    public static Integer getPresentAnnotationCode(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Repository.class)) {
            return ClassMetaDataExtractionUtils.AnnotationCodes.REPOSITORY;
        }

        if (clazz.isAnnotationPresent(Service.class)) {
            return ClassMetaDataExtractionUtils.AnnotationCodes.SERVICE;
        }

        if (clazz.isAnnotationPresent(Controller.class)) {
            return ClassMetaDataExtractionUtils.AnnotationCodes.CONTROLLER;
        }

        if (clazz.isAnnotationPresent(Component.class)) {
            return ClassMetaDataExtractionUtils.AnnotationCodes.COMPONENT;
        }

        if (clazz.isAnnotationPresent(Configuration.class)) {
            return ClassMetaDataExtractionUtils.AnnotationCodes.CONFIGURATION;
        }

        return ClassMetaDataExtractionUtils.AnnotationCodes.NONE;
    }

    public static Integer getPresentAnnotationCodeForAnnotationAsString(final String anntationAsString) {

        if (anntationAsString.contains(Repository.class.getName())) {
            return ClassMetaDataExtractionUtils.AnnotationCodes.REPOSITORY;
        }
        if (anntationAsString.contains(Service.class.getName())) {
            return ClassMetaDataExtractionUtils.AnnotationCodes.SERVICE;
        }
        if (anntationAsString.contains(Controller.class.getName())) {
            return ClassMetaDataExtractionUtils.AnnotationCodes.CONTROLLER;
        }
        if (anntationAsString.contains(Component.class.getName())) {
            return ClassMetaDataExtractionUtils.AnnotationCodes.COMPONENT;
        }
        if (anntationAsString.contains(Configuration.class.getName())) {
            return ClassMetaDataExtractionUtils.AnnotationCodes.CONFIGURATION;
        }

        return ClassMetaDataExtractionUtils.AnnotationCodes.NONE;
    }

    public static Optional<Annotation> getAnnotation(final Class<?> clazz, final Integer code) {

        if (code.equals(AnnotationCodes.REPOSITORY)) {
            return Optional.of(clazz.getAnnotation(Repository.class));
        }
        if (code.equals(AnnotationCodes.SERVICE)) {
            return Optional.of(clazz.getAnnotation(Service.class));
        }
        if (code.equals(AnnotationCodes.CONTROLLER)) {
            return Optional.of(clazz.getAnnotation(Controller.class));
        }
        if (code.equals(AnnotationCodes.COMPONENT)) {
            return Optional.of(clazz.getAnnotation(Component.class));
        }
        if (code.equals(AnnotationCodes.CONFIGURATION)) {
            return Optional.of(clazz.getAnnotation(Configuration.class));
        }

        return Optional.empty();
    }

    public static Boolean isValidAnnotation(final Integer code) {
        if (code.equals(AnnotationCodes.REPOSITORY)) {
            return Boolean.TRUE;
        }
        if (code.equals(AnnotationCodes.SERVICE)) {
            return Boolean.TRUE;
        }
        if (code.equals(AnnotationCodes.CONTROLLER)) {
            return Boolean.TRUE;
        }
        if (code.equals(AnnotationCodes.COMPONENT)) {
            return Boolean.TRUE;
        }
        if (code.equals(AnnotationCodes.CONFIGURATION)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    /**
     * Searches on all declared fields of the bean class for the @{@link Autowire} annotation and returns true if is one or more fields annotated.
     *
     * @param clazz Bean class where the fields will be scanned.
     * @return
     */
    public static Boolean isFieldDependenciesExist(final Class<?> clazz) {

        return Arrays.asList(clazz.getDeclaredFields()).stream().filter(field -> field.isAnnotationPresent(Autowire.class)).findFirst().isPresent();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static BeanScope getBeanScope(final Class<?> clazz, Integer code) {

        Optional<Annotation> annotation = ClassMetaDataExtractionUtils.getAnnotation(clazz, code);

        Method method = null;
        BeanScope scope = BeanScope.SINGELTON;

        if (!annotation.isPresent()) {
            return scope;
        }

        try {
            method = annotation.get().annotationType().getMethods()[AnnotationMethodIndices.SCOPE];
            scope = (BeanScope) method.invoke(annotation.get());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new InvokationException(String.format("Error while invoke annotation method %s.%s", annotation.get().annotationType().getName(), method.getName()), ex);
        }

        return scope;
    }

    public static String getBeanIdentifier(final Class<?> clazz, final Integer code) {

        Optional<Annotation> annotation = ClassMetaDataExtractionUtils.getAnnotation(clazz, code);

        Method method = null;
        String identifier = AnnotationConstants.ANNOTATION_BEAN_NAME_DEFAULT;

        if (!annotation.isPresent()) {
            return identifier;
        }

        try {
            method = annotation.get().annotationType().getMethods()[AnnotationMethodIndices.NAME];
            identifier = (String) method.invoke(annotation.get());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new InvokationException(String.format("Error while invoke annotation method %s.%s", annotation.get().annotationType().getName(), method.getName()), ex);
        }

        return NamingUtils.formatIdentifier(identifier, clazz.getSimpleName());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * <h2>ClassMetaDataExtractionUtils.AnnotationCodes</h2>
     * This class is a constants container for the codes which are available on a class. The following codes are available:
     * <ul>
     * <li>NONE = 0</li>
     * <li>REPOSITORY = 1</li>
     * <li>SERVICE = 2</li>
     * <li>CONTROLLER = 4</li>
     * <li>COMPONENT = 8</li>
     * <li>CONFIGURATION = 16</li>
     * </ul>
     *
     * @author Michael Rhöse
     * @since Di, Jun 28, 2016
     */
    public static class AnnotationCodes {

        public static final Integer //
                NONE = 0,
                REPOSITORY = 1,
                SERVICE = 2,
                CONTROLLER = 4,
                COMPONENT = 8,
                CONFIGURATION = 16;
    }

    /**
     * This class is a constants container for the possible position of {@link Autowire} annotation. The following positions are available:
     * <ul>
     * <li>NONE = 0</li>
     * <li>FIELD = 1</li>
     * <li>METHOD_PARAMETER = 2</li>
     * <li>METHOD = 3</li>
     * <li>CONSTRUCTOR = 4</li>
     * <li>CONSTRUCTOR_PARAMETER = 5</li>
     * </ul>
     *
     * @author Michael Rhöse
     * @since D0, Jun 30, 2016
     */
    public static class AnnotationPosition {

        public static final Integer //
                NONE = 0,
                FIELD = 1,
                METHOD_PARAMETER = 2,
                METHOD = 3,
                CONSTRUCTOR = 4,
                CONSTRUCTOR_PARAMETER = 5;
    }
}
