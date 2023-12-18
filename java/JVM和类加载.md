## 1、JVM  

### JVM概念  

Java 虚拟机，运行在操作系统上。JVM负责将Java源代码编译成字节码，并在程序运行时去执行这些字节码。

### JVM类型



### JVM内存结构

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16502903438581650290343033.png" style="zoom:67%;" />



![1700753669337Java内存模型-JVM内存结构.drawio.png](https://fastly.jsdelivr.net/gh/jbz9/picture@main/image/1700753669337Java%E5%86%85%E5%AD%98%E6%A8%A1%E5%9E%8B-JVM%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.drawio.png)

JVM内存结构：

| 名称          | 特征         | 配置参数 | 异常               | 作用                                                 |
| ------------- | ------------ | -------- | ------------------ | ---------------------------------------------------- |
| 方法区/元空间 | 线程共享     |          |                    | 存储：类的信息、静态变量、常量、                     |
| 堆内存        | **线程共享** |          | OutOfMemoryError   | 存储：对象实例、字符串常量池                         |
| 虚拟机栈      | 线程独占     |          | StackOverflowError | 方法运行产生的虚拟机栈，存储：局部变量、方法出口信息 |
| 本地方法栈    | 线程独占     |          |                    | 本地方法产生的虚拟机栈                               |
| 程序计数器    | 线程独占     |          |                    | 当前线程执行的字节码指令地址                         |

**Stack 栈**

**每一个方法执行都会产生一个Stack,用来保存方法的一些信息，局部变量表，操作数帧，方法出口、动态链接。**Stack 栈随着方法的调用结束而销毁。

Java栈中存放的是一个个的栈帧，每个栈帧对应一个被调用的方法，在栈帧中包括局部变量表(Local Variables)、操作数栈(Operand Stack)、指向当前方法所属的类的运行时常量池（运行时常量池的概念在方法区部分会谈到）的引用(Reference to runtime constant pool)、方法返回地址(Return Address)和一些额外的附加信息。当线程执行一个方法时，就会随之创建一个对应的栈帧，并将建立的栈帧压栈。当方法执行完毕之后，便会将栈帧出栈。

栈内存的大小可以有两种设置，固定值和根据线程需要动态增长。
在JVM栈这个数据区可能会发生抛出两种错误: 

① StackOverflowError 出现在栈内存设置成固定值的时候，当程序执行需要的栈内存超过设定的固定值会抛出这个错误。

② OutOfMemoryError 出现在栈内存设置成动态增长的时候，当JVM尝试申请的内存大小超过了其可用内存时会抛出这个错误。

**Heap 堆**

是所有线程共享的内存区域，用来存放**对象和数组**，以及**字符串常量池**。也是JVM中最大的内存空间，进行JVM调优（Xms、Xmx）也是主要针对这块内存区域，它里面又分为新生代、老年代、和元空间，GC垃圾回收用到的复制、标记算法也是主要针对这一块。

* 新生代：新生代还被进一步划分为 Eden 区、From Survivor 0、To Survivor 1 区，默认的虚拟机配置比例是Eden：from ：to = 8:1:1
  * Eden区：伊甸园 
  * Survivor（sərˈvaɪvər） 0（幸存）
  * Survivor 1
* 老年代

**Native Method Stack 本地方法栈**

**本地方法调用产生的栈帧**，它可以通过本地方法接口来访问JVM运行内存的数据。

当某个线程调用一个本地方法时，它就进入了一个全新的并且不再受虚拟机限制的世界。它和虚拟机拥有同样的权限。

- 本地方法可以通过本地方法接口来**访问虚拟机内部的运行时数据区**。
- 它甚至可以直接使用本地处理器中的寄存器
- 直接从本地内存的堆中分配任意数量的内存。

**PC Register 程序计数器**

记录线程的执行的内存地址

**MetaSpace 元空间 JDK8**

存储类的元数据信息，元空间使用的是本地内存，不是JVM内存，它替换了JDK7的永久代，为了避免OOM异常

**Method Area 方法区 JDK7**

存储类的信息，常量、静态变量。方法区也就是永久代（Permanet Generation），Java7及以前版本的Hotspot中方法区位于永久代中。

方法区是规范，永久代是Hotspot针对该规范进行的实现

实例

```java
public class MemoryExample {
  // 静态变量存储在方法区
  private static String staticVariable = "Static Variable";
// 实例变量存储在堆中
private String instanceVariable;

public MemoryExample(String instanceVariable) {
    // 构造方法中的局部变量存储在虚拟机栈中
    this.instanceVariable = instanceVariable;
}

public void exampleMethod() {
    // 方法中的局部变量也存储在虚拟机栈中
    int localVar = 42;

    // 操作数栈中存储计算的中间结果
    int result = localVar * 2;

    // 方法中调用其他方法，会创建新的栈帧
    otherMethod();
}

private void otherMethod() {
    // 其他方法的局部变量
    String localString = "Local String";

    // 程序计数器存储当前线程执行的字节码指令地址
    int address = 0;

    // 本地方法调用，相关信息存储在本地方法栈中
    nativeMethod();
}

private native void nativeMethod();

public static void main(String[] args) {
    // main 方法执行时，会创建主线程的虚拟机栈
    MemoryExample example = new MemoryExample("Hello, Memory!");
    example.exampleMethod();
}
} 
```

## 2、垃圾回收机制

### 垃圾回收器

1、分为**新生代、老年代和整堆**收集器：

* **新生代收集器（复制算法）：主要使用的是复制算法，Serial、ParNew、Parallel Scavenge**

* **老年代收集器（标记算法）：主要使用的是标记算法，CMS（标记-清理）、Serial Old（标记-整理）、Parallel Old（标记整理）**

* **整堆收集器： G1（一个Region中是标记-清除算法，2个Region之间是复制算法）**

#### 新生代

1. **Serial串行回收器**

   * **原理：使用单线程，运行时，会停止应用程序，出现stop-the-world，实现使用的是复制算法**

   * **使用：`-XX:+UseSerialGC`**

   - 适用场景： 适用于小型应用和客户端应用，单线程执行垃圾收集。
   - 特点： 简单、高效，适合单处理器环境。

2. **ParNew并行回收器**

   * **原理**：ParNew其实就是Serial的`多线程`版本，除了多线程之外，其它是和Serial一样的，包括Serial收集器可用的所有控制参数、收集算法、Stop The Worl、.对象分配规则、回收策略等都与Serial收集器完全一样。
   * 使用：` -XX:+UseParNewGC ` ` -XX:ParallelGCThreads`来设置线程数

   - 适用场景： 适用于多核处理器的服务器环境，关注吞吐量。
   - 特点： 多线程并行执行垃圾收集，适合对吞吐量要求较高的应用。

3. **Parallel Scavenge并行回收器**

   * 使用：`-XX:+UseParallelGC`
   * 特点：**注重提高吞吐量，适用于那些对响应时间要求相对较低，但对吞吐量要求较高的应用。**

#### 老年代

1. **Serial Old收集器-标记整理算法**：

   * 原理：是Seriall收集器的老年代版本，它同样是一个单线程（串行）收集器，使用标记整理算法。

2. **Parallel Old收集器-标记整理算法**

   * 原理：是Parallel Scavenge收集器的老年代版本，使用多线程和"标记-整理”算法。

3. **CMS（Concurrent Mark-Sweep）垃圾回收器**

   * 原理:**真正意义上的并发收集器，能够让垃圾收集线程与用户线程（基本上）进行并发工作。使用的标记-清清除算法，**

   * 使用：`-XX:+UseConcMarkSweepGC `

   - **适用场景：** 适用于对低停顿时间有要求的应用，如Web应用。
   - **特点：并发收集、低停顿，但可能会造成碎片。**垃圾回收过程中与应用程序线程并发执行，不需要停顿整个应用程序。

   

#### 整堆

1. **G1（Garbage-First，JDK11 引入）垃圾回收器**
   - **适用场景：** 适用于大内存、、CPU核数高、对低停顿时间有要求的应用。
   - **特点：** 使用分代垃圾收集算法，将堆划分为多个区域，以优化吞吐量和停顿时间。

2. **ZGC（Z Garbage Collector）**

- **适用场景：** 适用于对停顿时间要求非常低的应用，如需要毫秒级别的响应时间。
- **特点：** 与 CMS 中的 ParNew 和 G1 类似，ZGC 也采用标记-复制算法，不过 ZGC 对该算法做了改进。采用并发处理方式，几乎所有垃圾收集的工作都可以在应用线程的同时进行，以最小化停顿时间。

要验证 `-XX:+UseSerialGC` 是否生效，可以通过 Java 虚拟机的垃圾回收日志来确认。在启动 Java 应用程序时，添加 `-XX:+PrintCommandLineFlags` 参数，这样在启动时会打印出 JVM 的配置参数。

`java -XX:+UseSerialGC -XX:+PrintCommandLineFlags -jar YourApplication.jar`

`stop-the-world`。它会在任何一种GC算法中发生。stop-the-world 意味着JVM因为需要执行GC而`停止`应用程序的执行。

当stop-the-world 发生时，除GC所需的线程外，所有的`线程`都进入`等待`状态，直到GC任务完成。GC优化很多时候就是减少stop-the-world 的发生。对于 JVM 的调优，很多情况下也是在想办法对 Full GC 进行调优。

### 调优命令有哪些

**jps**

JVM Process Status 显示JVM进程

**jstat**

JVM statistics Monitoring JVM监控工具，统计信息监控

**jmap**

JVM Memory Map 生成heap dump文件

**jstack**

生成JVM当前时刻的线程快照

**jinfo**

JVM Configuration info  查看JVM运行参数。

### Minor GC与Full GC分别在什么时候发生？

Minor GC也叫young GC，新生代内存不够的时候发生YGC

Full GC 老年代内存不够的时候发生Full GC

### 需要JVM调优的情况

一般情况下不需要调优，只有如果出现了问题，比如出现OOM内存不足异常，频繁出现FUll GC 或者以及Full GC时间比较长,以及如果项目使用本地缓存，而且本地缓存比较大，也需要调整XMS和XMX初始内存和最大内存的大小，还有，**在实际调整的时候，最好还是通过监控工具比如JVisualVM、Prometheus来监控JVM进程，动态调整观察JVM的情况**

* 项目中用到了本地缓存，而且本地缓存的内存用的比较大
* FUll GC 比较频繁
* Full GC的时间比较长（超过1秒）
* 堆内存的老年代

### 垃圾回收算法

**①标记-清除算法 (Mark-Sweep Algorithm):**

- **标记阶段：** **遍历所有的可达对象，并给对象上打上标记**，标记这些对象是活动的（reachable）。
- **清除阶段：** 遍历整个堆Heap，**清除未标记的对象**，即认为这些对象是垃圾，将它们回收释放内存。

​    **优点：** 不需要额外的内存空间。

​    **缺点：** 会产生内存碎片，清除阶段可能导致停顿时间较长。

**②标记-整理算法 (Mark-Compact Algorithm)**：

- **标记阶段（Marking Phase）：** 同标记-清除算法，标记所有可达对象。
- **整理阶段（Compact Phase）：** 将所有活动对象向一端移动，然后清理掉其余的空间。

  **优点：** 避免了内存碎片，相对于标记-清除算法有更好的空间利用。

  **缺点：** 需要额外的内存空间来进行整理，可能会导致内存拷贝开销。

**③复制算法 (Copying Algorithm)**：

- **划分为两个半区（Semi-spaces）：** 将堆分为两个相等大小的半区，每次只使用其中一个半区。
- **标记-复制（Mark-Copy）：** 将活动对象标记后，将其复制到另一半区，并清理原半区。

 **优点：** 避免了内存碎片，垃圾回收时只需要操作半区的内存。

  **缺点：** 需要额外的内存空间来存储活动对象。

**分代回收算法**：

创建对象，分配到Eden区，当Eden区空间满了，就触发一次Young GC，将还在使用的对象复制到幸存区From,这样Eden被清空，以供继续存储对象，当Eden再次满了的时候，再触发一次Young GC，将Eden和幸存From区中还在被使用的对象复制到幸存区的to区，下一次，Young GC则是将Eden和To区中还在使用的对象放入到From区，这样，经过多次GC，有些对象会在From和To区经过多次复制，都没有被释放，那么到达一个阈值之后，这些对象就将放到老年代，如果老年代空间也用完，就会触发Full GC全量回收。

### 垃圾回收器有哪些

新生代收集器：Serial、 ParNew 、 Parallel Scavenge

 老年代收集器： CMS 、Serial Old、Parallel Old

整堆收集器： G1 ， ZGC

### 如何判断对象死亡

#### 引用计数法（Reference Counting）

加1减1。如果被引用就+1；如果引用被释放就—1；如果计数器=0，就会被GC

#### 可达性分析（Reachability Analysis）

### JVM调优方案

xms(最小)、xmx（最大）、xmn（新生代大小）、NewRatio（新生代和老年代占比）、SurvivorRatio（伊甸园和幸存区占比）

```shell
-Xms
  堆内存初始容量大小（包括新生代和老年代）， 例如：-Xms 20M
-Xmx
  堆内存（最大）容量  例如：-Xmx 30M
注意：建议将 -Xms 和 -Xmx 设为相同值，避免每次垃圾回收完成后JVM重新分配内存！
-Xmn
  新生代容量大小 例如：-Xmn 10M
-XX SurvivorRatio
  设置新生代中 Eden、S0、S1的比例，默认是 8:1:1 例如：-XX： SurvivorRatio=8 代表比例8：1：1
  注：没有直接设置老年代的参数，可以通过设置 堆空间大小和新生代空间大小来控制老年代大小。
  当Java堆内存没有存够的空间去分配实例，也无法扩展内存，将会抛出内存溢出的异常，OutOfMemoryError 
```

#### 调整内存大小

场景：GC回收比较频繁

Java堆区用于存储Java对象实例，那么堆的大小在JVM启动时就已经设定好了，大家可以通过选项"-Xmx"和"-Xms"来进行设置。例如：

> ```shell
> //设置堆初始值
>  指令1：-Xms2g
>  指令2：-XX:InitialHeapSize=2048m
>  
>  //设置堆区最大值
>  指令1：`-Xmx2g` 
>  指令2： -XX:MaxHeapSize=2048m
>  
>  //新生代内存配置
>  指令1：-Xmn512m
>  指令2：-XX:MaxNewSize=512m
>  
> nohup java -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xms1024m -Xmx1024m -Xmn256m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -jar /jar包路径 
> ```

- “**-Xms**"用于表示堆区的起始内存，等价于`-XX:InitialHeapSize`
- “-**Xmx**"则用于表示堆区的最大内存，等价于`-XX:MaxHeapSize`

一旦堆区中的内存大小超过“-Xmx"所指定的最大内存时，将会抛出OutofMemoryError异常（俗称OOM异常）。

通常会将-Xms和-Xmx两个参数配置相同的值，其目的是**为了能够在Java垃圾回收机制清理完堆区后不需要重新分隔计算堆区的大小，从而提高性能**。

默认情况：

- 初始内存大小：物理电脑内存大小 / 64
- 最大内存大小：物理电脑内存大小 / 4

#### 设置GC时间

现象：程序间接性的卡顿

原因：如果没有确切的停顿时间设定，垃圾收集器以吞吐量为主，那么垃圾收集时间就会不稳定。

```text
//GC停顿时间，垃圾收集器会尝试用各种手段达到这个时间
 -XX:MaxGCPauseMillis 
```

### 内存溢出OOM、内存泄漏、栈溢出

OOM异常（OutOfMemoryError）异常： java.lang.OutOfMemoryError: PermGen space

当JVM尝试在堆中创建新的对象实例，而堆内存耗尽时，就会抛出 `OutOfMemoryError`。

### OOM问题排查

1、先 `ps -ef | grep 应用名称`找到进程号

2、再用jstat查看GC

`jstat -gcutil pid interval(ms)`

3、看到的问题

老年代快要被占满、full GC次数非常多；原因：老年代有需要无法被gc回收的对象，导致老年代满了

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16535381182381653538117252.png" style="zoom:50%;" />

**结果说明：**

S0: 新生代中Survivor space 0区已使用空间的百分比

S1: 新生代中Survivor space 1区已使用空间的百分比

E: 新生代已使用空间的百分比

**O: 老年代已使用空间的百分比**

M：元数据区使用比例

CCS：压缩使用比例

YGC: 从应用程序启动到当前，发生Yang GC 的次数

YGCT: 从应用程序启动到当前，Yang GC所用的时间【单位秒】

**FGC: 从应用程序启动到当前，发生Full GC的次数**

FGCT: 从应用程序启动到当前，Full GC所用的时间

GCT: 从应用程序启动到当前，用于垃圾回收的总时间【单位秒】

4、使用阿里[Arthas](https://arthas.aliyun.com/zh-cn/)的查看堆栈信息

5、具体使用

使用Arthas dashboard命令查看详细的内存信息；

dashboard

使用Arthas heapdump命令查看具体的堆栈信息；

heapdump /tmp/dump.hprof

6、使用JDK的`VisualVM`去分析dump文件

jvisualvm.exe

查看类、实例数量、排名靠前的的实例

### Class 文件

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

### 对象一定分配在堆中吗？逃逸分析

不一定，有可能会发生逃逸分析。

会根据对象的引用范围，决定是否分配到堆内存或者是栈内存

逃逸分析 Escape Analysis

## 3、类加载

### 类加载器

类加载器（Class Loader）是Java虚拟机（JVM）的一部分，负责**将类的字节码加载到内存中，并生成对应的`Class`对象，一个类只能有且只有一个类加载器对其进行加载。**

### **双亲委派机制**

如何保证只有一个类加载器加载呢？双亲委派策略

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

### 类加载机制

![1700753669337Java内存模型-JVM内存结构.drawio.png](https://fastly.jsdelivr.net/gh/jbz9/picture@main/image/1700753669337Java%E5%86%85%E5%AD%98%E6%A8%A1%E5%9E%8B-JVM%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.drawio.png)

计算机只认识0和1。这意味着任何语言编写的程序最终都需要经过编译器编译成机器码才能被计算机执行。

通过JVM，我们的源代码不再必须根据不同平台翻译成0和1，而是间接翻译成字节码，储存字节码的文件再交由运行于不同平台上的JVM虚拟机去读取执行，从而实现一次编写，到处运行的目的。

```shell
cafe babe 0000 0034 002a 0a00 0800 1d07
001e 0800 1f0a 0002 0020 0800 210b 0022
0023 0700 2407 0025 0100 0b75 7365 7253
6572 7669 6365 0100 2c4c 636f 6d2f 6a69
616e 672f 6c65 6172 6e2f 7370 7269 6e67
2f73 6572 7669 6365 2f55 7365 7253 6572
7669 6365 3b01 0019 5275 6e74 696d 6556
6973 6962 6c65 416e 6e6f 7461 7469 6f6e
7301 001b 4c6a 6176 6178 2f61 6e6e 6f74
6174 696f 6e2f 5265 736f 7572 6365 3b01
0006 3c69 6e69 743e 0100 0328 2956 0100
0443 6f64 6501 000f 4c69 6e65 4e75 6d62
6572 5461 626c 6501 0012 4c6f 6361 6c56
6172 6961 626c 6554 6162 6c65 0100 0474
6869 7301 001e 4c63 6f6d 2f6a 6961 6e67
2f6c 6561 726e 2f73 7072 696e 672f 4865
6c6c 6f3b 0100 046d 6169 6e01 0016 285b
4c6a 6176 612f 6c61 6e67 2f53 7472 696e
673b 2956 0100 0461 7267 7301 0013 5b4c
6a61 7661 2f6c 616e 672f 5374 7269 6e67
3b01 0003 636f 6e01 0030 4c6f 7267 2f73
7072 696e 6766 7261 6d65 776f 726b 2f63
6f6e 7465 7874 2f41 7070 6c69 6361 7469
6f6e 436f 6e74 6578 743b 0100 104d 6574
686f 6450 6172 616d 6574 6572 7301 000a
536f 7572 6365 4669 6c65 0100 0a48 656c
6c6f 2e6a 6176 610c 000d 000e 0100 426f
7267 2f73 7072 696e 6766 7261 6d65 776f
726b 2f63 6f6e 7465 7874 2f73 7570 706f
7274 2f43 6c61 7373 5061 7468 586d 6c41
7070 6c69 6361 7469 6f6e 436f 6e74 6578
7401 0008 6265 616e 2e78 6d6c 0c00 0d00
2601 0000 0700 270c 0028 0029 0100 1c63
6f6d 2f6a 6961 6e67 2f6c 6561 726e 2f73
7072 696e 672f 4865 6c6c 6f01 0010 6a61
7661 2f6c 616e 672f 4f62 6a65 6374 0100
1528 4c6a 6176 612f 6c61 6e67 2f53 7472
696e 673b 2956 0100 2e6f 7267 2f73 7072
696e 6766 7261 6d65 776f 726b 2f63 6f6e
7465 7874 2f41 7070 6c69 6361 7469 6f6e
436f 6e74 6578 7401 0007 6765 7442 6561
6e01 0026 284c 6a61 7661 2f6c 616e 672f
5374 7269 6e67 3b29 4c6a 6176 612f 6c61
6e67 2f4f 626a 6563 743b 0021 0007 0008
0000 0001 0002 0009 000a 0001 000b 0000
0006 0001 000c 0000 0002 0001 000d 000e
0001 000f 0000 002f 0001 0001 0000 0005
2ab7 0001 b100 0000 0200 1000 0000 0600
0100 0000 1600 1100 0000 0c00 0100 0000
0500 1200 1300 0000 0900 1400 1500 0200
0f00 0000 5000 0300 0200 0000 14bb 0002
5912 03b7 0004 4c2b 1205 b900 0602 0057
b100 0000 0200 1000 0000 0e00 0300 0000
2200 0a00 2300 1300 2400 1100 0000 1600
0200 0000 1400 1600 1700 0000 0a00 0a00
1800 1900 0100 1a00 0000 0501 0016 0000
0001 001b 0000 0002 001c 
```

字节码文件由十六进制值组成，而 JVM 以两个十六进制值为一组，即以字节为单位进行读取。class文件的本质都是一组以 8 位字节为基础单位的2进制流。

**编译：**

1. 词法分析 => 把源代码转变为token
2. 语法分析 => 使用token生成抽象语法树
3. 语义分析 => 做一些逻辑上的验证，优化语法树
4. 生成字节码 => 调整完语法树后生成最终class文件

类加载：将class文件，加载到JVM内存，并进行解析，生成类对象。

按需加载：

Java虚拟机对class文件采用的是**按需加载**的方式，也就是说当需要使用该类时才会将它的class文件加载到内存生成Class对象。而且加载某个类的class文件时，JVM采用的就是双亲委派策略

**字面量**

```java
static final String MSG = "hello word";
static final int A = 1;
static final float F = 0.52f
```

那么`MSG`,`A`,`F`就是常说的常量. 而`hello word`, `1`,`0.52f`就是字面量. 其实就是这么简单. 如你所见的这些个“固定值”就是字面量

**符号引用**

符号引用可以分为三类:

- 类和接口的全限定名
- 字段的名称和描述符
- 方法的名称和描述符

**动态链接**

符号引用其实是`静态`的,那么直接引用就是`动态`的. 将.class文件加载到内存中之后,jvm会将符号引用转化为代码在内存中实际的内存地址.那么这就是直接引用.也就是类加载中的`动态链接`

## 4、JVM调优

一般会加XMS和XMX设置堆的初始内存和最大内存就够了。如果还存在性能问题，首选的还是去优化代码，最后的才是进行JVM调优。

调优