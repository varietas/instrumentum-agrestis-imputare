# agrestis imputare

[TOC]

Agrestis Imputare - The smart Dependency Injection is developed to provide dependency injection (DI) on java based applications (embedded or not). agrestis imputare (AI) is a runtime DI framework that is using annotations to find and inject beans. AI is inspired by the spring DI framework so if you know spring the learning should be easier for you.

At this time there is a number of annotations available:

* Annotations used to define beans
    * Repository
    * Service
    * Controller
    * Configuration
    * Component
    * Bean
* Annotations used to inject beans
    * Autowire

Every annotation for defining a bean has two optional properties:

* name - custom bean identifier
* scope - what kind of bean (singleton or prototype)

The custom bean identifier allows you to specify your own name of the bean. This option is optional. If you don't use an own identifier the name of the class will be used as the default identifier.
The default scope of a bean is the typical scope other frameworks are using too. However, sometimes it could be helpful to change the scope for a bean. So you could do it with this option. (Be sure you know what you do!)
#### 1.1. Get agrestis imputare

If you use a build tool like maven or gradle you are able to add the agrestis imputare dependency to your pom or .gralde file. (This time agrestis imputare is not available in the maven central repository so you have to add the dependency to your local repository.)

    <dependency>
        <groupId>io.varietas.mobile</groupId>
        <artifactId>agrestis-imputare</artifactId>
        <version>latest version</version>
    </dependency>

If you don't use any build tool you have to clone the project and to build it with the command `mvn package`.
#### 1.2. How to use
## 2. The annotations
#### 2.1. Repository
The `Repository` annotation is used to mark a bean occupied for the access of data in a database.

Default scope: *Singleton*
#### 2.2. Servics

The `Service` annotation is used to mark beans occupied as services. A service bean should only contain `Repository` beans or other `Service` beans.

Default scope: *Singleton*
#### 2.3. Controller
The `Controller` annotation is used to mark a bean occupied as controller. This beans normally are used to load and manipulate data as a result of a request or any action. A controller bean should only contain `Repository` beans, `Service` beans or other `Controller` beans.

Default scope: *Singleton*
#### 2.4. Configuration
The `Configuration` annotation is used to mark a class occupied as configuration. If you want to define beans with the `Bean` annotation the right place is here. Important: A `Configuration` is no instance in the application context. You will not be able to autowire them!
#### 2.5. Component
The `Component` annotation is used to define a basic bean. This means that the bean is not part of the core but you want to manage the bean via agrestis imputare. Every POJO could be a `Component` in your application.

Default scope: *Prototype*
#### 2.6. Bean
The `Bean` annotation is used to create a bean with a method contained in a `Configuration` class.

Default scope: *Prototype*
## 3. Inside
#### 3.1. Injection functionality
![Injection functionality](https://bitbucket.org/repo/yk6XMB/images/92307188-injection-principle_v2-prasentation.png)
#### 3.2. Injection workflow
![Injection workflow](https://bitbucket.org/repo/yk6XMB/images/1770838356-injection-workflow-complete.png =400x)
## 4. Technical Information
#### 4.1. Project dependencies
![Project dependencies](https://bitbucket.org/repo/yk6XMB/images/4179541309-project-dependencies.png =400x)