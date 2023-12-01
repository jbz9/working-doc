# Spring

它是一个轻量级的开源框架，能够提高开发效率，简化开发，它本身是模块化的，它有2个核心特性，一个DI依赖注入，一个是AOP切面编程。

## 知识点

### **spring模块**

- Spring Core 核心模块 IOC容器, 解决对象创建及依赖关系
- Spring Web  Spring对web模块的支持。
- - 可以与struts整合,让struts的action创建交给spring
  - spring mvc模式
- Spring JDBC  Spring 对jdbc操作的支持  【JdbcTemplate模板工具类】
- Spring Data 
- Spring ORM  spring对orm的支持：
- - 既可以与hibernate整合，【session】
  - 也可以使用spring的对hibernate操作的封装
- Spring AOP  切面编程

### **spring的优点**

* 低耦合、低侵入：各个模块组件之间低耦合，每一个都可以单独使用
* 生态成熟：spring本身集成的第三方库就比较多，另外想要扩展其它库，也是比较方便的

### **IOC和DI**

IOC : Inversion of control

DI：Dependency Injection

**IOC控制反转是一种设计思想，DI是它的实现方式**，它是将本来由我们创建对象的控制权，移交给了容器进行创建和管理，我们一般使用注解去注册一个bean，比如Component、service、controller、repository，还有springboot的**@Bean方法**注解。

DI依赖注入：注入有Set注入、构造器注入、和autowried以及resouces注解注入，我们经常用的就是注解注入。

补充：

**@Bean方法注解**

Spring的@Bean注解用于告诉方法，产生一个Bean对象，然后这个Bean对象交给Spring管理。产生这个Bean对象的方法Spring只会调用一次，随后这个Spring将会将这个Bean对象放在自己的IOC容器中。

spring容器读取了配置元数据之后，通过java反射创建类并注入其依赖类。在对象初始化的时候将数据注入到对象中，或者是将对象的引用注入到 另一个需要依赖它的类。

1、将类注册到spring容器：xml、Componen、Configuration

2、将对象注册到spring容器:

* ```java
  @Bean
  public A1Service getA2Service(){
     return new A1Service() ;
  }
  ```

* ```java
  //使用spring容器注册
  AnnotationConfigApplicationContext ann = new AnnotationConfigApplicationContext(AppConfig.class);
  ann.getBeanFactory().registerSingleton("testService",new A1Service());
  ```

**IOC容器初始化**

IOC容器有2种：

* 实现BeanFactory接口
* 实现ApplicationContext：应用上下文

### **AutoWired和Resouce注入区别**

AutoWired是默认按照类型注入，Resouce是默认按照名称注入，它也支持按照类型注入，AutoWried 需要和Qualifie配合才能使用名称注入

1、首先去IOC容器 查找类型为 UserRepository 的bean实例对象
2、如果查到了，且结果只有一个，那么就装载这个bean实例
3、如果查到了，结果不止一个，那么 @Autowired就根据名称byName来查找
3、如果找不到，则抛出异常，如果不想抛出异常，使用required=false

再加上 @Qualifier

### **依赖注入有哪几种方式**

为对象注入值

**1、Set注入**

通过springbeanxml中bean标签注入

```java
package com.bless.springdemo.action; 
public class SpringAction { 
  
//注入对象springDao 
private SpringDao springDao; 
  
//一定要写被注入对象的set方法 
public void setSpringDao(SpringDao springDao) { 
				this.springDao = springDao; 
	} 
}
```

xml对应配置

```xml
<!--配置bean,配置后该类由spring管理--> 
<bean name="springAction" class="com.bless.springdemo.action.SpringAction"> 
<!--(1)依赖注入,配置当前类中相应的属性--> 
			<property name="springDao" ref="springDao"></property> 
</bean> 
<bean name="springDao" class="com.bless.springdemo.dao.impl.SpringDaoImpl"></bean>
```

**2、构造函数注入**

```java
@Data
public class User {

    School school;

    public  User(){}
    //构造函数
    public User(String username){
        this.username = username;
        System.out.println("构造函数注入");
    }
 }
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

   <!-- 使用 xml文件来定义 bean 对象（user）-->
    <bean id="user" class="com.demo.entity.User" scope="singleton">
        <constructor-arg index="0" value="小九"></constructor-arg>
    </bean>
</beans>
```

3、注解注入

**Bean的自动装配**

**(1)byName的xml注入**

```xaml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

   <!-- 使用 xml文件来定义 bean 对象（user）-->
    <bean id="user" class="com.demo.entity.User" scope="singleton" autowire="byName">
    </bean>
    <bean id="school" class="com.demo.entity.School">
        <property name="name" value="科大"></property>
    </bean>
</beans>
```

**byType的xml注入**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

   <!-- 使用 xml文件来定义 bean 对象（user）-->
    <bean id="user" class="com.demo.entity.User" scope="singleton" autowire="byType">
    </bean>
    <bean id="school" class="com.demo.entity.School">
        <property name="name" value="科大"></property>
    </bean>
</beans>
```

@AutoWired 是按照byType类型注入，是容器配置的一个注解，可用于属性、Set方法、构造函数

```java
 @Autowired
 private UserRepository userRepository;
//注入一个 类型为 UserRepository 的属性，通过@Autowired自动装配方式，从IoC容器中去查找到，并返回给该属性，名称为 userRepository

1、首先去IOC容器 查找类型为 UserRepository 的bean实例对象
2、如果查到了，且结果只有一个，那么就装载这个bean实例
3、如果查到了，结果不止一个，那么 @Autowired就根据名称byName来查找
3、如果找不到，则抛出异常，如果不想抛出异常，使用required=false
  
再加上 @Qualifier
```

@Service 将service注册成为bean，成为一个实例

```java
 ClassPathXmlApplicationContext factory = new ClassPathXmlApplicationContext("spring.xml");
        //bean的 id 来获取bean 对象
        UserServiceImpl person = (UserServiceImpl) factory.getBean("userServiceImpl");
        person.add();
```

**PostConstruct**和**PreDestory**

**PostConstruct**作为初始化函数的一个替代,**PreDestory**作为销毁函数的一个替代



### 核心容器（Core Container）

spring的核心包含了Core模块、Beans模块、Context上下文模块等模块

* Core模块：

* Beans模块：提供了BeanFactory,用来生成Bean对象（Spring将管理的对象称为Bean）

IOC容器实现了ApplicationContext（`org.springframework.context.ApplicationContext`）接口，是它一个实例化对象，用来创建、配置和管理bean对象。

容器通过我们提交POJO类以及配置元数据，产生一个可以

spring主动创建被调用类的对象，然后把这个对象注入到我们自己的类中，使得我们可以使用它。

如何实例化Bean:

* 构造方法
* 实例工厂
* 静态工厂

### AOP

[Spring AOP 源码解析_Javadoop](https://javadoop.com/post/spring-aop-source)

Aspect Oriented Programming。运行时编译织入

总：AOP是面向切面编程，主要是用在解耦，把非业务模块和业务模块进行解耦，使用比较多的就是日志记录和权限验证。

分：然后在Spring 里面，我们一般是用Aspect注解定义一个切面类，在切面类里面定义它的切入点ponitCut，以及Advice通知方式，比如说前置、后置、异常、环绕和return。spring内部是使用JDK动态代理和cglib两种方式，是在bean初始化完成之后，在bean初始化`initializeBean`最后调用后置处理器beanpostprocessor，在这里判断如果有Advice,就会创建代理类，至于使用JDK还是cglib，还是看这个目标对象有没有实现接口

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1652066951324Spring-AOP.drawio.png)



相当于一个拦截器，可以拦截一些过程，比如，AOP可以拦截一个执行的方法，可以在方法执行前后添加一些额外的事件。比如日志管理，日志模块就是一个切面，它可以切入到需要其他其需要输出日志的模块，切入点就是需要输入日志的地方

**概念：**

- **切面(Aspect)** – 一些横跨多个类的公共模块，如日志、安全、事务等。简单地说，日志模块就是一个切面。
- **连接点(Joint Point)** – 目标类中插入代码的地方。连接点可以是方法、异常、字段，连接点处的切面代码会在方法执行、异常抛出、字段修改时触发执行。
- **通知(Advice)** – 在连接点插入的实际代码(即切面的方法)，有5种不同类型（后面介绍）。
- **切入点(Pointcut)** – 定义了连接点的条件，一般通过正则表达式。例如，可以定义所有以`loadUser`开头的方法作为连接点，插入日志代码。

### JDK动态代理和CGlib代理

作用：功能增强、访问控制

| JDK动态代理                                    | cglib代理                                                    |
| ---------------------------------------------- | ------------------------------------------------------------ |
| 代理的只能是接口                               | 代理的是可以是类                                             |
| 需要一个handler去实现**InvocationHandler**接口 | 基于ASM，把真实对象的class文件加载进来，然后修改字节码生成子类来实现 |

### 为什么JDK动态代理，必须是接口

这个是因为JDK生成的代理类继承了JDK反射包里Proxy类，Java单继承，所以只能代理接口，让代理类实现我们的真实接口。

## 常见问题

### 什么是bean？

bean是由IOC容器创建、管理的对象

### bean的生命周期

bean是由IOC进行实例化创建的，所以它的生命周期也是由容器进行控制的。对于普通的对象，它的生命周期就是创建，如果之后不再使用的话，就有java虚拟机通过垃圾回收算法进行销毁。而bean的生命周期分为几个阶段：

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1652078509243Spring-bean%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.drawio.png)

### 申明bean的注解有哪些

Component、service、controller、repository

### Spring 框架中的单例 bean 是线程安全的吗?

不是线程安全的，bean的作用域

### IOC

IOC是Inversion of Control，是程序解耦的一种设计思想，将程序里bean的创建交给了Spring容器。使用Autowried、Resource 进行属性注入。

IOC的整个流程可以分为几个阶段：

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16522398382211652239837376.png)

### beanfactory和applicationcontext

不同点：BeanFactory是延迟加载，如果Bean的某个属性没有注入，BeanFactory加载之后，直到第一次使用调用getBean方法才会抛出异常。

而ApplicationContext在初始化的时候就会校验属性是否注入。

beanfactory可以看做IOC容器，applicationcontext也可以看成一个beanfactory，因为它也实现了beanfactory这个接口

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16522364249721652236424181.png)



### 循环依赖

总：循环依赖就是多个bean循环引用，形成闭环，导致死循环，spring使用了三级缓存来解决循环依赖

分：

spring解决循环依赖的核心是`提前暴露bean`,它使用3个Map用来做缓存

**一级缓存用来存放已经实例化和初始化的bean**

**二级缓存存放已经实例化但是未初始化的bean**

**三缓存存放bean对应的一个beanfactory**

**spring会依次从这三个缓存里获取这个bean，直到获取到bean。**其实使用二级缓存，就可以解决循环引用里死循环的问题，但是没有办法解决AOP代理的问题，所以spring引入了第三级缓存，提前AOP的动作来解决AOP代理对象的问题。

情景：假设A、B两个service相互引用，**单例**Bean中，属性互相引用

步骤：

假设先创建A

① 对A进行实例化，把A放入二级缓存

②填充Aservice的B属性

​	（1）去一级缓存、二级缓存里面寻找，肯定找不到B

​      (2）那么使用beanFactory去创建serviceB

​      (3) 去实例化Bservice，完成之后吧Bservice放入到二级缓存中

​      (4）去填充Bservice的属性A，这时候能够从二级缓存里面找到Aservice，所以填充成功

​		 AOP代理对象的问题出在这里，因为此时A的AOP代理对象还没有创建，所以填充的是A的真实对象，所以这		 里去判断了如果A正在创建（代表循环依赖）而且需要AOP的话，那么提前把A进行AOP，获取A的代理对象,

​		 并且把代理对象A放入到二级缓存中

​      (5)  然后Bean进行初始化，完成之后bean创建成功，那么把B加入到单例池一级缓存中，然后把二级缓存里的B    		删除

③ 初始化Aservice，（如果有AOP，此处进行AOP，会直接从二级缓存里面拿到代理对象A）完成之后加入到单例池（如果A有AOP代理对象，放入代理对象）

| 名称                      | 描述                                                         |
| ------------------------- | ------------------------------------------------------------ |
| **singletonObjects**      | 一级缓存，存放已经实例化、初始化完成的Bean，成品库           |
| **earlySingletonObjects** | 二级缓存，用于存放已经实例化,但`未初始化的Bean`，半成品库    |
| **singletonFactories**    | 三级缓存，存放该Bean的BeanFactory,当加载一个Bean会先将该Bean包装为BeanFactory放入三级缓存（使用beanfactory去创建一个bean），工厂库 |

```java
public class AccountController {
    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private AccountService accountService;

    // 构造函数依赖注入
    // 不管是否设置为required为true，都会出现循环依赖问题
    @Autowire
    // @Autowired(required = false)
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
}
```





# SpringBoot

## 知识点

### 优点

springboot减少了很多配置，提高了开发和部署的效率

### SPI

Service Provider Interface，服务发现机制。将类的全限定名配置在文件中，并由服务加载器读取配置文件，加载实现类。这样可以在运行时，动态为接口替换实现类

### 启动原理

1. **启动类加载：**
   - 主要关注 `public static void main(String[] args)` 方法，这是 Java 应用程序的入口。`@SpringBootApplication` 注解表明这是 Spring Boot 应用。
2. **SpringApplication初始化：**
   - `SpringApplication` 类的 `run` 方法是整个应用启动的入口。这个方法会创建一个新的 `ApplicationContext`，并且会调用一系列初始化方法，如`initialize()`、`applyInitializers()` 和 `listeners.starting()`。
3. **创建ApplicationContext：**
   - 在 `run` 方法中，`SpringApplication` 会创建一个 `ApplicationContext`。它会根据应用是否为 Web 应用来创建不同的 `ApplicationContext` 实例，通常为 `AnnotationConfigApplicationContext` 或 `GenericApplicationContext`。
4. **加载应用默认配置：**
   - 这一阶段涉及到 Spring Boot 的自动配置机制。`SpringApplication` 会调用 `applyInitializers()` 方法，这些初始化器（`ApplicationContextInitializer`）会根据条件初始化应用程序上下文，包括加载默认配置。
5. **执行SpringApplicationRunListeners：**
   - `SpringApplicationRunListeners` 是一组监听器，用于监听应用程序的启动过程。在 `listeners.starting()` 中，会通知所有注册的监听器，即 `SpringApplicationRunListener` 实现类。
6. **准备上下文：**
   - 在 `refreshContext` 方法中，`ApplicationContext` 被刷新。这个过程会加载配置类、扫描包、创建 bean 等。涉及到 `AnnotatedBeanDefinitionReader`、`ClassPathBeanDefinitionScanner` 等类。
7. **加载外部配置：**
   - 外部配置加载是通过 `SpringApplication` 的 `load()` 方法实现的，它会解析命令行参数、配置文件等，将配置信息加载到 `Environment` 对象中。
8. **Bean初始化前准备工作：**
   - 这个过程主要涉及到 `invokeBeanFactoryPostProcessors()` 方法，它会调用 `BeanFactoryPostProcessor` 实现类，用于在 bean 创建之前对 bean 工厂进行操作。
9. **Web服务器初始化：**
   - 如果应用程序是一个 Web 应用，`SpringApplication` 会初始化内嵌的 Web 服务器。这一部分涉及到 `EmbeddedWebApplicationContext`、`EmbeddedServletContainer`、`TomcatEmbeddedServletContainerFactory` 等类。
10. **执行CommandLineRunner和ApplicationRunner：**
    - 这是在 `callRunners()` 方法中完成的，它会执行所有实现了 `CommandLineRunner` 和 `ApplicationRunner` 接口的 bean 中的逻辑。
11. **应用启动完成：**
    - 至此，整个应用启动的流程就结束了。在 `listeners.finished()` 中，所有注册的监听器都会被通知，应用程序启动完成。



### 自动配置原理

```
+-----------------------------+
|   启动  Application          |
|   Start Application         |
|                        |
+-----------------------------+
              |
              |
              v
+-----------------------------+
| 找到启动类注解                |
| @SpringBootApplication      |
|                |
+-----------------------------+
              |
              |
              v
+-----------------------------+
| 启动类注解包含了               |
| @EnableAutoConfiguration   |
| 自动装配注解                  |
+-----------------------------+
              |
              |
              v
+-----------------------------+
| 自动装配注解会导入             |
| AutoConfigurationImportSelector|
| 类                           |
+-----------------------------+
              |
              |
              v
+-----------------------------+
| 这个Selector类会加载所有依赖jar包下的     |
| META-INF/spring.factories   |
| 文件中，这个文件包含了所有自动配置类          |
+-----------------------------+
              |
              |
              v
+-----------------------------+
| 然后进行条件评估，包括启动环境、配置文件信息、bean注册信息等|
| Condition Evaluation        |
|                             |
+-----------------------------+
              |
              |
              v
+-----------------------------+
| 评估通过，根据条件注解Conditional |
| 进行选择加载bean              |
| Match Conditions            |
|                             |
+-----------------------------+
              |
              |
              v
+-----------------------------+
|                             |
| Enable Auto Configuration   |
| 进入配置类                  |
+-----------------------------+
              |
              |
              v
+-----------------------------+
|                             |
| Create & Configure Beans    |
| 创建和配置bean      
| 自动装配完成
+-----------------------------+

```

#### @Conditional

- `@ConditionalOnBean`：当容器里有指定 Bean 的条件下
- `@ConditionalOnMissingBean`：当容器里没有指定 Bean 的情况下
- `@ConditionalOnSingleCandidate`：当指定 Bean 在容器中只有一个，或者虽然有多个但是指定首选 Bean
- `@ConditionalOnClass`：当类路径下有指定类的条件下
- `@ConditionalOnMissingClass`：当类路径下没有指定类的条件下
- `@ConditionalOnProperty`：指定的属性是否有指定的值
- `@ConditionalOnResource`：类路径是否有指定的值
- `@ConditionalOnExpression`：基于 SpEL 表达式作为判断条件
- `@ConditionalOnJava`：基于 Java 版本作为判断条件
- `@ConditionalOnJndi`：在 JNDI 存在的条件下差在指定的位置
- `@ConditionalOnNotWebApplication`：当前项目不是 Web 项目的条件下
- `@ConditionalOnWebApplication`：当前项目是 Web 项 目的条件下

### Servlet容器

### 定义一个starter

①定义maven项目，引入spring-boot-starter依赖。

②定义一个配置类、一个自动装配类。配置类用ConfigurationProperties注解；自动装配类用ConditionalOnClass、EnableConfigurationProperties、ConditionalOnProperty、Configuration注解以及@bean注解注册bean。

③最后在META/INF创建一个spring.factories文件，把自动配置类的路径写进去

### 为什么可以使用Jar包启动

1、Spring Boot 通过 Maven 插件打包的 JAR 文件是可执行的，它是把所有项目依赖打包到 JAR 文件中，其中是包括内嵌Web 服务器；

2、可执行Jar中的 `META-INF/manifest.MF` 文件，指定了启动类

3、在运行`java -jar` 命令会将 JAR 文件添加到类路径（classpath）中，然后执行 `META-INF/MANIFEST.MF` 文件中指定的主类。这样，JVM 就知道从哪个类开始执行；并且自动检测到内式 Web 服务器，启动它，监听指定的端口。

目录结构

```shell
spring-boot-demo-0.0.1-SNAPSHOT
├── BOOT-INF
│   ├── classes
│   │   └── com
│   │       └── demo
│   │           └── demo
│   └── lib
├── META-INF
│   └── maven
│       └── com.demo
│           └── spring-boot-demo
└── org
    └── springframework
        └── boot
            └── loader
                ├── archive
                ├── data
                ├── jar
                ├── jarmode
                └── util
```

文件结构

```shell
spring-boot-demo-0.0.1-SNAPSHOT
├── BOOT-INF
│   ├── classes
│   │   ├── application.yaml
│   │   └── com
│   │       └── demo
│   │           └── demo
│   │               └── SpringBootDemoApplication.class
│   ├── classpath.idx
│   ├── layers.idx
│   └── lib
│       ├── jackson-annotations-2.13.3.jar
│       ├── jackson-core-2.13.3.jar
│       ├── jackson-databind-2.13.3.jar
│       ├── jackson-datatype-jdk8-2.13.3.jar
│       ├── jackson-datatype-jsr310-2.13.3.jar
│       ├── jackson-module-parameter-names-2.13.3.jar
│       ├── jakarta.annotation-api-1.3.5.jar
│       ├── jul-to-slf4j-1.7.36.jar
│       ├── log4j-api-2.17.2.jar
│       ├── log4j-to-slf4j-2.17.2.jar
│       ├── logback-classic-1.2.11.jar
│       ├── logback-core-1.2.11.jar
│       ├── monitor-spring-boot-starter-1.0.jar
│       ├── slf4j-api-1.7.36.jar
│       ├── snakeyaml-1.30.jar
│       ├── spring-aop-5.3.20.jar
│       ├── spring-beans-5.3.20.jar
│       ├── spring-boot-2.7.0.jar
│       ├── spring-boot-autoconfigure-2.7.0.jar
│       ├── spring-boot-jarmode-layertools-2.7.0.jar
│       ├── spring-context-5.3.20.jar
│       ├── spring-core-5.3.20.jar
│       ├── spring-expression-5.3.20.jar
│       ├── spring-jcl-5.3.20.jar
│       ├── spring-web-5.3.20.jar
│       ├── spring-webmvc-5.3.20.jar
│       ├── tomcat-embed-core-9.0.63.jar
│       ├── tomcat-embed-el-9.0.63.jar
│       └── tomcat-embed-websocket-9.0.63.jar
├── META-INF
│   ├── MANIFEST.MF //manifest 表明
│   └── maven
│       └── com.demo
│           └── spring-boot-demo
│               ├── pom.properties
│               └── pom.xml
└── org
    └── springframework
        └── boot
            └── loader
                ├── archive
                │   ├── Archive$Entry.class
                │   ├── Archive$EntryFilter.class
                │   ├── Archive.class
                │   ├── ExplodedArchive$AbstractIterator.class
                │   ├── ExplodedArchive$ArchiveIterator.class
                │   ├── ExplodedArchive$EntryIterator.class
                │   ├── ExplodedArchive$FileEntry.class
                │   ├── ExplodedArchive$SimpleJarFileArchive.class
                │   ├── ExplodedArchive.class
                │   ├── JarFileArchive$AbstractIterator.class
                │   ├── JarFileArchive$EntryIterator.class
                │   ├── JarFileArchive$JarFileEntry.class
                │   ├── JarFileArchive$NestedArchiveIterator.class
                │   └── JarFileArchive.class
                ├── ClassPathIndexFile.class
                ├── data
                │   ├── RandomAccessData.class
                │   ├── RandomAccessDataFile$1.class
                │   ├── RandomAccessDataFile$DataInputStream.class
                │   ├── RandomAccessDataFile$FileAccess.class
                │   └── RandomAccessDataFile.class
                ├── ExecutableArchiveLauncher.class
                ├── jar
                │   ├── AbstractJarFile$JarFileType.class
                │   ├── AbstractJarFile.class
                │   ├── AsciiBytes.class
                │   ├── Bytes.class
                │   ├── CentralDirectoryEndRecord$1.class
                │   ├── CentralDirectoryEndRecord$Zip64End.class
                │   ├── CentralDirectoryEndRecord$Zip64Locator.class
                │   ├── CentralDirectoryEndRecord.class
                │   ├── CentralDirectoryFileHeader.class
                │   ├── CentralDirectoryParser.class
                │   ├── CentralDirectoryVisitor.class
                │   ├── FileHeader.class
                │   ├── Handler.class
                │   ├── JarEntry.class
                │   ├── JarEntryCertification.class
                │   ├── JarEntryFilter.class
                │   ├── JarFile$1.class
                │   ├── JarFile$JarEntryEnumeration.class
                │   ├── JarFile.class
                │   ├── JarFileEntries$1.class
                │   ├── JarFileEntries$EntryIterator.class
                │   ├── JarFileEntries$Offsets.class
                │   ├── JarFileEntries$Zip64Offsets.class
                │   ├── JarFileEntries$ZipOffsets.class
                │   ├── JarFileEntries.class
                │   ├── JarFileWrapper.class
                │   ├── JarURLConnection$1.class
                │   ├── JarURLConnection$JarEntryName.class
                │   ├── JarURLConnection.class
                │   ├── StringSequence.class
                │   └── ZipInflaterInputStream.class
                ├── JarLauncher.class
                ├── jarmode
                │   ├── JarMode.class
                │   ├── JarModeLauncher.class
                │   └── TestJarMode.class
                ├── LaunchedURLClassLoader$DefinePackageCallType.class
                ├── LaunchedURLClassLoader$UseFastConnectionExceptionsEnumeration.class
                ├── LaunchedURLClassLoader.class
                ├── Launcher.class
                ├── MainMethodRunner.class
                ├── PropertiesLauncher$1.class
                ├── PropertiesLauncher$ArchiveEntryFilter.class
                ├── PropertiesLauncher$ClassPathArchives.class
                ├── PropertiesLauncher$PrefixMatchingArchiveFilter.class
                ├── PropertiesLauncher.class
                ├── util
                │   └── SystemPropertyUtils.class
                └── WarLauncher.class
```

manifest.mf文件

```shell
Manifest-Version: 1.0
Spring-Boot-Classpath-Index: BOOT-INF/classpath.idx
Implementation-Title: spring-boot-demo
Implementation-Version: 0.0.1-SNAPSHOT
Spring-Boot-Layers-Index: BOOT-INF/layers.idx
Start-Class: com.demo.demo.SpringBootDemoApplication
Spring-Boot-Classes: BOOT-INF/classes/
Spring-Boot-Lib: BOOT-INF/lib/
Build-Jdk-Spec: 1.8
Spring-Boot-Version: 2.7.0
Created-By: Maven JAR Plugin 3.2.2
Main-Class: org.springframework.boot.loader.JarLauncher
```

JarLauncher.java 类

```java
package org.springframework.boot.loader;

import org.springframework.boot.loader.archive.Archive;

/* loaded from: JarLauncher.class */
public class JarLauncher extends ExecutableArchiveLauncher {
    static final Archive.EntryFilter NESTED_ARCHIVE_ENTRY_FILTER = entry -> {
        if (entry.isDirectory()) {
            return entry.getName().equals("BOOT-INF/classes/");
        }
        return entry.getName().startsWith("BOOT-INF/lib/");
    };

    public JarLauncher() {
    }

    protected JarLauncher(Archive archive) {
        super(archive);
    }

    protected boolean isPostProcessingClassPathArchives() {
        return false;
    }

    protected boolean isNestedArchive(Archive.Entry entry) {
        return NESTED_ARCHIVE_ENTRY_FILTER.matches(entry);
    }

    protected String getArchiveEntryPathPrefix() {
        return "BOOT-INF/";
    }

    public static void main(String[] args) throws Exception {
        new JarLauncher().launch(args);
    }
}
```

### 如何在Spring Boot启动的时候运行一些特定的代码？


可以 `ApplicationRunner` 或 `CommandLineRunner` 接口。重写他们的`Run`方法

如果有多个 `ApplicationRunner` 或 `CommandLineRunner` Bean，它们的执行顺序可能是不确定的。**如果需要确保特定的顺序，可以使用 `@Order` 注解。**

#### 使用 `ApplicationRunner`

`ApplicationRunner` 接口提供了一个 `run` 方法，该方法在 `SpringApplication.run` 完成之前被调用。

```java
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 在应用程序启动后执行的代码
        System.out.println("ApplicationRunner executed!");
    }
}

```

#### 使用 `CommandLineRunner`

`CommandLineRunner` 接口提供了一个 `run` 方法，该方法在 `SpringApplication.run` 完成之前被调用。

```java
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // 在应用程序启动后执行的代码
        System.out.println("CommandLineRunner executed!");
    }
}

```







