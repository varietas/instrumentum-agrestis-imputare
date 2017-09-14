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
package io.varietas.agrestis.imputare.utils.containers;

import io.varietas.agrestis.imputare.enumerations.ResourceType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * <h2>ManagedValue</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 06/22/2017
 */
@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class ManagedValue {
    private String identifier;
    private ResourceType type;
    private Object value;
}
