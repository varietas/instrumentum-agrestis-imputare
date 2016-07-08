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

import io.varietas.test.TestHelper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.UnexpectedException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>ClassCollectorTest</h1>
 *
 * @author Michael Rhöse
 * @since Di, Jun 28, 2016
 */
public class ClassCollectorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassCollectorTest.class);

    @Before
    public void setUp() throws UnexpectedException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        URL jarUrl = this.getClass().getResource("/bins/lib-with-beans.jar");
        Optional<URLClassLoader> appClassLoader = Optional.ofNullable(((URLClassLoader) this.getClass().getClassLoader()));

        if (!appClassLoader.isPresent()) {
            throw new UnexpectedException("Class loader not available.");
        }

        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);

        TestHelper.invokeMethod(appClassLoader.get(), method, jarUrl);
    }

    @Test
    public void collectAnnotatedClazzes() throws IOException, ClassNotFoundException, URISyntaxException {

        ClassCollector classCollector = new ClassCollector(TestHelper.class.getPackage());

        for (Object clazz : classCollector.collectAnnotatedClazzes().getClazzStorage().getStorage()) {
            LOGGER.info("Class: {}", ((Class<?>) clazz).getName());
        }
    }
}
