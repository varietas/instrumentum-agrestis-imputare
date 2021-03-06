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
package io.varietas.mobile.agrestis.imputare.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * <h2>BeanScanUtilsTests</h2>
 *
 * @author Michael Rhöse
 * @since Di, Mai 10, 2016
 */
@Slf4j
@RunWith(JUnit4.class)
@Ignore
public class BeanScanUtilsTests {

//    @Test
//    public void getBeanIdentifierFromFieldCustomIdentifier() throws NoSuchFieldException {
//        Field field = SimpleBeanWithAnnotatedField1.class.getDeclaredField("intValue");
//
//        Assertions.assertThat(field).isNotNull();
//        LOGGER.info(String.format("Field '%s' with autowired annotation located.", field.getName()));
//
//        String expectedIdentifier = "intBeanValue";
//        String identifier = BeanScanUtils.getBeanIdentifier(field);
//
//        Assertions.assertThat(identifier).isEqualTo(expectedIdentifier);
//        LOGGER.info(String.format("Bean identifier '%s' | expected '%s'.", identifier, expectedIdentifier));
//    }
//
//    @Test
//    public void getBeanIdentifierFromFieldDefaultIdentifier() throws NoSuchFieldException {
//        Field field = SimpleBeanWithAnnotatedField2.class.getDeclaredField("intValue");
//
//        Assertions.assertThat(field).isNotNull();
//        LOGGER.info(String.format("Field '%s' with autowired annotation located.", field.getName()));
//
//        String expectedIdentifier = "intValue";
//        String identifier = BeanScanUtils.getBeanIdentifier(field);
//
//        Assertions.assertThat(identifier).isEqualTo(expectedIdentifier);
//        LOGGER.info(String.format("Bean identifier '%s' | expected '%s'.", identifier, expectedIdentifier));
//    }
//
//    @Test
//    public void getBeanIdentifierFromParamterCustomIdentifier() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//
//        Parameter[] parameters = SimpleBeanWithAnnotatedConstructor1.class.getConstructor(int.class).getParameters();
//
//        Assertions.assertThat(parameters).hasSize(1);
//        LOGGER.info(String.format("'%d' parameters located.", parameters.length));
//
//        String expectedIdentifier = "intBeanValue";
//        String identifier = BeanScanUtils.getBeanIdentifier(parameters[0]);
//
//        Assertions.assertThat(identifier).isEqualTo(expectedIdentifier);
//        LOGGER.info(String.format("Bean identifier '%s' | expected '%s'.", identifier, expectedIdentifier));
//    }
//
//    @Test
//    public void getBeanIdentifiersFromConstructorCustomIdentifier() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//
//        Constructor constructor = SimpleBeanWithAnnotatedConstructor2.class.getConstructor(int.class);
//
//        Assertions.assertThat(constructor).isNotNull();
//        LOGGER.info(String.format("Constructor '%s' with %d parameters located.", constructor.getName(), constructor.getParameterCount()));
//
//        String expectedIdentifier = "intBeanValue";
//        String identifier = BeanScanUtils.getBeanIdentifiers(constructor)[0];
//
//        Assertions.assertThat(identifier).isEqualTo(expectedIdentifier);
//        LOGGER.info(String.format("Bean identifier '%s' | expected '%s'.", identifier, expectedIdentifier));
//    }
//
//    @Test
//    public void getBeanScope() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//        Class<?> clazz = AllAnnotationsBeanClass.class;
//        LOGGER.info(String.format("Class '%s' for bean scope tests used.", clazz.getSimpleName()));
//
//        this.assertAndLogClazzAnnotation(clazz, Configuration.class, BeanScope.SINGELTON);
//        this.assertAndLogClazzAnnotation(clazz, Service.class, BeanScope.SINGELTON);
//        this.assertAndLogClazzAnnotation(clazz, Component.class, BeanScope.PROTOTYPE);
//
//        this.assertAndLogFieldAnnotation(clazz, Bean.class, (short) 2);
//    }
//
//    @Test
//    public void getBeanAnnotation() throws IOException, URISyntaxException {
//        this.assertAndLogAnnotation(SimpleConfigurationBean1.class, Configuration.class);
//        this.assertAndLogAnnotation(SimpleServiceBean1.class, Service.class);
//        this.assertAndLogAnnotation(SimpleComponentBean1.class, Component.class);
//    }
//
//    @Test
//    public void getSpecifiedConstructor() {
//        this.assertAndLogConstructor(SimpleParameterisedConstructor.class, ConstructorTypes.PARAMETERISED, false);
//        this.assertAndLogConstructor(SimpleCopyConstructor.class, ConstructorTypes.COPY, true);
//        this.assertAndLogConstructor(SimpleStandardConstructor.class, ConstructorTypes.STANDARD, true);
//    }
//
//    private void assertAndLogConstructor(final Class<?> clazz, final ConstructorTypes constructorType, final boolean isExpectedPresent) {
//
//        Optional constructor = BeanScanUtils.getSpecifiedConstructor(clazz, constructorType);
//
//        Assertions.assertThat(constructor.isPresent()).isEqualTo(isExpectedPresent);
//        LOGGER.info(String.format("ConstructorTypes.%s for '%s' is%spresent | Expected is%spresent", constructorType.name(), clazz.getSimpleName(), (constructor.isPresent()) ? " " : " not ", clazz.getSimpleName(), (isExpectedPresent) ? " " : " not "));
//    }
//
//    private void assertAndLogAnnotation(final Class<?> clazz, Class<? extends Annotation> expectedAnnotation) throws IOException, URISyntaxException {
//
////        Annotation annotation = BeanScanUtils.getBeanAnnotation(clazz);
////        Assertions.assertThat(annotation).isNotNull();
////        Assertions.assertThat(annotation.annotationType()).isEqualTo(expectedAnnotation);
////        LOGGER.info(String.format("Annotation '%s' located | Annotation '%s' expected", annotation.annotationType().getSimpleName(), expectedAnnotation.getSimpleName()));
//    }
//
//    private void assertAndLogClazzAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotationClass, final BeanScope expectedScope) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//        BeanScope scope = BeanScanUtils.getBeanScope(clazz.getAnnotation(annotationClass));
//        Assertions.assertThat(scope).isEqualTo(expectedScope);
//        LOGGER.info(String.format("BeanScope.%s located in annotation '%s', expected BeanScops.%s", scope.name(), annotationClass.getSimpleName(), expectedScope.name()));
//    }
//
//    private void assertAndLogFieldAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotationClass, short expectedMethodCount) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//        short methodCount = 0;
//        for (Method method : clazz.getMethods()) {
//            if (!method.isAnnotationPresent(Bean.class)) {
//                continue;
//            }
//            BeanScope scope = BeanScanUtils.getBeanScope(method.getAnnotation(annotationClass));
//
//            Assertions.assertThat(scope).isIn((Object[]) BeanScope.values());
//            methodCount++;
//            LOGGER.info(String.format("%d: BeanScope.%s located in annotation '%s'.", methodCount, scope.name(), annotationClass.getSimpleName()));
//        }
//
//        Assertions.assertThat(methodCount).overridingErrorMessage(String.format("%d annotated methods located | %d annotated methods expected.", methodCount, expectedMethodCount)).isEqualTo(expectedMethodCount);
//        LOGGER.info(String.format("%d annotated methods located | %d annotated methods expected.", methodCount, expectedMethodCount));
//    }
}
