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
package io.varietas.agrestis.imputare.analysis.factory;

import io.varietas.agrestis.imputare.analysis.container.ConstructorInformation;
import io.varietas.agrestis.imputare.analysis.container.DependencyInformation;
import java.lang.reflect.Constructor;
import java8.util.function.Function;

/**
 * <h2>ConstructorInformationFactory</h2>
 *
 * @author Michael Rhöse
 * @since So, Jul 31, 2016
 */
public class ConstructorInformationFactory implements InformationFactory<ConstructorInformation>{

    private Constructor constructor;
    Function<Constructor, DependencyInformation[]> operator;

    public ConstructorInformationFactory() {
    }

    public ConstructorInformationFactory setConstructor(Constructor constructor) {
        this.constructor = constructor;
        return this;
    }
    
    public ConstructorInformationFactory setDependencyOperator(Function<Constructor, DependencyInformation[]> operation) {
        this.operator = operation;
        return this;
    }
    
    @Override
    public ConstructorInformation build() {
        return new ConstructorInformation(this.constructor, this.operator.apply(this.constructor));
    }
}
