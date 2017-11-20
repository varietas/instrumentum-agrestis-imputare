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
package io.varietas.agrestis.imputare;

import io.varietas.agrestis.imputare.analysis.ClassAnalyser;
import io.varietas.agrestis.imputare.enumerations.BeanScopes;
import io.varietas.agrestis.imputare.error.AnalysisException;
import io.varietas.agrestis.imputare.error.ConstructorAccessException;
import io.varietas.agrestis.imputare.error.DuplicatedIdentifierException;
import io.varietas.agrestis.imputare.error.IllegalAnnotationException;
import io.varietas.agrestis.imputare.error.InternalException;
import io.varietas.agrestis.imputare.error.SortingException;
import io.varietas.agrestis.imputare.error.StorageInitialisingException;
import io.varietas.agrestis.imputare.error.ToManyInjectedConstructorsException;
import io.varietas.agrestis.imputare.injection.DependencyInjector;
import io.varietas.agrestis.imputare.injection.containers.BeanDefinition;
import io.varietas.agrestis.imputare.injection.containers.Definition;
import io.varietas.agrestis.imputare.injection.containers.singleton.SingletonBeanDefinition;
import io.varietas.agrestis.imputare.searching.ClassCollector;
import io.varietas.agrestis.imputare.searching.ClassSorter;
import io.varietas.agrestis.imputare.storage.DefinitionStorage;
import io.varietas.agrestis.imputare.storage.SortedInformationStorage;
import io.varietas.agrestis.imputare.utils.common.NamingUtils;
import io.varietas.instrumentum.simul.storage.SortedStorage;
import io.varietas.instrumentum.simul.storage.UnsortedStorage;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>ContextInitialiser</h2>
 *
 * This class represents the context initialiser implementation for Oracle Java based platforms.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 12/8/2016
 */
@Slf4j
public class ContextInitialiser {

    private final Package applicationPackage;

    protected DefinitionStorage<String, Class<?>, Definition> beanStorage;

    private final AgrestisImputareContextImpl agrestisImputareContext;

    public ContextInitialiser(final Class<?> application) {
        this.applicationPackage = application.getPackage();
        this.agrestisImputareContext = new AgrestisImputareContextImpl();
    }

    /**
     * Starts the whole initialising process of agrestis imputare. This includes all required operations for searching, analysing and injecting.
     *
     * @return Current instance of the initialiser for fluent like API.
     */
    public ContextInitialiser initializeContext() {
        final BeanDefinition contextDefinition = new SingletonBeanDefinition(
            agrestisImputareContext,
            NamingUtils.formatIdentifier(AgrestisImputareContext.class.getSimpleName(), "ApplicationContext"),
            BeanScopes.SINGELTON,
            AgrestisImputareContext.class);

        this.agrestisImputareContext.addContextDefinition(contextDefinition);

        UnsortedStorage unsortedStorage = this.initSearching(this.applicationPackage);
        SortedStorage sortetStorage = this.initSorting(unsortedStorage);
        SortedInformationStorage beanInformationStorage = this.initBeanAnalysis(sortetStorage);
        this.beanStorage = this.initInjection(beanInformationStorage, contextDefinition);

        return this;
    }

    public AgrestisImputareContext createContext() {

        this.agrestisImputareContext.addDefinitions(this.beanStorage.getStorage());

        return this.agrestisImputareContext;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected UnsortedStorage initSearching(final Package applicationPackage) {
        LOGGER.debug("Searching classes in package {}.", applicationPackage.getName());
        return new ClassCollector(applicationPackage).collectAnnotatedClazzes().getStorage();
    }

    protected SortedStorage initSorting(final UnsortedStorage unsortedStorage) throws SortingException {
        try {
            return new ClassSorter(unsortedStorage).sortLocatedClazzes().getStorage();
        } catch (IllegalArgumentException | IllegalAccessException | InstantiationException | IllegalAnnotationException ex) {
            throw new SortingException("Something goes wrong while sorting located classes.", ex);
        }
    }

    protected SortedInformationStorage initBeanAnalysis(final SortedStorage sortetStorage) throws AnalysisException {
        try {
            return new ClassAnalyser(sortetStorage).doAnalysis().getStorage();
        } catch (ToManyInjectedConstructorsException | ConstructorAccessException | DuplicatedIdentifierException | InternalException | StorageInitialisingException ex) {
            throw new AnalysisException("Something goes wrong while analysing dependency graph. " + ex.getLocalizedMessage());
        }
    }

    protected DefinitionStorage<String, Class<?>, Definition> initInjection(final SortedInformationStorage beanInformationStorage, final BeanDefinition contextDefinition) {
        return new DependencyInjector(beanInformationStorage, contextDefinition).doInjectionWork().getStorage();
    }
}
