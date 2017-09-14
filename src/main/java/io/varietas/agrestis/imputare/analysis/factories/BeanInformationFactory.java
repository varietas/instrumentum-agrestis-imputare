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

import io.varietas.agrestis.imputare.analysis.containers.BeanInformation;
import io.varietas.agrestis.imputare.analysis.containers.DependencyInformation;
import io.varietas.agrestis.imputare.enumerations.BeanScopes;
import java.util.Objects;
import java.util.function.Function;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <h2>BeanInformationFactory</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/30/2016
 */
@Accessors(fluent = true, chain = true)
@Setter
public class BeanInformationFactory implements InformationFactory<BeanInformation> {

    
    private Object creationInformation;
    private BeanScopes scope;
    private String identifier;
    private Class<?> type;
    Function<Class<?>, DependencyInformation[]> operator;

    @Override
    public BeanInformation get() {

        if (Objects.isNull(this.operator)) {
            return new BeanInformation(this.creationInformation, this.scope, this.identifier, this.type);
        }

        return new BeanInformation(this.creationInformation, this.scope, this.identifier, this.type, this.operator.apply(this.type));
    }
}
