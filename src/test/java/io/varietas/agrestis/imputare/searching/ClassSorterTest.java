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
package io.varietas.agrestis.imputare.searching;

import io.varietas.agrestis.imputare.storage.SortedStorage;
import io.varietas.agrestis.imputare.storage.UnsortedStorage;
import io.varietas.agrestis.imputare.utils.analysis.classes.ClassMetaDataExtractionUtils;
import io.varietas.test.TestHelper;
import java.io.IOException;
import java.net.URISyntaxException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>ClassSorterTest</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Jul 9, 2016
 */
public class ClassSorterTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassSorterTest.class);

    private static ClassCollector classCollector;
    private static ClassSorter classSorter;

    private int count;

    @BeforeClass
    public static void ontimeSetUp() throws IOException, ClassNotFoundException, URISyntaxException, IllegalArgumentException, IllegalAccessException, InstantiationException {

        ClassSorterTest.classCollector = new ClassCollector(TestHelper.class.getPackage());
        UnsortedStorage storage = classCollector.collectAnnotatedClazzes().getStorage();
        ClassSorterTest.classSorter = new ClassSorter(storage);
        ClassSorterTest.classSorter.sortLocatedClazzes();

        for (Object clazz : classCollector.collectAnnotatedClazzes().getStorage().getStorage()) {
            LOGGER.info("Class: {}", ((Class<?>) clazz).getName());
        }
    }

    @Before
    public void setUp() {
        this.count = 0;
    }

    @Test
    public void categoryCreation() throws IOException, ClassNotFoundException, URISyntaxException {
        UnsortedStorage storage = ClassSorterTest.classCollector.collectAnnotatedClazzes().getStorage();

        int count = storage.getStorage().size();

        Assertions.assertThat(count).isEqualTo(17);
        LOGGER.info("Located classes: {}", count);

        count = ClassSorterTest.classSorter.getStorage().getStorage().size();
        Assertions.assertThat(count).isEqualTo(5);
        LOGGER.info("Lists for types: {}", count);
    }

    @Test
    public void sortAnnotatedClassesForRepository() {

        SortedStorage<Integer, Class<?>> sortedStorage = ClassSorterTest.classSorter.getStorage();

        count = sortedStorage.getStorage().get(ClassMetaDataExtractionUtils.AnnotationCodes.REPOSITORY).size();
        Assertions.assertThat(count).isEqualTo(0);
        LOGGER.info("{} classes for {} stored.", count, ClassMetaDataExtractionUtils.AnnotationCodes.REPOSITORY);
    }

    @Test
    public void sortAnnotatedClassesForService() {

        SortedStorage<Integer, Class<?>> sortedStorage = ClassSorterTest.classSorter.getStorage();

        count = sortedStorage.getStorage().get(ClassMetaDataExtractionUtils.AnnotationCodes.SERVICE).size();
        Assertions.assertThat(count).isEqualTo(6);
        LOGGER.info("{} classes for {} stored.", count, ClassMetaDataExtractionUtils.AnnotationCodes.SERVICE);
    }

    @Test
    public void sortAnnotatedClassesForController() {

        SortedStorage<Integer, Class<?>> sortedStorage = ClassSorterTest.classSorter.getStorage();

        count = sortedStorage.getStorage().get(ClassMetaDataExtractionUtils.AnnotationCodes.CONTROLLER).size();
        Assertions.assertThat(count).isEqualTo(0);
        LOGGER.info("{} classes for {} stored.", count, ClassMetaDataExtractionUtils.AnnotationCodes.CONTROLLER);
    }

    @Test
    public void sortAnnotatedClassesForComponent() {

        SortedStorage<Integer, Class<?>> sortedStorage = ClassSorterTest.classSorter.getStorage();

        count = sortedStorage.getStorage().get(ClassMetaDataExtractionUtils.AnnotationCodes.COMPONENT).size();
        Assertions.assertThat(count).isEqualTo(6);
        LOGGER.info("{} classes for {} stored.", count, ClassMetaDataExtractionUtils.AnnotationCodes.COMPONENT);
    }

    @Test
    public void sortAnnotatedClassesForConfiguration() {

        SortedStorage<Integer, Class<?>> sortedStorage = ClassSorterTest.classSorter.getStorage();

        count = sortedStorage.getStorage().get(ClassMetaDataExtractionUtils.AnnotationCodes.CONFIGURATION).size();
        Assertions.assertThat(count).isEqualTo(5);
        LOGGER.info("{} classes for {} stored.", count, ClassMetaDataExtractionUtils.AnnotationCodes.CONFIGURATION);
    }
}
