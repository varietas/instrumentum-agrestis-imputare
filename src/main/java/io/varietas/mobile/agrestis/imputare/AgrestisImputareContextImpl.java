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
package io.varietas.mobile.agrestis.imputare;

import io.varietas.mobile.agrestis.imputare.container.BeanDefinition;
import io.varietas.agrestis.imputare.error.BeanLoadException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <h1>AgrestisImputareContextImpl</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
public class AgrestisImputareContextImpl implements AgrestisImputareContext {

    private static final Logger LOGGER = Logger.getLogger(AgrestisImputareContextImpl.class.getSimpleName());

    private BeanDefinition[] store;

    private BeanDefinition contextDefinition;

    public AgrestisImputareContextImpl() {
    }

    @Override
    public <T> Optional<T> getBean(Class<T> beanClazz) {
        return this.getBean(bean -> bean.getBeanClass().equals(beanClazz), beanClazz, beanClazz.getSimpleName(), "class");
    }

    @Override
    public <T> Optional<T> getBean(String beanIdentifier, Class<T> targetType) {
        return this.getBean(bean -> bean.getIdentifier().equals(beanIdentifier), targetType, beanIdentifier, "identifier");
    }

    @Override
    public Optional<AgrestisImputareContext> getContext() {
        try {
            return Optional.of((AgrestisImputareContext) this.contextDefinition.getInstance());
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return Optional.empty();
    }

    public void addContextDefinition(BeanDefinition contextDefinition) {
        this.contextDefinition = contextDefinition;
    }

    public void addBeanDefinitions(BeanDefinition... beanDefinitions) {
        this.store = beanDefinitions;
    }

    public Integer beanCount() {
        return this.store.length;
    }

    private <T> Optional<T> getBean(Predicate<BeanDefinition> predicate, Class<T> targetType, String logObject, String logObjectType) {
        List<BeanDefinition> res = Arrays.asList(this.store).stream().filter(predicate).collect(Collectors.toList());
        try {
            if (res.size() > 1) {
                throw new BeanLoadException(String.format("There are %d beans located for bean %s '%s' but only 1 is allowed.", res.size(), logObjectType, logObject));
            }

            if (res.isEmpty()) {
                throw new ClassNotFoundException(String.format("There is now bean located for %s '%s'.", logObjectType, logObject));
            }
            return Optional.of((T) res.get(0).getInstance());
        } catch (BeanLoadException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        return Optional.empty();
    }
}
