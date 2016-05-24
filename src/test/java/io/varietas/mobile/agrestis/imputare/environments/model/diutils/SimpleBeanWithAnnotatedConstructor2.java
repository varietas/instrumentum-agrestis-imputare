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
package io.varietas.mobile.agrestis.imputare.environments.model.diutils;

import io.varietas.mobile.agrestis.imputare.annotation.Autowire;
import io.varietas.mobile.agrestis.imputare.annotation.Component;

/**
 * <h1>SimpleBeanWithAnnotatedConstructor1</h1>
 *
 * @author Michael Rhöse
 * @since So, Mai 22, 2016
 */
@Component
public class SimpleBeanWithAnnotatedConstructor2 {

    private int intValue;

    public SimpleBeanWithAnnotatedConstructor2(@Autowire("intBeanValue") int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
