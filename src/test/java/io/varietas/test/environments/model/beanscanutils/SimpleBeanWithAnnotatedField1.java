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
package io.varietas.test.environments.model.beanscanutils;

import io.varietas.agrestis.imputare.annotation.injections.Autowire;
import java.util.Random;

/**
 * <h2>SimpleBeanWithAnnotatedField1</h2>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 21, 2016
 */
public class SimpleBeanWithAnnotatedField1 {

    @Autowire("intBeanValue")
    private final int intValue;

    public SimpleBeanWithAnnotatedField1() {
        this.intValue = (new Random().nextInt(100));
    }

    public int getIntValue() {
        return intValue;
    }
}
