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
package io.varietas.agrestis.imputare.analysis.containers;

/**
 * <h2>DependencyInformation</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/1/2016
 */
public class DependencyInformation {

    private final String identifier;
    private final Class<?> type;

    public DependencyInformation(String identifier, Class<?> type) {
        this.identifier = identifier;
        this.type = type;
    }

    public String identifier() {
        return identifier;
    }

    public Class<?> type() {
        return type;
    }
}