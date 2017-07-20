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
 * <h2>BeanScopes</h2>
 *
 * Agrestis imputare supports different kinds of bean instantiation. The possible scopes are described in the following list:
 *
 * <ul>
 * <li><b>PROTOTYPE:</b> Every injected variable of the bean will be a new instance.</li>
 * <li><b>SINGELTON:</b> Every injected variable of the bean will be the same instance.</li>
 * </ul>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 5/7/2016
 */
public enum BeanScopes {
    PROTOTYPE,
    SINGELTON,
}
