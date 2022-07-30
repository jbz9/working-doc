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

### maven的scope

scope的作用是控制 dependency（依赖）的使用范围，默认值是compile。

范围：

| cope取值     | 有效范围（compile, runtime, test） | 说明                                                         | 依赖传递 | 例子        |
| :----------- | :--------------------------------- | ------------------------------------------------------------ | :------- | :---------- |
| **compile**  | all                                | 默认的scope，代表这个dependency依赖会参与项目的编译、测试、运行、打包阶段 | 是       | spring-core |
| **provided** | compile, test                      | 只参与编译和测试时，同时没有传递性                           | 否       | servlet-api |
| **runtime**  | runtime, test                      | 跳过编译，只参与运行和测试阶段                               | 是       | JDBC驱动    |
| **test**     | test                               | 只在项目的编译、测试阶段使用，不会随项目发布                 | 否       | JUnit       |
| **system**   | compile, test                      | 在系统中要以外部JAR包的形式提供，maven不会在repository查找它。需通过外部引入，不会在仓库中查找。例如一些特殊的jar我们或通过拷贝jar到web-info/lib下，这些jar就可以配置为system范围。 | 是       |             |

#### import

只能用在dependencyManagement，且仅用于<type>为pom的<dependency>。

<type>pom</type> :引入spring-cloud-dependencies项目的pom文件

<scope>import</scope>：将spring-cloud-dependencies项目的依赖（即dependencies标签中的所有依赖）引入到当前项目的dependencyManagement中，

```xml
 <dependencyManagement>
       <dependencies>
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

### 依赖传递

如有三个项目A、B、C，当前项目为A，A依赖于B，B依赖于C。则项目C在A中是什么样的依赖关系呢？ 
 我们可以根据B依赖于C的scope来判断：

- scope为test、provided、system时，则项目C被抛弃，A并不依赖于C
- 否则A依赖于C

即A中是否存在C的jar包，取决于B引入C时，设置的scope

### 依赖隔断

由依赖传递所述，maven的依赖关系是有传递性的。如：A–>B，B–>C，则A–>C。但有时候，项目A可能不是必需依赖C，因此需要在项目A中隔断对C的依赖。隔断依赖有2种方式：

1. 可选依赖（Optional Dependencies）
2. 依赖排除（Dependency Exclusions）

#### 可选依赖

在项目B上设置，配置optional选项，待选值为true/false。默认为false，此时依赖关系为强依赖。

```xml
<project>

  <dependencies>
    <!-- declare the dependency to be set as optional -->
    <dependency>
      <groupId>sample.ProjectC</groupId>
      <artifactId>Project-C</artifactId>
      <version>1.0</version>
      <scope>compile</scope>
      <optional>true</optional> <!-- value will be true or false only -->
    </dependency>
  </dependencies>
    
</project>
```

这段配置为项目B依赖于项目C的配置，由于配置了optional为true，则项目A就被隔断了与项目C的依赖关系。如果想依赖C，则需要在项目A中另行配置

#### 依赖排除

在项目A上设置，依赖排除用标签exclusions，样例如下：

```xml
<project>  

  <dependencies>  
    <dependency>  
      <groupId>sample.ProjectB</groupId>  
      <artifactId>Project-B</artifactId>  
      <version>1.0</version>  
      <scope>compile</scope>  
      <exclusions>  
        <exclusion>  
          <!-- declare the exclusion here -->  
          <groupId>sample.ProjectC</groupId>  
          <artifactId>Project-C</artifactId>  
        </exclusion>  
      </exclusions>   
    </dependency>  
  </dependencies>  
    
</project>  
```

此配置为项目A中的配置。当项目B依赖项目C时，并没有配置optional选项时，又不能更改项目B，则可以用此种方式隔断依赖关系。

### DepencyManagement

由于我们的模块很多，所以我们又抽象了一层，抽出一个itoo-base-parent来管理子项目的公共的依赖。为了项目的正确运行，必须让所有的子项目使用依赖项的统一版本，必须确保应用的各个项目的依赖项和版本一致，才能保证测试的和发布的是相同的结果。

 在我们项目顶层的POM文件中，我们会看到dependencyManagement元素。通过它元素来管理jar包的版本，让子项目中引用一个依赖而不用显示的列出版本号。Maven会沿着父子层次向上走，直到找到一个拥有dependencyManagement元素的项目，然后它就会使用在这个dependencyManagement元素中指定的版本号。

**dependencyManagement****里只是声明依赖，并不实现引入，因此子项目需要显示的声明需要用的依赖。如果不在子项目中声明依赖，是不会从父项目中继承下来的；只有在子项目中写了该依赖项，并且没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父pom;另外如果子项目中指定了版本号，那么会使用子项目中指定的jar版本。**

### Maven命令

**mvn dependency:tree** 

mvn dependency:tree -Dscope=compile

查看项目依赖关系

**mvn compile**

编译

### 私服

