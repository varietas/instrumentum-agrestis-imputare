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

import io.varietas.agrestis.imputare.annotation.Bean;
import io.varietas.agrestis.imputare.annotation.Component;
import io.varietas.agrestis.imputare.annotation.Configuration;
import io.varietas.agrestis.imputare.annotation.Service;
import io.varietas.agrestis.imputare.enumeration.BeanScope;
import io.varietas.mobile.agrestis.imputare.container.BeanDefinition;
import io.varietas.mobile.agrestis.imputare.container.PrototypeMethodBeanDefinition;
import io.varietas.mobile.agrestis.imputare.container.SingletonBeanDefinition;
import io.varietas.agrestis.imputare.contant.AnnotationConstants;
import io.varietas.agrestis.imputare.enumeration.ConstructorTypes;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
            try {
                beanDefinitions.add(BeanDefinitionUtils.createBeanInformationSimple(beanClazz, annotationClass));
            } catch (IllegalArgumentException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        });
        return beanDefinitions;
    }

    public static BeanDefinition createBeanInformationSimple(final Class<?> beanClazz, Class<? extends Annotation> annotationClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Annotation annotation = beanClazz.getAnnotation(annotationClass);
        String identifier = BeanScanUtils.getBeanIdentifier(beanClazz, annotation);
        BeanScope scope = BeanScanUtils.getBeanScope(annotation);

        Optional contructor = BeanScanUtils.getSpecifiedConstructor(beanClazz, ConstructorTypes.STANDARD);
        if (!contructor.isPresent()) {
            throw new NoSuchMethodException(String.format("No constructor located for ConstructorTypes.%s", ConstructorTypes.STANDARD.name()));
        }

        return BeanDefinitionUtils.createBeanDefinition(beanClazz, scope, identifier, ((Optional<Constructor>) contructor).get());
    }

    /**
     * Creates a {@link BeanDefinition} without instance of the bean. If the bean is configured as prototype the instance will created if required. If the bean configured as singleton the instance
     * will created in a following step.
     *
     * @param beanClazz Class of the configuration.
     * @param method Given method which will used to create the bean.
     * @return
     */
//    public static BeanDefinition createBeanDefinitionSimple(final Class<?> beanClazz, final Method method) {
//        ///< TODO: Search nested dependencies
//        Bean beanAnnotation = (Bean)method.getAnnotation(Bean.class);
//        BeanDefinition beanDefinition = BeanDefinitionUtils.createBeanDefinition(method.getReturnType(), beanAnnotation.scope(), BeanDefinitionUtils.formatIdentifier(beanAnnotation.name(), method.getName()), constructor, params)
//        return method.invoke(beanClazz.newInstance());
//    }
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
    public static BeanDefinition createBeanDefinition(final Class<?> beanClazz, final BeanScope scope, final String identifier, final Constructor constructor, Object... params)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//        LOGGER.log(Level.FINER, String.format("Bean definition for bean '%s' of type '%s' (%s) created.", identifier, scope.name(), beanClazz.getName()));
//        if (scope.equals(BeanScope.SINGELTON)) {
//            Object instance = constructor.newInstance(params);
//            LOGGER.log(Level.FINER, String.format("Instance for bean '%s' created.", identifier));
//            return new SingletonBeanDefinition(instance, identifier, scope, beanClazz, constructor);
//        }
//        LOGGER.log(Level.FINER, String.format("Bean definition for bean '%s' of type '%s' (%s) created.", identifier, scope.name(), beanClazz.getName()));
//        return new PrototypeConstructorBeanDefinition(params, identifier, scope, beanClazz, constructor);
        return null;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

    /**
     * Creates a {@link BeanDefinition} for a given method. The {@link BeanDefinition} could be an instace of {@link SingletonBeanDefinition} or {@link PrototypeMethodBeanDefinition}.
     *
     * @param parentObject Instance of the class tie method should invoked on.
     * @param method Method which is used to create the bean.
     * @param params (Optional) Required method parameters.
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static BeanDefinition createBeanDefinition(final Object parentObject, final Method method, Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Bean beanAnnotation = (Bean) method.getAnnotation(Bean.class);
        BeanScope scope = beanAnnotation.scope();
        String identifier = BeanDefinitionUtils.formatIdentifier(beanAnnotation.name(), method.getName());
        boolean isSingleton = Objects.equals(scope, BeanScope.PROTOTYPE);

        Object instanceObject = method.invoke(parentObject, params);

        if (isSingleton) {
            return new SingletonBeanDefinition(identifier, method.getReturnType(), instanceObject);
        }

        return new PrototypeMethodBeanDefinition(identifier, scope, method.getReturnType(), method, parentObject, params);
    }

}
