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
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <h1>PrototypeConstructorBeanDefinition</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
public class PrototypeConstructorBeanDefinition extends AbstractBeanDefinition {

    private static final Logger LOGGER = Logger.getLogger(PrototypeConstructorBeanDefinition.class.getName());

    final Constructor constructor;
    final Object[] params;

    public PrototypeConstructorBeanDefinition(String beanIdentifier, Class<?> beanClazz, Constructor constructor, Object... params) {
        super(beanIdentifier, BeanScopes.PROTOTYPE, beanClazz);
        this.constructor = constructor;
        this.params = params;
    }

    @Override
    public Object getInstance() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {
        int parameterCount = this.params.length;
        LOGGER.log(Level.FINER, String.format("Constructor for identifier %s with %d parameters called.", this.beanIdentifier, parameterCount));
        return this.constructor.newInstance(this.params);
    }
}
