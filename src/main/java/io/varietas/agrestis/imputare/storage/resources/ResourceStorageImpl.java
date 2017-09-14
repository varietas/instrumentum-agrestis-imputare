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
import io.varietas.agrestis.imputare.utils.containers.DynamicValue;
import io.varietas.agrestis.imputare.utils.containers.ManagedValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <h2>ResourceStorageImpl</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 06/20/2017
 */
public class ResourceStorageImpl implements ResourceStorage {

    private final List<ManagedValue> managedValues;
    
    public ResourceStorageImpl() {
        this.managedValues = new ArrayList<>();
    }

    @Override
    public int store(final String identifier, final ResourceType type, final Object value) {
        this.managedValues.add(new ManagedValue(identifier, type, value));
        return this.managedValues.size();
    }

    @Override
    public int storeAllForResourceType(final ResourceType type, final Map<String, Object> values) {
        values.entrySet().stream().map(entry -> new ManagedValue(entry.getKey(), type, entry.getValue())).forEach(this.managedValues::add);
        return this.managedValues.size();
    }

    @Override
    public List<ManagedValue> getStorage() {
        return this.managedValues;
    }

    @Override
    public boolean reload(String identifier, Object value) {
        List<ManagedValue> settings = this.managedValues.stream().filter(val -> val.getIdentifier().equals(identifier) && val.getType().equals(ResourceType.DYNAMIC)).collect(Collectors.toList());

        if (settings.isEmpty()) {
            throw new RuntimeException("No values for identifier '" + identifier + "' found.");
        }

        if (settings.size() > 1) {
            throw new RuntimeException("To many values for identifier '" + identifier + "' found.");
        }

        final ManagedValue managedValue = settings.get(0);

        if (!(managedValue.getValue() instanceof DynamicValue)) {
            throw new RuntimeException("Wrong type of dynamic values for identifier '" + identifier + "' found.");
        }

        if (value instanceof DynamicValue) {
            throw new RuntimeException("Wrong type of new values for identifier '" + identifier + "' found.");
        }

        ((DynamicValue) managedValue.getValue()).setValue(value);

        return true;
    }

    @Override
    public Boolean isEmpty() {
        return this.managedValues.isEmpty();
    }
}
