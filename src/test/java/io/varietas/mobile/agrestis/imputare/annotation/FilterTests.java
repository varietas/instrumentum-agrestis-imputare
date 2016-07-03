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

import io.varietas.mobile.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.test.environments.model.ComponentBeanWithConstructorDependency;
import io.varietas.test.environments.model.ComponentBeanWithoutDependencies;
import io.varietas.test.environments.model.PojoOnlyWithParamConstructor;
import io.varietas.test.environments.model.PojoWithoutConstructor;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * <h1>FilterTests</h1>
 *
 * @author Michael Rhöse
 * @since Mo, Mai 9, 2016
 */
@RunWith(JUnit4.class)
public class FilterTests {
    
    private static final Logger LOGGER = Logger.getLogger(FilterTests.class.getName());
    
    @Test
    public void getAllConstructors(){
        int constructorCount = ComponentBeanWithoutDependencies.class.getConstructors().length;
        
        LOGGER.info(String.format("Class '%s' contains %d contructors.", ComponentBeanWithoutDependencies.class.getSimpleName(), constructorCount));
        
        for(Constructor constructor : ComponentBeanWithoutDependencies.class.getConstructors()){
            LOGGER.info(String.format("  -> [%s] with %d parameters", constructor.getName().substring(constructor.getName().lastIndexOf('.') + 1), constructor.getParameterCount()));
            LOGGER.info(String.format("Autowire annotation is%spresent", (constructor.isAnnotationPresent(Autowire.class))? " " : " not "));
        }
    }
    
    @Test
    public void getAllConstructorsWithOneAutowire(){
        int constructorCount = ComponentBeanWithConstructorDependency.class.getConstructors().length;
        
        LOGGER.info(String.format("Class '%s' contains %d contructors.", ComponentBeanWithConstructorDependency.class.getSimpleName(), constructorCount));
        
        for(Constructor constructor : ComponentBeanWithConstructorDependency.class.getConstructors()){
            LOGGER.info(String.format("  -> [%s] with %d parameters", constructor.getName().substring(constructor.getName().lastIndexOf('.') + 1), constructor.getParameterCount()));
            LOGGER.info(String.format("Autowire annotation is%spresent", (constructor.isAnnotationPresent(Autowire.class))? " " : " not "));
        }
    }
    
    @Test
    public void getAllConstructorsWithoutExplisitConstructor(){
        int constructorCount = PojoWithoutConstructor.class.getConstructors().length;
        
        LOGGER.info(String.format("Class '%s' contains %d contructors.", PojoWithoutConstructor.class.getSimpleName(), constructorCount));
        
        for(Constructor constructor : PojoWithoutConstructor.class.getConstructors()){
            LOGGER.info(String.format("  -> [%s] with %d parameters", constructor.getName().substring(constructor.getName().lastIndexOf('.') + 1), constructor.getParameterCount()));
            LOGGER.info(String.format("Autowire annotation is%spresent", (constructor.isAnnotationPresent(Autowire.class))? " " : " not "));
        }
    }
    
    @Test
    public void getAllConstructorsWithOnlyParamConstructor(){
        int constructorCount = PojoOnlyWithParamConstructor.class.getConstructors().length;
        
        LOGGER.info(String.format("Class '%s' contains %d contructors.", PojoOnlyWithParamConstructor.class.getSimpleName(), constructorCount));
        
        for(Constructor constructor : PojoOnlyWithParamConstructor.class.getConstructors()){
            LOGGER.info(String.format("  -> [%s] with %d parameters", constructor.getName().substring(constructor.getName().lastIndexOf('.') + 1), constructor.getParameterCount()));
            LOGGER.info(String.format("Autowire annotation is%spresent", (constructor.isAnnotationPresent(Autowire.class))? " " : " not "));
        }
    }
}
