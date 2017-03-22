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
package io.varietas.agrestis.imputare.utils;

import io.varietas.agrestis.imputare.utils.analysis.classes.ClassLoadUtils;
import io.varietas.test.TestHelper;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.UnexpectedException;
import java.util.List;
import java8.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

/**
 * <h2>ClassLoadUtilsTest</h2>
 *
 * @author Michael Rhöse
 * @since Di, Jun 28, 2016
 */
@Slf4j
public class ClassLoadUtilsTest {

    @Before
    public void setUp() throws UnexpectedException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        URL jarUrl = this.getClass().getResource("/bins/http-server-plugin.jar");
        Optional<URLClassLoader> appClassLoader = Optional.ofNullable(((URLClassLoader) this.getClass().getClassLoader()));

        if (!appClassLoader.isPresent()) {
            throw new UnexpectedException("Class loader not available.");
        }

        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);

        TestHelper.invokeMethod(appClassLoader.get(), method, jarUrl);
    }

    @Test
    public void getRootUrls() {
        LOGGER.info("Test started.");
        List<URL> urls = ClassLoadUtils.getRootUrls(Thread.currentThread().getContextClassLoader());

        Assertions.assertThat(urls).isNotEmpty();

        for (URL url : urls) {
            LOGGER.info("URL: {}", url.toString());
        }
    }
}
