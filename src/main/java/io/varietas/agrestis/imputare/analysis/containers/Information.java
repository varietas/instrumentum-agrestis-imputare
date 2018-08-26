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
package io.varietas.agrestis.imputare.analysis.containers;

import io.varietas.agrestis.imputare.analysis.InformationType;

/**
 * <h2>Information</h2>
 *
 * This interface is the minimum API an information container requires.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 09/14/2017
 */
public interface Information {

    /**
     * Gets the specific identifier of an object.
     *
     * @return Identifier.
     */
    String getIdentifier();

    /**
     * Gets the specific type of an object. The type is used for the creation of an instance.
     *
     * @return Type.
     */
    Class<?> getType();

    /**
     * Gets the specific type of the information container. This is used in the injection process to identify the required handling.
     *
     * @return Information type.
     */
    InformationType getInformationType();
}
