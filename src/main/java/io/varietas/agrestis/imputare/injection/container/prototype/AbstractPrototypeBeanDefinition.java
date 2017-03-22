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
package io.varietas.agrestis.imputare.injection.container.prototype;

import io.varietas.agrestis.imputare.enumeration.BeanScope;
import io.varietas.agrestis.imputare.injection.container.AbstractBeanDefinition;
import io.varietas.agrestis.imputare.utils.container.Pair;
import io.varietas.agrestis.imputare.utils.injection.InjectionUtils;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.util.List;
import java8.util.Optional;

/**
 * <h2>AbstractPrototypeBeanDefinition</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/7/2016
 */
public abstract class AbstractPrototypeBeanDefinition<ActivationTarget extends Executable> extends AbstractBeanDefinition {

    private final ActivationTarget activationTarget;
    private final Object[] activationTargetParam;

    public AbstractPrototypeBeanDefinition(ActivationTarget activationTarget, Object[] activationTargetParam, String beanIdentifier, BeanScope beanScope, Class beanClazz) {
        super(beanIdentifier, beanScope, beanClazz);
        this.activationTarget = activationTarget;
        this.activationTargetParam = activationTargetParam;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Object get() {
        Object res = InjectionUtils.invoke(this.activationTarget, this.activationTargetParam, this.beanIdentifier, this.targetParent());

        InjectionUtils.addDependenciesToBean(res, this.beanDependencies());

        return res;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Provides the parent type for a method activation. The target is optional and should be an empty optional object if is it not required.
     *
     * @return
     */
    public abstract Optional<Class<?>> targetParent();

    /**
     * Provides the nested dependencies for a bean instance. The dependencies will not injected in an method created bean.
     *
     * @return
     */
    public abstract List<Pair<Field, Object>> beanDependencies();
}
