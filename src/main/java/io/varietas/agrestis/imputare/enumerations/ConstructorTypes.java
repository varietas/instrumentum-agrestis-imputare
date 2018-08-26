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
package io.varietas.agrestis.imputare.enumerations;

/**
 * <h2>ConstructorTypes</h2>
 *
 * These constants are used in the agrestis imputare core for identification of used constructor. A bean can created by one constructor. If a constructor requires injected parameters the
 * {@link ConstructorTypes} is used to signal agrestis imputare how it has to handle the instantiation process.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 5/17/2016
 */
public enum ConstructorTypes {
    STANDARD, PARAMETERISED, COPY, INJECTED
}
