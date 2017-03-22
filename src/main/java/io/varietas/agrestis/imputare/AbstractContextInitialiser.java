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
import io.varietas.agrestis.imputare.enumeration.BeanScope;
import io.varietas.agrestis.imputare.error.AnalysisException;
import io.varietas.agrestis.imputare.error.IllegalAnnotationException;
import io.varietas.agrestis.imputare.error.SortingException;
import io.varietas.agrestis.imputare.error.ToManyInjectedConstructorsException;
import io.varietas.agrestis.imputare.injection.DependencyInjector;
import io.varietas.agrestis.imputare.injection.container.BeanDefinition;
import io.varietas.agrestis.imputare.injection.container.singleton.SingletonBeanDefinition;
import io.varietas.agrestis.imputare.searching.ClassSorter;
import io.varietas.agrestis.imputare.storage.DefinitionStorage;
import io.varietas.agrestis.imputare.storage.SortedBeanInformationStorage;
import io.varietas.instrumentum.simul.storage.SortedStorage;
import io.varietas.instrumentum.simul.storage.UnsortedStorage;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>AbstractContextInitialiser</h2>
 *
 * This class is used to implement different context initialiser. For example if agrestis imputare is used on android a different handling of class scan is required.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 12/8/2016
 * @param <ContextInitialiserType> Type of specific context initialiser implementation.
 */
@Slf4j
public abstract class AbstractContextInitialiser<ContextInitialiserType extends AbstractContextInitialiser> {

    protected DefinitionStorage<String, Class<?>, BeanDefinition> beanStorage;

    public abstract ContextInitialiserType initializeContext();

    public AgrestisImputareContext createContext() {

        final AgrestisImputareContextImpl agrestisImputareContext = new AgrestisImputareContextImpl();

        BeanDefinition[] definitions = new BeanDefinition[this.beanStorage.getStorage().size()];
        for (int index = 0; index < this.beanStorage.getStorage().size(); ++index) {
            definitions[index] = this.beanStorage.getStorage().get(index);
        }
        agrestisImputareContext.addBeanDefinitions(definitions);

        agrestisImputareContext.addContextDefinition(new SingletonBeanDefinition(agrestisImputareContext, AgrestisImputareContext.class.getSimpleName(), BeanScope.SINGELTON, AgrestisImputareContext.class));

        return agrestisImputareContext;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    protected SortedStorage initSorting(final UnsortedStorage unsortedStorage) throws SortingException {
        try {
            return new ClassSorter(unsortedStorage).sortLocatedClazzes().getStorage();
        } catch (IllegalArgumentException | IllegalAccessException | InstantiationException | IllegalAnnotationException ex) {
            throw new SortingException("Something goes wrong while sorting located classes.", ex);
        }
    }

    protected SortedBeanInformationStorage initAnalysis(final SortedStorage sortetStorage) throws AnalysisException {
        try {
            return new ClassAnalyser(sortetStorage).doAnalysis().getStorage();
        } catch (ToManyInjectedConstructorsException | NoSuchMethodException | IOException | IllegalArgumentException | IllegalAccessException | InstantiationException ex) {
            throw new AnalysisException("Something goes wrong while analysing dependency graph.", ex);
        }
    }

    protected DefinitionStorage<String, Class<?>, BeanDefinition> initInjection(final SortedBeanInformationStorage beanInformationStorage) {
        return new DependencyInjector(beanInformationStorage).doInjectionWork().getStorage();
    }
}
