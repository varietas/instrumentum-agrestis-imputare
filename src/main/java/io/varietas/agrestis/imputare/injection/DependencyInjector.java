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

import io.varietas.agrestis.imputare.analysis.InformationType;
import io.varietas.agrestis.imputare.analysis.containers.AbstractDependencyRequester;
import io.varietas.agrestis.imputare.analysis.containers.BeanInformation;
import io.varietas.agrestis.imputare.analysis.containers.ConstructorInformation;
import io.varietas.agrestis.imputare.analysis.containers.DependencyInformation;
import io.varietas.agrestis.imputare.analysis.containers.FieldDependencyInformation;
import io.varietas.agrestis.imputare.analysis.containers.Information;
import io.varietas.agrestis.imputare.analysis.containers.MethodInformation;
import io.varietas.agrestis.imputare.analysis.containers.ResourceInformation;
import io.varietas.agrestis.imputare.analysis.containers.SettingsInformation;
import io.varietas.agrestis.imputare.enumerations.BeanScopes;
import io.varietas.agrestis.imputare.enumerations.ResourceType;
import io.varietas.agrestis.imputare.injection.containers.BeanDefinition;
import io.varietas.agrestis.imputare.injection.containers.Definition;
import io.varietas.agrestis.imputare.injection.containers.prototype.ConstructorPrototypeBeanDefinition;
import io.varietas.agrestis.imputare.injection.containers.prototype.MethodPrototypeBeanDefinition;
import io.varietas.agrestis.imputare.injection.containers.resource.ResourceDefinition;
import io.varietas.agrestis.imputare.injection.containers.singleton.SingletonBeanDefinition;
import io.varietas.agrestis.imputare.storage.BeanDefinitionStorage;
import io.varietas.agrestis.imputare.storage.DefinitionStorage;
import io.varietas.agrestis.imputare.storage.SortedInformationStorage;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.containers.DynamicValue;
import io.varietas.agrestis.imputare.utils.containers.Pair;
import io.varietas.agrestis.imputare.utils.injection.InjectionUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

/**
 * <h2>DependencyInjector</h2>
 *
 * The dependency injector process the recursive injection for beans and resources.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 7/7/2016
 */
@Slf4j
public final class DependencyInjector {

    private final SortedInformationStorage informationStorage;
    private final DefinitionStorage<String, Class<?>, Definition> definitionStorage;

    private final Yaml SettingsReader;

    public DependencyInjector(SortedInformationStorage informationStorage, final BeanDefinition contextDefinition) {
        this.informationStorage = informationStorage;
        this.definitionStorage = new BeanDefinitionStorage();
        this.definitionStorage.store(contextDefinition);
        this.SettingsReader = new Yaml();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Starts the bean injection process for collected bean information.
     *
     * @return Current instance if the injector for fluent like API.
     */
    public final DependencyInjector doInjectionWork() {

        ///< Loads all settings files because no dependencies are required but they can be dependencies for other beans.
        this.informationStorage
            .getStorage()
            .get(ClassMetaDataExtractionUtils.AnnotationCodes.SETTINGS)
            .parallelStream().map(info -> (SettingsInformation) info).forEach(this::handleSettingsInjection);

        this.informationStorage.getStorage().remove(ClassMetaDataExtractionUtils.AnnotationCodes.SETTINGS);

        ///< Handles all beans and resources which are created by methods.
        Optional<Information> next = this.informationStorage.next();

        while (next.isPresent()) {

            final Definition definition = this.singleInjectionWork(next.get());

            if (Objects.nonNull(definition)) {
                this.definitionStorage.store(definition);
            }

            next = this.informationStorage.next();
        }

        return this;
    }

    public DefinitionStorage<String, Class<?>, Definition> getStorage() {
        return this.definitionStorage;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private Definition singleInjectionWork(final Information information) {

        ///< If resource
        if (information.getInformationType().equals(InformationType.RESOURCE)) {
            return this.handleResourceInjection((ResourceInformation) information);
        }
        ///< If bean
        return this.handleBeanInjection((BeanInformation) information);
    }

    /**
     * Resources are not allowed in other classes than Configuration. Only a default constructor is allowed.
     *
     * @param resourceInformation Information of resource which has to be created.
     * @return Definition for the resource.
     */
    private Definition handleResourceInjection(final ResourceInformation resourceInformation) {

        final String identifier = resourceInformation.getIdentifier();

        final MethodInformation methodInformation = resourceInformation.getCreationInformation();

        final Executable activationTarget = methodInformation.getMethod();
        final Optional<Class<?>> targetParent = Optional.of(methodInformation.getParent());
        Object[] activationTargetParameters = null;

        if (methodInformation.isParametersRequired()) {
            activationTargetParameters = this.loadDependencies(methodInformation);
        }

        Object managedValue;
        if (resourceInformation.getResourceType().equals(ResourceType.STATIC)) {
            managedValue = InjectionUtils.invoke(activationTarget, activationTargetParameters, identifier, targetParent);
        } else {
            managedValue = new DynamicValue(InjectionUtils.invoke(activationTarget, activationTargetParameters, identifier, targetParent));
        }

        return new ResourceDefinition(identifier, resourceInformation.getResourceType(), managedValue);
    }

    /**
     * Loads and extracts values from a given settings file and stores them in the definition storage.
     *
     * @param settingsInformation Information of settings files where the values are.
     * @throws FileNotFoundException Thrown if an error occurred while file loading.
     */
    private void handleSettingsInjection(final SettingsInformation settingsInformation) {
        ///< Settings file will be parsed and each pair stored.
        ///< External resource will be a File object.

        final String fileName = (settingsInformation.getFile().endsWith(".yml")) ? settingsInformation.getFile() : settingsInformation.getFile() + ".yml";

        final Path fullFilePath = Paths.get(settingsInformation.getPath(), fileName);

        if (!Files.exists(fullFilePath)) {

        }

        InputStream settingsStream;
        if (settingsInformation.isInClassPath()) {
            settingsStream = settingsInformation.getParent().getResourceAsStream(fullFilePath.toString());
        } else {
            try {
                settingsStream = new FileInputStream(fullFilePath.toFile());
            } catch (FileNotFoundException ex) {
                LOGGER.error("No file '{}' found.", fullFilePath.toString());
                return;
            }
        }

        final Map<String, Object> data = (Map<String, Object>) this.SettingsReader.load(settingsStream);

        if (Objects.isNull(data)) {
            LOGGER.debug("No settings in file '{}'.", fullFilePath.toString());
            return;
        }

        data.entrySet().parallelStream()
            .map(setting -> new ResourceDefinition(setting.getKey(), ResourceType.SETTING, new DynamicValue(setting.getValue())))
            .forEach(setting -> this.definitionStorage.store(setting));
    }

    /**
     * Creates a bean instance by given information and stores them in the definition storage.
     *
     * @param beanInformation Extracted bean information.
     * @return Definition for the bean.
     */
    private BeanDefinition handleBeanInjection(final BeanInformation beanInformation) {
        ///< If dependencies required
        final Object creationInformation = beanInformation.getCreationInformation();

        final String beanIdentifier = beanInformation.getIdentifier();

        Executable activationTarget = null;
        Optional<Class<?>> targetParent = Optional.empty();

        Object[] activationTargetParameters = null;

        boolean isConstructor = creationInformation instanceof ConstructorInformation;

        if (isConstructor) {

            final ConstructorInformation constructorInformation = (ConstructorInformation) creationInformation;

            activationTarget = constructorInformation.getConstructor();

            if (constructorInformation.isParametersRequired()) {
                activationTargetParameters = this.loadDependencies(constructorInformation);
            }
        }

        if (!isConstructor) {
            MethodInformation methodInformation = (MethodInformation) creationInformation;

            activationTarget = methodInformation.getMethod();
            targetParent = Optional.of(methodInformation.getParent());

            if (methodInformation.isParametersRequired()) {
                activationTargetParameters = this.loadDependencies(methodInformation);
            }
        }

        final List<Pair<Field, Object>> fieldDependencies = this.loadFieldDependencies(beanInformation);

        if (Objects.equals(beanInformation.getScope(), BeanScopes.SINGELTON)) {
            if (Objects.isNull(activationTargetParameters)) {
                activationTargetParameters = new Object[0];
            }

            Object instance;
            if (isConstructor) {

                instance = InjectionUtils.invoke(activationTarget, activationTargetParameters, beanIdentifier, targetParent);

                InjectionUtils.addDependenciesToBean(instance, fieldDependencies);
            } else {
                instance = InjectionUtils.invokeMethod(activationTarget, activationTargetParameters, beanIdentifier, targetParent, fieldDependencies);
            }

            return new SingletonBeanDefinition(instance, beanIdentifier, beanInformation.getScope(), beanInformation.getType());
        }

        if (isConstructor) {
            return new ConstructorPrototypeBeanDefinition(
                fieldDependencies,
                (Constructor) activationTarget,
                activationTargetParameters,
                beanIdentifier,
                beanInformation.getScope(),
                beanInformation.getType());
        }

        return new MethodPrototypeBeanDefinition(
            targetParent.get(),
            (Method) activationTarget,
            activationTargetParameters,
            beanIdentifier,
            beanInformation.getScope(),
            beanInformation.getType(),
            fieldDependencies);
    }

    private Object[] loadDependencies(final AbstractDependencyRequester dependencyRequester) {
        final List<Definition> dependencies = new ArrayList<>();
        for (DependencyInformation dependencyInformation : dependencyRequester.getDependencies()) {
            ///< if a definition is in the storage
            if (this.definitionStorage.contains(dependencyInformation.getIdentifier())) {
                dependencies.add((Definition) this.definitionStorage.findForIdentifier(dependencyInformation.getIdentifier()).get());
                continue;
            }

            ///< if an information is in the storage
            if (!this.informationStorage.contains(dependencyInformation.getIdentifier())) {
                throw new NullPointerException("No dependency information located for " + dependencyInformation.getIdentifier());
            }

            final Optional<Information> information = this.informationStorage.findByIdentifier(dependencyInformation.getIdentifier());

            if (!information.isPresent()) {
                throw new NullPointerException("No dependency information located for " + dependencyInformation.getIdentifier());
            }

            final Definition dependencyDefinition = this.singleInjectionWork(information.get());
            this.definitionStorage.store(dependencyDefinition);
            dependencies.add(dependencyDefinition);
        }

        final Object[] res = new Object[dependencies.size()];
        IntStream.range(0, dependencies.size()).forEachOrdered(index -> res[index] = dependencies.get(index).get());

        return res;
    }

    private List<Pair<Field, Object>> loadFieldDependencies(final AbstractDependencyRequester dependencyRequester) {
        final List<Pair<Field, Object>> res = new ArrayList<>();

        ///< Extract getField dependencies
        if (dependencyRequester.isParametersRequired()) {

            for (DependencyInformation dependencyInformation : dependencyRequester.getDependencies()) {
                final Field field = ((FieldDependencyInformation) dependencyInformation).getField();

                if (this.definitionStorage.contains(dependencyInformation.getIdentifier())) {
                    res.add(new Pair<>(field, this.definitionStorage.findForIdentifier(dependencyInformation.getIdentifier()).get().get()));
                    continue;
                }

                if (!this.informationStorage.contains(dependencyInformation.getIdentifier())) {
                    throw new NullPointerException("No dependency information located for " + dependencyInformation.getIdentifier());
                }

                res.add(new Pair<>(field, this.singleInjectionWork(this.informationStorage.findByIdentifier(dependencyInformation.getIdentifier()).get()).get()));
            }
        }
        return res;
    }
}
