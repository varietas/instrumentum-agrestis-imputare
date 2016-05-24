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
import io.varietas.mobile.agrestis.imputare.container.BeanDefinition;
import io.varietas.mobile.agrestis.imputare.contant.AnnotationConstants;
import io.varietas.mobile.agrestis.imputare.contant.AnnotationMethodIndices;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

/**
 * <h1>BeanCreationUtils</h1>
 *
 * @author Michael Rhöse
 * @since Di, Mai 24, 2016
 */
public class BeanCreationUtils {

    public static final Optional<Object> getBeanInstance(final List<BeanDefinition> store, final Field field) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {

        Autowire autowireAnnotation = (Autowire) field.getAnnotation(Autowire.class);

        String configuredIdentifier = autowireAnnotation.value()[AnnotationMethodIndices.NAME];
        final String identifier;
        if (configuredIdentifier.equals(AnnotationConstants.ANNOTATION_BEAN_NAME_DEFAULT)) {
            identifier = field.getName();
        } else {
            identifier = configuredIdentifier;
        }

        return Optional.ofNullable(store.stream().filter(beanDefinition -> beanDefinition.getIdentifier().equals(identifier)).findFirst().get().getInstance());
    }

    public static final Optional<Object> getBeanInstance(final List<BeanDefinition> store, Constructor beanConstructor) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        if (beanConstructor.getParameterCount() == 0) {
            return Optional.of(beanConstructor.newInstance());
        }
        ///< If the constructor itself is annotated the names from the annotatio will used.
        ///< TODO: Replace later with parameter names (not possible at this moment).
        Boolean isConstructorAnnotatedIdentifiers = (beanConstructor.isAnnotationPresent(Autowire.class));

        Object[] params = new Object[beanConstructor.getParameterCount()];

        for (short index = 0; index < beanConstructor.getParameterCount(); ++index) {

            Parameter currentParameter = beanConstructor.getParameters()[index];

            String beanIdentifier;

            if (isConstructorAnnotatedIdentifiers) {
                beanIdentifier = BeanScanUtils.getBeanIdentifiers(beanConstructor)[index];
            } else {
                beanIdentifier = BeanScanUtils.getBeanIdentifier(currentParameter);
            }

            Optional<BeanDefinition> beanDefinition = store.stream().filter(beanDef -> beanDef.getIdentifier().equals(beanIdentifier)).findFirst();

            if (!beanDefinition.isPresent()) {
                return Optional.empty();
            }

            params[index] = beanDefinition.get().getInstance();
        }

        return Optional.of(beanConstructor.newInstance(params));
    }
}
