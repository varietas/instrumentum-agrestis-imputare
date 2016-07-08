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
package io.varietas.agrestis.imputare.injection.container;

import io.varietas.agrestis.imputare.enumeration.BeanScope;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * <h1>AbstractBeanDefinition</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
public abstract class AbstractBeanDefinition implements BeanDefinition {

    protected final String beanIdentifier;
    protected final BeanScope beanScope;
    protected final Class beanClazz;

    public AbstractBeanDefinition(final String beanIdentifier, final BeanScope beanScope, final Class beanClazz) {
        this.beanIdentifier = beanIdentifier;
        this.beanScope = beanScope;
        this.beanClazz = beanClazz;
    }

    @Override
    public String identifier() {
        return this.beanIdentifier;
    }

    @Override
    public Class type() {
        return this.beanClazz;
    }

    @Override
    public BeanScope scope() {
        return this.beanScope;
    }

    @Override
    public abstract Object get();

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected Object[] convertToArgs(List<BeanDefinition> beanDefinitions) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object[] res = new Object[beanDefinitions.size()];

        for (int index = 0; index < beanDefinitions.size(); ++index) {
            res[index] = beanDefinitions.get(index).get();
        }

        return res;
    }
}
