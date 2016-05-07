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
package io.varietas.mobile.agrestis.imputare.annotation;

import io.varietas.mobile.agrestis.imputare.environments.model.SingletonBeanWithoutDependencies;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * <h1>AnnotationTests</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
@RunWith(JUnit4.class)
public class AnnotationTests {
    
    private static final Logger LOGGER = Logger.getLogger(AnnotationTests.class.getName());
    
    @Test
    public void findAnnotatedClasses(){
        
        Object singletonObject = new SingletonBeanWithoutDependencies();
        
        Assertions.assertThat(singletonObject.getClass()).isEqualTo(SingletonBeanWithoutDependencies.class);
        LOGGER.log(Level.INFO, String.format("Object type '%s' is equals to '%s'.", singletonObject.getClass().getName(), SingletonBeanWithoutDependencies.class.getName()));
        Assertions.assertThat(singletonObject.getClass().isAnnotationPresent(Service.class)).isTrue();
        LOGGER.log(Level.INFO, String.format("Annotation '%s' is present.", Service.class.getName()));
    }
}