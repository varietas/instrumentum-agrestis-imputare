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
package io.varietas.mobile.agrestis.imputare;

import io.varietas.mobile.agrestis.imputare.annotation.Bean;
import io.varietas.mobile.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.mobile.agrestis.imputare.container.BeanDefinition;
import io.varietas.mobile.agrestis.imputare.container.information.BeanInformation;
import io.varietas.mobile.agrestis.imputare.enumeration.BeanScopes;
import io.varietas.mobile.agrestis.imputare.utils.BeanDefinitionUtils;
import io.varietas.mobile.agrestis.imputare.utils.BeanScanUtils;
import io.varietas.mobile.agrestis.imputare.utils.ReflectionUtils;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java8.util.Objects;

/**
 * <h1>AgrestisImputareContextInitialiser</h1>
 *
 * @author Michael Rhöse
 * @since Mo, Jun 6, 2016
 */
public class AgrestisImputareContextInitialiser {

    private static final Logger LOGGER = Logger.getLogger(AgrestisImputareContextInitialiser.class.getName());

    private final Package applicationPackage;

    private final Map<Integer, List<Class<?>>> preSortedStorage;

    private final List<BeanDefinition> store;

    public AgrestisImputareContextInitialiser(Object application) throws InstantiationException, IllegalAccessException {
        this.applicationPackage = application.getClass().getPackage();

        this.preSortedStorage = this.initialiseStorage();
        this.store = new ArrayList<>();
    }

    public void initializeContext() {

    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void init() throws IOException, URISyntaxException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException {

        ///< TODO: Scan all annotated classes
        List<Class<?>> locatedClazzes = ReflectionUtils.searchClasses(applicationPackage);

        ///< TODO: Sort classes for hierarchy
        this.sortClazzesForHierarchy(locatedClazzes);

        ///< TODO: Filter classes for dependencies
        Map<Integer, List<BeanInformation>> filteredBeanInformation = this.filterClazzesForDependencies();

        ///< TODO: Create configuration beans
        this.doConfigurationBeanCreation();

        ///< TODO: Create complexe beans
    }

    private void sortClazzesForHierarchy(List<Class<?>> clazzes) {
        for (Class<?> clazz : clazzes) {
            Integer state = BeanScanUtils.getPresentAnnotationCode(clazz);

            if (state.equals(BeanScanUtils.AnnotationCodes.NONE)) {
                continue;
            }

            this.preSortedStorage.get(state).add(clazz);
        }
    }

    private void doConfigurationBeanCreation() {
        this.preSortedStorage.get(BeanScanUtils.AnnotationCodes.CONFIGURATION).forEach(configClazz -> {
            List<Method> annotatedMethods = Arrays.asList(configClazz.getMethods()).stream().filter(method -> method.isAnnotationPresent(Bean.class)).collect(Collectors.toList());

            List<Method> methodsWithRequiredDependenciesAnnotatedMethod = annotatedMethods.stream().filter(method -> method.isAnnotationPresent(Autowire.class)).collect(Collectors.toList());
            annotatedMethods.removeAll(methodsWithRequiredDependenciesAnnotatedMethod);

            List<Method> methodsWithRequiredDependenciesAnnotatedParameters = annotatedMethods.stream().filter(method -> Arrays.asList(method.getParameters()).stream().findFirst().isPresent()).collect(Collectors.toList());
            annotatedMethods.removeAll(methodsWithRequiredDependenciesAnnotatedParameters);

        });
    }

    ///< TODO: Test configuration class for field dependencies. This fields could be used in the methods.
    private void initialiseBeansByMethods(Class<?> configurationClazz, List<Method> methods, int annotationPosition) throws InstantiationException, IllegalAccessException, NoSuchMethodException {

        if (configurationClazz.getConstructors().length > 1 || configurationClazz.getConstructors().length == 0) {
            throw new IllegalAccessException(String.format("Configuration class %s requires only one contructor but there were %d", configurationClazz.getSimpleName(), configurationClazz.getConstructors().length));
        }

        if (configurationClazz.getConstructor().getParameterCount() != 0) {
            throw new IllegalAccessException(String.format("Constructor of configuration class %s has no standard constructor.", configurationClazz.getSimpleName()));
        }

        Object configurationInstance = configurationClazz.newInstance();

        if (Objects.equals(annotationPosition, AnnotationPosition.NONE)) {
            methods.forEach(method -> {
                try {
                    BeanDefinitionUtils.createBeanDefinition(configurationInstance, method);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            });

            return;
        }

        if (Objects.equals(annotationPosition, AnnotationPosition.METHOD_PARAMETER)) {
            ///< TODO: Take dependencies, if not exist start bean creation with BeanInformation
        }

        if (Objects.equals(annotationPosition, AnnotationPosition.METHOD)) {

        }
    }

    private Map<Integer, List<BeanInformation>> filterClazzesForDependencies() throws IllegalArgumentException, IllegalAccessException, InstantiationException {

        final Map<Integer, List<BeanInformation>> temp = this.initialiseStorage();

        this.preSortedStorage.entrySet()
                .parallelStream().forEach(entry -> entry.getValue().stream().forEach(clazz -> {
                    try {
                        temp.get(entry.getKey()).add(this.createBeanInformation(entry.getKey(), clazz));
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
                        LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    }
                }));

        return temp;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private Map initialiseStorage() throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        Map res = new HashMap<>();

        Object annotationCodesInstance = BeanScanUtils.AnnotationCodes.class.newInstance();
        Field[] annotationCodesFields = BeanScanUtils.AnnotationCodes.class.getDeclaredFields();

        for (int index = 1; index < annotationCodesFields.length - 1; ++index) {
            this.preSortedStorage.put(annotationCodesFields[index].getInt(annotationCodesInstance), new ArrayList<>(0));
        }

        return res;
    }

    private BeanInformation createBeanInformation(Integer typeCode, Class<?> clazz) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {

        Annotation beanAnnotation = BeanScanUtils.convertAnnotationCodeToAnnotation(typeCode).get();
        String beanIdentifier = BeanScanUtils.getBeanIdentifier(clazz, beanAnnotation);
        BeanScopes scope = BeanScanUtils.getBeanScope(beanAnnotation);
        List<String> dependencies = new ArrayList<>();;

        Arrays.asList(clazz.getDeclaredFields()).stream().filter(field -> field.isAnnotationPresent(Autowire.class)).collect(Collectors.toList()).forEach(field -> {
            dependencies.add(BeanScanUtils.getBeanIdentifier(field));
        });

        return new BeanInformation(clazz, beanIdentifier, scope, dependencies);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static class AnnotationPosition {

        public static final Integer //
                NONE = 0,
                FIELD = 1,
                METHOD_PARAMETER = 2,
                METHOD = 3,
                CONSTRUCTOR = 4;
    }
}
