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
package io.varietas.mobile.agrestis.imputare.environments.model.beaininstantiationutils;

import io.varietas.mobile.agrestis.imputare.annotation.Autowire;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleComponentBean1;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleComponentBean2;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleComponentBean3;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleConfigurationBean1;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleConfigurationBean2;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleConfigurationBean3;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleServiceBean1;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleServiceBean2;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleServiceBean3;

/**
 * <h1>ClassWithAllFields</h1>
 *
 * @author Michael Rhöse
 * @since Di, Mai 24, 2016
 */
public class ClassWithAllFields {

    @Autowire
    private SimpleConfigurationBean1 simpleConfigurationBean1;
    @Autowire
    private SimpleConfigurationBean2 simpleConfigurationBean2;
    @Autowire
    private SimpleConfigurationBean3 simpleConfigurationBean3;

    @Autowire
    private SimpleServiceBean1 simpleServiceBean1;
    @Autowire
    private SimpleServiceBean2 simpleServiceBean2;
    @Autowire
    private SimpleServiceBean3 simpleServiceBean3;

    @Autowire
    private SimpleComponentBean1 simpleComponentBean1;
    @Autowire
    private SimpleComponentBean2 simpleComponentBean2;
    @Autowire
    private SimpleComponentBean3 simpleComponentBean3;
}
