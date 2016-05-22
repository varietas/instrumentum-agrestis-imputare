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
package io.varietas.mobile.agrestis.imputare.environments.model.beanscanutils;

import io.varietas.mobile.agrestis.imputare.annotation.Bean;
import io.varietas.mobile.agrestis.imputare.annotation.Component;
import io.varietas.mobile.agrestis.imputare.annotation.Configuration;
import io.varietas.mobile.agrestis.imputare.annotation.Service;
import io.varietas.mobile.agrestis.imputare.enumeration.BeanScopes;

/**
 * <h1>AllAnnotationsBeanClass</h1>
 *
 * @author Michael Rhöse
 * @since So, Mai 22, 2016
 */
@Configuration
@Service
@Component
public class AllAnnotationsBeanClass {

    private int intValue;

    @Bean
    public int intBean() {
        return this.intValue;
    }

    @Bean(scope = BeanScopes.SINGELTON)
    public int int2Bean() {
        return this.intValue;
    }
}
