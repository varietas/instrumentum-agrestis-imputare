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

import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleComponentBean1;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleComponentBean2;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleComponentBean3;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleConfigurationBean1;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleConfigurationBean2;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleConfigurationBean3;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleServiceBean1;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleServiceBean2;
import io.varietas.mobile.agrestis.imputare.environments.model.utilssimple.SimpleServiceBean3;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * <h1>DIUtilsTests</h1>
 *
 * @author Michael Rhöse
 * @since Di, Mai 10, 2016
 */
public class DIUtilsTests {

    private static final Logger LOGGER = Logger.getLogger(DIUtilsTests.class.getSimpleName());

    @Test
    public void searchClassesFromPackage() throws URISyntaxException, IOException {
        
        List<Class<?>> testClazzes = new ArrayList<Class<?>>(){{
            add(SimpleComponentBean1.class);
            add(SimpleComponentBean2.class);
            add(SimpleComponentBean3.class);
            add(SimpleConfigurationBean1.class);
            add(SimpleConfigurationBean2.class);
            add(SimpleConfigurationBean3.class);
            add(SimpleServiceBean1.class);
            add(SimpleServiceBean2.class);
            add(SimpleServiceBean3.class);
        }};
        
        LOGGER.info(String.format("Package '%s' will scanned.", testClazzes.get(0).getPackage().toString()));
        List<Class<?>> clazzList = DIUtils.searchClassesFromPackage(testClazzes.get(0).getPackage());
        
        Assertions.assertThat(clazzList).hasSize(9);
        LOGGER.info(String.format("%d classes located", clazzList.size()));
        Assertions.assertThat(clazzList).hasSameElementsAs(testClazzes);
        LOGGER.info("List of located classes contains all expected classes.");
    }
}
