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
package io.varietas.agrestis.imputare.analysis.container;

import io.varietas.agrestis.imputare.enumeration.BeanScope;

/**
 * <h1>InstanceCreationInformation</h1>
 *
 * @author Michael Rhöse
 * @since Mo, Jul 4, 2016
 */
public interface InstanceCreationInformation {

    public String identifier();

    public BeanScope scope();

    public Class<?> type();
}
