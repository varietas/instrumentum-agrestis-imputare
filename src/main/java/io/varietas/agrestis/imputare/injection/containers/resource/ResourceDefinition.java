/*
 * Copyright 2017 Michael Rhöse.
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
package io.varietas.agrestis.imputare.injection.containers.resource;

import io.varietas.agrestis.imputare.enumerations.ResourceType;
import io.varietas.agrestis.imputare.injection.containers.Definition;
import lombok.AllArgsConstructor;

/**
 * <h2>ResourceDefinition</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 09/20/2017
 * @param <TYPE> Generic type of value. It allows the usage of different primitives and dynamic value container.
 */
@AllArgsConstructor
public class ResourceDefinition<TYPE> implements Definition<TYPE> {

    private final String identifier;
    private final ResourceType type;
    private final TYPE value;

    @Override
    public String identifier() {
        return this.identifier;
    }

    public ResourceType resourceType() {
        return this.type;
    }

    @Override
    public Class<?> type() {
        return this.value.getClass();
    }

    @Override
    public TYPE get() {
        return this.value;
    }
}
