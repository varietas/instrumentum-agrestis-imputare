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
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * <h1>BeaninstantiationUtils</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 21, 2016
 */
public class BeaninstantiationUtils {

    public static final Optional<Object> getBeanInstance(final List<BeanDefinition> store, final Field field) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Annotation autowireAnnotation = field.getAnnotation(Autowire.class);
        Method[] methods = autowireAnnotation.annotationType().getDeclaredMethods();

        String configuredIdentifier = (String) methods[AnnotationMethodIndices.NAME].invoke(autowireAnnotation);
        final String identifier;
        if (configuredIdentifier.equals(AnnotationConstants.ANNOTATION_BEAN_NAME_DEFAULT)) {
            identifier = field.getName();
        } else {
            identifier = configuredIdentifier;
        }

        return Optional.ofNullable(store.stream().filter(beanDefinition -> beanDefinition.getIdentifier().equals(identifier)).findFirst().get());
    }
}
