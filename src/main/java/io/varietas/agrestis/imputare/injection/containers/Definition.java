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
package io.varietas.agrestis.imputare.injection.containers;

/**
 * <h2>Definition</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 09/19/2017
 */
public interface Definition<TYPE> {

    /**
     * Returns the identifier of the bean. This is required for the iteration of the bean lists at runtime by identifier.
     *
     * @return
     */
    public String identifier();

    /**
     * Returns the class of the bean. This is required for the iteration of the bean lists at runtime by class.
     *
     * @return
     */
    public Class<?> type();
    
    /**
     * Creates and returns an instance of the bean. If the bean has the scope {@link BeanScopes}.PROTOTYPE a new instance will created.
     *
     * @return
     */
    public TYPE get();
}
