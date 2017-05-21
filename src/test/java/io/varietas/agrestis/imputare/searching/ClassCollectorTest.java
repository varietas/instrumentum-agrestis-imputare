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
package io.varietas.agrestis.imputare.searching;

import io.varietas.instrumentum.simul.storage.UnsortedStorage;
import io.varietas.test.TestHelper;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.UnexpectedException;
import java.util.Arrays;
import java8.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h2>ClassCollectorTest</h2>
 *
 * @author Michael Rhöse
 * @since Di, Jun 28, 2016
 */
public class ClassCollectorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassCollectorTest.class);

    @Before
    public void setUp() throws UnexpectedException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        URL jarUrl = this.getClass().getResource("/bins/lib-with-beans.jar");

        Optional<URLClassLoader> urlClassLoader = Optional.ofNullable(((URLClassLoader) ClassLoader.getSystemClassLoader()));

        if (!urlClassLoader.isPresent()) {
            throw new UnexpectedException("Class loader not available.");
        }

        this.printClasspath();

        Class urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(urlClassLoader.get(), new Object[]{jarUrl});

        this.printClasspath();
    }

    private void printClasspath() {
        String classpath = System.getProperty("java.class.path");
        Arrays.asList(classpath.split(File.pathSeparator)).forEach(LOGGER::info);
    }

    @Test
    public void collectAnnotatedClazzes() throws IOException, ClassNotFoundException, URISyntaxException {

        ClassCollector classCollector = new ClassCollector(TestHelper.class.getPackage());

        UnsortedStorage storage = classCollector.collectAnnotatedClazzes().getStorage();

        int count = storage.getStorage().size();

        LOGGER.info("Located classes: {}", count);

        Assertions.assertThat(count).isEqualTo(17);

        for (Object clazz : storage.getStorage()) {
            LOGGER.info("Class: {}", ((Class<?>) clazz).getName());
        }
    }
}
