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
package io.varietas.mobile.agrestis.imputare.utils;

import io.varietas.mobile.agrestis.imputare.annotation.Component;
import io.varietas.mobile.agrestis.imputare.annotation.Configuration;
import io.varietas.mobile.agrestis.imputare.annotation.Service;
import io.varietas.mobile.agrestis.imputare.container.BeanDefinition;
import io.varietas.mobile.agrestis.imputare.enumeration.BeanScopes;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleComponentBean1;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleComponentBean2;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleComponentBean3;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleConfigurationBean1;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleConfigurationBean2;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleConfigurationBean3;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleServiceBean1;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleServiceBean2;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleServiceBean3;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * <h1>BeanDefinitionUtilsTests</h1>
 *
 * @author Michael Rhöse
 * @since Di, Mai 10, 2016
 */
public class BeanDefinitionUtilsTests {

    private static final Logger LOGGER = Logger.getLogger(BeanDefinitionUtilsTests.class.getName());

    @Test
    public void createBeanInformationServiceSimple() {

        List<Class<?>> clazzList = new ArrayList<Class<?>>() {
            {
                add(SimpleServiceBean1.class);
                add(SimpleServiceBean2.class);
                add(SimpleServiceBean3.class);
            }
        };
        List<BeanDefinition> beanDefinitions = BeanDefinitionUtils.createBeanInformationSimple(clazzList, Service.class);
        
        Assertions.assertThat(beanDefinitions).hasSize(clazzList.size());
        LOGGER.log(Level.INFO, String.format("%d bean definitions creatded, %d expected.", beanDefinitions.size(), clazzList.size()));
        Assertions.assertThat(clazzList).contains(beanDefinitions.get(0).getBeanClass(), beanDefinitions.get(1).getBeanClass(), beanDefinitions.get(2).getBeanClass());
        LOGGER.log(Level.INFO, "Bean definitions for all classes created.");
        
        beanDefinitions.forEach(beanDefinition -> {
            Assertions.assertThat(clazzList).contains(beanDefinition.getBeanClass());
            LOGGER.log(Level.INFO, String.format("Bean definition class '%s' contains in class list.", beanDefinition.getBeanClass().getSimpleName()));
            
            Assertions.assertThat(beanDefinition.getBeanScope()).isEqualTo(BeanScopes.SINGELTON);
            LOGGER.log(Level.INFO, String.format("BeanScopes.%s of bean definition is equals to configured BeanScopes.%s", beanDefinition.getBeanScope().name(), BeanScopes.SINGELTON.name()));
            
            try {
                Assertions.assertThat(beanDefinition.getInstance()).isNotNull();
                LOGGER.log(Level.INFO, "Is not null.");
                
                int firstIntValue = (int) beanDefinition.getBeanClass().getMethod("getIntValue").invoke(beanDefinition.getInstance());
                int secondIntValue = (int) beanDefinition.getBeanClass().getMethod("getIntValue").invoke(beanDefinition.getInstance());
                
                Assertions.assertThat(firstIntValue).isEqualTo(secondIntValue);
                LOGGER.log(Level.INFO, String.format("Bean is a singleton. Saved value %d == %d", firstIntValue, secondIntValue));
                
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @Test
    public void createBeanInformationConfigurationSimple() {

        List<Class<?>> clazzList = new ArrayList<Class<?>>() {
            {
                add(SimpleConfigurationBean1.class);
                add(SimpleConfigurationBean2.class);
                add(SimpleConfigurationBean3.class);
            }
        };
        List<BeanDefinition> beanDefinitions = BeanDefinitionUtils.createBeanInformationSimple(clazzList, Configuration.class);
        
        Assertions.assertThat(beanDefinitions).hasSize(clazzList.size());
        LOGGER.log(Level.INFO, String.format("%d bean definitions creatded, %d expected.", beanDefinitions.size(), clazzList.size()));
        Assertions.assertThat(clazzList).contains(beanDefinitions.get(0).getBeanClass(), beanDefinitions.get(1).getBeanClass(), beanDefinitions.get(2).getBeanClass());
        LOGGER.log(Level.INFO, "Bean definitions for all classes created.");
        
        beanDefinitions.forEach(beanDefinition -> {
            Assertions.assertThat(clazzList).contains(beanDefinition.getBeanClass());
            LOGGER.log(Level.INFO, String.format("Bean definition class '%s' contains in class list.", beanDefinition.getBeanClass().getSimpleName()));
            
            Assertions.assertThat(beanDefinition.getBeanScope()).isEqualTo(BeanScopes.SINGELTON);
            LOGGER.log(Level.INFO, String.format("BeanScopes.%s of bean definition is equals to configured BeanScopes.%s", beanDefinition.getBeanScope().name(), BeanScopes.SINGELTON.name()));
            
            try {
                Assertions.assertThat(beanDefinition.getInstance()).isNotNull();
                LOGGER.log(Level.INFO, "Is not null.");
                
                int firstIntValue = (int) beanDefinition.getBeanClass().getMethod("getIntValue").invoke(beanDefinition.getInstance());
                int secondIntValue = (int) beanDefinition.getBeanClass().getMethod("getIntValue").invoke(beanDefinition.getInstance());
                
                Assertions.assertThat(firstIntValue).isEqualTo(secondIntValue);
                LOGGER.log(Level.INFO, String.format("Bean is a singleton. Saved value %d == %d", firstIntValue, secondIntValue));
                
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @Test
    public void createBeanInformationComponentSimple() {

        List<Class<?>> clazzList = new ArrayList<Class<?>>() {
            {
                add(SimpleComponentBean1.class);
                add(SimpleComponentBean2.class);
                add(SimpleComponentBean3.class);
            }
        };
        List<BeanDefinition> beanDefinitions = BeanDefinitionUtils.createBeanInformationSimple(clazzList, Component.class);
        
        Assertions.assertThat(beanDefinitions).hasSize(clazzList.size());
        LOGGER.log(Level.INFO, String.format("%d bean definitions creatded, %d expected.", beanDefinitions.size(), clazzList.size()));
        Assertions.assertThat(clazzList).contains(beanDefinitions.get(0).getBeanClass(), beanDefinitions.get(1).getBeanClass(), beanDefinitions.get(2).getBeanClass());
        LOGGER.log(Level.INFO, "Bean definitions for all classes created.");
        
        beanDefinitions.forEach(beanDefinition -> {
            Assertions.assertThat(clazzList).contains(beanDefinition.getBeanClass());
            LOGGER.log(Level.INFO, String.format("Bean definition class '%s' contains in class list.", beanDefinition.getBeanClass().getSimpleName()));
            
            Assertions.assertThat(beanDefinition.getBeanScope()).isEqualTo(BeanScopes.SINGELTON);
            LOGGER.log(Level.INFO, String.format("BeanScopes.%s of bean definition is equals to configured BeanScopes.%s", beanDefinition.getBeanScope().name(), BeanScopes.SINGELTON.name()));
            
            try {
                Assertions.assertThat(beanDefinition.getInstance()).isNotNull();
                LOGGER.log(Level.INFO, "Is not null.");
                
                int firstIntValue = (int) beanDefinition.getBeanClass().getMethod("getIntValue").invoke(beanDefinition.getInstance());
                int secondIntValue = (int) beanDefinition.getBeanClass().getMethod("getIntValue").invoke(beanDefinition.getInstance());
                
                Assertions.assertThat(firstIntValue).isEqualTo(secondIntValue);
                LOGGER.log(Level.INFO, String.format("Bean is a singleton. Saved value %d == %d", firstIntValue, secondIntValue));
                
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    @Test
    public void createBeanDefinitionServiceSimple() {

    }
    
    @Test
    public void createBeanDefinitionConfigurationSimple() {

    }
    
    @Test
    public void createBeanDefinitionComponentSimple() {

    }

    @Test
    public void formatIdentifier() {

    }
}
