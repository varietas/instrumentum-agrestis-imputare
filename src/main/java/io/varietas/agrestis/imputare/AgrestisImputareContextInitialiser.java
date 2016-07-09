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
package io.varietas.agrestis.imputare;

import io.varietas.agrestis.imputare.analysis.ClassAnalyser;
import io.varietas.agrestis.imputare.enumeration.BeanScope;
import io.varietas.agrestis.imputare.error.AnalysisException;
import io.varietas.agrestis.imputare.error.IllegalAnnotationException;
import io.varietas.agrestis.imputare.error.SearchingException;
import io.varietas.agrestis.imputare.error.SortingException;
import io.varietas.agrestis.imputare.error.ToManyInjectedConstructorsException;
import io.varietas.agrestis.imputare.injection.DependencyInjector;
import io.varietas.agrestis.imputare.injection.container.BeanDefinition;
import io.varietas.agrestis.imputare.injection.container.singleton.SingletonBeanDefinition;
import io.varietas.agrestis.imputare.searching.ClassCollector;
import io.varietas.agrestis.imputare.searching.ClassSorter;
import io.varietas.agrestis.imputare.storage.DefinitionStorage;
import io.varietas.agrestis.imputare.storage.SortedBeanInformationStorage;
import io.varietas.agrestis.imputare.storage.SortedStorage;
import io.varietas.agrestis.imputare.storage.UnsortedStorage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * <h1>AgrestisImputareContextInitialiser</h1>
 *
 * @author Michael Rhöse
 * @since Mo, Jun 6, 2016
 */
public class AgrestisImputareContextInitialiser {

    private static final Logger LOGGER = Logger.getLogger(AgrestisImputareContextInitialiser.class.getName());

    private final Package applicationPackage;

    private DefinitionStorage<String, Class<?>, BeanDefinition> beanStorage;

    public AgrestisImputareContextInitialiser(Object application) throws InstantiationException, IllegalAccessException {
        this.applicationPackage = application.getClass().getPackage();

    }

    public AgrestisImputareContextInitialiser initializeContext() {
        UnsortedStorage unsortedStorage = this.initSearching(this.applicationPackage);
        SortedStorage sortetStorage = this.initSorting(unsortedStorage);
        SortedBeanInformationStorage beanInformationStorage = this.initAnalysis(sortetStorage);
        this.beanStorage = this.initInjection(beanInformationStorage);

        return this;
    }

    public AgrestisImputareContext createContext() {

        final AgrestisImputareContextImpl agrestisImputareContext = new AgrestisImputareContextImpl();

        this.beanStorage.getStorage().parallelStream().forEach((BeanDefinition entry) -> agrestisImputareContext.addBeanDefinitions(entry));

        agrestisImputareContext.addContextDefinition(new SingletonBeanDefinition(agrestisImputareContext, AgrestisImputareContext.class.getSimpleName(), BeanScope.SINGELTON, AgrestisImputareContext.class));

        return agrestisImputareContext;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private UnsortedStorage initSearching(final Package applicationPackage) {
        try {
            return new ClassCollector(applicationPackage).collectAnnotatedClazzes().getStorage();
        } catch (IOException | ClassNotFoundException | URISyntaxException ex) {
            throw new SearchingException("Something goes wrong while searching annotated classes.", ex);
        }
    }

    private SortedStorage initSorting(final UnsortedStorage unsortedStorage) throws SortingException {
        try {
            return new ClassSorter(unsortedStorage).sortLocatedClazzes().getStorage();
        } catch (IllegalArgumentException | IllegalAccessException | InstantiationException | IllegalAnnotationException ex) {
            throw new SortingException("Something goes wrong while sorting located classes.", ex);
        }
    }

    private SortedBeanInformationStorage initAnalysis(final SortedStorage sortetStorage) throws AnalysisException {
        try {
            return new ClassAnalyser(sortetStorage).doAnalysis().getStorage();
        } catch (ToManyInjectedConstructorsException | NoSuchMethodException | IOException | IllegalArgumentException | IllegalAccessException | InstantiationException ex) {
            throw new AnalysisException("Something goes wrong while analysing dependency graph.", ex);
        }
    }

    private DefinitionStorage<String, Class<?>, BeanDefinition> initInjection(final SortedBeanInformationStorage beanInformationStorage) {
        return new DependencyInjector(beanInformationStorage).doInjectionWork().getStorage();
    }
}
