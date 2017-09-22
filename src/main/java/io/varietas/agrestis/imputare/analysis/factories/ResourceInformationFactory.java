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
package io.varietas.agrestis.imputare.analysis.factories;

import io.varietas.agrestis.imputare.analysis.containers.MethodInformation;
import io.varietas.agrestis.imputare.analysis.containers.ResourceInformation;
import io.varietas.agrestis.imputare.enumerations.ResourceType;
import io.varietas.agrestis.imputare.utils.analysis.dependency.DependencyMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.analysis.methods.MethodMetaDataExtractionUtils;
import java.lang.reflect.Method;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <h2>ResourceInformationFactory</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 09/15/2017
 */
@Accessors(fluent = true, chain = true)
public class ResourceInformationFactory implements InformationFactory<ResourceInformation> {

    @Setter
    private Class<?> parent;
    @Setter
    private Method method;

    @Override
    public ResourceInformation get() throws Exception {
        final String identifier = MethodMetaDataExtractionUtils.getResourceIdentifier(this.method);
        final Class<?> type = method.getReturnType();
        final ResourceType resourceType = MethodMetaDataExtractionUtils.getResourceType(this.method);
        final MethodInformation creationInformation = new MethodInformationFactory()
            .method(this.method)
            .parent(this.parent)
            .operator(DependencyMetaDataExtractionUtils::getDependenciesWithIdentifier)
            .get();

        if (MethodMetaDataExtractionUtils.isDependenciesExist(method)) {
            return new ResourceInformation(
                creationInformation,
                identifier,
                type,
                resourceType,
                DependencyMetaDataExtractionUtils.getDependenciesWithIdentifier(method)
            );
        }
        return new ResourceInformation(
            creationInformation,
            identifier,
            type,
            resourceType
        );
    }
}
