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
package io.varietas.agrestis.imputare.injection.containers.prototype;

import io.varietas.agrestis.imputare.enumerations.BeanScopes;
import io.varietas.agrestis.imputare.utils.containers.Pair;
import io.varietas.agrestis.imputare.utils.injection.InjectionUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * <h2>MethodPrototypeBeanDefinition</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 7/7/2016
 */
public class MethodPrototypeBeanDefinition extends AbstractPrototypeBeanDefinition<Method> {

    private final Class<?> parent;
    private final List<Pair<Field, Object>> beanDependencies;

    public MethodPrototypeBeanDefinition(
        final Class<?> parent,
        final Method activationTarget,
        final Object[] activationTargetParam,
        final String beanIdentifier,
        final BeanScopes beanScope,
        final Class beanClazz,
        final List<Pair<Field, Object>> beanDependencies
    ) {
        super(activationTarget, activationTargetParam, beanIdentifier, beanScope, beanClazz);
        this.parent = parent;
        this.beanDependencies = beanDependencies;
    }

    @Override
    public Optional<Class<?>> targetParent() {
        return Optional.of(this.parent);
    }

    @Override
    public List<Pair<Field, Object>> beanDependencies() {
        return this.beanDependencies;
    }

    @Override
    public Object get() {

        return InjectionUtils.invokeMethod(this.activationTarget, this.activationTargetParam, this.beanIdentifier, this.targetParent(), this.beanDependencies);
    }
}
