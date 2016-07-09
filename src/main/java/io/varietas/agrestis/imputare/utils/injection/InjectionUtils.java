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
package io.varietas.agrestis.imputare.utils.injection;

import io.varietas.agrestis.imputare.error.InvokationException;
import io.varietas.agrestis.imputare.utils.container.Pair;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java8.util.Optional;
import java8.util.stream.StreamSupport;
import org.slf4j.Logger;
;
import org.slf4j.LoggerFactory;

/**
 * <h1>InjectionUtils</h1>
 *
 * @author Michael Rhöse
 * @since Fr, Jul 8, 2016
 */


public class InjectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(InjectionUtils.class);

    public static final <ActivationTarget extends Executable> Object invoke(final ActivationTarget activationTarget, final Object[] activationTargetParam, final String beanIdentifier, Optional<Class<?>> targetParent) {

        Object res = null;

        if (activationTarget instanceof Method) {
            res = InjectionUtils.invokeMethod(activationTarget, activationTargetParam, beanIdentifier, targetParent);
        }

        if (activationTarget instanceof Constructor) {
            res = InjectionUtils.activateConstructor(activationTarget, activationTargetParam, beanIdentifier);
        }

        if (res == null) {
            throw new NullPointerException("There is no bean instance created.");
        }

        return res;
    }

    public static final void addDependenciesToBean(final Object beanInstance, final List<Pair<Field, Object>> dependencies) {

        if (dependencies.isEmpty()) {
            LOGGER.debug("No dependencies for bean type {} available.", beanInstance.getClass().getSimpleName());
            return;
        }

        StreamSupport.stream(dependencies).forEach(pair -> {
            try {
                Field field = pair.getValue1();

                boolean isAccessible = field.isAccessible();

                field.setAccessible(true);
                field.set(beanInstance, pair.getValue2());

                field.setAccessible(isAccessible);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
        });
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static final <ActivationTarget extends Executable> Object invokeMethod(final ActivationTarget activationTarget, final Object[] activationTargetParam, final String beanIdentifier, Optional<Class<?>> targetParent) {
        try {
            Method targetAsMethod = (Method) activationTarget;

            if (!targetParent.isPresent()) {
                throw new NullPointerException("There is no parent class available but it is required for the creation of an method bean instance.");
            }

            return targetAsMethod.invoke(targetParent.get().newInstance(), activationTargetParam);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NullPointerException ex) {
            throw new InvokationException("Instance of the prototype bean" + beanIdentifier + "couldn't be created", ex);
        }
    }

    public static final <ActivationTarget extends Executable> Object activateConstructor(final ActivationTarget activationTarget, final Object[] activationTargetParam, final String beanIdentifier) {

        try {
            Constructor constructor = (Constructor) activationTarget;

            return constructor.newInstance(activationTargetParam);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new InvokationException("Instance of the prototype bean" + beanIdentifier + "couldn't be created", ex);
        }
    }
}
