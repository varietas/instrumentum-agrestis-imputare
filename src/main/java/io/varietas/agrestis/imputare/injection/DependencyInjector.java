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
package io.varietas.agrestis.imputare.injection;

import io.varietas.agrestis.imputare.analysis.container.AbstractDependencyRequester;
import io.varietas.agrestis.imputare.analysis.container.BeanInformation;
import io.varietas.agrestis.imputare.analysis.container.ConstructorInformation;
import io.varietas.agrestis.imputare.analysis.container.DependencyInformation;
import io.varietas.agrestis.imputare.analysis.container.FieldDependencyInformation;
import io.varietas.agrestis.imputare.analysis.container.MethodInformation;
import io.varietas.agrestis.imputare.enumeration.BeanScope;
import io.varietas.agrestis.imputare.injection.container.BeanDefinition;
import io.varietas.agrestis.imputare.injection.container.prototype.ConstructorPrototypeBeanDefinition;
import io.varietas.agrestis.imputare.injection.container.prototype.MethodPrototypeBeanDefinition;
import io.varietas.agrestis.imputare.injection.container.singleton.SingletonBeanDefinition;
import io.varietas.agrestis.imputare.storage.BeanDefinitionStorage;
import io.varietas.agrestis.imputare.storage.DefinitionStorage;
import io.varietas.agrestis.imputare.storage.SortedBeanInformationStorage;
import io.varietas.agrestis.imputare.utils.container.Pair;
import io.varietas.agrestis.imputare.utils.injection.InjectionUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java8.util.Optional;

/**
 * <h2>DependencyInjector</h2>
 *
 * @author Michael Rhöse
 * @since Do, Jul 7, 2016
 */
public class DependencyInjector {

    private final SortedBeanInformationStorage informationStorage;
    private final DefinitionStorage<String, Class<?>, BeanDefinition> definitionStorage;

    public DependencyInjector(SortedBeanInformationStorage informationStorage) {
        this.informationStorage = informationStorage;
        this.definitionStorage = new BeanDefinitionStorage();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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
    private final BeanDefinition singleInjectionWork(final BeanInformation beanInformation) {
        ///< If are dependencies required
        Object creationInformation = beanInformation.getCreationInformation();

        String beanIdentifier = beanInformation.identifier();

        Executable activationTarget = null;
        Optional<Class<?>> targetParent = null;

        Object[] activationTargetPara = null;

        Boolean isConstructor = Boolean.FALSE;

        List<Pair<Field, Object>> fieldDependencies = new ArrayList<>();

        if (creationInformation instanceof ConstructorInformation) {
            isConstructor = Boolean.TRUE;

            ConstructorInformation constructorInformation = (ConstructorInformation) creationInformation;

            activationTarget = constructorInformation.getConstructor();
            targetParent = Optional.empty();

            if (constructorInformation.isDependenciesRequired()) {
                activationTargetPara = this.loadDependencies(constructorInformation);
            }
        }

        if (creationInformation instanceof MethodInformation) {
            MethodInformation methodInformation = (MethodInformation) creationInformation;

            activationTarget = methodInformation.getMethod();
            targetParent = Optional.of(methodInformation.getParent());

            if (methodInformation.isDependenciesRequired()) {
                activationTargetPara = this.loadDependencies(methodInformation);
            }
        }

        final List<Pair<Field, Object>> dependencies = new ArrayList<>();

        ///< Exctract field dependencies
        if (beanInformation.isDependenciesRequired()) {
            Class<?> type = beanInformation.type();

            for (DependencyInformation dependencyInformation : beanInformation.getDependencies()) {
                Field field = ((FieldDependencyInformation) dependencyInformation).field();

                if (this.definitionStorage.contains(dependencyInformation.identifier())) {
                    dependencies.add(new Pair<>(field, this.definitionStorage.findForIdentifier(dependencyInformation.identifier())));
                    continue;
                }

                if (!this.informationStorage.contains(dependencyInformation.identifier())) {
                    throw new NullPointerException("No dependency information located for " + dependencyInformation.identifier());
                }

                dependencies.add(new Pair<>(field, this.singleInjectionWork(this.informationStorage.findByIdentifier(dependencyInformation.identifier()).get())));
            }
        }

        if (Objects.equals(beanInformation.scope(), BeanScope.SINGELTON)) {
            Object instance = InjectionUtils.invoke(activationTarget, activationTargetPara, beanIdentifier, targetParent);

            InjectionUtils.addDependenciesToBean(instance, dependencies);

            return new SingletonBeanDefinition(instance, beanIdentifier, beanInformation.scope(), beanInformation.type());
        }

        if (isConstructor) {

            return new ConstructorPrototypeBeanDefinition(fieldDependencies, (Constructor) activationTarget, activationTargetPara, beanIdentifier, beanInformation.scope(), beanInformation.type());
        }

        return new MethodPrototypeBeanDefinition(targetParent.get(), (Method) activationTarget, activationTargetPara, beanIdentifier, beanInformation.scope(), beanInformation.type());

    }

    private Object[] loadDependencies(final AbstractDependencyRequester dependencyRequester) {
        List<Object> dependencies = new ArrayList<>();
        for (DependencyInformation dependencyInformation : dependencyRequester.getDependencies()) {
            ///< if is in definition storage
            if (this.definitionStorage.contains(dependencyInformation.identifier())) {
                dependencies.add(this.definitionStorage.findForIdentifier(dependencyInformation.identifier()).get());
                continue;
            }

            ///< if is in information storage
            if (!this.informationStorage.contains(dependencyInformation.identifier())) {
                throw new NullPointerException("No dependency information located for " + dependencyInformation.identifier());
            }

            BeanDefinition dependencyDefinition = this.singleInjectionWork(this.informationStorage.findByIdentifier(dependencyInformation.identifier()).get());
            this.definitionStorage.store(dependencyDefinition);
            dependencies.add(dependencyDefinition.get());
        }

        return dependencies.toArray();
    }
}
