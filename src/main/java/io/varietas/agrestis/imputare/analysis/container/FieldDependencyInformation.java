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
package io.varietas.agrestis.imputare.analysis.container;

import java.lang.reflect.Field;

/**
 * <h2>FieldDependencyInformation</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/8/2016
 */
public class FieldDependencyInformation extends DependencyInformation {

    private final Field field;

    public FieldDependencyInformation(Field field, String identifier, Class<?> type) {
        super(identifier, type);
        this.field = field;
    }

    public Field field() {
        return this.field;
    }
}
