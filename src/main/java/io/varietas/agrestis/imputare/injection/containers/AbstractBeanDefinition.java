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
package io.varietas.agrestis.imputare.injection.containers;

import io.varietas.agrestis.imputare.enumerations.BeanScopes;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <h2>AbstractBeanDefinition</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 5/7/2016
 */
public abstract class AbstractBeanDefinition implements BeanDefinition {

    protected final String beanIdentifier;
    protected final BeanScopes beanScope;
    protected final Class<?> beanClazz;
    protected Object[] args;

    public AbstractBeanDefinition(final String beanIdentifier, final BeanScopes beanScope, final Class beanClazz) {
        this.beanIdentifier = beanIdentifier;
        this.beanScope = beanScope;
        this.beanClazz = beanClazz;
        this.args = null;
    }

    @Override
    public String identifier() {
        return this.beanIdentifier;
    }

    @Override
    public Class<?> type() {
        return this.beanClazz;
    }

    @Override
    public BeanScopes scope() {
        return this.beanScope;
    }

    @Override
    public abstract Object get();

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected Object[] convertToArgs(List<BeanDefinition> beanDefinitions) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if (Objects.isNull(this.args)) {
            Object[] res = new Object[beanDefinitions.size()];
            this.args = beanDefinitions.stream().map(beanDefinition -> beanDefinition.get()).collect(Collectors.toList()).toArray(res);
        }
        return this.args;
    }
}
