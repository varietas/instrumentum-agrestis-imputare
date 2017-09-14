/*
 * Copyright 2017 Michael Rhöse.
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

import io.varietas.instrumentum.simul.storage.Storage;

/**
 * <h2>Analyser</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0, 09/14/2017
 * @param <STORAGE> Generic type of storage.
 */
public interface Analyser<STORAGE extends Storage> {

    /**
     * Starts the analysis process.
     *
     * @return Current instance if the analyser for fluent like API.
     * @throws Exception Thrown if an error occurred.
     */
    Analyser doAnalysis() throws Exception;

    /**
     * Gets the result of an analysis process.
     *
     * @return Information storage.
     */
    STORAGE getStorage();
}
