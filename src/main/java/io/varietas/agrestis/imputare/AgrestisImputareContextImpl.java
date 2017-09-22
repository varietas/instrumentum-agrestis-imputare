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
package io.varietas.agrestis.imputare;

import io.varietas.agrestis.imputare.injection.containers.Definition;
import io.varietas.agrestis.imputare.error.BeanLoadException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>AgrestisImputareContextImpl</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 5/7/2016
 */
@Slf4j
public class AgrestisImputareContextImpl implements AgrestisImputareContext {

    private List<Definition> store;

    private Definition contextDefinition;

    public AgrestisImputareContextImpl() {
    }

    @Override
    public <T> Optional<T> getBean(Class<T> beanClazz) {
        return this.getBean(bean -> bean.type().equals(beanClazz), beanClazz, beanClazz.getSimpleName(), "class");
    }

    @Override
    public <T> Optional<T> getBean(String beanIdentifier, Class<T> targetType) {

        return this.getBean(bean -> bean.identifier().equals(beanIdentifier), targetType, beanIdentifier, "identifier");
    }

    @Override
    public Optional<AgrestisImputareContext> getContext() {
        return Optional.of((AgrestisImputareContext) this.contextDefinition.get());
    }

    public void addContextDefinition(Definition contextDefinition) {
        this.contextDefinition = contextDefinition;
    }

    public void addDefinitions(List<Definition> beanDefinitions) {
        this.store = beanDefinitions;
    }

    public Integer beanCount() {
        return this.store.size();
    }

    private <T> Optional<T> getBean(Predicate<Definition> predicate, Class<T> targetType, String logObject, String logObjectType) {

        List<Definition> res = this.store.stream().filter(predicate).collect(Collectors.toList());

        try {
            if (res.size() > 1) {
                throw new BeanLoadException(String.format("There are %d beans located for bean %s '%s' but only 1 is allowed.", res.size(), logObjectType, logObject));
            }
            if (res.isEmpty()) {
                throw new ClassNotFoundException(String.format("There is now bean located for %s '%s'.", logObjectType, logObject));
            }
            return Optional.of((T) res.get(0).get());
        } catch (BeanLoadException | ClassNotFoundException ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        return Optional.empty();
    }
}
