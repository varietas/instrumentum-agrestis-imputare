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

import io.varietas.agrestis.imputare.analysis.containers.ResourceInformation;
import io.varietas.agrestis.imputare.analysis.containers.SettingsInformation;
import io.varietas.agrestis.imputare.analysis.factories.ResourceInformationFactory;
import io.varietas.agrestis.imputare.annotation.resources.File;
import io.varietas.agrestis.imputare.annotation.resources.Resource;
import io.varietas.agrestis.imputare.annotation.resources.Settings;
import io.varietas.agrestis.imputare.storage.resources.SortedResourceInformationStorage;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import io.varietas.instrumentum.simul.storage.SortedStorage;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>ResourceAnalyser</h2>
 *
 * The resource analyser is used to search values which are required to start an agrestis imputare based application. After collecting of values from config/settings files and methods, the values got
 * stored and used in injection process.
 *
 * @author Michael Rhöse
 * @version 2.1.0, 07/18/2017
 */
@Slf4j
public class ResourceAnalyser implements Analyser<SortedStorage<Boolean, Object>> {

    private final SortedStorage<Integer, Class<?>> sortedClassesStorage;
    private final SortedStorage<Boolean, Object> resourceInformationStorage;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ResourceAnalyser(final SortedStorage<Integer, Class<?>> sortedClassesStorage) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        this.sortedClassesStorage = sortedClassesStorage;
        this.resourceInformationStorage = new SortedResourceInformationStorage();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Starts the resource and settings analyse process for collected classes.
     *
     * @return Current instance if the analyser for fluent like API.
     */
    @Override
    public final ResourceAnalyser doAnalysis() {

        this
            ///< Do resources analysis
            .initResourceCollecting()
            ///< Do settings files analysis
            .initSettingsCollecting();

        return this;
    }

    @Override
    public SortedStorage<Boolean, Object> getStorage() {
        return this.resourceInformationStorage;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ResourceAnalyser initResourceCollecting() {

        ///< Search all Resource annotated methods to collect programmatically created values.
        this.sortedClassesStorage.getStorage().values()
            .stream()
            .flatMap(List::stream)
            .filter(clazz -> Stream.of(clazz.getMethods()).anyMatch(method -> method.isAnnotationPresent(Resource.class)))
            .forEach(parent -> {
                Stream.of(parent.getMethods())
                    .filter(method -> method.isAnnotationPresent(Resource.class))
                    .map(method -> {
                        return this.createResourceInformation(parent, method);
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(information -> this.resourceInformationStorage.store(information, Boolean.FALSE));
            });

        return this;
    }

    private ResourceAnalyser initSettingsCollecting() {

        ///< Search all settings files to extract settings.
        Optional<Class<?>> next = this.sortedClassesStorage.next(ClassMetaDataExtractionUtils.AnnotationCodes.SETTINGS);

        while (next.isPresent()) {
            final Class<?> parent = next.get();
            final Settings settings = parent.getAnnotation(Settings.class);

            Stream.of(settings.files())
                .map(file -> this.createSettingsInformation(parent, file))
                .forEach(information -> this.resourceInformationStorage.store(information, Boolean.TRUE));

            next = this.sortedClassesStorage.next(ClassMetaDataExtractionUtils.AnnotationCodes.SETTINGS);
        }
        return this;
    }

    private Optional<ResourceInformation> createResourceInformation(final Class<?> parent, final Method method) {
        try {
            return Optional.of(new ResourceInformationFactory().parent(parent).method(method).get());
        } catch (Exception ex) {
            LOGGER.error(ex.getLocalizedMessage());
        }
        return Optional.empty();
    }

    private SettingsInformation createSettingsInformation(final Class<?> parent, final File file) {
        return new SettingsInformation(parent, file.file(), file.path());
    }
    // TODO: Write settings file extraction utils.
    // TODO: create ManagedValue
    //  - Get type of value
    //  - If value a dynamic value, a container is required to store them.
}
