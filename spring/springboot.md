#### 简介

##### 优点

springboot减少了很多配置，提高了开发和部署的效率

##### 启动类

SpringBoot有一个Application 的入口类，作为工程的启动类。

@SpringBootApplication是启动类的核心注解，它是一个组合注解：@Configuration、@EnableAutoConfiguration、@ComponentScan

**@EnableAutoConfiguration 让 Spring Boot 根据类路径中的 jar 包依赖为当前项目进行自动配置**，例如，添加了 spring-boot-starter-web 依赖，会自动添加 Tomcat 和 Spring MVC 的依赖，那么 Spring Boot 会对 Tomcat 和 Spring MVC 进行自动配置。

SpringBoot会自动扫描和启动类同级包以及以下包的Bean

##### 启动原理

通过启动

##### SPI

Service Provider Interface，服务发现机制。将类的全限定名配置在文件中，并由服务加载器读取配置文件，加载实现类。这样可以在运行时，动态为接口替换实现类

##### 自动配置原理

通过enableAutoConfiguration注解实现的，这个注解里面包含了AutoConfigurationPackage自动配置包注解和Import注解。

①会扫描META/INF目录下的spring.factories文件，这个文件的数据格式是key/value，有一个EnableAutoConfiguration的key对应的value值是AutoConfigure**所有自动配置类的全限定名**

②通过自动配置类上的ConditionalOnClass条件注解进行筛选，只有在classpath路径下找到依赖类，才会通过IOC容器去实例化bean

```xml
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration,\
org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration,\
org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration,\
```

条件注解：

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

##### Servlet容器

##### 定义一个starter

①定义一个maven项目，引入spring-boot-starter依赖。

②然后定义一个配置类、一个自动装配类。配置类用ConfigurationProperties注解；自动装配类用ConditionalOnClass、EnableConfigurationProperties、ConditionalOnProperty、Configuration注解以及@bean注解注册bean。

③最后在META/INF创建一个spring.factories文件，把自动配置类的路径写进去

##### SpringBoot为什么可以使用Jar包启动

①使用 `spring-boot-maven-plugin`插件把依赖都BOOT-INF\lib目录下，类加载器会从这里寻找依赖。

②spring-boot-loader

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

