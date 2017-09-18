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
package io.varietas.agrestis.imputare.utils.analysis.dependency;

import io.varietas.agrestis.imputare.analysis.containers.DependencyInformation;
import io.varietas.agrestis.imputare.analysis.containers.FieldDependencyInformation;
import io.varietas.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.agrestis.imputare.annotation.injections.Value;
import io.varietas.agrestis.imputare.error.MismatchParameterException;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.common.NamingUtils;
import io.varietas.agrestis.imputare.utils.analysis.methods.MethodMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.containers.Pair;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>DependencyMetaDataExtractionUtils</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/4/2016
 */
@Slf4j
public class DependencyMetaDataExtractionUtils {

    /**
     * Extracts the dependencies from a given executable. The executable will analysed for the presents of the {@link Autowire} and {@link Value} annotation. For later operations the getType and the
     * getIdentifier will be extracted.
     *
     * @param executable Instance of executable where the identifiers are on.
     * @return An array of dependency informations.
     */
    public static DependencyInformation[] getDependenciesWithIdentifier(final Executable executable) {

        final boolean isMethodAnnotated = Objects.equals(MethodMetaDataExtractionUtils.getAnnotationPosition(executable), ClassMetaDataExtractionUtils.AnnotationPosition.METHOD);

        final List<Pair<String, Boolean>> identifiers = new ArrayList<>();
        if (isMethodAnnotated) {

            identifiers.addAll(extractIdentifiersFromMethod(executable));
        } else {

            identifiers.addAll(extractIdentifiersFromParameters(executable));
        }

        int parameterCount = executable.getParameterCount();

        if (parameterCount != identifiers.size()) {
            throw new MismatchParameterException(parameterCount, executable);
        }

        DependencyInformation[] res = new DependencyInformation[parameterCount];

        for (int index = 0; index < executable.getParameterCount(); ++index) {
            final Pair<String, Boolean> identifier = identifiers.get(index);
            res[index] = new DependencyInformation(identifier.getValue1(), executable.getParameters()[index].getType(), identifier.getValue2());
        }

        return res;
    }

    private static List<Pair<String, Boolean>> extractIdentifiersFromMethod(final Executable method) {
        final List<Pair<String, Boolean>> res = new ArrayList<>();
        if (method.isAnnotationPresent(Autowire.class)) {

            Arrays.asList(((Autowire) method.getAnnotation(Autowire.class)).value())
                .stream()
                .map(entry -> new Pair<>(entry, true))
                .forEach(res::add);
        }

        if (method.isAnnotationPresent(Value.class)) {
            Arrays.asList(((Autowire) method.getAnnotation(Value.class)).value())
                .stream()
                .map(entry -> new Pair<>(entry, false))
                .forEach(res::add);
        }

        return res;
    }

    private static List<Pair<String, Boolean>> extractIdentifiersFromParameters(final Executable method) {
        return Stream.of(method.getParameters()).map((Parameter param) -> {
            if (param.isAnnotationPresent(Autowire.class)) {
                return Optional.of(new Pair<>(param.getAnnotation(Autowire.class).value()[0], true));
            }

            if (param.isAnnotationPresent(Value.class)) {
                return Optional.of(new Pair<>(param.getAnnotation(Value.class).value()[0], false));
            }

            return Optional.empty();
        })
            .filter(ident -> ident.isPresent())
            .map(ident -> (Pair<String, Boolean>) ident.get())
            .collect(Collectors.toList());
    }

    /**
     * Extracts the dependencies from a given class. The class will analysed for the presents of the {@link Autowire} and {@link Value} annotation. For later operations the getType and the
     * getIdentifier will be extracted.
     *
     * @param clazz Class where the identifiers are on.
     * @return An array of dependency informations.
     */
    public static DependencyInformation[] getDependenciesWithIdentifier(final Class<?> clazz) {

        final List<DependencyInformation> res = new ArrayList<>();

        Stream.of(clazz.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Autowire.class))
            .forEach(field -> {
                String identifier = NamingUtils.formatIdentifier(((Autowire) field.getAnnotation(Autowire.class)).value()[0], field.getName());
                res.add(new FieldDependencyInformation(field, identifier, field.getType(), true));
            });

        Stream.of(clazz.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Value.class))
            .forEach(field -> {
                String identifier = NamingUtils.formatIdentifier(((Autowire) field.getAnnotation(Autowire.class)).value()[0], field.getName());
                res.add(new FieldDependencyInformation(field, identifier, field.getType(), false));
            });

        return (DependencyInformation[]) res.toArray();
    }
}
