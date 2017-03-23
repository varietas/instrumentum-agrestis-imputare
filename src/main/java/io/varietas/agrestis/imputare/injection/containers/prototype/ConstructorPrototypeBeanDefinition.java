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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java8.util.Optional;

/**
 * <h2>ConstructorPrototypeBeanDefinition</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/8/2016
 */
public class ConstructorPrototypeBeanDefinition extends AbstractPrototypeBeanDefinition<Constructor> {

    private final List<Pair<Field, Object>> beanDependencies;

    public ConstructorPrototypeBeanDefinition(List<Pair<Field, Object>> beanDependencies, Constructor activationTarget, Object[] activationTargetParam, String beanIdentifier, BeanScopes beanScope, Class beanClazz) {
        super(activationTarget, activationTargetParam, beanIdentifier, beanScope, beanClazz);
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
