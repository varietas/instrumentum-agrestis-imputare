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

/**
 * <h2>InformationFactory</h2>
 *
 * <p>
 * This interface represents the minimal API of factories used in acrestis imputare.</p>
 *
 * @param <Info>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/30/2016
 */
public interface InformationFactory<Info> {

    /**
     * Creates a new instance of a information container. This is not a normal getter method because it could perform required operations.
     *
     * @return New instance for collected information.
     * @throws Exception Thrown if a performed operation produces an error.
     */
    public Info get() throws Exception;
}
