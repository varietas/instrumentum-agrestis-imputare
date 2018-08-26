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
package io.varietas.agrestis.imputare.storage;

import io.varietas.agrestis.imputare.analysis.containers.DependencyInformation;
import io.varietas.instrumentum.simul.storage.UnsortedStorage;
import java.util.List;
import java.util.Optional;

/**
 * <h2>DefinitionStorage</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 7/7/2016
 * @param <IDENTIFIER> Generic identifier of stored definitions.
 * @param <TYPE> Generic type for the value which is stored.
 * @param <ENTRY>
 */
public interface DefinitionStorage<IDENTIFIER, TYPE, ENTRY> extends UnsortedStorage<ENTRY> {

    public Optional<ENTRY> findForIdentifier(final IDENTIFIER identifier);

    public List<ENTRY> findForType(final TYPE type);

    public Optional<ENTRY> findForDependency(final DependencyInformation dependency);

    public List<ENTRY> findForDependencies(final List<DependencyInformation> dependencies);

    public Boolean contains(final IDENTIFIER identifier);
}
