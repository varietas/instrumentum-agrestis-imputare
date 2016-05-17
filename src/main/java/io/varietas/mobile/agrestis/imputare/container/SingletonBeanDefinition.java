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
import java.lang.reflect.Constructor;

/**
 * <h1>SingletonBeanDefinition</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
public class SingletonBeanDefinition extends AbstractBeanDefinition implements BeanDefinition{

    private final Object instance;
    
    public SingletonBeanDefinition(Object instance, String beanIdentifier, BeanScopes beanScope, Class beanClazz, Constructor beanConstructor) {
        super(beanIdentifier, beanScope, beanClazz, beanConstructor);
        this.instance = instance;
    }
    
    @Override
    public Object getInstance() {
        return this.instance;
    }
}
