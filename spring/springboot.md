#### 简介

##### 优点

springboot减少了很多配置，提高了开发和部署的效率

##### 启动类

SpringBoot有一个*Application 的入口类，作为工程的启动类。

@SpringBootApplication是启动类的核心注解，它是一个组合注解：@Configuration、@EnableAutoConfiguration、@ComponentScan

**@EnableAutoConfiguration 让 Spring Boot 根据类路径中的 jar 包依赖为当前项目进行自动配置**，例如，添加了 spring-boot-starter-web 依赖，会自动添加 Tomcat 和 Spring MVC 的依赖，那么 Spring Boot 会对 Tomcat 和 Spring MVC 进行自动配置。

SpringBoot会自动扫描和启动类同级包以及以下包的Bean

##### 启动原理

##### 定义一个starter