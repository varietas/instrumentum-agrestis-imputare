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
package io.varietas.agrestis.imputare.analysis.container;

import io.varietas.agrestis.imputare.enumeration.BeanScope;

/**
 * <h1>BeainInformation</h1>
 *
 * @author Michael Rhöse
 * @since Fr, Jul 1, 2016
 */
public class BeanInformation extends AbstractDependencyRequester {

    private final Object creationInformation;
    private final BeanScope scope;
    private final String identifier;
    private final Class<?> type;

    public BeanInformation(Object creationInformation, BeanScope scope, String identifier, Class<?> type) {
        this(creationInformation, scope, identifier, type, null);
    }

    public BeanInformation(Object creationInformation, BeanScope scope, String identifier, Class<?> type, DependencyInformation... dependencyInformations) {
        super(dependencyInformations, dependencyInformations.length);
        this.creationInformation = creationInformation;
        this.scope = scope;
        this.identifier = identifier;
        this.type = type;
    }

    public Object getCreationInformation() {
        return creationInformation;
    }

    public BeanScope scope() {
        return scope;
    }

    public String identifier() {
        return identifier;
    }

    public Class<?> type() {
        return type;
    }
}
