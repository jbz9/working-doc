# spring

参考：

https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html

## 1. 概述

Spring最开始指的是spring-framework工程，之后又出来了spring-boot工程。spring-boot更倾向于约定而不是配置

## 2. 基础

### 1. 什么是spring

它是一个轻量级的开源框架，能够提高开发效率，简化开发，它本身是模块化的，它有2个核心特性，一个依赖注入，一个是AOP切面编程。

**服务端三层架构**

* 表现层 —— web层
* 业务层 —— service层
* 持久层 —— dao层

Rest架构：前端请求后端接口，返回JSON的这样一种REST架构

### 2. **spring模块**

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

### 3. spring的优点

* 低耦合、低侵入：各个模块组件之间低耦合，每一个都可以单独使用
* 方便集成其它框架：比如mybatis、Hibernate、Quartz等

### 4. IOC控制反转和DI注入

Spring的IOC容器是基于IOC控制反转思想，通过依赖注入DI的实现方式来设计的，它是将本来由我们创建对象的控制权，移交给了容器进行创建和管理，容器通过读取配置元数据去初始化实例，配置元数据可以使用XML、注解（Componen/Configuration 将类注册到bean容器）或者Java类的方式进行配置，

将手动创建的对象的控制权交给了spring容器，如果要使用某个对象，只需要在容器中获取，而不需要自己创建。BeanFactory和ApplicationContext是Spring的两大核心接口，
而其中ApplicationContext是BeanFactory的子接口。它们都可以当做Spring的容器，
Spring容器是生成Bean实例的工厂，并管理容器中的Bean。

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

### 5. 依赖注入的方式

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

### 6. IOC的实现



### 6. 核心容器（Core Container）

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

### 7.  切面编程

动态代理

相当于一个拦截器，可以拦截一些过程，比如，AOP可以拦截一个执行的方法，可以在方法执行前后添加一些额外的事件。比如日志管理，日志模块就是一个切面，它可以切入到需要其他其需要输出日志的模块，切入点就是需要输入日志的地方

**概念：**

- **切面(Aspect)** – 一些横跨多个类的公共模块，如日志、安全、事务等。简单地说，日志模块就是一个切面。
- **连接点(Joint Point)** – 目标类中插入代码的地方。连接点可以是方法、异常、字段，连接点处的切面代码会在方法执行、异常抛出、字段修改时触发执行。
- **建议(Advice)** – 在连接点插入的实际代码(即切面的方法)，有5种不同类型（后面介绍）。
- **切点(Pointcut)** – 定义了连接点的条件，一般通过正则表达式。例如，可以定义所有以`loadUser`开头的方法作为连接点，插入日志代码。

**能够通知的种类：**

- **before** – 在方法之前运行建议（插入的代码）
- **after** – 不管方法是否成功执行，在方法之后运行插入建议（插入的代码）
- **after-returning** – 当方法执行成功，在方法之后运行建议（插入的代码）
- **after-throwing** – 仅在方法抛出异常后运行建议（插入的代码）
- **around** – 在方法被调用之前和之后运行建议（插入的代码）

bean就是普通的java，由Spring IoC容器实例化。

* 使用xml配置
* 使用@Configuration配置
* 隐式的bean发现机制和自动装配

**bean装配**

```jade
// 注册
@Configuration
public class BeanConfiguration {
    @Bean
    public AtomicInteger getAtomicIntegerBean() {
        return new AtomicInteger();
    }
}
//或者 
@Componment
public class Foo{}

// 扫描
@ComponentScan(basePackages={})
@Configuration
public class BeanConfiguration {}

//装配AtomicInteger的实例
@Autowired
private AtomicInteger c;

```

