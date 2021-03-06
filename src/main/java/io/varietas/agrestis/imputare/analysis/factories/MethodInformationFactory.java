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
package io.varietas.agrestis.imputare.analysis.factories;

import io.varietas.agrestis.imputare.analysis.containers.DependencyInformation;
import io.varietas.agrestis.imputare.analysis.containers.MethodInformation;
import java.lang.reflect.Method;
import java.util.function.Function;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <h2>MethodInformationFactory</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 7/4/2016
 */
@Accessors(fluent = true, chain = true)
@Setter
public class MethodInformationFactory implements InformationFactory<MethodInformation> {

    private Class<?> parent;
    private Method method;
    Function<Method, DependencyInformation[]> operator;

    public MethodInformationFactory() {
    }

    @Override
    public MethodInformation get() {
        return new MethodInformation(parent, method, this.operator.apply(this.method));
    }
}
