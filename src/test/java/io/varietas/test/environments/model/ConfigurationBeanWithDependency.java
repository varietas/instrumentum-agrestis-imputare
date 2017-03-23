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
package io.varietas.test.environments.model;

import io.varietas.agrestis.imputare.annotation.Bean;
import io.varietas.agrestis.imputare.annotation.Configuration;
import io.varietas.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.agrestis.imputare.enumerations.BeanScopes;

/**
 * <h2>ConfigurationBeanWithoutDependency</h2>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
@Configuration
public class ConfigurationBeanWithDependency {

    private String stringValue;

    public ConfigurationBeanWithDependency() {
    }

    public ConfigurationBeanWithDependency(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    @Bean
    public Pojo pojoBean(@Autowire Pojo pojoBean) {
        return new Pojo(pojoBean.getIntValue());
    }

    @Bean(scope = BeanScopes.SINGELTON, name = "customPojoNameBean")
    public Pojo pojoBean(@Autowire Pojo pojoBean1, @Autowire Pojo pojoBean2) {
        return new Pojo(pojoBean1.getIntValue());
    }
}
