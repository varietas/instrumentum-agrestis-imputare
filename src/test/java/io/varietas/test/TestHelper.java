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
package io.varietas.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>TestHelper</h1>
 *
 * @author Michael Rhöse
 * @since Di, Jun 28, 2016
 */
public class TestHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestHelper.class);

    public static Optional<Object> invokeMethod(URLClassLoader classLoader, Method method, Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (method.getModifiers() != Modifier.PUBLIC) {
            method.setAccessible(true);
        }

        Object returnValue = method.invoke(classLoader, params);
        LOGGER.trace(String.format("Method '%s' on object '%s' invoked successful. Following parameters used:", method.getName(), classLoader.getClass().getSimpleName()));
        Arrays.asList(params).forEach(param -> LOGGER.trace(String.format("Parameter: %s", param.getClass().getSimpleName())));

        return Optional.ofNullable(returnValue);
    }
}
