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
package io.varietas.agrestis.imputare.utils.analysis.method;

import io.varietas.agrestis.imputare.annotation.Bean;
import io.varietas.agrestis.imputare.enumerations.BeanScopes;
import io.varietas.agrestis.imputare.utils.analysis.methods.MethodMetaDataExtractionUtils;
import io.varietas.test.environments.model.ConfigurationBeanWithDependency;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <h2>MethodMetaDataExtractionUtilsTest</h2>
 *
 * @author Michael Rhöse
 * @since @version 1.0.0, 7/9/2016
 */
@Slf4j
public class MethodMetaDataExtractionUtilsTest {

    private static Class<?> type;
    private static List<Method> methods;

    @BeforeClass
    public static void beforeClazz() throws IOException, ClassNotFoundException, URISyntaxException, IllegalArgumentException, IllegalAccessException, InstantiationException {

        MethodMetaDataExtractionUtilsTest.type = ConfigurationBeanWithDependency.class;
        MethodMetaDataExtractionUtilsTest.methods = Stream.of(MethodMetaDataExtractionUtilsTest.type.getMethods()).filter(meth -> meth.isAnnotationPresent(Bean.class)).collect(Collectors.toList());
    }

    @Test
    public void getAnnotatedMethods() {
        List<Method> res = MethodMetaDataExtractionUtils.getAnnotatedMethods(MethodMetaDataExtractionUtilsTest.type, Bean.class);

        Assertions.assertThat(res).hasSameSizeAs(MethodMetaDataExtractionUtilsTest.methods);
        LOGGER.info("Located methods count: {} | Expected methods count: {}", res.size(), MethodMetaDataExtractionUtilsTest.methods.size());
        Assertions.assertThat(res).hasSameElementsAs(MethodMetaDataExtractionUtilsTest.methods);

        for (int index = 0; index < res.size(); ++index) {
            Assertions.assertThat(res.get(index).getName()).isEqualTo(MethodMetaDataExtractionUtilsTest.methods.get(index).getName());
            LOGGER.info("Both methods equals ({},{}).", res.get(index).getName(), MethodMetaDataExtractionUtilsTest.methods.get(index).getName());
        }
    }

    @Test
    public void getBeanScope() {
        BeanScopes scope = MethodMetaDataExtractionUtils.getBeanScope(MethodMetaDataExtractionUtilsTest.methods.get(0));
        Assertions.assertThat(scope).isEqualTo(BeanScopes.PROTOTYPE);
        LOGGER.info("Bean scope expected: {} | located: {}", BeanScopes.PROTOTYPE, scope);

        scope = MethodMetaDataExtractionUtils.getBeanScope(MethodMetaDataExtractionUtilsTest.methods.get(1));
        Assertions.assertThat(scope).isEqualTo(BeanScopes.SINGELTON);
        LOGGER.info("Bean scope expected: {} | located: {}", BeanScopes.SINGELTON, scope);
    }

    @Test
    public void getBeanIdentifier() {
        String identifier = MethodMetaDataExtractionUtils.getBeanIdentifier(MethodMetaDataExtractionUtilsTest.methods.get(0));
        Assertions.assertThat(identifier).isEqualTo("pojoBean");
        LOGGER.info("Bean identifier expected: {} | located: {}", "pojoBean", identifier);

        identifier = MethodMetaDataExtractionUtils.getBeanIdentifier(MethodMetaDataExtractionUtilsTest.methods.get(1));
        Assertions.assertThat(identifier).isEqualTo("customPojoNameBean");
        LOGGER.info("Bean identifier expected: {} | located: {}", "customPojoNameBean", identifier);
    }

    @Test
    public void isDependenciesExist() {
        boolean exists = MethodMetaDataExtractionUtils.isDependenciesExist(MethodMetaDataExtractionUtilsTest.methods.get(0));
        Assertions.assertThat(exists).isTrue();
        LOGGER.info("Annotation dependency{}required.", (exists) ? " is " : " is not ");

        exists = MethodMetaDataExtractionUtils.isDependenciesExist(MethodMetaDataExtractionUtilsTest.methods.get(1));
        Assertions.assertThat(exists).isTrue();
        LOGGER.info("Annotation dependency{}required.", (exists) ? " is " : " is not ");
    }

    @Test
    public void getAnnotationPosition() {
        int code = MethodMetaDataExtractionUtils.getAnnotationPosition(MethodMetaDataExtractionUtilsTest.methods.get(0));
        Assertions.assertThat(code).isEqualTo(2);
        LOGGER.info("Annotation position expected: {} | located: {}", 2, code);

        code = MethodMetaDataExtractionUtils.getAnnotationPosition(MethodMetaDataExtractionUtilsTest.methods.get(1));
        Assertions.assertThat(code).isEqualTo(2);
        LOGGER.info("Annotation position expected: {} | located: {}", 2, code);
    }
}
