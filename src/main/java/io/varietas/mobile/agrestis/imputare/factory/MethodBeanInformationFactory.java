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
package io.varietas.mobile.agrestis.imputare.factory;

import io.varietas.mobile.agrestis.imputare.AgrestisImputareContextInitialiser;
import io.varietas.mobile.agrestis.imputare.annotation.Bean;
import io.varietas.mobile.agrestis.imputare.annotation.injections.Autowire;
import io.varietas.mobile.agrestis.imputare.container.information.BeanInformation;
import io.varietas.mobile.agrestis.imputare.enumeration.BeanScopes;
import io.varietas.mobile.agrestis.imputare.utils.BeanDefinitionUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <h1>MethodBeanInformationFactory</h1>
 *
 * This class creates {@link BeanInformation} for beans created with methods. The factory will capsules all required logic.
 *
 * @author Michael Rhöse
 * @since Do, Jun 16, 2016
 */
public class MethodBeanInformationFactory {

    final Class<?> parentClazz;

    final List<BeanInformation> beanInformation;

    public MethodBeanInformationFactory(Class<?> parentClazz) {
        this.parentClazz = parentClazz;
        this.beanInformation = new ArrayList<>();
        List<Method> beanAnnotatedClasses = Arrays.asList(parentClazz.getDeclaredMethods()).stream().filter(method -> method.isAnnotationPresent(Bean.class)).collect(Collectors.toList());

        this.filterMethodsForAnnotationPosition(beanAnnotatedClasses);
    }

    private void filterMethodsForAnnotationPosition(List<Method> methods) {

        this.beanInformation.addAll(this.createBeanInformationForMethods(AgrestisImputareContextInitialiser.AnnotationPosition.METHOD, methods.stream().filter(method -> method.isAnnotationPresent(Autowire.class)).collect(Collectors.toList())));

        this.beanInformation.addAll(this.createBeanInformationForMethods(AgrestisImputareContextInitialiser.AnnotationPosition.METHOD_PARAMETER, methods.stream().filter(method -> Arrays.asList(method.getParameters()).stream().filter(param -> param.isAnnotationPresent(Autowire.class)).findFirst().isPresent()).collect(Collectors.toList())));
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public List<BeanInformation> getBeanInformations() {
        return this.beanInformation;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private List<BeanInformation> createBeanInformationForMethods(Integer typeCode, List<Method> methods) {
        final List<BeanInformation> res = new ArrayList<>();

        methods.forEach(method -> {

            String beanIdentifier = BeanDefinitionUtils.formatIdentifier(((Bean) method.getAnnotation(Bean.class)).name(), method.getName());
            BeanScopes scope = ((Bean) method.getAnnotation(Bean.class)).scope();
            List<String> dependencies = null;

            ///< Is Autowire existing? (y) collect identifier
            if (Objects.equals(typeCode, AgrestisImputareContextInitialiser.AnnotationPosition.METHOD)) ///< Is Autowire on method existing? (y) collect identifier
            {
                dependencies = Arrays.asList(((Autowire) method.getAnnotation(Autowire.class)).value());
            }

            if (Objects.equals(typeCode, AgrestisImputareContextInitialiser.AnnotationPosition.METHOD_PARAMETER)) ///< Is Autowire on params existing? (y) collect identifier
            {
                dependencies = new ArrayList<>(method.getParameterCount());
                for (Parameter param : method.getParameters()) {
                    dependencies.add(((Autowire) param.getAnnotation(Autowire.class)).value()[0]);
                }
            }

            res.add(new BeanInformation(method.getReturnType(), beanIdentifier, scope, dependencies));
        });

        return res;
    }
}
