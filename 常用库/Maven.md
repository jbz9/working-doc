#### maven
##### 常见配置  
 
```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.10.0</version>
    <exclusions>
        <exclusion>
        <artifactId>log4j-api</artifactId>
        <groupId>org.apache.logging.log4j</groupId>
        </exclusion>
    </exclusions>
</dependency>
  //exclusions 不下载 log4j-api jar包
log4j-core本身是依赖了log4j-api的，但是因为一些其他的模块也依赖了log4j-api，并且两个log4j-api版本不同，所以我们使用<exclusion>标签排除掉log4j-core所依赖的log4j-api，这样Maven就不会下载log4j-core所依赖的log4j-api了，也就保证了我们的项目中只有一个版本的log4j-api。
```