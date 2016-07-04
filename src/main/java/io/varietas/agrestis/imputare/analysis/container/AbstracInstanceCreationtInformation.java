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
 * <h1>AbstracInstanceCreationtInformation</h1>
 *
 * @author Michael Rhöse
 * @since Fr, Jul 1, 2016
 */
public abstract class AbstracInstanceCreationtInformation implements Information, InstanceCreationInformation {

    private final String identifier;
    private final BeanScope scope;
    private final Class<?> type;

    public AbstracInstanceCreationtInformation(String identifier, BeanScope scope, Class<?> type) {
        this.identifier = identifier;
        this.scope = scope;
        this.type = type;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public String identifier() {
        return this.identifier;
    }

    @Override
    public BeanScope scope() {
        return this.scope;
    }

    @Override
    public Class<?> type() {
        return this.type;
    }
}
