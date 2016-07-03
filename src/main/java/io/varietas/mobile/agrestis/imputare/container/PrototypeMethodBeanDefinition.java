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
import java.lang.reflect.Method;

/**
 * <h1>PrototypeMethodBeanDefinition</h1>
 *
 * @author Michael Rhöse
 * @since Di, Jun 14, 2016
 */
public class PrototypeMethodBeanDefinition extends AbstractBeanDefinition {

    final Method method;
    final Object parentObject;
    final Object[] params;

    public PrototypeMethodBeanDefinition(String beanIdentifier, BeanScopes beanScope, Class beanClazz, Method method, Object parentObject, Object... params) {
        super(beanIdentifier, beanScope, beanClazz);
        this.method = method;
        this.parentObject = parentObject;
        this.params = params;
    }

    @Override
    public Object getInstance() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return this.method.invoke(this.parentObject, this.params);
    }

}
