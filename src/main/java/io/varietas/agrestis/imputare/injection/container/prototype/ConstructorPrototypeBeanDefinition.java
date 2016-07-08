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
package io.varietas.agrestis.imputare.injection.container.prototype;

import io.varietas.agrestis.imputare.enumeration.BeanScope;
import io.varietas.agrestis.imputare.utils.container.Pair;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * <h1>ConstructorPrototypeBeanDefinition</h1>
 *
 * @author Michael Rhöse
 * @since Fr, Jul 8, 2016
 */
public class ConstructorPrototypeBeanDefinition extends AbstractPrototypeBeanDefinition<Constructor> {

    private final List<Pair<Field, Object>> beanDependencies;

    public ConstructorPrototypeBeanDefinition(List<Pair<Field, Object>> beanDependencies, Constructor activtionTarget, Object[] activationTargetParam, String beanIdentifier, BeanScope beanScope, Class beanClazz) {
        super(activtionTarget, activationTargetParam, beanIdentifier, beanScope, beanClazz);
        this.beanDependencies = beanDependencies;
    }

    @Override
    public Optional<Class<?>> targetParent() {
        return Optional.empty();
    }

    @Override
    public List<Pair<Field, Object>> beanDependencies() {
        return this.beanDependencies;
    }

}
