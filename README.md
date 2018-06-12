# common-application-framework

This a common application framework built on top of the Spring Boot. It meets basic application needs for any project either monolithic or micro-services and abstracts out the technical implementation with business logic.

This provides a central framework of various applications, microservices etc and easy to bring changes across the enterprise either fixing a bug or introducing a new feature.

Current version provides the basic and advance needs of any application.  

#### Basic:
1. Springboot dependencies for RestFul services, Flyway, JMS, JPA, Actuator
2. Support for Flyway clean operation
3. Configuring MOM specific properties with SpringBoot out of the box properties and connection management. 
The common application framework provides an example with ActiveMq which is used as in-memory MOM.
4. Application properties with default setting for -
    * in-memory database(H2)
    * in-memory active-mq
    * endpoints (Spring Actuator)
    * Flyway
    * Jms and Active-MQ
    * Logging (using SpringBoot default LogBack)

#### Advance (These are not provided by neither Spring nor SpringBoot):
1. **Support for preprocessing of jms message on methods annotated with @JmsListener (e.g. Schema validation)**  
    The common-application framework provides a generic design for application to define any number of annotation and associated    processors. Thus each listener can have different annotations and preprocessing will be happen accordingly.
2. **Spring sleuth instrumentation with Spring-jms**  
    Spring sleuth is very common way of tracking your request in a microservices, but it breaks when spring-jms comes within a request chain to complete a business flow. Currently Spring-JMS doesn't support sleuth instrumentation and is put on hold, see the refer the below link for more details.  
    * [Stackoverflow](https://stackoverflow.com/questions/50536896/spring-boot-integration-of-sleuth-with-jms/50539929#50539929) There are many more such post on stackoverflow.
    * [Brave Open issue](https://github.com/openzipkin/brave/issues/584)  Brave is currently working on a proper fix.

    The common-application framework provides this missing support which can be enabled by setting the property `spring.jms.sleuth.instrumentation.enable=true` in the application.properties file.

All the above features are showcased with Demo application which not only shows how to use the common-application-framework but also provided a useful resources of code which can be used as libraries in any project.

## Available Downloads:  
#### Maven -  
```  
        <parent>  
            <groupId>com.arpit9mittal.fmk</groupId>  
            <artifactId>common-application-framework-parent</artifactId>  
            <version>1.0.0-RC1</version>  
        </parent>
  
        <dependency>  
            <groupId>com.arpit9mittal.fmk</groupId>  
            <artifactId>common-application-framework-core</artifactId>  
            <version>1.0.0-RC1</version>  
        </dependency>  
```
## Documentation

In progress

## License

The Common Application Framework is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).
