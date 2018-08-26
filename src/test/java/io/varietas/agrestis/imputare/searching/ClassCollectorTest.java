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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <h2>ClassCollectorTest</h2>
 *
 * @author Michael Rhöse
 * @version 1.0.0.0, 6/28/2016
 */
@Slf4j
public class ClassCollectorTest {

    private Path tempJar;
    private URLClassLoader classLoader;
    
    @Before
    public void setup() throws IOException{
        this.tempJar = Files.createTempFile("lib-with-beans", ".jar");
        Files.copy(this.getClass().getResourceAsStream("/bins/lib-with-beans.jar"), this.tempJar, StandardCopyOption.REPLACE_EXISTING);
        
        this.classLoader = new URLClassLoader(new URL[]{tempJar.toUri().toURL()});
    }
    
    @After
    public void clean() throws IOException{
        this.classLoader.close();
        Files.deleteIfExists(this.tempJar);
    }
    
    @Test
    public void collectAnnotatedClazzes() throws MalformedURLException {
        
        ClassCollector classCollector = new ClassCollector()
            .additionalClassLoader(this.classLoader)
            .addOther("-io.varietas.test.environments");
        
        UnsortedStorage storage = classCollector.collectAnnotatedClazzes().getStorage();
        
        int count = storage.getStorage().size();
        
        LOGGER.info("Located classes: {}", count);
        
        Assertions.assertThat(count).isEqualTo(5);
        
        for (Object clazz : storage.getStorage()) {
            LOGGER.info("Class: {}", ((Class<?>) clazz).getName());
        }
    }
}
