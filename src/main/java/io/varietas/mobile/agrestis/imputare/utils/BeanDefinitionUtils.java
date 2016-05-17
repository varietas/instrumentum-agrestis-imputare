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
import io.varietas.mobile.agrestis.imputare.annotation.Component;
import io.varietas.mobile.agrestis.imputare.annotation.Configuration;
import io.varietas.mobile.agrestis.imputare.annotation.Service;
import io.varietas.mobile.agrestis.imputare.container.BeanDefinition;
import io.varietas.mobile.agrestis.imputare.container.PrototypeBeanDefinition;
import io.varietas.mobile.agrestis.imputare.container.SingletonBeanDefinition;
import io.varietas.mobile.agrestis.imputare.contant.AnnotationConstants;
import io.varietas.mobile.agrestis.imputare.contant.AnnotationMethodIndices;
import io.varietas.mobile.agrestis.imputare.enumeration.BeanScopes;
import io.varietas.mobile.agrestis.imputare.enumeration.ConstructorTypes;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <h1>BeanDefinitionUtils</h1>
 *
 * @author Michael Rhöse
 * @since Di, Mai 10, 2016
 */
public class BeanDefinitionUtils {

    private static final Logger LOGGER = Logger.getLogger(BeanDefinitionUtils.class.getName());

    /**
     * Creates the bean definitions for a list of given classes. The simple version of this method handles only classes annotated with {@link Configuration}, {@link Service} and {@link Component} and
     * standard constructor only.
     *
     * @param clazzList
     * @param annotationClass
     * @return
     */
    public static List<BeanDefinition> createBeanInformationSimple(final List<Class<?>> clazzList, Class<? extends Annotation> annotationClass) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>(clazzList.size());
        clazzList.stream().forEach((Class<?> beanClazz) -> {
            Annotation annotation = beanClazz.getAnnotation(annotationClass);
            Method[] methods = annotation.annotationType().getDeclaredMethods();
            String identifier = AnnotationConstants.ANNOTATION_BEAN_NAME_DEFAULT;
            BeanScopes scope = BeanScopes.SINGELTON;
            try {
                identifier = BeanDefinitionUtils.formatIdentifier((String) methods[AnnotationMethodIndices.NAME].invoke(annotation), beanClazz.getSimpleName());
                scope = ((BeanScopes) methods[AnnotationMethodIndices.SCOPE].invoke(annotation));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, String.format("Method '%s' of annotation '%s' could not invoked.", methods[2].getName(), annotation.getClass().getSimpleName()), ex);
            }
            try {
                Optional contructor = BeanDefinitionUtils.findSpecifiedConstructor(beanClazz, ConstructorTypes.STANDARD);
                if (!contructor.isPresent()) {
                    throw new NoSuchMethodException(String.format("No constructor located for ConstructorTypes.%s", ConstructorTypes.STANDARD.name()));
                }

                beanDefinitions.add(BeanDefinitionUtils.createBeanInformationSimple(beanClazz, annotationClass));
            } catch (IllegalArgumentException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        });
        return beanDefinitions;
    }

    public static BeanDefinition createBeanInformationSimple(final Class<?> beanClazz, Class<? extends Annotation> annotationClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Annotation annotation = beanClazz.getAnnotation(annotationClass);
        Method[] methods = annotation.annotationType().getDeclaredMethods();
        String identifier = AnnotationConstants.ANNOTATION_BEAN_NAME_DEFAULT;
        BeanScopes scope = BeanScopes.SINGELTON;
        try {
            identifier = BeanDefinitionUtils.formatIdentifier((String) methods[AnnotationMethodIndices.NAME].invoke(annotation), beanClazz.getSimpleName());
            scope = ((BeanScopes) methods[AnnotationMethodIndices.SCOPE].invoke(annotation));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.log(Level.SEVERE, String.format("Method '%s' of annotation '%s' could not invoked.", methods[2].getName(), annotation.getClass().getSimpleName()), ex);
        }

        Optional contructor = BeanDefinitionUtils.findSpecifiedConstructor(beanClazz, ConstructorTypes.STANDARD);
        if (!contructor.isPresent()) {
            throw new NoSuchMethodException(String.format("No constructor located for ConstructorTypes.%s", ConstructorTypes.STANDARD.name()));
        }

        return BeanDefinitionUtils.createBeanDefinition(beanClazz, scope, identifier, ((Optional<Constructor>) contructor).get());
    }

    /**
     * Creates a {@link BeanDefinition} for a given class. The identifier must follow the Java naming conventions (camel case, first letter lower case). If needed this method creates a new instance of
     * the class (used for prototypes only).
     *
     * @param beanClazz
     * @param scope
     * @param identifier
     * @param constructor
     * @param params
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static BeanDefinition createBeanDefinition(final Class<?> beanClazz, final BeanScopes scope, final String identifier, final Constructor constructor, Object... params)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        LOGGER.log(Level.FINER, String.format("Bean definition for bean '%s' of type '%s' (%s) created.", identifier, scope.name(), beanClazz.getName()));
        if (scope.equals(BeanScopes.SINGELTON)) {
            Object instance = constructor.newInstance(params);
            LOGGER.log(Level.FINER, String.format("Instance for bean '%s' created.", identifier));
            return new SingletonBeanDefinition(instance, identifier, scope, beanClazz, constructor);
        }
        LOGGER.log(Level.FINER, String.format("Bean definition for bean '%s' of type '%s' (%s) created.", identifier, scope.name(), beanClazz.getName()));
        return new PrototypeBeanDefinition(params, identifier, scope, beanClazz, constructor);
    }

    /**
     * Formats the identifier of a bean to the internal required format. This format follows the java naming conventions.
     *
     * @param identifier Custom or default name loads from an annotation.
     * @param defaultName Required class/method name if the identifier is the default.
     * @return
     */
    public static String formatIdentifier(String identifier, final String defaultName) {
        if (!identifier.equals(AnnotationConstants.ANNOTATION_BEAN_NAME_DEFAULT)) {
            return identifier.substring(0, 1).toLowerCase() + identifier.substring(1);
        }
        return defaultName.substring(0, 1).toLowerCase() + defaultName.substring(1);
    }

    public static Optional<Constructor> findSpecifiedConstructor(Class<?> clazz, ConstructorTypes constructorType) {
        List<Constructor> constructors = Arrays.asList(clazz.getConstructors());

        switch (constructorType) {
            case STANDARD:
                ///< Standard
                return constructors.stream().filter(constructor -> constructor.getParameterCount() == 0).findFirst();
            case COPY:
                ///< Copy
                return constructors.stream().filter(constructor -> constructor.getParameterCount() == 1 && constructor.getParameters()[1].getType().equals(clazz)).findFirst();
            default:
                ///< Injected
                return constructors.stream().filter(constructor -> constructor.isAnnotationPresent(Autowire.class)).findFirst();
        }
    }
}
