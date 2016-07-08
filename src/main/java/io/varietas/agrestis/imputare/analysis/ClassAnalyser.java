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
package io.varietas.agrestis.imputare.analysis;

import io.varietas.agrestis.imputare.analysis.container.BeanInformation;
import io.varietas.agrestis.imputare.analysis.container.ConstructorInformation;
import io.varietas.agrestis.imputare.analysis.container.DependencyInformation;
import io.varietas.agrestis.imputare.analysis.container.MethodInformation;
import io.varietas.agrestis.imputare.storage.SortedStorage;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.analysis.methods.MethodMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.annotation.Bean;
import io.varietas.agrestis.imputare.enumeration.BeanScope;
import io.varietas.agrestis.imputare.enumeration.ConstructorTypes;
import io.varietas.agrestis.imputare.error.DuplicatedIdentifierException;
import io.varietas.agrestis.imputare.error.ToManyInjectedConstructorsException;
import io.varietas.agrestis.imputare.storage.SortedBeanInformationStorage;
import io.varietas.agrestis.imputare.utils.analysis.constructor.ConstructorMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.container.Pair;
import io.varietas.agrestis.imputare.utils.analysis.dependency.DependencyMetaDataExtractionUtils;
import io.varietas.agrestis.imputare.utils.analysis.fields.FieldMetaDataExtractorUtils;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;
import java8.util.Objects;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>ClassAnalyser</h1>
 *
 * @author Michael Rhöse
 * @since Fr, Jul 1, 2016
 */
public class ClassAnalyser {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClassAnalyser.class);

    private final SortedBeanInformationStorage sortedBeanInformationStorage;
    private final SortedStorage<Integer, Class<?>> sortedClassesStorage;

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassAnalyser(SortedStorage<Integer, Class<?>> storage) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        this.sortedBeanInformationStorage = new SortedBeanInformationStorage();
        this.sortedClassesStorage = storage;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ClassAnalyser doAnalysis() throws ToManyInjectedConstructorsException, NoSuchMethodException, IOException {

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
                    throw new InternalException("An internal error occured while storing a new entry.");
                }

                if (status == -2) {
                    throw new DuplicatedIdentifierException("Critical error occured. COntect initialising abourted.");
                }
            }

            next = this.sortedClassesStorage.next(ClassMetaDataExtractionUtils.AnnotationCodes.CONFIGURATION);
        }
        return this;
    }

    private ClassAnalyser doBeanAnalysis() throws ToManyInjectedConstructorsException, NoSuchMethodException, IOException, DuplicatedIdentifierException, InternalException {

        for (Integer annotationType : this.sortedClassesStorage.getStorage().keySet()) {

            ///< Skip type category is is there no entry
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
                    throw new DuplicatedIdentifierException("Critical error occured. COntect initialising abourted.");
                }

                next = this.sortedClassesStorage.next(annotationType);
            }
        }

        return this;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private BeanInformation createMethodInformation(final Method method, Class<?> parent) {
        ///< Bean meta data
        Class<?> beanType = method.getReturnType();
        BeanScope scope = MethodMetaDataExtractionUtils.getBeanScope(method);
        String identifier = MethodMetaDataExtractionUtils.getBeanIdentifier(method);
        MethodInformation methodInformation = null;

        ///< Depednency meta data
        if (MethodMetaDataExtractionUtils.isDependenciesExist(method)) {
            try {
                ///< Collect Dependencies
                methodInformation = new MethodInformation(parent, method, DependencyMetaDataExtractionUtils.getDependenciesWithIdentifier(method));
            } catch (IOException ex) {
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
        }

        return new BeanInformation(methodInformation, scope, identifier, beanType);
    }

    private BeanInformation createClassInformation(final Class<?> beanType, final Integer annotationType) throws ToManyInjectedConstructorsException, NoSuchMethodException, IOException {
        ///< Bean meta data
        BeanScope scope = ClassMetaDataExtractionUtils.getBeanScope(beanType, annotationType);
        String identifier = ClassMetaDataExtractionUtils.getBeanIdentifier(beanType, annotationType);

        ///< Constructor analysis
        Pair<ConstructorTypes, Constructor> chosenConstructor = ConstructorMetaDataExtractionUtils.chooseConstructor(beanType);

        ConstructorInformation constructorInformation = null;

        if (!Objects.equals(chosenConstructor.getValue1(), ConstructorTypes.STANDARD)) {
            try {
                ///< Constructor parameter analysis
                constructorInformation = new ConstructorInformation(chosenConstructor.getValue2(), DependencyMetaDataExtractionUtils.getDependenciesWithIdentifier(chosenConstructor.getValue2()));
            } catch (IOException ex) {
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
        }

        DependencyInformation[] dependencies = null;
        ///< Bean field analysis
        if (FieldMetaDataExtractorUtils.isDependenciesExist(beanType)) {
            dependencies = DependencyMetaDataExtractionUtils.getDependenciesWithIdentifier(beanType);
        }

        return new BeanInformation(constructorInformation, scope, identifier, beanType, dependencies);
    }
}
