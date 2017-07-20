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

/**
 * <h2>DIUtilsTests</h2>
 *
 * @author Michael Rhöse
 * @since Di, Mai 10, 2016
 */
@Slf4j
@Ignore
public class DIUtilsTests {

//    @Test
//    public void searchClassesFromPackage() throws URISyntaxException, IOException {
//
//        List<Class<?>> testClazzes = new ArrayList<Class<?>>() {
//            {
//                add(SimpleComponentBean1.class);
//                add(SimpleComponentBean2.class);
//                add(SimpleComponentBean3.class);
//                add(SimpleConfigurationBean1.class);
//                add(SimpleConfigurationBean2.class);
//                add(SimpleConfigurationBean3.class);
//                add(SimpleServiceBean1.class);
//                add(SimpleServiceBean2.class);
//                add(SimpleServiceBean3.class);
//            }
//        };
//
//        LOGGER.info(String.format("Package '%s' will scanned.", testClazzes.get(0).getPackage().toString()));
//        List<Class<?>> clazzList = DIUtils.searchClassesFromPackage(testClazzes.get(0).getPackage());
//
//        Assertions.assertThat(clazzList).hasSize(9);
//        LOGGER.info(String.format("%d classes located", clazzList.size()));
//        Assertions.assertThat(clazzList).hasSameElementsAs(testClazzes);
//        LOGGER.info("List of located classes contains all expected classes.");
//    }
//
//    @Test(expected = ToManyInjectedConstructorsException.class)
//    public void getConstructorToMannyInjectedConstructor() throws ToManyInjectedConstructorsException, NoSuchMethodException {
//        DIUtils.getConstructor(ToManyInjectionsClass1.class);
//    }
//
//    @Test(expected = ToManyInjectedConstructorsException.class)
//    public void getConstructorToMannyInjectedParameter() throws ToManyInjectedConstructorsException, NoSuchMethodException {
//        DIUtils.getConstructor(ToManyInjectionsClass2.class);
//    }
//
//    @Test
//    public void getConstructorNoInjectedConstructor() throws ToManyInjectedConstructorsException, NoSuchMethodException {
//        Constructor standardConstructor = DIUtils.getConstructor(NoInjectedConstructorClass1.class);
//
//        Assertions.assertThat(standardConstructor).isNotNull();
//        Assertions.assertThat(standardConstructor.getParameterCount()).isEqualTo(0);
//        LOGGER.info(String.format("Constructor '%s' is not injected and has %d parameters", standardConstructor.getName(), standardConstructor.getParameterCount()));
//    }
//
//    @Test
//    public void getConstructorWithInjectedConstructor() throws ToManyInjectedConstructorsException, NoSuchMethodException {
//        Constructor injectedConstructor = DIUtils.getConstructor(SimpleBeanWithAnnotatedConstructor1.class);
//
//        Assertions.assertThat(injectedConstructor).isNotNull();
//        Assertions.assertThat(injectedConstructor.getParameterCount()).isEqualTo(1);
//        LOGGER.info(String.format("Constructor '%s' is injected and has %d parameters", injectedConstructor.getName(), injectedConstructor.getParameterCount()));
//    }
//
//    @Test
//    public void getConstructorWithInjectedParameterConstructor() throws ToManyInjectedConstructorsException, NoSuchMethodException {
//        Constructor injectedConstructor = DIUtils.getConstructor(SimpleBeanWithAnnotatedConstructor2.class);
//
//        Assertions.assertThat(injectedConstructor).isNotNull();
//        Assertions.assertThat(injectedConstructor.getParameterCount()).isEqualTo(1);
//        LOGGER.info(String.format("Constructor '%s' is injected and has %d parameters", injectedConstructor.getName(), injectedConstructor.getParameterCount()));
//    }
//
//    @Test
//    public void stack() {
//        // First way -----------------------------------------
//        String anyString = "This is a moe_than_on_word string.";
//
//        int spaceCount = 0;
//        int underlineCount = 0;
//
//        // Iterate over them and count the spaces and underlines. In this short example the count of both is in one for-statement
//        for (char singleLetter : anyString.toCharArray()) {
//            if (singleLetter == ' ') {
//                spaceCount++;
//            }
//
//            if (singleLetter == '_') {
//                underlineCount++;
//            }
//        }
//        // Second way -----------------------------------------
//        // Split the string on every space
//        String[] anyStringParts = anyString.split(" ");
//        // Create a boolean array to store if a part contains a underline symbole
//        boolean[] stringPartUnderlineContainingFlag = new boolean[anyStringParts.length];
//
//        // Iterate the string parts and test for underline symbole
//        for (int index = 0; index < anyStringParts.length; ++index) {
//            stringPartUnderlineContainingFlag[index] = anyStringParts[index].contains("_");
//        }
//    }
}
