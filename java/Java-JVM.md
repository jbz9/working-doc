## JVM  

### 1. JVM概念  

Java 虚拟机，运行在操作系统上。hotSpot

#### 1. 内存溢出OOM、内存泄漏、栈溢出

##### 1. **OutOfMemoryError**

OOM异常，java.lang.OutOfMemoryError: PermGen space





### 2. JVM内存结构

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16502903438581650290343033.png" style="zoom:67%;" />



#### 1. **Stack 栈**

每一个方法执行都会产生一个Stack,用来保存方法的一些信息，局部变量表，操作数帧，方法出口、动态链接。Stack 栈随着方法的调用结束而销毁。



Java栈中存放的是一个个的栈帧，每个栈帧对应一个被调用的方法，在栈帧中包括局部变量表(Local Variables)、操作数栈(Operand Stack)、指向当前方法所属的类的运行时常量池（运行时常量池的概念在方法区部分会谈到）的引用(Reference to runtime constant pool)、方法返回地址(Return Address)和一些额外的附加信息。当线程执行一个方法时，就会随之创建一个对应的栈帧，并将建立的栈帧压栈。当方法执行完毕之后，便会将栈帧出栈。



栈内存的大小可以有两种设置，固定值和根据线程需要动态增长。
在JVM栈这个数据区可能会发生抛出两种错误: 

① StackOverflowError 出现在栈内存设置成固定值的时候，当程序执行需要的栈内存超过设定的固定值会抛出这个错误。

② OutOfMemoryError 出现在栈内存设置成动态增长的时候，当JVM尝试申请的内存大小超过了其可用内存时会抛出这个错误。



#### 2  **Heap 堆**

是所有线程共享的内存区域，用来存放**对象和数组**，以及**字符串常量池**。也是JVM中最大的内存空间，进行JVM调优（Xms、Xmx）也是主要针对这块内存区域，它里面又分为伊甸园、2个幸存区、和老年代、元空间，GC垃圾回收用到的复制、标记算法也是主要针对这一块。

#### 3. Native Method Stack **本地方法栈**

用来管理本地方法的调用，它可以通过本地方法接口来访问JVM运行内存的数据。



当某个线程调用一个本地方法时，它就进入了一个全新的并且不再受虚拟机限制的世界。它和虚拟机拥有同样的权限。

- 本地方法可以通过本地方法接口来**访问虚拟机内部的运行时数据区**。
- 它甚至可以直接使用本地处理器中的寄存器
- 直接从本地内存的堆中分配任意数量的内存。

#### 4. PC Register 程序计数器

是线程私有的，用来记录线程的执行行号

#### 5. MetaSpace **元空间** JDK8

纯粹类的元数据信息，元空间处在本地内存当中，替换了JDK7的永久代，为了避免OOM异常

#### 5. Method Area **方法区** JDK7

存储类的信息，常量、静态变量。方法区也就是永久代（Permanet Generation），Java7及以前版本的Hotspot中方法区位于永久代中。

方法区是规范，永久代是Hotspot针对该规范进行的实现



### 3. 类加载器

**一个类只能有且只有一个类加载器对其进行加载**

如何保证只有一个类加载器加载呢？

#### **双亲委派机制**

它们之间的关系：启动类加载器（高）-扩展类加载器（中）—应用类加载器（低）

加载器顺序：

如果一个类加载器收到类加载的请求，那么首先它自己不会去加载这个请求，而是会把这个请求委托给它的父级加载器去加载，同样它的父级也是这样，那么所有的请求最会到**顶层的启动类加载器首先进行加载**，如果它无法进行加载，那么请求就会层层下发给它的子级加载器进行加载。

```java
/**
 * Project Name : learn
 * File Name    : Hello
 * Package Name : com.jiang.loader
 * Date         : 2022-04-19 16:40
 * Author       : jbz
 */
package com.jiang.learn.loader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author : jbz
 * @ClassName : Hello
 * @Date : 2022-04-19 16:40
 * @Description :
 */
public class Hello {

    public static void main(String[] args) {
        Hello hello = new Hello();
        //app loader
        System.out.println(hello.getClass().getClassLoader());
				//sun.misc.Launcher$AppClassLoader@18b4aac2
      
        // bootstrap loader
        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (URL url : urls) {
            System.out.println(url);
        }
				//file:/E:/develop/java/jdk1.8.0_92/jre/lib/rt.jar
      
        //extension loader
        URL[] extensions = ((URLClassLoader) ClassLoader.getSystemClassLoader().getParent()).getURLs();
        for (URL url : extensions) {
            System.out.println(url);
        }
      //file:/E:/develop/java/jdk1.8.0_92/jre/lib/ext/zipfs.jar
    }

}
```

**最终加载顺序：启动类加载器 ->扩展类加载器 ->应用类加载器**

* 启动类加载器-**BootstrapClassLoader**：负责加载 jre/lib目录下的jar包和类
* 扩展类加载器-**ExtensionClassLoader**：主要负责加载目录 jre/lib/ext 目录下的jar包和类
* 应用程序类加载器-**AppClassLoader**：负责加载当前应用classpath下的所有jar包和类。

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1650358310129image-20201014151857832.png" style="zoom:50%;" />



一个类的完整生命周期

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16503596092301650359609162.png" style="zoom:50%;" />

① 类加载器

如果 **JVM** 想要执行这个 **.class** 文件，我们需要将其装进一个 **类加载器** 中，它就像一个搬运工一样，会把所有的 **.class** 文件全部搬进JVM里面



### 4. JVM 调优

#### 1. 设置堆内存大小

Java堆区用于存储Java对象实例，那么堆的大小在JVM启动时就已经设定好了，大家可以通过选项"-Xmx"和"-Xms"来进行设置。例如：

> -Xms10m：最小堆内存 -Xmx10m：最大堆内存

- “**-Xms**"用于表示堆区的起始内存，等价于`-XX:InitialHeapSize`
- “-**Xmx**"则用于表示堆区的最大内存，等价于`-XX:MaxHeapSize`

一旦堆区中的内存大小超过“-Xmx"所指定的最大内存时，将会抛出OutofMemoryError异常（俗称OOM异常）。

通常会将-Xms和-Xmx两个参数配置相同的值，其目的是**为了能够在Java垃圾回收机制清理完堆区后不需要重新分隔计算堆区的大小，从而提高性能**。

默认情况：

- 初始内存大小：物理电脑内存大小 / 64
- 最大内存大小：物理电脑内存大小 / 4



### 5. Class 文件

使用JDK自带命令javap查看class文件的汇编字节码

`javap.exe -v Hello.class`

Hello.java

```java
/**
 * Project Name : learn
 * File Name    : Hello
 * Package Name : com.jiang.collection
 * Date         : 2022-04-18 23:14
 * Author       : jbz
 */
package com.jiang.learn.collection;

/**
 * @author : jbz
 * @ClassName : Hello
 * @Date : 2022-04-18 23:14
 * @Description :
 */
public class Hello {
    public static void main(String[] args) {
        System.out.println("hello");
    }

    public void hello(String name) {
        System.out.println("hello: " + name);
    }
}
```

Hello.class

```java
Classfile /E:/learn/github_repo/working-doc/code-learn/target/classes/com/jiang/collection/Hello.class
  Last modified 2022-4-18; size 877 bytes
  MD5 checksum 94f89fb1614c70c545cfc425e124c8e2
  Compiled from "Hello.java"
public class com.jiang.collection.Hello
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #11.#30        // java/lang/Object."<init>":()V
   #2 = Fieldref           #31.#32        // java/lang/System.out:Ljava/io/PrintStream;
   #3 = String             #24            // hello
   #4 = Methodref          #33.#34        // java/io/PrintStream.println:(Ljava/lang/String;)V
   #5 = Class              #35            // java/lang/StringBuilder
   #6 = Methodref          #5.#30         // java/lang/StringBuilder."<init>":()V
   #7 = String             #36            // hello:
   #8 = Methodref          #5.#37         // java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
   #9 = Methodref          #5.#38         // java/lang/StringBuilder.toString:()Ljava/lang/String;
  #10 = Class              #39            // com/jiang/collection/Hello
  #11 = Class              #40            // java/lang/Object
  #12 = Utf8               <init>
  #13 = Utf8               ()V
  #14 = Utf8               Code
  #15 = Utf8               LineNumberTable
  #16 = Utf8               LocalVariableTable
  #17 = Utf8               this
  #18 = Utf8               Lcom/jiang/collection/Hello;
  #19 = Utf8               main
  #20 = Utf8               ([Ljava/lang/String;)V
  #21 = Utf8               args
  #22 = Utf8               [Ljava/lang/String;
  #23 = Utf8               MethodParameters
  #24 = Utf8               hello
  #25 = Utf8               (Ljava/lang/String;)V
  #26 = Utf8               name
  #27 = Utf8               Ljava/lang/String;
  #28 = Utf8               SourceFile
  #29 = Utf8               Hello.java
  #30 = NameAndType        #12:#13        // "<init>":()V
  #31 = Class              #41            // java/lang/System
  #32 = NameAndType        #42:#43        // out:Ljava/io/PrintStream;
  #33 = Class              #44            // java/io/PrintStream
  #34 = NameAndType        #45:#25        // println:(Ljava/lang/String;)V
  #35 = Utf8               java/lang/StringBuilder
  #36 = Utf8               hello:
  #37 = NameAndType        #46:#47        // append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
  #38 = NameAndType        #48:#49        // toString:()Ljava/lang/String;
  #39 = Utf8               com/jiang/collection/Hello
  #40 = Utf8               java/lang/Object
  #41 = Utf8               java/lang/System
  #42 = Utf8               out
  #43 = Utf8               Ljava/io/PrintStream;
  #44 = Utf8               java/io/PrintStream
  #45 = Utf8               println
  #46 = Utf8               append
  #47 = Utf8               (Ljava/lang/String;)Ljava/lang/StringBuilder;
  #48 = Utf8               toString
  #49 = Utf8               ()Ljava/lang/String;
{
  public com.jiang.collection.Hello();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 17: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/jiang/collection/Hello;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #3                  // String hello
         5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
      LineNumberTable:
        line 19: 0
        line 20: 8
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       9     0  args   [Ljava/lang/String;
    MethodParameters:
      Name                           Flags
      args

  public void hello(java.lang.String);
    descriptor: (Ljava/lang/String;)V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=2, args_size=2
         0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: new           #5                  // class java/lang/StringBuilder
         6: dup
         7: invokespecial #6                  // Method java/lang/StringBuilder."<init>":()V
        10: ldc           #7                  // String hello:
        12: invokevirtual #8                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        15: aload_1
        16: invokevirtual #8                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        19: invokevirtual #9                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
        22: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        25: return
      LineNumberTable:
        line 23: 0
        line 24: 25
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      26     0  this   Lcom/jiang/collection/Hello;
            0      26     1  name   Ljava/lang/String;
    MethodParameters:
      Name                           Flags
      name
}
```

