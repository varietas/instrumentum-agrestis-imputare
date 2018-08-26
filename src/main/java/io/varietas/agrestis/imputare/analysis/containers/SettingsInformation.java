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
package io.varietas.agrestis.imputare.analysis.containers;

import io.varietas.agrestis.imputare.analysis.InformationType;
import lombok.Getter;

/**
 * <h2>SettingsInformation</h2>
 *
 * The settings information contains which are required for a application component. These information can be database settings or something like this.
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 09/13/2017
 */
@Getter
public class SettingsInformation implements Information {

    private final Class<?> parent;
    private final String file;
    private final String path;
    private final boolean inClassPath;

    private final String identifier;
    private final Class<?> type;
    private final InformationType informationType;

    public SettingsInformation(final Class<?> parent, final String file, final String path, final boolean inClassPath) {
        this.parent = parent;
        this.file = file;
        this.path = path;
        this.inClassPath = inClassPath;

        this.identifier = "";
        this.type = null;
        this.informationType = InformationType.SETTINGS;
    }
}
