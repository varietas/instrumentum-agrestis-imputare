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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <h1>PrototypeBeanDefinition</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
public class PrototypeBeanDefinition extends AbstractBeanDefinition implements BeanDefinition {

    private static final Logger LOGGER = Logger.getLogger(PrototypeBeanDefinition.class.getName());

    private final Object[] parameters;

    public PrototypeBeanDefinition(final String beanIdentifier, final BeanScopes beanScope, final Class beanClazz, final Object... parameters) {
        super(beanIdentifier, beanScope, beanClazz);
        this.parameters = parameters;
    }

    @Override
    public Object getInstance() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {
        int parameterCount = this.parameters.length;

        LOGGER.log(Level.FINER, String.format("Constructor for identifier %s with %d parameters called.", this.beanIdentifier, parameterCount));
        
        if (parameterCount == 0) {
            return this.beanClazz.getConstructor().newInstance();
        }

        Class[] parameterTypes = new Class[this.parameters.length];
        for (int index = 0; index < parameterCount; ++index) {
            parameterTypes[parameterCount] = this.parameters[index].getClass();
        }

        return this.beanClazz.getConstructor(parameterTypes).newInstance(this.parameters);
    }
}
