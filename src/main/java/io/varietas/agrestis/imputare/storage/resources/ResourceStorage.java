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
package io.varietas.agrestis.imputare.storage.resources;

import io.varietas.agrestis.imputare.enumerations.ResourceType;
import io.varietas.agrestis.imputare.utils.containers.ManagedValue;
import java.util.List;
import java.util.Map;

/**
 * <h2>ResourceStorage</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 06/22/2017
 */
public interface ResourceStorage {

    public int store(String identifier, ResourceType type, Object value);

    int storeAllForResourceType(ResourceType type, Map<String, Object> values);

    List<ManagedValue> getStorage();

    boolean reload(String identifier, Object value);

    Boolean isEmpty();
}
