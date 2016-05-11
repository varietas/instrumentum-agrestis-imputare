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

package io.varietas.mobile.agrestis.imputare.utils;

import io.varietas.mobile.agrestis.imputare.TestApplication;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

/**
 * <h1>DIUtilsTests</h1>
 *
 * @author Michael Rhöse
 * @since Di, Mai 10, 2016
 */
public class DIUtilsTests {

    private static final Logger LOGGER = Logger.getLogger(DIUtilsTests.class.getName());
    
    public void searchClassesFromPackage() throws URISyntaxException, IOException{
        
        Path packagePath = Paths.get(Thread.currentThread().getContextClassLoader().getResource(TestApplication.class.getPackage().toString()).toURI());
        List<Class<?>> locatedClazzes = DIUtils.searchClassesFromPackage(packagePath);
        
    }
}
