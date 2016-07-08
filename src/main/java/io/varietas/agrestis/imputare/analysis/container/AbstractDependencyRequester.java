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

/**
 * <h1>AbstractDependencyRequester</h1>
 *
 * @author Michael Rhöse
 * @since Mi, Jul 6, 2016
 */
public class AbstractDependencyRequester implements DependencyRequester {

    private final DependencyInformation[] dependencies;
    private final Integer parameterCount;

    public AbstractDependencyRequester(DependencyInformation[] dependencies, Integer parameterCount) {
        this.dependencies = dependencies;
        this.parameterCount = parameterCount;
    }

    public DependencyInformation[] getDependencies() {
        return dependencies;
    }

    public Boolean isDependenciesRequired() {
        return this.parameterCount.intValue() > 0;
    }

    public Boolean isDependenciesAvailable() {
        if (this.dependencies == null) {
            return Boolean.TRUE;
        }

        return this.dependencies.length == 0;
    }
}
