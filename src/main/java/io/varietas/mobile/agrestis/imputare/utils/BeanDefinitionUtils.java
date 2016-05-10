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

import io.varietas.mobile.agrestis.imputare.annotation.Component;
import io.varietas.mobile.agrestis.imputare.annotation.Configuration;
import io.varietas.mobile.agrestis.imputare.annotation.Service;
import io.varietas.mobile.agrestis.imputare.container.BeanDefinition;
import io.varietas.mobile.agrestis.imputare.container.PrototypeBeanDefinition;
import io.varietas.mobile.agrestis.imputare.container.SingletonBeanDefinition;
import io.varietas.mobile.agrestis.imputare.contant.AnnotationConstants;
import io.varietas.mobile.agrestis.imputare.contant.AnnotationMethodIndices;
import io.varietas.mobile.agrestis.imputare.enumeration.BeanScopes;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
    public static List<BeanDefinition> createBeanInformationSimple(final List<Class<?>> clazzList, Class annotationClass) {
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
                beanDefinitions.add(BeanDefinitionUtils.createBeanDefinition(beanClazz, scope, identifier, beanClazz.getConstructors().length - 1));
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        });
        return beanDefinitions;
    }

    /**
     * Creates a {@link BeanDefinition} for a given class. The identifier must follow the Java naming conventions (camel case, first letter lower case). If needed this method creates a new instance of
     * the class (used for prototypes only).
     *
     * @param beanClazz
     * @param scope
     * @param identifier
     * @param constructorIndex
     * @param params
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static BeanDefinition createBeanDefinition(final Class<?> beanClazz, final BeanScopes scope, final String identifier, final int constructorIndex, Object... params) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        LOGGER.log(Level.FINER, String.format("Bean definition for bean '%s' of type '%s' (%s) created.", identifier, scope.name(), beanClazz.getName()));
        if (scope.equals(BeanScopes.SINGELTON)) {
            Object instance = beanClazz.getConstructors()[constructorIndex].newInstance(params);
            LOGGER.log(Level.FINER, String.format("Instance for bean '%s' created.", identifier));
            return new SingletonBeanDefinition(identifier, scope, beanClazz, instance);
        }
        LOGGER.log(Level.FINER, String.format("Bean definition for bean '%s' of type '%s' (%s) created.", identifier, scope.name(), beanClazz.getName()));
        return new PrototypeBeanDefinition(identifier, scope, beanClazz, params);
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
            return identifier.substring(0, 0).toLowerCase() + identifier.substring(1);
        }
        return defaultName.substring(0, 0).toLowerCase() + defaultName.substring(1);
    }

}
