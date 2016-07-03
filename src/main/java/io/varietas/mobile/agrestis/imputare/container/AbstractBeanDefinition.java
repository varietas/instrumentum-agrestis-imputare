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
package io.varietas.mobile.agrestis.imputare.container;

import io.varietas.mobile.agrestis.imputare.enumeration.BeanScopes;
import java.lang.reflect.InvocationTargetException;

/**
 * <h1>AbstractBeanDefinition</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
public abstract class AbstractBeanDefinition implements BeanDefinition {

    protected final String beanIdentifier;
    protected final BeanScopes beanScope;
    protected final Class beanClazz;

    public AbstractBeanDefinition(final String beanIdentifier, final BeanScopes beanScope, final Class beanClazz) {
        this.beanIdentifier = beanIdentifier;
        this.beanScope = beanScope;
        this.beanClazz = beanClazz;
    }

    @Override
    public String getIdentifier() {
        return this.beanIdentifier;
    }

    @Override
    public Class getBeanClass() {
        return this.beanClazz;
    }

    @Override
    public BeanScopes getBeanScope() {
        return this.beanScope;
    }

    @Override
    public abstract Object getInstance() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException;
}
