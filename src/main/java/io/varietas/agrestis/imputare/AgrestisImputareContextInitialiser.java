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

import io.varietas.agrestis.imputare.error.SearchingException;
import io.varietas.agrestis.imputare.searching.ClassCollector;
import io.varietas.agrestis.imputare.storage.SortedBeanInformationStorage;
import io.varietas.instrumentum.simul.storage.SortedStorage;
import io.varietas.instrumentum.simul.storage.UnsortedStorage;
import java.io.IOException;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>AgrestisImputareContextInitialiser</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 6/6/2016
 */
@Slf4j
public class AgrestisImputareContextInitialiser extends AbstractContextInitialiser<AgrestisImputareContextInitialiser> {

    private final Package applicationPackage;

    public AgrestisImputareContextInitialiser(Object application) throws InstantiationException, IllegalAccessException {
        this.applicationPackage = application.getClass().getPackage();
    }

    @Override
    public AgrestisImputareContextInitialiser initializeContext() {
        UnsortedStorage unsortedStorage = this.initSearching(this.applicationPackage);
        SortedStorage sortetStorage = this.initSorting(unsortedStorage);
        SortedBeanInformationStorage beanInformationStorage = this.initAnalysis(sortetStorage);
        this.beanStorage = this.initInjection(beanInformationStorage);

        return this;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private UnsortedStorage initSearching(final Package applicationPackage) {
        try {
            LOGGER.debug("Searching classes in package {}.", this.applicationPackage.getName());
            return new ClassCollector(applicationPackage).collectAnnotatedClazzes().getStorage();
        } catch (IOException | ClassNotFoundException | URISyntaxException ex) {
            throw new SearchingException("Something goes wrong while searching annotated classes.", ex);
        }
    }
}
