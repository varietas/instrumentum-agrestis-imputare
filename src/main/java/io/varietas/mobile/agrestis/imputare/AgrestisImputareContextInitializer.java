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
package io.varietas.mobile.agrestis.imputare;

/**
 * <h1>AgrestisImputareContextInitializer</h1>
 *
 * @author Michael Rhöse
 * @since Sa, Mai 7, 2016
 */
@Deprecated
public class AgrestisImputareContextInitializer {

//    private static final Logger LOGGER = Logger.getLogger(AgrestisImputareContextInitializer.class.getName());
//
//    ///< List of classes which contains fields of other beans
//    private final List<Class<?>> nestedRequiredDependenciesClazzes;
//
//    ///< List of classes which requires beans as parameters 
//    private final List<Class<?>> constructorWithAutowireAnnotationClazzes;
//
//    ///< List of classes containing beans
//    private final List<Class<?>> configurationClazzes;
//
//    ///< List of service classes
//    private final List<Class<?>> serviceClazzes;
//
//    ///< List of component classes
//    private final List<Class<?>> componentClazzes;
//
//    private final Map<Class<? extends Annotation>, List<Class<?>>> filteredClazzes;
//
//    private final List<BeanDefinition> store;
//
//    private final Class<?> applicationClazz;
//
//    private static final Integer MAX_ITERATION_FACTOR = 100;
//    private static final String BAD_APPLICATION_ERROR = "Unable to scan package. Be sure that a package/application is configured";
//
//    public AgrestisImputareContextInitializer(final Object application) {
//
//        this(application.getClass());
//    }
//
//    public AgrestisImputareContextInitializer(final Class<?> applicationClazz) {
//
//        this.nestedRequiredDependenciesClazzes = new ArrayList<>(0);
//        this.constructorWithAutowireAnnotationClazzes = new ArrayList<>(0);
//
//        this.configurationClazzes = new ArrayList<>(0);
//        this.serviceClazzes = new ArrayList<>(0);
//        this.componentClazzes = new ArrayList<>(0);
//
//        this.filteredClazzes = new HashMap<>();
//
//        this.store = new ArrayList<>();
//
//        this.applicationClazz = applicationClazz;
//    }
//
//    public final AgrestisImputareContext initializeContext() throws IllegalArgumentException, URISyntaxException, IOException, ToManyInjectedConstructorsException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, RecursiveInjectionException, BeanLoadException {
//
//        final AgrestisImputareContextImpl context = new AgrestisImputareContextImpl();
//
//        final BeanDefinition contectDefinition = new SingletonBeanDefinition(context, "applicationContext", BeanScopes.SINGELTON, AgrestisImputareContext.class, AgrestisImputareContextImpl.class.getConstructor());
//
//        this.store.add(contectDefinition);
//
//        this.init();
//
//        context.addContextDefinition(contectDefinition);
//
//        final BeanDefinition[] convertedStore = new BeanDefinition[this.store.size()];
//
//        for (int index = 0; index < this.store.size(); ++index) {
//            convertedStore[index] = this.store.get(index);
//        }
//
//        context.addBeanDefinitions(convertedStore);
//
//        return context;
//    }
//
//    private void init() throws IllegalArgumentException, URISyntaxException, IOException, ToManyInjectedConstructorsException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, RecursiveInjectionException, BeanLoadException {
//
//        if (this.applicationClazz == null) {
//            throw new IllegalArgumentException(BAD_APPLICATION_ERROR);
//        }
//
//        List<Class<?>> locatedClasses = DIUtils.searchClassesFromPackage(this.applicationClazz.getPackage());
//
//        this.sorting(locatedClasses);
//        this.filtering();
//
//        this.initialBeanCreationProcessing();
//        this.constructorBeanInjectionProcessing();
//        this.postBeanFieldInjectionProcessing();
//    }
//
//    /**
//     * Sorts all located classes into the classes for the following initialisation steps.
//     *
//     * @param locatedClasses
//     */
//    private void sorting(final List<Class<?>> locatedClasses) {
//
//        ///< Sortiing classes for type
//        this.configurationClazzes.addAll(locatedClasses.stream().filter(clazz -> clazz.isAnnotationPresent(Configuration.class)).collect(Collectors.toList()));
//        this.serviceClazzes.addAll(locatedClasses.stream().filter(clazz -> clazz.isAnnotationPresent(Service.class)).collect(Collectors.toList()));
//        this.componentClazzes.addAll(locatedClasses.stream().filter(clazz -> clazz.isAnnotationPresent(Component.class)).collect(Collectors.toList()));
//    }
//
////    private void scanConfigurationClazzesForBeans() {
////        this.configurationClazzes.parallelStream().forEach(configurationClazz -> {
////            this.filteredClazzes.put(Bean.class, serviceClazzes)
////        });
////    }
//    /**
//     * Filters the sorted classes into classes with and without dependencies. Bean definitions for classes without dependencies will created. Classes with dependencies will stored and prepared for the
//     * following steps.
//     */
//    private void filtering() {
//        this.filteredClazzes.put(Service.class, this.filterSimpleBeansForSecondLevelIteration(this.serviceClazzes));
//        this.filteredClazzes.put(Component.class, this.filterSimpleBeansForSecondLevelIteration(this.componentClazzes));
//    }
//
//    /**
//     * First step of initialisation. All classes without dependencies are used to create bean definitions. This t´´definitions will used for the initialisation of beans with dependencies.
//     */
//    private void initialBeanCreationProcessing() {
//
//        this.filteredClazzes.forEach((annotation, clazzes) -> {
//            clazzes.forEach(clazz -> {
//                try {
//                    this.store.add(BeanDefinitionUtils.createBeanInformationSimple(clazz, annotation));
//                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//                    LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
//                }
//            });
//        });
//    }
//
//    ///< TODO: field injection should run after bean creation.
//    ///< TODO: handle recursive injection (means bean requires itself)
//    private void constructorBeanInjectionProcessing() throws ToManyInjectedConstructorsException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, URISyntaxException, RecursiveInjectionException, BeanLoadException {
//
//        boolean isRotationIterationRequired = !this.configurationClazzes.isEmpty() && !this.serviceClazzes.isEmpty() || !this.componentClazzes.isEmpty();
//
//        while (isRotationIterationRequired) {
//            final Class<?> clazz = this.chooseAClazz(!this.configurationClazzes.isEmpty(), !this.serviceClazzes.isEmpty(), !this.componentClazzes.isEmpty());
//            final Constructor beanConstructor = DIUtils.getConstructor(clazz);
//            final Object[] params = BeanCreationUtils.getBeanConstructorParameterBeans(this.store, beanConstructor);
//            final Annotation annotation = null;
//            final String beanIdentifier = BeanScanUtils.getBeanIdentifier(clazz, annotation);
//            final BeanScopes scope = BeanScanUtils.getBeanScope(annotation);
//
//            ///< TODO: Load beans for constructor
//            ///< TODO: change naming of constructor beans
//            this.store.add(BeanDefinitionUtils.createBeanDefinition(clazz, scope, beanIdentifier, beanConstructor, params));
//
//            isRotationIterationRequired = !this.configurationClazzes.isEmpty() && !this.serviceClazzes.isEmpty() || !this.componentClazzes.isEmpty();
//        }
//    }
//
//    /**
//     * Third step. After creation of all beans the required fields will be injected.
//     */
//    private void postBeanFieldInjectionProcessing() {
//        this.store.parallelStream().forEach(storedBeanDefinition -> {
//            try {
//                List<Field> fields = Arrays.asList(storedBeanDefinition.getBeanClass().getFields()).stream().filter(field -> field.isAnnotationPresent(Autowire.class)).collect(Collectors.toList());
//
//                this.fieldDependencyInjection(fields, storedBeanDefinition);
//            } catch (RecursiveInjectionException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | BeanLoadException ex) {
//                LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
//            }
//        });
//    }
//
//    private void fieldDependencyInjection(List<Field> fields, final BeanDefinition storedBeanDefinition) throws RecursiveInjectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, BeanLoadException {
//        for (Field field : fields) {
//            if (!field.isAnnotationPresent(Autowire.class)) {
//                continue;
//            }
//
//            field.setAccessible(true);
//            Optional beanForField = BeanCreationUtils.getBeanInstance(this.store, field);
//
//            if (!beanForField.isPresent()) {
//                field.setAccessible(false);
//                throw new BeanLoadException(String.format("Required bean %s for field %s %s of bean %s not present.", ((BeanDefinition) beanForField.get()).getIdentifier(), field.getType().getSimpleName(), field.getName(), storedBeanDefinition.getIdentifier()));
//            }
//
//            field.set(storedBeanDefinition.getInstance(), ((Optional<Object>) beanForField).get());
//            field.setAccessible(false);
//        }
//    }
//
//    /**
//     * Takes a class from any list/stack and remove from the list/stack
//     *
//     * @param queue
//     * @param flags
//     * @return
//     */
//    private Class<?> chooseAClazz(Boolean... flags) {
//
//        if (flags[0]) {
//            Class<?> clazz = this.configurationClazzes.iterator().next();
//            this.configurationClazzes.remove(clazz);
//            return clazz;
//        }
//
//        if (flags[1]) {
//            Class<?> clazz = this.serviceClazzes.iterator().next();
//            this.serviceClazzes.remove(clazz);
//            return clazz;
//        }
//
//        Class<?> clazz = this.componentClazzes.iterator().next();
//        this.componentClazzes.remove(clazz);
//        return clazz;
//    }
//
//    private List<Class<?>> filterSimpleBeansForSecondLevelIteration(List<Class<?>> clazzList) {
//
//        this.constructorWithAutowireAnnotationClazzes.addAll(clazzList.stream().filter(clazz -> Arrays.asList(clazz.getConstructors()).stream().filter(constructor -> constructor.isAnnotationPresent(Autowire.class)).findFirst().isPresent()).collect(Collectors.toList()));
//
//        List<Class<?>> constructorFilteredClazzes = new ArrayList<>(clazzList);
//        constructorFilteredClazzes.removeAll(this.constructorWithAutowireAnnotationClazzes);
//
//        this.nestedRequiredDependenciesClazzes.addAll(constructorFilteredClazzes.stream().filter(clazz -> Arrays.asList(clazz.getFields()).stream().filter(field -> field.isAnnotationPresent(Autowire.class)).findFirst().isPresent()).collect(Collectors.toList()));
//
//        List<Class<?>> constructorAndFieldFilteredClazzes = new ArrayList<>(constructorFilteredClazzes);
//        constructorAndFieldFilteredClazzes.removeAll(this.nestedRequiredDependenciesClazzes);
//
//        return constructorAndFieldFilteredClazzes;
//    }
//    private List<Class<?>> filterSimpleMethodBeansForSecondLevelIteration(List<Class<?>> clazzList) {
//
//        List<BeanDefinition> methodBeans = new ArrayList<>();
//        
//        clazzList.forEach(clazz -> {
//            Arrays.asList(clazz.getMethods()).forEach(method -> {
//
//            });
//        });
//        
//        return null;
//    }
}
