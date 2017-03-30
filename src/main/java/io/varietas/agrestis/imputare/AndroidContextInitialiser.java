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

import io.varietas.agrestis.imputare.storage.SortedBeanInformationStorage;
import io.varietas.agrestis.imputare.storage.impl.UnsortedStorageImpl;
import io.varietas.instrumentum.simul.storage.SortedStorage;
import io.varietas.instrumentum.simul.storage.UnsortedStorage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>AndroidContextInitialiser</h2>
 *
 * This class represents the context initialiser implementation for Android based platforms. The OpenJDK used on Android doesn't provide the Oracle NIO framework. To get all classes for agrestis
 * imputares you have to handle a scan outside.
 *
 * @deprecated Agrestis imputare will not work on openJDK based java applications. There is no need for an alternative.
 *
 * @author Michael Rhöse
 * @version 1.0.0, 12/8/2016
 */
@Slf4j
@Deprecated
public class AndroidContextInitialiser extends AbstractContextInitialiser<AndroidContextInitialiser> {

    private final List<Class<?>> collectedClazzes;

    public AndroidContextInitialiser(final List<Class<?>> collectedClazzes) {
        this.collectedClazzes = collectedClazzes;
    }

    @Override
    public AndroidContextInitialiser initializeContext() {
        UnsortedStorage unsortedStorage = this.createUnsortedStorage(this.collectedClazzes);
        SortedStorage sortetStorage = this.initSorting(unsortedStorage);
        SortedBeanInformationStorage beanInformationStorage = this.initAnalysis(sortetStorage);
        this.beanStorage = this.initInjection(beanInformationStorage);

        return this;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private UnsortedStorage createUnsortedStorage(final List<Class<?>> collectedCLazzes) {
        UnsortedStorageImpl storage = new UnsortedStorageImpl(collectedCLazzes.size());
        storage.storeAll(collectedCLazzes);
        LOGGER.debug("{} classes collected and stored.", collectedCLazzes.size());
        return storage;
    }
}
