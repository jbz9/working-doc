### maven 将jar打入本地仓库
1. 将jar包打包到本地仓库:进入到jar所在位置，执行maven命令
```shell 
mvn install:install-file -Dfile=tmp-xk-common-1.0.jar -DgroupId=com.ustcinfo.ishare.tmp -DartifactId=tmp-xk-common -Dversion=1.0 -Dpackaging=jar
```
pom文件引入
```xml
    <dependency>
      <groupId>com.ustcinfo.ishare.tmp</groupId>
      <artifactId>tmp-xk-common</artifactId>
      <version>1.0</version>
  </dependency>
```
### maven依赖   
原则一：最短路径原则 

假如引入了2个Jar包A和B，都传递依赖了Z这个Jar包：

> A -> X -> Y -> Z(2.5)
>
> B -> X -> Z(2.0)

此时，B的依赖路径较A短，那么Z实际使用的是Z(2.0)这个版本

原则二：最先声明原则

如果路径长短一样，优先选最先声明的那个。

> A -> Z(3.0)
>
> B -> Z(2.5)

这里A最先声明，所以传递过来的Z选择用3.0版本的。即pom文件中dependence中定义的顺序

### maven的scope作用域

scope的作用是控制 dependency（依赖）的使用范围，默认值是compile。

范围：

| cope取值     | 有效范围（compile, runtime, test） | 依赖传递 | 例子        |
| :----------- | :--------------------------------- | :------- | :---------- |
| **compile**  | all                                | 是       | spring-core |
| **provided** | compile, test                      | 否       | servlet-api |
| **runtime**  | runtime, test                      | 是       | JDBC驱动    |
| **test**     | test                               | 否       | JUnit       |
| **system**   | compile, test                      | 是       |             |

### spring-boot-maven-plugin

多模块需要从根目录开始install

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1625478999823-1625478999814.png)

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1625479049538-1625479049527.png)

BOOT-INF/classes：目录存放应用编译后的class文件。

BOOT-INF/lib：目录存放应用依赖的第三方JAR包文件。

META-INF：目录存放应用打包信息(Maven坐标、pom文件)和MANIFEST.MF文件。

org：目录存放SpringBoot相关class文件。

MANIFEST.MF内容

```java
Manifest-Version: 1.0
Spring-Boot-Classpath-Index: BOOT-INF/classpath.idx
Archiver-Version: Plexus Archiver
Built-By: jbz
Spring-Boot-Layers-Index: BOOT-INF/layers.idx
//入口地址
Start-Class: com.ustcinfo.ithink.neop.CnccNeopNtiApplication
Spring-Boot-Classes: BOOT-INF/classes/
Spring-Boot-Lib: BOOT-INF/lib/
Spring-Boot-Version: 2.5.2
Created-By: Apache Maven 3.6.0
Build-Jdk: 1.8.0_40
//可执行jar包启动器：JarLauncher
Main-Class: org.springframework.boot.loader.JarLauncher

```

作用：将springboot打成一个可执行的jar，包含所有第三方jar，只能使用在springboot项目中。

```xml
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
```

### 继承和引用

Maven的继承和Java的继承一样，是无法实现多继承的。可以使用pom、import来实现多继承

父工程pom

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.10.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <groupId>cn.china3y</groupId>
    <artifactId>myspringcloud</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>myspringcloud</name>
    <description>Demo project for Spring Boot</description>
    
    <dependencyManagement>
            <dependencies>
        	    <!-- <type>pom</type>把spring-cloud-dependencies引入为pom 文件 -->
                <!-- <scope>import</scope>解决单继承问题，类似parent标签，把spring-cloud-dependencies引入到dependencyManagement -->

            <dependency>
                  <groupId>org.springframework.cloud</groupId>
                  <artifactId>spring-cloud-dependencies</artifactId>
                  <version>Greenwich.RELEASE</version>
                  <type>pom</type>
                  <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

子工程pom

```xml
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            <!-- 这里会自动引入版本，类似parent标签继承 -->
</dependency>

//注意使用import标签时，不再使用<parent>标签
//表示将父项目的dependencyManagement拿到本POM中，不再继承parent
<dependencyManagement>
		<dependencies>
			<dependency>
				    <groupId>cn.china3y</groupId>
					<artifactId>myspringcloud</artifactId>
					<version>0.0.1-SNAPSHOT</version>
					<type>pom</type>//必须是type=pom
					<scope>import</scope>//必须是scope=import
			</dependency>
		</dependencies>
</dependencyManagement>

```





