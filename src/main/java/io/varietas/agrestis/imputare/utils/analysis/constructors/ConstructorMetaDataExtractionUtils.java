/*
 * Copyright 2016 varietas.io
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
package io.varietas.agrestis.imputare.utils.analysis.constructors;

import io.varietas.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.agrestis.imputare.enumerations.ConstructorTypes;
import io.varietas.agrestis.imputare.error.ToManyInjectedConstructorsException;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.containers.Pair;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * <h2>ConstructorMetaDataExtractionUtils</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/6/2016
 */
public class ConstructorMetaDataExtractionUtils {

    /**
     * Chooses a constructor from a given bean class. The result of the method will be {@link Pair} with the type of the constructor and the constructor itself.
     *
     * The possible constructor types are:
     * <ul>
     * <li><b>{@link ConstructorTypes}.PARAMETERISED:</b> Constructor contains parameters. The parameters are dependencies.</li>
     * <li><b>{@link ConstructorTypes}.STANDARD:</b> Constructor without parameters.</li>
     * </ul>
     *
     * @param clazz Bean class where the constructor will be scanned.
     * @return
     * @throws ToManyInjectedConstructorsException
     * @throws NoSuchMethodException
     */
    public static final Pair chooseConstructor(Class<?> clazz) throws ToManyInjectedConstructorsException, NoSuchMethodException {
        ///< Constructor dependencies
        List<Constructor> injectedConstructors = StreamSupport.stream(Arrays.asList(clazz.getConstructors())).filter(constructor -> constructor.isAnnotationPresent(Autowire.class)).collect(Collectors.toList());

        if (injectedConstructors.size() > 1) {
            throw new ToManyInjectedConstructorsException(String.format("There are %d constructors injected. Only one is allowed.", injectedConstructors.size()));
        }

        if (!(injectedConstructors.isEmpty())) {
            return new Pair<ConstructorTypes, Constructor>(ConstructorTypes.PARAMETERISED, injectedConstructors.get(0));
        }

        List<Constructor> annotatedParamsConstructor = StreamSupport
            .stream(Arrays.asList(clazz.getConstructors())).filter(constructor -> StreamSupport
            .stream(Arrays.asList(constructor.getParameters())).filter(parameter -> parameter.isAnnotationPresent(Autowire.class)).findFirst().isPresent())
            .collect(Collectors.toList());

        if (annotatedParamsConstructor.size() > 1) {
            throw new ToManyInjectedConstructorsException(String.format("There are %d constructors with injected parameters. Only one is allowed.", injectedConstructors.size()));
        }

        if (!(annotatedParamsConstructor.isEmpty())) {
            return new Pair<ConstructorTypes, Constructor>(ConstructorTypes.PARAMETERISED, annotatedParamsConstructor.get(0));
        }

        return new Pair<ConstructorTypes, Constructor>(ConstructorTypes.STANDARD, clazz.getConstructor());
    }

    /**
     * Searches for {@link Autowire} annotation on a given method. Codes could be:
     *
     * <ul>
     * <li>NONE = 0</li>
     * <li>CONSTRUCTOR = 4</li>
     * <li>CONSTRUCTOR_PARAMETER = 5</li>
     * </ul>
     *
     * A full list of available codes in general could be found on the {@link ClassMetaDataExtractionUtils.AnnotationPosition}.
     *
     * @param method Method where the annotation will searched on.
     * @return
     */
    public static Integer getAnnotationPosition(Constructor constructor) {
        if (constructor.isAnnotationPresent(Autowire.class)) {
            return ClassMetaDataExtractionUtils.AnnotationPosition.CONSTRUCTOR;
        }

        if (StreamSupport.stream(Arrays.asList(constructor.getParameters())).filter(param -> param.isAnnotationPresent(Autowire.class)).findFirst().isPresent()) {
            return ClassMetaDataExtractionUtils.AnnotationPosition.CONSTRUCTOR_PARAMETER;
        }

        return ClassMetaDataExtractionUtils.AnnotationPosition.NONE;
    }
}
