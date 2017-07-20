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
package io.varietas.agrestis.imputare.analysis;

import io.varietas.agrestis.imputare.analysis.containers.BeanInformation;
import io.varietas.agrestis.imputare.analysis.containers.ConstructorInformation;
import io.varietas.agrestis.imputare.analysis.containers.MethodInformation;
import io.varietas.agrestis.imputare.analysis.factories.BeanInformationFactory;
import io.varietas.agrestis.imputare.analysis.factories.ConstructorInformationFactory;
import io.varietas.agrestis.imputare.analysis.factories.MethodInformationFactory;
import io.varietas.instrumentum.simul.storage.SortedStorage;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.analysis.methods.MethodMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.annotation.Bean;
import io.varietas.agrestis.imputare.annotation.Configuration;
import io.varietas.agrestis.imputare.enumerations.ConstructorTypes;
import io.varietas.agrestis.imputare.error.ConstructorAccessException;
import io.varietas.agrestis.imputare.error.DuplicatedIdentifierException;
import io.varietas.agrestis.imputare.error.InternalException;
import io.varietas.agrestis.imputare.error.StorageInitialisingException;
import io.varietas.agrestis.imputare.error.ToManyInjectedConstructorsException;
import io.varietas.agrestis.imputare.storage.SortedBeanInformationStorage;
import io.varietas.agrestis.imputare.utils.analysis.constructors.ConstructorMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.containers.Pair;
import io.varietas.agrestis.imputare.utils.analysis.dependency.DependencyMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.analysis.fields.FieldMetaDataExtractorUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * <h2>ClassAnalyser</h2>
 *
 * <p>
 * The class analyser iterates over all annotated classes and collects the relevant information. The analyse process is separated in two independent tasks. The first task analyses all methods of
 * {@link Configuration.class} annotated classes. Normally beans will be created in configuration classes which are important and/or used in other beans.</p>
 * <p>
 * The second task is for analysing of "normal" bean classes annotated with one of the other available annotations.</p>
 *
 *
 * @author Michael Rhöse
 * @version 1.0.0, 7/1/2016
 */
public final class ClassAnalyser {

    private final SortedBeanInformationStorage sortedBeanInformationStorage;
    private final SortedStorage<Integer, Class<?>> sortedClassesStorage;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassAnalyser(SortedStorage<Integer, Class<?>> storage) throws StorageInitialisingException {
        this.sortedBeanInformationStorage = new SortedBeanInformationStorage();
        this.sortedClassesStorage = storage;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Starts the bean analyse process for collected classes.
     *
     * @return Current instance if the analyser for fluent like API.
     * @throws ToManyInjectedConstructorsException Thrown if a class has more than one injected constructor. Agrestis imputare can not analyse which constructor has to be used.
     * @throws ConstructorAccessException Thrown if a bean producing method isn't accessible.
     * @throws io.varietas.agrestis.imputare.error.InternalException
     */
    public final ClassAnalyser doAnalysis() throws ToManyInjectedConstructorsException, DuplicatedIdentifierException, InternalException, ConstructorAccessException {

        ///< Do configuration class analysis
        this.doMethodBeanAnalysis();

        ///< Do bean class analysis
        this.doBeanAnalysis();

        return this;
    }

    public SortedBeanInformationStorage getStorage() {
        return this.sortedBeanInformationStorage;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ClassAnalyser doMethodBeanAnalysis() throws DuplicatedIdentifierException, InternalException {

        Optional<Class<?>> next = this.sortedClassesStorage.next(ClassMetaDataExtractionUtils.AnnotationCodes.CONFIGURATION);

        while (next.isPresent()) {

            for (Method method : MethodMetaDataExtractionUtils.getAnnotatedMethods(next.get(), Bean.class)) {
                int status = this.sortedBeanInformationStorage.store(this.createMethodInformation(method, next.get()), ClassMetaDataExtractionUtils.AnnotationCodes.CONFIGURATION);

                if (status == -1) {
                    throw new InternalException("An internal error occured while storing a new entry. New entry not stored.");
                }

                if (status == -2) {
                    throw new DuplicatedIdentifierException("Critical error occured. Context initialising abourted. An bean information with this identifier already exists. (" + method.getName() + ")");
                }
            }

            next = this.sortedClassesStorage.next(ClassMetaDataExtractionUtils.AnnotationCodes.CONFIGURATION);
        }
        return this;
    }

    private ClassAnalyser doBeanAnalysis() throws ToManyInjectedConstructorsException, DuplicatedIdentifierException, InternalException, ConstructorAccessException {

        for (Integer annotationType : this.sortedClassesStorage.getStorage().keySet()) {

            ///< Skip type category if is there no entry
            if (this.sortedClassesStorage.isEmpty(annotationType)) {
                continue;
            }

            Optional<Class<?>> next = this.sortedClassesStorage.next(annotationType);

            while (next.isPresent()) {
                int status = this.sortedBeanInformationStorage.store(this.createClassInformation(next.get(), annotationType), annotationType);

                if (status == -1) {
                    throw new InternalException("An internal error occured while storing a new entry.");
                }

                if (status == -2) {
                    throw new DuplicatedIdentifierException("Critical error occured. Contect initialising abourted.");
                }

                next = this.sortedClassesStorage.next(annotationType);
            }
        }

        return this;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private BeanInformation createMethodInformation(final Method method, Class<?> parent) {
        ///< Bean meta data
        ///< Depednency meta data
        ///< Collect Dependencies
        final MethodInformation methodInformation = new MethodInformationFactory()
            .setMethod(method)
            .setParent(parent)
            .setDependencyOperator(DependencyMetaDataExtractionUtils::getDependenciesWithIdentifier)
            .get();

        return new BeanInformationFactory()
            .setCreationInformation(methodInformation)
            .setScope(MethodMetaDataExtractionUtils.getBeanScope(method))
            .setIdentifier(MethodMetaDataExtractionUtils.getBeanIdentifier(method))
            .setType(method.getReturnType())
            .get();
    }

    private BeanInformation createClassInformation(final Class<?> beanType, final Integer annotationType) throws ToManyInjectedConstructorsException, ConstructorAccessException {

        ///< Constructor analysis
        Pair<ConstructorTypes, Constructor> chosenConstructor = ConstructorMetaDataExtractionUtils.chooseConstructor(beanType);

        ///< Constructor parameter analysis
        ConstructorInformation constructorInformation = new ConstructorInformationFactory()
            .setConstructor(chosenConstructor.getValue2())
            .setDependencyOperator(DependencyMetaDataExtractionUtils::getDependenciesWithIdentifier)
            .get();

        BeanInformationFactory informationFactory = new BeanInformationFactory();

        ///< Bean field analysis
        if (FieldMetaDataExtractorUtils.isDependenciesExist(beanType)) {
            informationFactory.setDependencyOperator(DependencyMetaDataExtractionUtils::getDependenciesWithIdentifier);
        }

        return informationFactory
            .setCreationInformation(constructorInformation)
            .setScope(ClassMetaDataExtractionUtils.getBeanScope(beanType, annotationType))
            .setIdentifier(ClassMetaDataExtractionUtils.getBeanIdentifier(beanType, annotationType))
            .setType(beanType)
            .get();
    }
}
