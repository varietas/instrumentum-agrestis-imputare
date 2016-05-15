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

import io.varietas.mobile.agrestis.imputare.annotation.Component;
import io.varietas.mobile.agrestis.imputare.annotation.Configuration;
import io.varietas.mobile.agrestis.imputare.annotation.Service;
import io.varietas.mobile.agrestis.imputare.utils.DIUtils;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <h1>AgrestisImputareContextInitializer</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
public class AgrestisImputareContextInitializer {

    private static final Logger LOGGER = Logger.getLogger(AgrestisImputareContextInitializer.class.getName());

    ///< List of classes which contains fields of other beans
    private final List<Class<?>> nestedRequiredDependenciesClazzes;

    ///< List of classes which requires beans as parameters 
    private final List<Class<?>> constructorWithAutowireAnnotationClazzes;

    ///< List of classes containing beans
    private final List<Class<?>> configurationClazzes;

    ///< List of service classes
    private final List<Class<?>> serviceClazzes;

    ///< List of component classes
    private final List<Class<?>> componentClazzes;

    ///< List of bean classes
    private final List<Class<?>> beanClazzes;

    private final Class<?> applicationClazz;

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Be sure the package '%s' exists?";
    private static final String BAD_APPLICATION_ERROR = "Unable to scan package. Be sure that a package/application is configured";

    public AgrestisImputareContextInitializer(final Object application) {

        this(application.getClass());
    }
    
    public AgrestisImputareContextInitializer(final Class<?> applicationClazz){
        
        this.nestedRequiredDependenciesClazzes = new ArrayList<>(0);
        this.constructorWithAutowireAnnotationClazzes = new ArrayList<>(0);

        this.configurationClazzes = new ArrayList<>(0);
        this.serviceClazzes = new ArrayList<>(0);
        this.componentClazzes = new ArrayList<>(0);
        this.beanClazzes = new ArrayList<>(0);
        
        this.applicationClazz = applicationClazz;
    }

    public AgrestisImputareContext initializeContext() throws IllegalArgumentException, URISyntaxException, IOException {
        AgrestisImputareContextImpl context = new AgrestisImputareContextImpl();

        this.init();

        return context;
    }

    private void init() throws IllegalArgumentException, URISyntaxException, IOException {

        if (this.applicationClazz == null) {
            throw new IllegalArgumentException(BAD_APPLICATION_ERROR);
        }

        List<Class<?>> locatedClasses = DIUtils.searchClassesFromPackage(this.applicationClazz.getPackage());

        ///< Filter classes for type
        this.configurationClazzes.addAll(locatedClasses.stream().filter(clazz -> clazz.isAnnotationPresent(Configuration.class)).collect(Collectors.toList()));
        this.serviceClazzes.addAll(locatedClasses.stream().filter(clazz -> clazz.isAnnotationPresent(Service.class)).collect(Collectors.toList()));
        this.componentClazzes.addAll(locatedClasses.stream().filter(clazz -> clazz.isAnnotationPresent(Component.class)).collect(Collectors.toList()));
    }

}
