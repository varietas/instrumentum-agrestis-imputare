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

/**
 * <h1>ToManyInjectionsClass1</h1>
 *
 * @author Michael Rhöse
 * @since So, Mai 22, 2016
 */
public class ToManyInjectionsClass1 {

    @Autowire("intBean")
    public ToManyInjectionsClass1(int intBean) {
    }

    @Autowire({"intBean", "doubleBean"})
    public ToManyInjectionsClass1(int intBean, double doubleBean) {
    }

    @Autowire({"intBean", "doubleBean", "floatBean"})
    public ToManyInjectionsClass1(int intBean, double doubleBean, float floatBean) {
    }
}
