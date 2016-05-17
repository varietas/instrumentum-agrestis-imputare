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
import io.varietas.mobile.agrestis.imputare.utils.BeanDefinitionUtils;
import io.varietas.mobile.agrestis.imputare.utils.DIUtils;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    public AgrestisImputareContext initializeContext() throws IllegalArgumentException, URISyntaxException, IOException {
        AgrestisImputareContextImpl context = new AgrestisImputareContextImpl();

        this.init();

        return context;
    }

    private void init() throws IllegalArgumentException, URISyntaxException, IOException {

        if (this.applicationClazz == null) {
            throw new IllegalArgumentException(BAD_APPLICATION_ERROR);
        }

        List<Class<?>> locatedClasses = DIUtils.searchClassesFromPackage(this.applicationClazz.getPackage());

        this.sorting(locatedClasses);
        this.filtering();

        this.initialIteration();
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
    
    private void rotationIteration(){
        Queue<Class<?>> rotationQueue = new LinkedList<>();
        
        boolean isConfigurationClazzListNotEmpty = !this.configurationClazzes.isEmpty(), isServiceClazzListNotEmpty = !this.serviceClazzes.isEmpty(), isComponentClazzListNotEmpty = !this.componentClazzes.isEmpty();
        
        final Class<?> currentClazz;
        
        while(isConfigurationClazzListNotEmpty && isServiceClazzListNotEmpty && isComponentClazzListNotEmpty){

            Class<?> clazz = this.chooseAClazz(rotationQueue, isConfigurationClazzListNotEmpty, isServiceClazzListNotEmpty, isComponentClazzListNotEmpty);
            
            ///< get all dependencies
                ///< Constructor dependencies
            
                ///< Field dependencies
            
            ///< look if are dependencies available in store
                ///< Yes -> Create bean definition
            
                ///< No -> Move to Stack
        }
    }
    
    /**
     * Takes a class from any list/stack and remove from the list/stack
     * 
     * @param queue
     * @param flags
     * @return 
     */
    private Class<?> chooseAClazz(Queue<Class<?>> queue, Boolean... flags){
        
        if(flags[0]){
            Class<?> clazz = this.configurationClazzes.iterator().next();
            this.configurationClazzes.remove(clazz);
            return clazz;
        }
        if(flags[1]){
            Class<?> clazz = this.serviceClazzes.iterator().next();
            this.serviceClazzes.remove(clazz);
            return clazz;
        }
        if(flags[2]){
            Class<?> clazz = this.componentClazzes.iterator().next();
            this.componentClazzes.remove(clazz);
            return clazz;
        }
        
        return queue.remove();
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
