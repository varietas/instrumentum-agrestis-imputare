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
package io.varietas.test.environments.model.beaininstantiationutils;

import io.varietas.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.test.environments.model.utilssimple.SimpleComponentBean1;
import io.varietas.test.environments.model.utilssimple.SimpleComponentBean2;
import io.varietas.test.environments.model.utilssimple.SimpleComponentBean3;
import io.varietas.test.environments.model.utilssimple.SimpleConfigurationBean1;
import io.varietas.test.environments.model.utilssimple.SimpleConfigurationBean2;
import io.varietas.test.environments.model.utilssimple.SimpleConfigurationBean3;
import io.varietas.test.environments.model.utilssimple.SimpleServiceBean1;
import io.varietas.test.environments.model.utilssimple.SimpleServiceBean2;
import io.varietas.test.environments.model.utilssimple.SimpleServiceBean3;

/**
 * <h2>ClassWithBeansInConstructor</h2>
 *
 * @author Michael Rhöse
 * @since Di, Mai 24, 2016
 */
public class ClassWithBeansInConstructor {

    private SimpleConfigurationBean1 simpleConfigurationBean1;
    private SimpleConfigurationBean2 simpleConfigurationBean2;
    private SimpleConfigurationBean3 simpleConfigurationBean3;

    private SimpleServiceBean1 simpleServiceBean1;
    private SimpleServiceBean2 simpleServiceBean2;
    private SimpleServiceBean3 simpleServiceBean3;

    private SimpleComponentBean1 simpleComponentBean1;
    private SimpleComponentBean2 simpleComponentBean2;
    private SimpleComponentBean3 simpleComponentBean3;

    @Autowire({"simpleConfigurationBean1", "simpleConfigurationBean2", "simpleConfigurationBean3", "simpleServiceBean1", "simpleServiceBean2", "simpleServiceBean3", "simpleComponentBean1", "simpleComponentBean2", "simpleComponentBean3"})
    public ClassWithBeansInConstructor(SimpleConfigurationBean1 simpleConfigurationBean1, SimpleConfigurationBean2 simpleConfigurationBean2, SimpleConfigurationBean3 simpleConfigurationBean3, SimpleServiceBean1 simpleServiceBean1, SimpleServiceBean2 simpleServiceBean2, SimpleServiceBean3 simpleServiceBean3, SimpleComponentBean1 simpleComponentBean1, SimpleComponentBean2 simpleComponentBean2, SimpleComponentBean3 simpleComponentBean3) {
        this.simpleConfigurationBean1 = simpleConfigurationBean1;
        this.simpleConfigurationBean2 = simpleConfigurationBean2;
        this.simpleConfigurationBean3 = simpleConfigurationBean3;
        this.simpleServiceBean1 = simpleServiceBean1;
        this.simpleServiceBean2 = simpleServiceBean2;
        this.simpleServiceBean3 = simpleServiceBean3;
        this.simpleComponentBean1 = simpleComponentBean1;
        this.simpleComponentBean2 = simpleComponentBean2;
        this.simpleComponentBean3 = simpleComponentBean3;
    }
}
