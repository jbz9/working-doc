# Gradle入门

参考：

[Gradle官网](https://docs.gradle.org/current/userguide/userguide.html)

## gradle项目结构

```shell
├── a-subproject
│   └── build.gradle
├── settings.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
```

gradle.properties位于项目根目录下，主要设置Gradle后台进程JVM内存大小、日记级别等等；
settings.gradle配置文件位于根目录下，用于指示Gradle在构建应用时应将哪些模块包含在内；
build.gradle为gradle的核心配置文件，负责整体项目的一些配置，类似maven的pom.xml；

## Gradle的scope

参考：[The Java Library Plugin (gradle.org)](https://docs.gradle.org/current/userguide/java_library_plugin.html)

api：当其他模块依赖于此模块时，此模块使用api声明的依赖包是可以被其他模块使用

implementation：当其他模块依赖此模块时，此模块使用implementation声明的依赖包只限于模块内部使用，不允许其他模块使用

api：当其他模块依赖于此模块时，此模块使用api声明的依赖包是可以被其他模块使用
implementation：当其他模块依赖此模块时，此模块使用implementation声明的依赖包只限于模块内部使用，不允许其他模块使用

compile作用同api，是Gradle 3.4以下的版本，使用的关键字，来自'java'插件，implementation和api是3.4以上使用的关键字，来自'java-library'插件

## 常用命令

**gradle init** 

初始化一个gradle项目

**gradlew clean**

清理项目下build目录

**gradlew build**

编译打包项目

**gradlew install**

打包安装到本地仓库

**gradlew uninstall**

卸载安装包

**gradlew dependencies**

gradlew dependencies --configuration runtimeClasspath

查看项目依赖树

**gradlew --offline**

离线模式，只使用本地缓存的依赖包

**gradlew --daemon**

使用守护线程，改善 Gradle 的启动和执行时间

**gradlew --no-daemon**

本次不使用

**gradlew --parallel --parallel-threads=N**

并行编译

### gradle build --scan

构建项目，并扫描形成图形化展示。

### gradle wrapper --gradle-version 6.0.1

更新项目使用的gradle版本

## 兼容性

向下兼容（Downward Compatibility）：高版本可以运行低版本的代码，即是新版本对旧版本的兼容

**向后兼容**（**Backward** Compatibility）：同向下兼容

向上兼容：低版本可以运行高版本的代码，即高版本也能被低版本兼容。

**向前兼容**（Upward Compatibility）：与向上兼容

前 **forward** 未来拓展，后 **backward** 兼容以前

gradle只保证主要版本（e.g. `1.x`, `2.x`, etc.）的向后兼容性，即高版本在大部分情况能够兼容低版本。[功能生命周期 (gradle.org)](https://docs.gradle.org/current/userguide/feature_lifecycle.html#backwards_compatibility)

## Gradlew

### 原理

gradlew是gradle Wrapper，Wrapper的意思就是包装。Gradle Wrapper 它是一个脚本，调用了已经声明的 Gradle 版本。

因为不是每个人的电脑中都安装了gradle，也不一定安装的版本是要编译项目需要的版本。所以我们在Gradle Wrapper 中声明项目需要的gradle版本。

然后用户只需要运行项目中的gradlew命令，就可以按照配置，自动下载申明的gradle版本，之后使用gradlew命令去执行clean、build等操作，执行gradlew命令时，就会去使用Gradle Wrapper 申明的gradle版本去执行gradle命令。

### 使用Gradle Wrapper

在项目根目录下直接运行`gradlew build`或者`gradlew.bat build`（根据操作系统自行选择）就会自动下载项目需要使用的`Gradle`。

**直接使用 gradlew  dependencies命令，第一次也会先去下载声明的gradle版本，再生成依赖树。**

### 目录结构

```shell
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar  
│       └── gradle-wrapper.properties
├── gradlew  
├── gradlew.bat  
```

在`gradle/wrapper`目录下就是`Gradle Wrapper`了。其中

- `gradle-wrapper.jar`就是下载项目构建使用的`Gradle`的下载器
- `gradle-wrapper.properties`就是就是`Gradle Wrapper`的配置文件了

而`gradlew`和`gradlew.bat`则分别是`UNIX`和`Windows`环境下调用包装器的脚本

优势：

将 wrapper 配置相关文件提交到代码仓库，用统一的 Gradle 版本进行构建工程，可以避免因为 Gradle 版本不统一而带来的问题。

条件

需要源码项目中使用了Gradle Wrapper

### 无网络的情况

使用gradlew需要联网下载gradle，如果在无法联网的情况下，将会下载失败。

解决方案：

提前下载好项目需要的gradle版本的zip包放到服务器上，然后更改项目根目录下gradle/wrapper/gradle-wrapper.properties文件，更改distributionUrl的值，更改为gradle安装包路径。

```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=file\:/d:/develop/gradle/gradle-7.5/.gradle/wrapper/dists/gradle-7.3.3-bin.zip
#distributionUrl=https\://services.gradle.org/distributions/gradle-7.3.3-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

### 项目使用

目前项目中使用的是gradle 命令生成依赖树，即需要先去下载固定的gradle版本，然后配置好gradle的环境变量，再通过gradle --no-daemon dependencies命令生成依赖树

1、更改gradle命令

目前的命令：gradle --no-daemon dependencies

需要更改为：gradlew --no-daemon dependencies

2、赋予gradlew 执行权限

目前通过第一阶段，将源码zip解压到临时目录后，gradlew 文件失去了执行权限，需要给gradlew 赋予执行权限

结论



## maven-wrapper

传统**maven**的使用流程

- 传统使用maven需要先到官网上下载
- 配置环境变量把mvn可执行文件路径加入到环境变量，以便之后使用直接使用mvn命令。
- 另外项目pom.xml文件描述的依赖文件默认是下载在用户目录下的.m2文件下的repository目录下。
- 再次，如果需要更换maven的版本，需要重新下载maven并替换环境变量path中的maven路径。

现在有了**maven-wrapper**，会获得以下特性

- 执行mvnw比如`mvnw clean` ，如果本地没有匹配的maven版本，直接会去下载maven，放在用户目录下的.m2/wrapper中
- 如果需要更换maven的版本，只需要更改项目当前目录下.mvn/wrapper/maven-wrapper.properties的distributionUrl属性值，更换对应版本的maven下载地址。mvnw命令就会自动重新下载maven。

作用:

mvnw是一个maven wrapper script,它可以让你在没有安装maven或者maven版本不兼容的条件下运行maven的命令.

### 项目初始化mvnw文件

如果你的项目没有mvnw文件，需要进行安装

```shell
mvn -N io.takari:maven:wrapper
或者
mvn -N io.takari:maven:0.7.7:wrapper -Dmaven=3.6.0
# 参数N（non-recursive）是指不在每个子模块中生成文件，只在当前目录下生成
```

目录结构：

```shell
├── .mvn 
│   └── wrapper 
│       ├── maven-wrapper.jar 
│       └── maven-wrapper.properties 
├── mvnw 
└── mvnw.cmd
```

### 生成依赖树

```shell
mvnw dependency:tree 
```

## 私服

### 依赖Nexus私服仓库

修改项目的build.gradle文件，在repositories函数的参数中加入maven {url **你的私服仓库地址**} 即可。

```groovy
//申明所有模块的私服
allprojects {
    repositories {
        maven { url "https://maven.aliyun.com/repository/public/" }
        maven { url "https://mvn-rep.hf.seczone.cn/nexus/content/repositories/snapshots/" }
        maven { url "https://10.0.0.101:8081/repository/maven-public/" }
        maven { url "https://mvn-rep.hf.seczone.cn/nexus/content/repositories/snapshots/" }
    }
}

或
//申明当前模块的私服
repositories {
        maven { url "https://maven.aliyun.com/repository/public/" }
        maven { url "https://mvn-rep.hf.seczone.cn/nexus/content/repositories/snapshots/" }
        maven { url "https://10.0.0.101:8081/repository/maven-public/" }
        maven { url "https://mvn-rep.hf.seczone.cn/nexus/content/repositories/snapshots/" }
}
```



# 安卓

## 依赖管理

### 本地依赖

`场景：多见于引用工程中libs目录下的jar、arr、so 等包`，[关于jar和arr的知识可以来这里科普 ~](https://blog.csdn.net/qq_20451879/article/details/80423642)

整体引用 - 二进制依赖（常见）

```java
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
}
```

单独引用 - 二进制依赖（常见）

```java
dependencies {
    implementation files('libs/aaa.jar', 'libs/bbb.jar')
    implementation files('libs/nkSdk.aar')
}
```

### 模块依赖

引用项目中的本地model 

```java
dependencies {
    implementation project(':projectL')
}
```

#### 远端依赖

二进制依赖（常见）

```java
dependencies {
    implementation 'androidx.appcompat:appcompat:1.0.2'
}
```

### gradle dependencies

gradlew  :common:dependencies
