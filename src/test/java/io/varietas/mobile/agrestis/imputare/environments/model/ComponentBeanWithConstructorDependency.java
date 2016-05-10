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
package io.varietas.mobile.agrestis.imputare.environments.model;

import io.varietas.mobile.agrestis.imputare.annotation.Autowire;
import io.varietas.mobile.agrestis.imputare.annotation.Service;
import java.util.Random;

/**
 * <h1>ComponentBeanWithoutDependencies</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
@Service
public class ComponentBeanWithConstructorDependency {

    @Autowire
    private String stringValue;
    private ComponentBeanWithoutDependencies beanWithoutDependencies;

    public ComponentBeanWithConstructorDependency() {
        this("generated string " + new Random().ints(100));
    }

    public ComponentBeanWithConstructorDependency(String stringValue) {
        this(stringValue, new ComponentBeanWithoutDependencies());
    }

    @Autowire
    public ComponentBeanWithConstructorDependency(String stringValue, ComponentBeanWithoutDependencies beanWithoutDependencies) {
        this.stringValue = stringValue;
        this.beanWithoutDependencies = beanWithoutDependencies;
    }

    public String getStringValue() {
        return stringValue;
    }

    public ComponentBeanWithoutDependencies getBeanWithoutDependencies() {
        return beanWithoutDependencies;
    }
}
