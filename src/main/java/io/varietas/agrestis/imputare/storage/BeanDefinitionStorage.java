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
package io.varietas.agrestis.imputare.storage;

import io.varietas.agrestis.imputare.analysis.containers.DependencyInformation;
import io.varietas.agrestis.imputare.injection.containers.Definition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <h2>BeanDefinitionStorage</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/7/2016
 */
public class BeanDefinitionStorage implements DefinitionStorage<String, Class<?>, Definition> {

    private final List<Definition> storage;

    public BeanDefinitionStorage() {
        this.storage = new ArrayList<>();
    }

    @Override
    public Optional<Definition> findForIdentifier(String identifier) {
        return this.storage.stream()
            .filter(entry -> Objects.equals(entry.identifier(), identifier))
            .findFirst();
    }

    @Override
    public List<Definition> findForType(Class<?> type) {
        return this.storage.stream()
            .filter(entry -> Objects.equals(entry.type(), type))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Definition> findForDependency(DependencyInformation dependency) {
        return this.findForIdentifier(dependency.getIdentifier());
    }

    @Override
    public List<Definition> findForDependencies(List<DependencyInformation> dependencies) {
        List<Definition> res = new ArrayList<>();

        dependencies.stream()
            .map((dependencyInformation) -> this.findForDependency(dependencyInformation))
            .filter((dependency) -> !(!dependency.isPresent()))
            .map(Optional::get)
            .forEachOrdered(storage::add);

        return res;
    }

    @Override
    public int store(Definition entry) {
        if (this.storage.add(entry)) {
            return this.storage.size();
        }

        return -1;
    }

    @Override
    public int storeAll(Collection<Definition> entries) {
        for (Definition beanDefinition : entries) {
            if (this.store(beanDefinition) == -1) {
                return -1;
            }
        }

        return this.storage.size();
    }

    @Override
    public List<Definition> getStorage() {
        return this.storage;
    }

    @Override
    public Optional<Definition> next() {

        if (this.isEmpty()) {
            return Optional.empty();
        }

        Definition res = this.storage.get(this.storage.size() - 1);
        this.storage.remove(res);

        return Optional.ofNullable(res);
    }

    @Override
    public Boolean isEmpty() {
        return this.storage.isEmpty();
    }

    @Override
    public Boolean contains(String identifier) {
        return this.storage.stream()
            .filter(entry -> Objects.equals(entry.identifier(), identifier))
            .findFirst()
            .isPresent();
    }
}
