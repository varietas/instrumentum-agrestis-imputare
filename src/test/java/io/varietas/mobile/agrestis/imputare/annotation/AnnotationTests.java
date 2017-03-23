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
package io.varietas.mobile.agrestis.imputare.annotation;

import io.varietas.agrestis.imputare.annotation.Service;
import io.varietas.agrestis.imputare.annotation.Component;
import io.varietas.agrestis.imputare.annotation.Configuration;
import io.varietas.test.environments.model.PrototypeBeanWithoutDependencies;
import io.varietas.test.environments.model.ComponentBeanWithoutDependencies;
import io.varietas.test.environments.model.ConfigurationBeanWithoutDependency;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * <h2>AnnotationTests</h2>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
@RunWith(JUnit4.class)
public class AnnotationTests {

    private static final Logger LOGGER = Logger.getLogger(AnnotationTests.class.getName());

    @Test
    public void findServiceAnnotationPresent() {

        Object singletonObject = new ComponentBeanWithoutDependencies();

        Assertions.assertThat(singletonObject.getClass()).isEqualTo(ComponentBeanWithoutDependencies.class);
        LOGGER.log(Level.INFO, String.format("Object type '%s' is equals to '%s'.", singletonObject.getClass().getName(), ComponentBeanWithoutDependencies.class.getName()));
        Assertions.assertThat(singletonObject.getClass().isAnnotationPresent(Service.class)).isTrue();
        LOGGER.log(Level.INFO, String.format("Annotation '%s' is present.", Service.class.getName()));
    }

    @Test
    public void findComponentAnnotationPresent() {

        Object componentObject = new PrototypeBeanWithoutDependencies();

        Assertions.assertThat(componentObject.getClass()).isEqualTo(PrototypeBeanWithoutDependencies.class);
        LOGGER.log(Level.INFO, String.format("Object type '%s' is equals to '%s'.", componentObject.getClass().getName(), PrototypeBeanWithoutDependencies.class.getName()));
        Assertions.assertThat(componentObject.getClass().isAnnotationPresent(Component.class)).isTrue();
        LOGGER.log(Level.INFO, String.format("Annotation '%s' is present.", Component.class.getName()));
    }

    @Test
    public void findConfigurationAnnotationPresent() {

        Object configurationObject = new ConfigurationBeanWithoutDependency();

        Assertions.assertThat(configurationObject.getClass()).isEqualTo(ConfigurationBeanWithoutDependency.class);
        LOGGER.log(Level.INFO, String.format("Object type '%s' is equals to '%s'.", configurationObject.getClass().getName(), ConfigurationBeanWithoutDependency.class.getName()));
        Assertions.assertThat(configurationObject.getClass().isAnnotationPresent(Configuration.class)).isTrue();
        LOGGER.log(Level.INFO, String.format("Annotation '%s' is present.", Configuration.class.getName()));
    }
}
