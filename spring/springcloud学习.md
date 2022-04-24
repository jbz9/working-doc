### 简介
1.springcloud的作用  
将一个大的项目分拆成一个个小模块，每个模块都是一个独立的子系统，可以单独运行。
2.springcloud的基础功能    

* 服务治理： Spring  Cloud Eureka  
* 客户端负载均衡： Spring Cloud Ribbon    
* 服务容错保护： Spring  Cloud Hystrix   
* 声明式服务调用： Spring  Cloud Feign    
* API网关服务：Spring Cloud Zuul   
* 分布式配置中心： Spring Cloud Config    

3.springcloud的高级功能  
- 消息总线： Spring Cloud Bus
- 消息驱动的微服务： Spring Cloud Stream
- 分布式服务跟踪： Spring Cloud Sleuth  

4.包含

* Distributed/versioned configuration    分布式/版本化配置

* Service registration and discovery     服务注册和发现

* Routing                         智能路由

* Service-to-service calls             service-to-service调用

* Load balancing                    负载均衡

* Circuit  Breakers                   断路器

* Global locks                      全局锁

* Leadership election and cluster state   leader选举和集群状态管理

* Distributed messaging              分布式消息

5.主要项目

| 项目名称                         | 项目职能                                                     |
| -------------------------------- | ------------------------------------------------------------ |
| Spring Cloud Config              | Spring Cloud 提供的分布式配置中心，为外部配置提供了客户端和服务端的支持。 |
| Spring Cloud Netflix             | 与各种Netflix OSS组件集成（Eureka，Hystrix，Zuul，Archaius等）。 |
| Spring Cloud Bus                 | 用于将服务和服务实例与分布式消息传递连接在一起的事件总线。用于跨群集传播状态更改（例如，配置更改事件）。 |
| Spring Cloud Cloudfoundry        | 提供应用程序与 Pivotal Cloud Foundry 集成。提供服务发现实现，还可以轻松实现受SSO和OAuth2保护的资源。 |
| Spring Cloud Open Service Broker | 为构建实现 Open service broker API 的服务代理提供了一个起点。 |
| Spring Cloud Cluster             | 提供Leadership选举，如：Zookeeper, Redis, Hazelcast, Consul等常见状态模式的抽象和实现。 |
| Spring Cloud Consul              | 封装了Consul操作，consul 是一个服务发现与配置工具，与Docker容器可以无缝集成。 |
| Spring Cloud Security            | 基于spring security的安全工具包，为你的应用程序添加安全控制。在Zuul代理中为负载平衡的OAuth2 rest客户端和身份验证头中继提供支持。 |
| Spring Cloud Sleuth              | Spring Cloud 提供的分布式链路跟踪组件，兼容zipkin、HTracer和基于日志的跟踪（ELK） |
| Spring Cloud Data Flow           | 大数据操作工具，作为Spring XD的替代产品，它是一个混合计算模型，结合了流数据与批量数据的处理方式。 |
| Spring Cloud Stream              | 数据流操作开发包，封装了与Redis,Rabbit、Kafka等发送接收消息。 |
| Spring Cloud CLI                 | 基于 Spring Boot CLI，可以让你以命令行方式快速建立云组件。   |
| Spring Cloud OpenFeign           | 一个http client客户端，致力于减少http client客户端构建的复杂性。 |
| Spring Cloud Gateway             | Spring Cloud 提供的网关服务组件                              |
| Spring Cloud Stream App Starters | Spring Cloud Stream App Starters是基于Spring Boot的Spring 集成应用程序，可提供与外部系统的集成。 |
| Spring Cloud Task                | 提供云端计划任务管理、任务调度。                             |
| Spring Cloud Task App Starters   | Spring Cloud任务应用程序启动器是SpringBoot应用程序，它可以是任何进程，包括不会永远运行的Spring批处理作业，并且在有限的数据处理周期后结束/停止。 |
| Spring Cloud Zookeeper           | 操作Zookeeper的工具包，用于使用zookeeper方式的服务发现和配置管理。 |
| Spring Cloud AWS                 | 提供与托管的AWS集成                                          |
| Spring Cloud Connectors          | 便于云端应用程序在各种PaaS平台连接到后端，如：数据库和消息代理服务。 |
| Spring Cloud Starters            | Spring Boot式的启动项目，为Spring Cloud提供开箱即用的依赖管理。 |
| Spring Cloud Contract            | Spring Cloud Contract是一个总体项目，其中包含帮助用户成功实施消费者驱动合同方法的解决方案。 |
| Spring Cloud Pipelines           | Spring Cloud Pipelines提供了一个固定意见的部署管道，其中包含确保您的应用程序可以零停机方式部署并轻松回滚出错的步骤。 |
| Spring Cloud Function            | Spring Cloud Function通过函数促进业务逻辑的实现。 它支持Serverless 提供商之间的统一编程模型，以及独立运行（本地或PaaS）的能力。 |

### 模块  

#### Eureka  
是为了解决各个模块之间的远程调用问题。系统与子系统之间不是在同一个环境下，那就需要**远程调用**。远程调用可能就会想到httpClient，WebService等等这些技术来实现。既然是远程调用，就必须知道ip地址，当服务部署到不同服务器上，如果其中一个服务A的IP改变了，那么就需要相应去更改服务A调用地址，不易维护。

**Eureka如何解决远程调用问题**

- 创建一个E服务，将A、B、C、D四个服务的信息都**注册**到E服务上，E服务维护这些已经注册进来的信息，这个E服务就是注册中心
- A、B、C、D四个服务都可以**拿到**Eureka(服务E)那份**注册清单**。A、B、C、D四个服务互相调用不再通过具体的IP地址，而是**通过服务名来调用**！

<img src="D:\软件\Markdown\typora-user-images\image-20200723143023306.png" alt="image-20200723143023306" style="zoom: 50%;" />

**Eureka机制**

* **服务提供者**   
  * **服务注册：**服务提供者启动时，通过发送REST请求的方式将自己注册到Eureka Server上
  * **服务续约：**注册完服务之后，**服务提供者会维护一个心跳**用来持续告诉Eureka Server: "我还活着 ” 
  * **服务下线：**当服务正常关闭时，服务提供者会触发一个服务下线的REST请求告诉Eureka Server。
* **服务消费者**
  * **服务获取：**当服务消费者启动时，它会发送一个REST请求给服务注册中心，来获取上面注册的服务清单
  * **服务调用：**服务消费者在获取服务清单后，**通过服务名可以获得具体提供服务的实例名和该实例的元数据信息**。在进行服务调用的时候，优先访问同处一个Zone中的服务提供方。

* **注册中心**
  * **失效剔除**：默认每隔一段时间（默认为60秒） 将当前清单中超时（默认为90秒）**没有续约的服务剔除出去**。
  * **自我保护：**EurekaServer 在运行期间，会统计心跳失败的比例在15分钟之内是否低于85%(通常由于网络不稳定导致)。 Eureka Server会将当前的**实例注册信息保护起来**， 让这些实例不会过期，尽可能**保护这些注册信息**。

#### **Ribbon**     

为了实现服务的**高可用**，我们可以将**服务提供者集群**，Ribbon实现客户端的负载均衡，默认的负载均衡策略是轮询。

#### Hystrix

如果我们在**调用多个远程服务时，某个服务出现延迟**,会怎么样？

在**高并发**的情况下，由于单个服务的延迟，可能导致**所有的请求都处于延迟状态**，甚至在几秒钟就使服务处于负载饱和的状态，资源耗尽，直到不可用，最终导致这个分布式系统都不可用，这就是“雪崩”。

针对上述问题， Spring Cloud Hystrix实现了**断路器、线程隔离**等一系列服务保护功能。

#### Feign

基于 Netflix Feign 实现，**整合**了 Spring Cloud Ribbon 与 Spring Cloud Hystrix, 除了整合这两者的强大功能之外，它还提 供了**声明式的服务调用**(不再通过RestTemplate)。

```
Feign是一种声明式、模板化的HTTP客户端。在Spring Cloud中使用Feign, 我们可以做到使用HTTP请求远程服务时能与调用本地方法一样的编码体验，开发者完全感知不到这是远程方法，更感知不到这是个HTTP请求。
```

#### Zuul

现在的架构很可能会设计成这样：

<img src="D:\软件\Markdown\typora-user-images\image-20200723172226070.png" alt="image-20200723172226070" style="zoom:50%;" />

这样的架构会有两个比较麻烦的问题：

1. **路由规则与服务实例的维护间题**：外层的负载均衡(nginx)需要**维护**所有的服务实例清单(图上的OpenService)
2. **签名校验、 登录校验冗余问题**：为了保证对外服务的安全性， 我们在服务端实现的微服务接口，往往都会有一定的**权限校验机制**，但我们的服务是独立的，我们**不得不在这些应用中都实现这样一套校验逻辑**，这就会造成校验逻辑的冗余。

为了解决上面这些常见的架构问题，**API网关**的概念应运而生。在SpringCloud中了提供了基于Netfl ix Zuul实现的API网关组件**Spring Cloud Zuul**。

Spring Cloud Zuul是这样解决上述两个问题的：

- SpringCloud Zuul通过与SpringCloud Eureka进行整合，将自身注册为Eureka服务治理下的应用，同时从Eureka中获得了所有其他微服务的实例信息。**外层调用都必须通过API网关**，使得**将维护服务实例的工作交给了服务治理框架自动完成**。
- 在API网关服务上进行统一调用来**对微服务接口做前置过滤**，以实现对微服务接口的**拦截和校验**。

Zuul天生就拥有线程隔离和断路器的自我保护功能，以及对服务调用的客户端负载均衡功能。也就是说：**Zuul也是支持Hystrix和Ribbon**。

#### SpringCloud Config

随着业务的扩展，我们的服务会越来越多，越来越多。每个服务都有自己的配置文件。

既然是配置文件，给我们配置的东西，那**难免会有些改动**的。

Spring Cloud Config项目是一个解决分布式系统的配置管理方案。它包含了Client和Server两个部分，**server提供配置文件的存储、以接口的形式将配置文件的内容提供出去，client通过接口获取数据、并依据此数据初始化自己的应用**。

- 简单来说，使用Spring Cloud Config就是将配置文件放到**统一的位置管理**(比如GitHub)，客户端通过接口去获取这些配置文件。
- 在GitHub上修改了某个配置文件，应用加载的就是修改后的配置文件。