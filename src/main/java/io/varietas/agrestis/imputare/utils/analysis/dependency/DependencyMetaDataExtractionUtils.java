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
package io.varietas.agrestis.imputare.utils.analysis.dependency;

import io.varietas.agrestis.imputare.analysis.container.DependencyInformation;
import io.varietas.agrestis.imputare.analysis.container.FieldDependencyInformation;
import io.varietas.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.agrestis.imputare.utils.analysis.constructor.ConstructorMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.common.NamingUtils;
import io.varietas.agrestis.imputare.utils.analysis.methods.MethodMetaDataExtractionUtils;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>DependencyMetaDataExtractionUtils</h1>
 *
 * @author Michael Rhöse
 * @since Mo, Jul 4, 2016
 */
public class DependencyMetaDataExtractionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyMetaDataExtractionUtils.class);

    /**
     * Extracts the dependencies from a given method. The method will analysed for the presents of the {@link Autowire} annotation. For later operations the type and the identifier will be extracted.
     *
     * @param method
     * @return
     * @throws IOException
     */
    public static DependencyInformation[] getDependenciesWithIdentifier(final Method method) throws IOException {

        String[] identifiers;

        if (Objects.equals(MethodMetaDataExtractionUtils.getAnnotationPosition(method), ClassMetaDataExtractionUtils.AnnotationPosition.METHOD)) {
            ///< If is method annotated
            identifiers = ((Autowire) method.getAnnotation(Autowire.class)).value();
        } else {
            ///< If are params annotated
            identifiers = new String[method.getParameterCount()];

            for (int index = 0; index < method.getParameterCount(); ++index) {
                identifiers[index] = ((Autowire) method.getParameters()[index].getAnnotation(Autowire.class)).value()[0];
            }
        }

        return DependencyMetaDataExtractionUtils.createDependencyInformation(method, identifiers);
    }

    /**
     * Extracts the dependencies from a given constructor. The constructor will analysed for the presents of the {@link Autowire} annotation. For later operations the type and the identifier will be
     * extracted.
     *
     * @param constructor
     * @return
     * @throws IOException
     */
    public static DependencyInformation[] getDependenciesWithIdentifier(final Constructor constructor) throws IOException {

        String[] identifiers;

        if (Objects.equals(ConstructorMetaDataExtractionUtils.getAnnotationPosition(constructor), ClassMetaDataExtractionUtils.AnnotationPosition.CONSTRUCTOR)) {
            ///< If is method annotated
            identifiers = ((Autowire) constructor.getAnnotation(Autowire.class)).value();
        } else {
            ///< If are params annotated
            identifiers = new String[constructor.getParameterCount()];

            for (int index = 0; index < constructor.getParameterCount(); ++index) {
                identifiers[index] = ((Autowire) constructor.getParameters()[index].getAnnotation(Autowire.class)).value()[0];
            }
        }

        return DependencyMetaDataExtractionUtils.createDependencyInformation(constructor, identifiers);
    }

    public static DependencyInformation[] getDependenciesWithIdentifier(final Class<?> clazz) throws IOException {

        final List<DependencyInformation> res = new ArrayList<>();

        Arrays.asList(clazz.getDeclaredFields()).stream().filter(field -> field.isAnnotationPresent(Autowire.class)).collect(Collectors.toList()).forEach(field -> {
            String identifier = NamingUtils.formatIdentifier(((Autowire) field.getAnnotation(Autowire.class)).value()[0], field.getName());
            res.add(new FieldDependencyInformation(field, identifier, field.getType()));
        });

        return (DependencyInformation[]) res.toArray();
    }

    /**
     * Creates the {@link DependencyInformation} for identifiers and types. The {@link Executable} parameters will iterated and with the identifier on the same index stored.
     *
     * @param method
     * @param identifiers
     * @return
     */
    public static DependencyInformation[] createDependencyInformation(final Executable method, final String[] identifiers) {
        int parameterCount = method.getParameterCount();
        DependencyInformation[] res = new DependencyInformation[parameterCount];

        for (int index = 0; index < method.getParameterCount(); ++index) {
            res[index] = new DependencyInformation(identifiers[index], method.getParameters()[index].getType());
        }

        return res;
    }
}
