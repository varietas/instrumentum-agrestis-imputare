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

package io.varietas.mobile.agrestis.imputare;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>AgrestisImputareContextInitializer</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
public class AgrestisImputareContextInitializer {
    
    public static AgrestisImputareContext initializeContext(Object application){
        AgrestisImputareContextImpl context = new AgrestisImputareContextImpl();
        
        ///< List of classes containing beans
        List<Class> configurationClazzes = new ArrayList<>();
        
        ///< List of service classes
        List<Class> serviceClazzes = new ArrayList<>();
        
        
        
        Package packageToScan = application.getClass().getPackage();
        
        
        
        return context;
    }
}
