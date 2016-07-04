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
package io.varietas.mobile.agrestis.imputare.container.information;

import io.varietas.agrestis.imputare.enumeration.BeanScope;
import io.varietas.mobile.agrestis.imputare.container.BeanDefinition;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>BeanInformation</h1>
 *
 * This class holds the information for a single bean. After scanning the hole custom project the information container will used to create the beans. It should allows to inject all dependencies
 * ordered and faster than using the {@link BeanDefinition} directly.
 *
 * @author Michael Rhöse
 * @since Di, Jun 7, 2016
 */
public class BeanInformation {

    private final Class<?> beanClazz;
    private final String beanIdentifier;
    private final BeanScope scope;

    private final List<String> dependencies = new ArrayList<>();

    public BeanInformation(Class<?> beanClazz, String beanIdentifier, BeanScope scope, List<String> dependencies) {
        this.beanClazz = beanClazz;
        this.beanIdentifier = beanIdentifier;
        this.scope = scope;
        this.dependencies.addAll(dependencies);
    }

    public Class<?> getBeanClazz() {
        return beanClazz;
    }

    public String getBeanIdentifier() {
        return beanIdentifier;
    }

    public BeanScope getScope() {
        return scope;
    }

    public List<String> getDependencies() {
        return dependencies;
    }
}
