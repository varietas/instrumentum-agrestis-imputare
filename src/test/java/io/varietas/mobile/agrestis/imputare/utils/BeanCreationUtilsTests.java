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
package io.varietas.mobile.agrestis.imputare.utils;

import io.varietas.agrestis.imputare.injection.containers.BeanDefinition;
import io.varietas.agrestis.imputare.error.BeanLoadException;
import io.varietas.agrestis.imputare.error.RecursiveInjectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * <h2>BeanCreationUtilsTests</h2>
 *
 * @author Michael Rhöse
 * @since Di, Mai 24, 2016
 */
@Slf4j
@RunWith(JUnit4.class)
@Ignore
public class BeanCreationUtilsTests {

    private List<BeanDefinition> store;

    @Before
    public void setUp() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        this.store = new ArrayList<>();

//        store.add(BeanDefinitionUtils.createBeanInformationSimple(SimpleConfigurationBean1.class, Configuration.class));
//        store.add(BeanDefinitionUtils.createBeanInformationSimple(SimpleServiceBean1.class, Service.class));
//        store.add(BeanDefinitionUtils.createBeanInformationSimple(SimpleComponentBean1.class, Component.class));
//        store.add(BeanDefinitionUtils.createBeanInformationSimple(SimpleConfigurationBean2.class, Configuration.class));
//        store.add(BeanDefinitionUtils.createBeanInformationSimple(SimpleServiceBean2.class, Service.class));
//        store.add(BeanDefinitionUtils.createBeanInformationSimple(SimpleComponentBean2.class, Component.class));
//        store.add(BeanDefinitionUtils.createBeanInformationSimple(SimpleConfigurationBean3.class, Configuration.class));
//        store.add(BeanDefinitionUtils.createBeanInformationSimple(SimpleServiceBean3.class, Service.class));
//        store.add(BeanDefinitionUtils.createBeanInformationSimple(SimpleComponentBean3.class, Component.class));
    }

    @Test
    public void getBeanInstanceByFieldDefaultNaming() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {

//        Field[] loadedFields = ClassWithAllFields.class.getDeclaredFields();
//        Assertions.assertThat(loadedFields).hasSize(9);
//        LOGGER.info(String.format("%d fields located | %d fields expected", loadedFields.length, 9));
//
//        for (Field field : loadedFields) {
//            Optional<Object> beanInstance = BeanCreationUtils.getBeanInstance(store, field);
//
//            Assertions.assertThat(beanInstance.isPresent()).isTrue();
//            LOGGER.info(String.format("Bean for field name '%s' located.", field.getName()));
//        }
    }

    @Test
    public void getBeanInstanceWithBeansInConstructor() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, BeanLoadException, SecurityException, RecursiveInjectionException {

//        Optional<Constructor<?>> constructor = Arrays.asList(ClassWithBeansInConstructor.class.getConstructors()).stream().filter(constr -> constr.isAnnotationPresent(Autowire.class)).findFirst();
//
//        Assertions.assertThat(constructor.isPresent()).isTrue();
//        LOGGER.info("Annotated constructor located");
//
//        Optional beanInstance = BeanCreationUtils.getBeanInstance(this.store, constructor.get());
//
//        Assertions.assertThat(beanInstance.isPresent()).isTrue();
//        Assertions.assertThat(beanInstance.get().getClass()).isEqualTo(ClassWithBeansInConstructor.class);
//        ClassWithBeansInConstructor castedInstance = (ClassWithBeansInConstructor) beanInstance.get();
//        LOGGER.info(String.format("Bean with type '%s' for constructor '%s' with %d beans located.", castedInstance.getClass().getSimpleName(), constructor.get().getName(), constructor.get().getParameterCount()));
    }
}
