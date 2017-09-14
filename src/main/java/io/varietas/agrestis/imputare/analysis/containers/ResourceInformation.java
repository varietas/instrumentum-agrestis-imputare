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
package io.varietas.agrestis.imputare.analysis.containers;

import java.lang.reflect.Method;
import lombok.Getter;

/**
 * <h2>ResourceInformation</h2>
 *
 * The resource information contains all information about the creation of values. The dependencies represents the required parameters if the method needs parameters.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 09/13/2017
 */
@Getter
public class ResourceInformation extends AbstractDependencyRequester {

    private final Class<?> parent;
    private final Method method;

    public ResourceInformation(final Class<?> parent, final Method method, final DependencyInformation... dependencies) {
        super(dependencies, method.getParameterCount());
        this.parent = parent;
        this.method = method;
    }
}
