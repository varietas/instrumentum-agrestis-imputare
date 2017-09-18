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
package io.varietas.agrestis.imputare.injection;

import io.varietas.agrestis.imputare.analysis.containers.AbstractDependencyRequester;
import io.varietas.agrestis.imputare.analysis.containers.BeanInformation;
import io.varietas.agrestis.imputare.analysis.containers.ConstructorInformation;
import io.varietas.agrestis.imputare.analysis.containers.DependencyInformation;
import io.varietas.agrestis.imputare.analysis.containers.FieldDependencyInformation;
import io.varietas.agrestis.imputare.analysis.containers.MethodInformation;
import io.varietas.agrestis.imputare.enumerations.BeanScopes;
import io.varietas.agrestis.imputare.injection.containers.BeanDefinition;
import io.varietas.agrestis.imputare.injection.containers.prototype.ConstructorPrototypeBeanDefinition;
import io.varietas.agrestis.imputare.injection.containers.prototype.MethodPrototypeBeanDefinition;
import io.varietas.agrestis.imputare.injection.containers.singleton.SingletonBeanDefinition;
import io.varietas.agrestis.imputare.storage.BeanDefinitionStorage;
import io.varietas.agrestis.imputare.storage.DefinitionStorage;
import io.varietas.agrestis.imputare.storage.SortedBeanInformationStorage;
import io.varietas.agrestis.imputare.utils.containers.Pair;
import io.varietas.agrestis.imputare.utils.injection.InjectionUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * <h2>DependencyInjector</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/7/2016
 */
public final class DependencyInjector {

    private final SortedBeanInformationStorage informationStorage;
    private final DefinitionStorage<String, Class<?>, BeanDefinition> definitionStorage;

    public DependencyInjector(SortedBeanInformationStorage informationStorage) {
        this.informationStorage = informationStorage;
        this.definitionStorage = new BeanDefinitionStorage();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Starts the bean injection process for collected bean information.
     *
     * @return Current instance if the injector for fluent like API.
     */
    public final DependencyInjector doInjectionWork() {

        Optional<BeanInformation> next = this.informationStorage.next();

        while (next.isPresent()) {

            this.definitionStorage.store(this.singleInjectionWork(next.get()));

            next = this.informationStorage.next();
        }

        return this;
    }

    public DefinitionStorage<String, Class<?>, BeanDefinition> getStorage() {
        return this.definitionStorage;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private BeanDefinition singleInjectionWork(final BeanInformation beanInformation) {
        ///< If dependencies required
        final Object creationInformation = beanInformation.getCreationInformation();

        final String beanIdentifier = beanInformation.getIdentifier();

        Executable activationTarget = null;
        Optional<Class<?>> targetParent = Optional.empty();

        Object[] activationTargetPara = null;

        boolean isConstructor = creationInformation instanceof ConstructorInformation;

        final List<Pair<Field, Object>> fieldDependencies = new ArrayList<>();

        if (isConstructor) {
            
            final ConstructorInformation constructorInformation = (ConstructorInformation) creationInformation;

            activationTarget = constructorInformation.getConstructor();

            if (constructorInformation.isDependenciesRequired()) {
                activationTargetPara = this.loadDependencies(constructorInformation);
            }
        }

        if (!isConstructor) {
            MethodInformation methodInformation = (MethodInformation) creationInformation;

            activationTarget = methodInformation.getMethod();
            targetParent = Optional.of(methodInformation.getParent());

            if (methodInformation.isDependenciesRequired()) {
                activationTargetPara = this.loadDependencies(methodInformation);
            }
        }

        final List<Pair<Field, Object>> dependencies = new ArrayList<>();

        ///< Exctract getField dependencies
        if (beanInformation.isDependenciesRequired()) {

            for (DependencyInformation dependencyInformation : beanInformation.getDependencies()) {
                final Field field = ((FieldDependencyInformation) dependencyInformation).getField();

                if (this.definitionStorage.contains(dependencyInformation.getIdentifier())) {
                    dependencies.add(new Pair<>(field, this.definitionStorage.findForIdentifier(dependencyInformation.getIdentifier())));
                    continue;
                }

                if (!this.informationStorage.contains(dependencyInformation.getIdentifier())) {
                    throw new NullPointerException("No dependency information located for " + dependencyInformation.getIdentifier());
                }

                dependencies.add(new Pair<>(field, this.singleInjectionWork(this.informationStorage.findByIdentifier(dependencyInformation.getIdentifier()).get())));
            }
        }

        if (Objects.equals(beanInformation.getScope(), BeanScopes.SINGELTON)) {
            if (Objects.isNull(activationTargetPara)) {
                activationTargetPara = new Object[0];
            }

            final Object instance = InjectionUtils.invoke(activationTarget, activationTargetPara, beanIdentifier, targetParent);

            InjectionUtils.addDependenciesToBean(instance, dependencies);

            return new SingletonBeanDefinition(instance, beanIdentifier, beanInformation.getScope(), beanInformation.getType());
        }

        if (isConstructor) {
            return new ConstructorPrototypeBeanDefinition(
                fieldDependencies,
                (Constructor) activationTarget,
                activationTargetPara,
                beanIdentifier,
                beanInformation.getScope(),
                beanInformation.getType());
        }

        return new MethodPrototypeBeanDefinition(targetParent.get(), (Method) activationTarget, activationTargetPara, beanIdentifier, beanInformation.getScope(), beanInformation.getType());

    }

    private Object[] loadDependencies(final AbstractDependencyRequester dependencyRequester) {
        final List<BeanDefinition> dependencies = new ArrayList<>();
        for (DependencyInformation dependencyInformation : dependencyRequester.getDependencies()) {
            ///< if a definition is in the storage
            if (this.definitionStorage.contains(dependencyInformation.getIdentifier())) {
                dependencies.add((BeanDefinition) this.definitionStorage.findForIdentifier(dependencyInformation.getIdentifier()).get());
                continue;
            }

            ///< if an information is in the storage
            if (!this.informationStorage.contains(dependencyInformation.getIdentifier())) {
                throw new NullPointerException("No dependency information located for " + dependencyInformation.getIdentifier());
            }

            final Optional<BeanInformation> information = this.informationStorage.findByIdentifier(dependencyInformation.getIdentifier());

            if (!information.isPresent()) {
                throw new NullPointerException("No dependency information located for " + dependencyInformation.getIdentifier());
            }

            final BeanDefinition dependencyDefinition = this.singleInjectionWork(information.get());
            this.definitionStorage.store(dependencyDefinition);
            dependencies.add(dependencyDefinition);
        }

        final Object[] res = new Object[dependencies.size()];
        IntStream.range(0, dependencies.size()).forEachOrdered(index -> res[index] = dependencies.get(index).get());

        return res;
    }
}
