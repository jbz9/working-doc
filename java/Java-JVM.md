## JVM  

### 1. JVM概念  

Java 虚拟机，运行在操作系统上。hotSpot

#### 1. 内存溢出OOM、内存泄漏、栈溢出
heap

### 2. JVM内存结构

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16502903438581650290343033.png" style="zoom:67%;" />



#### （1）Stack 栈

每一个方法执行都会产生一个Stack,用来保存方法的一些信息，局部变量表，操作数帧，方法出口、动态链接。Stack 栈随着方法的调用结束而销毁。



Java栈中存放的是一个个的栈帧，每个栈帧对应一个被调用的方法，在栈帧中包括局部变量表(Local Variables)、操作数栈(Operand Stack)、指向当前方法所属的类的运行时常量池（运行时常量池的概念在方法区部分会谈到）的引用(Reference to runtime constant pool)、方法返回地址(Return Address)和一些额外的附加信息。当线程执行一个方法时，就会随之创建一个对应的栈帧，并将建立的栈帧压栈。当方法执行完毕之后，便会将栈帧出栈。

#### （2）Heap 堆

#### （3）Native Method Stack 本地方法栈

#### （4）PC Register 程序计数器

是线程私有的，用来记录线程的执行行号

#### （5）Metadata 元数据



### 3.类加载器

### 4. Class 文件

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
package com.jiang.collection;

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

