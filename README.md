# agrestis imputare #

Agrestis Imputare - The smart Dependency Injection is developed to provide dependency injection (DI) on java based applications (embedded or not). agrestis imputare (AI) is a runtime DI framework that is using annotations to find and inject beans. AI is written in plain Java - there is no third-party library.

At this time there is a number of annotations available:

1. Annotations used to define beans
    * Component
    * Service
    * Configuration
    * Bean
2. Annotations used to inject beans
    * Autowire

Every annotation for defining a bean has to optional properties:

* name - custom bean identifier
* scope - what kind of bean (singleton or prototype)

### Component ###

The Component annotation is used to define a basic bean. The default scope of component beans is prototype means every injection will be a new instance of this class.

### Service ###

The Service annotation is used to define beans used as services. The default scope of service beans is singleton means every injection will be the same instance.

### Configuration ###

The Configuration annotation is used to define beans used as configuration. The default scope of configuration beans is singleton means every injection will be the same instance. If you want to define beans with the Bean annotation the right place is here. It is possible to use the Bean annotation in component and service beans too but the cleanest way is to do it in configuration classes.

## The current agrestis imputare principle ##

![injection-principle.png](https://bitbucket.org/repo/yk6XMB/images/2861867991-injection-principle.png)