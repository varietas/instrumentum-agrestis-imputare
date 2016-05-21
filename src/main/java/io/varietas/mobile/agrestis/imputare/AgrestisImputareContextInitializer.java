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

import io.varietas.mobile.agrestis.imputare.annotation.Autowire;
import io.varietas.mobile.agrestis.imputare.annotation.Component;
import io.varietas.mobile.agrestis.imputare.annotation.Configuration;
import io.varietas.mobile.agrestis.imputare.annotation.Service;
import io.varietas.mobile.agrestis.imputare.container.BeanDefinition;
import io.varietas.mobile.agrestis.imputare.error.RecursiveInjectionException;
import io.varietas.mobile.agrestis.imputare.error.ToManyInjectedConstructorsException;
import io.varietas.mobile.agrestis.imputare.utils.BeanDefinitionUtils;
import io.varietas.mobile.agrestis.imputare.utils.BeanScanUtils;
import io.varietas.mobile.agrestis.imputare.utils.BeaninstantiationUtils;
import io.varietas.mobile.agrestis.imputare.utils.DIUtils;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <h1>AgrestisImputareContextInitializer</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
public class AgrestisImputareContextInitializer {

    private static final Logger LOGGER = Logger.getLogger(AgrestisImputareContextInitializer.class.getName());

    ///< List of classes which contains fields of other beans
    private final List<Class<?>> nestedRequiredDependenciesClazzes;

    ///< List of classes which requires beans as parameters 
    private final List<Class<?>> constructorWithAutowireAnnotationClazzes;

    ///< List of classes containing beans
    private final List<Class<?>> configurationClazzes;

    ///< List of service classes
    private final List<Class<?>> serviceClazzes;

    ///< List of component classes
    private final List<Class<?>> componentClazzes;

    ///< List of bean classes
    private final List<Class<?>> beanClazzes;

    private final Map<Class<? extends Annotation>, List<Class<?>>> filteredClazzes;

    private final List<BeanDefinition> store;

    private final Class<?> applicationClazz;

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Be sure the package '%s' exists?";
    private static final String BAD_APPLICATION_ERROR = "Unable to scan package. Be sure that a package/application is configured";

    public AgrestisImputareContextInitializer(final Object application) {

        this(application.getClass());
    }

    public AgrestisImputareContextInitializer(final Class<?> applicationClazz) {

        this.nestedRequiredDependenciesClazzes = new ArrayList<>(0);
        this.constructorWithAutowireAnnotationClazzes = new ArrayList<>(0);

        this.configurationClazzes = new ArrayList<>(0);
        this.serviceClazzes = new ArrayList<>(0);
        this.componentClazzes = new ArrayList<>(0);
        this.beanClazzes = new ArrayList<>(0);

        this.filteredClazzes = new HashMap<>();

        this.store = new ArrayList<>();

        this.applicationClazz = applicationClazz;
    }

    public AgrestisImputareContext initializeContext() throws IllegalArgumentException, URISyntaxException, IOException, ToManyInjectedConstructorsException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, RecursiveInjectionException {
        AgrestisImputareContextImpl context = new AgrestisImputareContextImpl();

        this.init();

        return context;
    }

    private void init() throws IllegalArgumentException, URISyntaxException, IOException, ToManyInjectedConstructorsException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, RecursiveInjectionException {

        if (this.applicationClazz == null) {
            throw new IllegalArgumentException(BAD_APPLICATION_ERROR);
        }

        List<Class<?>> locatedClasses = DIUtils.searchClassesFromPackage(this.applicationClazz.getPackage());

        this.sorting(locatedClasses);
        this.filtering();

        this.initialIteration();

        this.rotationIteration();
    }

    /**
     * Sorts all located classes into the classes for the following initialisation steps.
     *
     * @param locatedClasses
     */
    private void sorting(final List<Class<?>> locatedClasses) {

        ///< Sortiing classes for type
        this.configurationClazzes.addAll(locatedClasses.stream().filter(clazz -> clazz.isAnnotationPresent(Configuration.class)).collect(Collectors.toList()));
        this.serviceClazzes.addAll(locatedClasses.stream().filter(clazz -> clazz.isAnnotationPresent(Service.class)).collect(Collectors.toList()));
        this.componentClazzes.addAll(locatedClasses.stream().filter(clazz -> clazz.isAnnotationPresent(Component.class)).collect(Collectors.toList()));
    }

    /**
     * Filters the sorted classes into classes with and without dependencies. Bean definitions for classes without dependencies will created. Classes with dependencies will stored and prepared for the
     * following steps.
     */
    private void filtering() {

        this.filteredClazzes.put(Configuration.class, this.filterSimpleBeansForSecondLevelIteration(this.configurationClazzes));
        this.filteredClazzes.put(Service.class, this.filterSimpleBeansForSecondLevelIteration(this.serviceClazzes));
        this.filteredClazzes.put(Component.class, this.filterSimpleBeansForSecondLevelIteration(this.componentClazzes));
    }

    /**
     * First step of initialisation. All classes without dependencies are used to create bean definitions. This t´´definitions will used for the initialisation of beans with dependencies.
     */
    private void initialIteration() {

        this.filteredClazzes.forEach((annotation, clazzes) -> {
            clazzes.forEach(clazz -> {
                try {
                    this.store.add(BeanDefinitionUtils.createBeanInformationSimple(clazz, annotation));
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                }
            });
        });
    }

    ///< TODO: field injection should run after bean creation.
    ///< TODO: handle recursive injection (means bean requires itself)
    private void rotationIteration() throws ToManyInjectedConstructorsException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, URISyntaxException, RecursiveInjectionException {
        Queue<Class<?>> rotationQueue = new LinkedList<>();

        boolean isConfigurationClazzListNotEmpty = !this.configurationClazzes.isEmpty(), isServiceClazzListNotEmpty = !this.serviceClazzes.isEmpty(), isComponentClazzListNotEmpty = !this.componentClazzes.isEmpty();

        while (isConfigurationClazzListNotEmpty && isServiceClazzListNotEmpty && isComponentClazzListNotEmpty) {
            final Class<?> clazz = this.chooseAClazz(rotationQueue, isConfigurationClazzListNotEmpty, isServiceClazzListNotEmpty, isComponentClazzListNotEmpty);
            final Constructor beanConstructor = this.getConstructor(clazz);
            final Optional<Object> beanInstance = this.getBeanInstance(beanConstructor);
            final Annotation annotation = BeanScanUtils.getBeanAnnotation(clazz);
            final String beanIdentifier = BeanScanUtils.getBeanIdentifier(clazz, annotation, annotation.annotationType().getDeclaredMethods());

            if (!beanInstance.isPresent()) {
                rotationQueue.add(clazz);
                continue;
            }

            ///< Field dependencies
            List<Field> fields = Arrays.asList(clazz.getFields()).stream().filter(field -> field.isAnnotationPresent(Autowire.class)).collect(Collectors.toList());

            this.fieldDependencyInjection(rotationQueue, fields, beanInstance, clazz, beanIdentifier);
        }
    }

    private void fieldDependencyInjection(final Queue<Class<?>> rotationQueue, List<Field> fields, final Optional<Object> beanInstance, final Class<?> clazz, final String beanIdentifier) throws RecursiveInjectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        for (Field field : fields) {
            String fieldBeanIdentifier = BeanScanUtils.getBeanIdentifier(field);
            ///< TODO: Get current bean identifier to test for recursive init
            if (beanIdentifier.equals(fieldBeanIdentifier)) {
                throw new RecursiveInjectionException(String.format("Bean '%s' contains dependencies to itself. This is not allowed! Field %s %s ", beanIdentifier, field.getType().getSimpleName(), field.getName()));
            }

            if (!field.isAnnotationPresent(Autowire.class)) {
                continue;
            }

            field.setAccessible(true);
            if (!Objects.isNull(field.get(beanInstance.get()))) {
                field.setAccessible(false);
                continue;
            }

            Optional beanForField = BeaninstantiationUtils.getBeanInstance(this.store, field);

            if (!beanForField.isPresent()) {
                field.setAccessible(false);
                rotationQueue.add(clazz);
                return;
            }

            field.set(beanInstance.get(), ((Optional<Object>) beanForField).get());
        }
    }

    /**
     * Takes a class from any list/stack and remove from the list/stack
     *
     * @param queue
     * @param flags
     * @return
     */
    private Class<?> chooseAClazz(Queue<Class<?>> queue, Boolean... flags) {

        if (flags[0]) {
            Class<?> clazz = this.configurationClazzes.iterator().next();
            this.configurationClazzes.remove(clazz);
            return clazz;
        }

        if (flags[1]) {
            Class<?> clazz = this.serviceClazzes.iterator().next();
            this.serviceClazzes.remove(clazz);
            return clazz;
        }

        if (flags[2]) {
            Class<?> clazz = this.componentClazzes.iterator().next();
            this.componentClazzes.remove(clazz);
            return clazz;
        }

        return queue.remove();
    }

    private Constructor getConstructor(Class<?> clazz) throws ToManyInjectedConstructorsException, NoSuchMethodException {
        ///< Constructor dependencies
        List<Constructor> injectedConstructors = Arrays.asList(clazz.getConstructors()).stream().filter(constructor -> constructor.isAnnotationPresent(Autowire.class)).collect(Collectors.toList());

        if (injectedConstructors.size() > 1) {
            throw new ToManyInjectedConstructorsException(String.format("There are %d constructors injected. Only one is allowed.", injectedConstructors.size()));
        }

        if (!(injectedConstructors.isEmpty())) {
            return injectedConstructors.get(0);
        }

        List<Constructor> annotatedParamsConstructor = Arrays.asList(clazz.getConstructors()).stream().filter(constructor -> Arrays.asList(constructor.getParameterAnnotations()).stream().filter(annotation -> annotation.getClass().equals(Autowire.class)).findFirst().isPresent()).collect(Collectors.toList());

        if (annotatedParamsConstructor.size() > 1) {
            throw new ToManyInjectedConstructorsException(String.format("There are %d constructors with injected parameters. Only one is allowed.", injectedConstructors.size()));
        }

        if (!(annotatedParamsConstructor.isEmpty())) {
            return annotatedParamsConstructor.get(0);
        }

        return clazz.getConstructor();
    }

    private Optional<Object> getBeanInstance(Constructor beanConstructor) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        if (beanConstructor.getParameterCount() > 0) {
            return Optional.of(beanConstructor.newInstance());
        }
        ///< If the constructor itself is annotated the names of the parameters will be the identifiers for the injection.
        Boolean isNotCustomIdentifiers = (beanConstructor.getAnnotations().length > 0);

        Object[] params = new Object[beanConstructor.getParameterCount()];

        for (short index = 0; index < beanConstructor.getParameterCount(); ++index) {

            Parameter currentParameter = beanConstructor.getParameters()[index];
            String beanIdentifier = BeanScanUtils.getBeanIdentifier(currentParameter, !isNotCustomIdentifiers);

            Optional<BeanDefinition> beanDefinition = this.store.stream().filter(beanDef -> beanDef.getIdentifier().equals(beanIdentifier)).findFirst();

            if (!beanDefinition.isPresent()) {
                return Optional.empty();
            }

            params[index] = beanDefinition.get().getInstance();
        }

        return Optional.of(beanConstructor.newInstance(params));
    }

    private List<Class<?>> filterSimpleBeansForSecondLevelIteration(List<Class<?>> clazzList) {

        this.constructorWithAutowireAnnotationClazzes.addAll(clazzList.stream().filter(clazz -> Arrays.asList(clazz.getConstructors()).stream().filter(constructor -> constructor.isAnnotationPresent(Autowire.class)).findFirst().isPresent()).collect(Collectors.toList()));

        List<Class<?>> constructorFilteredClazzes = new ArrayList<>(clazzList);
        constructorFilteredClazzes.removeAll(this.constructorWithAutowireAnnotationClazzes);

        this.nestedRequiredDependenciesClazzes.addAll(constructorFilteredClazzes.stream().filter(clazz -> Arrays.asList(clazz.getFields()).stream().filter(field -> field.isAnnotationPresent(Autowire.class)).findFirst().isPresent()).collect(Collectors.toList()));

        List<Class<?>> constructorAndFieldFilteredClazzes = new ArrayList<>(constructorFilteredClazzes);
        constructorAndFieldFilteredClazzes.removeAll(this.nestedRequiredDependenciesClazzes);

        return constructorAndFieldFilteredClazzes;
    }
}
