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
package io.varietas.agrestis.imputare.analysis;

import io.varietas.agrestis.imputare.storage.SortedStorage;
import io.varietas.agrestis.imputare.utils.classes.ClassMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.methods.MethodMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.annotation.Bean;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>ClassAnalyser</h1>
 *
 * @author Michael Rhöse
 * @since Fr, Jul 1, 2016
 */
public class ClassAnalyser {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClassAnalyser.class);
    
    private final SortedStorage<Integer,Class<?>> storage;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassAnalyser(SortedStorage<Integer,Class<?>> storage) {
        this.storage = storage;
    }
    
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassAnalyser doBeanAnalysis(){
        
        // Do configuration class analysis
        this.doMethodBeanAnalysis();
        
        // Do bean class analysis
        
        return this;
    }
    
    public ClassAnalyser doMethodBeanAnalysis(){
        
        Optional<Class<?>> next = this.storage.next(ClassMetaDataExtractionUtils.AnnotationCodes.CONFIGURATION);
        
        while(next.isPresent()){
            
            List<Method> methods = MethodMetaDataExtractionUtils.getAnnotatedMethods(next.get(), Bean.class);
            
            methods.stream().forEach(method -> {
                Class<?> beanType = method.getReturnType();
                
            });
            
            next = this.storage.next(ClassMetaDataExtractionUtils.AnnotationCodes.CONFIGURATION);
        }
        
        return this;
    }
    
    public ClassAnalyser doClazzBeanAnalysis(){
        
        return this;
    }
    
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
}
