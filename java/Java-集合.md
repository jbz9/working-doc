### 一. 集合框架

####  简介

什么是集合？

参考：https://diguage.github.io/jdk-source-analysis

​			https://visualgo.net/en

存放对象的一个容器

#### Collection 接口

##### List 接口

######  ArrayList

底层数据结构是动态数组，能够使用索引访问，存储的元素：有序、可以重复的（Map无序，且不能重复）

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649941420332image-20201016141555812.png" style="zoom:50%;" />



`ArrayList`是`AbstractList`的子类，同时实现了`List`接口。除此之外，它还实现RandomAccess、Cloneable和Serializable三个标识型的接口。这几个接口都没有任何方法，仅作为标识表示实现类具备某项功能。`RandomAccess`表示实现类支持快速随机访问，`Cloneable`表示实现类支持克隆，具体表现为重写了`clone`方法，`java.io.Serializable`则表示支持序列化。

**Constructor 构造函数**

对于ArrayList，我们在new的时候，如果没有指定长度，它内部会初始化一个空数组，然后再第一次添加元素时，会扩容到10（比较size+1和10比较，取大）；如果指定了长度，就会直接实例化一个直接长度的数组

**add**

当创建一个ArrayList没有指定它的长度，集合里面有一个elementData数组用来存储元素，此时它的容量默认是0，当我们第一次往这个集合添加元素时，它会创建一个长度为10的数组，然后将这个新数组通过Arrays.copyOf（）方法复制给elementData数组；当二次添加元素时，集合的长度2，小于10，所有不需要扩容；当第11次扩容的时候，elementData数组长度是不够了，所有它会进行扩容，它会创建一个1.5倍的新数组，并将原数组元素的引用复制给新数组。

**remove**

先判断给定的索引，有没有超出size范围，超过了，直接报异常，接着通过 System.arraycopy 方法对数组进行自身拷贝，让索引后的元素整体位移1位。

**update**

先判断给定的索引，然后进行替换

**search**

先判断给定的索引，然后直接返回处于下标的数组元素

###### LinkedList

数据结构是双向链接结构，每个元素都一个前指针和后指针。查询数据需要从链表头开始遍历查找

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649941746331image-20201016170254152.png" style="zoom:50%;" />

双向链表是由节点组成的，每个数据节点都有2个指针，pre和next指针

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649941841333image-20201016163347919.png" style="zoom:50%;" />

LinkedList是`AbstractSequentialList`的子类，实现了Cloneable、Serializable、List和Deque（队列）接口L



**插入元素**

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16501184484991650118447742.png" style="zoom: 50%;" />



**指定位置插入元素**

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16501656832011650165682748.png" style="zoom:50%;" />



###### Vector

| ArrayList  | Vector   |
| ---------- | -------- |
| 线程不安全 | 线程安全 |
| 1.5倍扩容  | 2倍扩容  |

`Vector`是`AbstractList`的子类，同时实现了`List`接口。除此之外，它还实现RandomAccess、Cloneable和Serializable三个标识型的接口。

Vector也是基于数组实现的，功能上和ArrayList没有什么差别，只不过Vector的方法很多加了同步语句synchronized（see  k re na a zi），因此是线程安全的

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649941948333image-20201017165530086.png" style="zoom:50%;" />

##### Set 接口

######  HashSet

HashSet是AbstractSet的子类，实现Set接口，同时也实现了Serializable、Cloneable标识接口。它的**底层是HashMap**实现的，在创建HashSet对象时，它的构造函数实际是去new了一个HashMap,在添加、删除、查找数据时它都去调用HashMap的添加和删除方法。

类图

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942047334image-20201017174125159.png" style="zoom:50%;" />





**ArrayList和LinkedList区别**

ArrayList底层是数组结构，查找数据效率比较高，获取下标索引，也不需要去遍历集合，LinkedList是双向链表结构，数据插入、删除效率更好，而ArrayList在指定位置插入时会重新复制数组，不指定则插入到最后、删除数据时需要重新复制数组，效率要偏低一些，LinkedList查找元素效率偏低一些

**List和Set区别**

* List有序(集合里的元素可以根据 key 或 index 访问 )，可重复；Set不可重复，无序(集合里的元素只能遍历)

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942151336image-20200925115300231.png" style="zoom:50%;" />

**集合和数组的区别**

* 集合和数组都是容器，都是用来存储数据的

* 数组的长度是在定义数组时候就确定的，不能更改，集合长度是可变的
* 数组能够存储的数据类型包含了基本数据类型和引用数据类型，但是一个数组只能存相同的数据类型；集合存储的是数据类型只能是引用类型，我们一般使用泛型来规范集合具体存哪种数据类型

**Arraylist初始值及扩容**

jdk1.8之后：

1.ArrayList初始集合不初始化数组容量的时候，默认值为0 

2.添加元素后，扩容为10，之后每次扩容为原来的0.5倍

Map 接口

|————HashTable	接口实现类 线程安全

|————HashMap	接口实现类  线程不安全

​					|——LinkedHashMap	 双向链表和哈希表实现

|————TreeMap	接口实现类	红黑树对所有的key进行排序

#### Map 接口

##### HashMap

数据结构：数组+单链表+红黑树

HashMap是AbstractMap的子类，实现了Serializable、Cloneable和Map接口。它存储的数据结构是键值对的形式，它的键不能够重复，并且允许存储null值和null的key，内部存储的元素是无序的，它也是非线程安全的，即同一个时刻有多个线程同时写HashMap，会造成数据不一致。

HashMap它的数据结构是数组+链表+红黑树，当我们添加数据时，首先会根据计算的hash值，来确定这个元素插入到数组的哪个位置，这样，hash值相同的元素，就会放到数组的同一位置，形成链表，当链表长度过长，就会影响查询速度，所以，当HashMap中链表长度达到8时，链表就会转成红黑树，提高查询效率

当链表的长度大于等于8，链表改为红黑树。数据结构：

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942241333image-20201019145317759.png" style="zoom:50%;" />



HashMap添加数据的流程：

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942339346Java%E9%9B%86%E5%90%88%E6%A1%86%E6%9E%B6.png" style="zoom: 67%;" />

<img src="D:\软件\Markdown\typora-user-images\image-20220417180428063.png" alt="image-20220417180428063" style="zoom:67%;" />

&运算和取模运算

位运算规则：

| 符号 | 描述       | 运算规则                                                     |
| ---- | ---------- | ------------------------------------------------------------ |
| &    | 与         | 两个位都为1时，结果才为1                                     |
| \|   | 或         | 两个位都为0时，结果才为0                                     |
| ^    | 异或       | 两个位相同为0，相异为1                                       |
| ~    | 取反       | 0变1，1变0                                                   |
| <<   | 左移       | 各二进位全部左移若干位，高位丢弃，低位补0                    |
| >>   | 右移       | 各二进位全部右移若干位，对无符号数，高位补0，有符号数，各编译器处理方法不一样，有的补符号位（算术右移），有的补0（逻辑右移） |
| >>>  | 吴符号右移 |                                                              |

- 举例：3 << 2
  将数字 3 左移 2 位，将 3 转换为二进制数字:`0000 0000 0000 0000 0000 0000 0000 0011`，然后把该数字高位 (左侧) 的两个零移出，其他的数字都朝左平移 2 位，最后在低位 (右侧) 的两个空位补零。则得到的最终结果是 `0000 0000 0000 0000 0000 0000 0000 1100`，则转换为十进制是 12。
- `>>`: 右移运算符
  举例：11 >> 2
  则是将数字 11 右移 2 位，11 的二进制形式为:`0000 0000 0000 0000 0000 0000 0000 1011`，然后把低位的最后两个数字移出，因为该数字是正数，所以在高位补零。则得到的最终结果是 `0000 0000 0000 0000 0000 0000 0000 0010`。转换为十进制是 3。

**取模运算**

取模运算： Math.floorMod(10, -3)  结果为-2 运算结果和-3方向一致

余数运算：返回余数 10 % -3 结果为-1 运算结果和10方向一致

**为什么使用红黑树？**

红黑树的查找效率是非常的高，查找效率会从链表的o(n)降低为o(logn)。

**为什么一开始不使用红黑树？**

红黑树比较复杂，在链表节点不多的情况下，没有必要一开始就使用红黑树；HashMap 频繁的扩容，会造成底部红黑树不断的进行拆分和重组，这是非常耗时的。因此，也就是链表长度比较长的时候转变成红黑树才会显著提高效率。

**HashMap中hash函数是如何实现？**

①先计算key的哈希值

②然后哈希值无符号右移16位

③右移后的值再和哈希值进行异或运算

```java
(h = key.hashCode()) ^ (h >>> 16)
```

**好处**

高效率，减少哈希碰撞

###### 如何扩容



扩容是和它的容量以及负载因子有关联的，第一次添加元素的时候，它会进行扩容，初始默认容量是16，负载因子是0.75，那么它的扩容阈值就是两个的乘积12，也就是当元素达到13的时候，会触发第一次扩容，新的容量是之前的2倍，也就是32。第二次也是阈值*容量即：32 * 0.75 = 24

例子：

假设当前 `HashMap` 的桶数量为16，负载因子为0.75。触发扩容的条件是：

当前元素数量>0.75×当前桶数量当前元素数量>0.75×当前桶数量

当前元素数量>0.75×16=12当前元素数量>0.75×16=12

当 `HashMap` 中的元素数量达到或超过12个时，就会触发扩容操作。新的桶数组的长度将会是原来的两倍，即32。

###### 如何查找元素

①通过key取值，先计算key的hash值

②然后通过hash值得到table数组下标，得到节点数据

③再判断是不是红黑树，是的话，从根节点往下查找，如果是链表的话，直接循环查找值。

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942387335image-20201017174519189.png" style="zoom:50%;" />

**数组**

* 一组**连续的内存空间**，来存储一组具有**相同类型**的数据，通过下标可以快速查找数据
* 插入数据困难：数组结构在插入数据时，后面的元素都需要向后移动，才能进行操作，比较麻烦。

**单链表**

* 插入、删除数据较长：直接修改指针的指向
* 查询效率低（ 遍历节点，时间复杂度O(n) ）

**红黑树**

* 是一种自平衡的二叉树

#####  ConcurrentHashMap

相对比HashMap，是线程安全的。

##### TreeMap

TreeMap是AbstractMap的子类，实现了序列接口、Cloneable、NavigableMap接口。底层数据结构是红黑树，储存的是有序的key-value集合。

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942464335image-20201020171338640.png" style="zoom:50%;" />



* 

### JVM

JVM主要分为三部分，类加载器、运行时数据区和执行引擎。

| 名称       | 特征         | 配置参数 | 异常               |
| ---------- | ------------ | -------- | ------------------ |
| 方法区     | 线程共享     |          |                    |
| 堆内存     | **线程共享** |          | OutOfMemoryError   |
| 虚拟机栈   | 线程独占     |          | StackOverflowError |
| 本地方法栈 | 线程独占     |          |                    |
| 程序计数器 | 线程独占     |          |                    |

**方法区（Java1.7）：**也被成为永久代，存放类的信息（类的字节码、类的结构）、常量、静态变量等，**字符串常量就在方法区中。**

**元空间（Java1.8）：**MetaSpace，使用本地内存来存储类的元数据，而不再使用JVM内部空间，那么存储的类元信息只受到本地内存的限制了。常量池也被放到了堆内存中。由于永久代的设计容易导致内存溢出等问题，在1.8中放弃了永久代。

**堆内存：**存放对象和数组，也就是说我们在代码里面new 出来的对象都会存放在这里，所有堆内存也是垃圾收集器主要活动的对象，因此堆内存也叫做GC堆，并且每个JVM进程都会有一个GC堆。Java是线程共享的，它在JVM启动时就会被创建。根据垃圾回收器的规则，我们可以对 Java 堆进行进一步的划分，具体 Java 堆内存结构如下图所示：

* 新生代：新生代还被进一步划分为 Eden 区、From Survivor 0、To Survivor 1 区，默认的虚拟机配置比例是Eden：from ：to = 8:1:1
  * Eden区：伊甸园 
  * Survivor（sərˈvaɪvər） 0（幸存）
  * Survivor 1
* 老年代

Xms和Xmn都是控制堆的JVM参数，我们可以通过参数大小来控制堆里面的内存大小

```java
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

**虚拟机栈：**存放方法里面的局部变量、对象的引用、基本数据类型变量等，每个线程都会有自己虚拟机栈。每个方法调用都会产生一个栈帧信息

**本地方法栈：**和虚拟机用到的本地方法有关，本地方法的栈帧

**程序计数器：**用来保存当前执行指令的地址，每个线程都会有自己的程序计数器

**常量池**

* 运行时常量池：

JVM 堆内存溢出后，其他线程是否可继续工作？

一般来说，发生内存溢出的线程，都会死亡，因为发生内存溢出之前，gc肯定会对该线程进行垃圾回收，进行释放内存，也就是该线程持有的对象被GC回收了，那么该线程都会无法工作了，对于其他线程来说，就算能够正常工作，频繁的GC也会产生很大的影响。

##### 垃圾回收机制

GC是Garbage Collection，即垃圾回收。默认垃圾收集器Parallel Scavenge（新生代）+Parallel Old（老年代）

**回收算法**

* 标记-清除算法：首先标记出所有需要回收的对象，标记完成之后，进行清除
* 复制算法：
* 分代回收算法：创建对象，分配到Eden区，当Eden区空间满了，就触发一次Young GC，将还在使用的对象复制到幸存区From,这样Eden被清空，以供继续存储对象，当Eden再次满了的时候，再触发一次Young GC，将Eden和幸存From区中还在被使用的对象复制到幸存区的to区，下一次，Young GC则是将Eden和To区中还在使用的对象放入到From区，这样，经过多次GC，有些对象会在From和To区经过多次复制，都没有被释放，那么到达一个阈值之后，这些对象就将放到老年代，如果老年代空间也用完，就会触发Full GC全量回收。

### 注解

@PostConstruct和@PreDestroy

作用于Servlet生命周期的注解，实现Bean初始化之前和销毁之前的自定义操作

```java
  @PostConstruct
    public void initialize() {
        log.info("它用来修饰一个非静态的void方法。它会在服务器加载Servlet的时候运行，并且只运行一次。");
    }

    @PreDestroy
    public void shutDown() {
        log.info("结束时运行，也只运行一次");
    }
```



#### 基础

数据类型

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942999335import.png" style="zoom:50%;" />

基础类型的包装类是对象。

1. 变量注意点

```
1. 定义变量需要赋予初始值int name = 1;
2. 基础数据类型有自己的初始值； 引用类型初始值为null
```

2. null

   空指针，引用指向的对象不存在，即内存中没有为该对象开辟存储空间

##### 匿名类

内部类：类里面的类。分为成员内部类和局部内部类，成员内部类和成员变量类似，定义在方法外面，而局部内部类则和局部变量类似，定义在方法里面。匿名类属于局部内部类，**就是在方法里面的类。**

##### 常见问题

1. equals和==

   在引用类型中，"=="是比较两个引用是否指向堆内存里的同一个地址（同一个对象），即**内存地址**，equals比较的是**值相等**

#####  堆和栈

堆区：stack

栈区：heap

1. 堆区：存取快，可以共享

存储以下数据：

* 基本类型
* 引用类型的变量
* 方法函数
* main（）

2. 堆区：

存储**实例对象**，new person()；new即意味着在堆区开辟一个内存空间。

对应引用类型来说，=相当于拷贝了一份内存地址。

```
arr1=arr2
意味着将arr1的内存地址在栈区拷贝了一份给arr2，即arr1和arr2指向的同一个堆区示例对象
```

当==两边是基本数据类型时，==于比较的是两边的两个值是否相等，==两边是引用类型时比较的是两个内存地址，也可以看成是看这两个引用是否指向堆内存里的同一块地址。

#### lambda表达式

它是一个匿名函数，即没有函数名称， 有时候甚至连入参和返回都可以省略 

表达式语法：

```
(params) -> expression
(params) -> {expression}
```

说明：

1.  params ：参数；expression 表达式；-> 将参数和表达式分开

2.  ()可以选，如果只有一个参数，可以不用()，如果多个参数，必须要()
3.  ()中参数类型可选，编译器会自动判断参数类型
4.  ->必须要
5.  {}可选
6.  {}中返回值可选， 如果只有一个表达式，可以自动返回，不需要 return 语句；否则花括号中需要 return 语法。 
7.  {}中使用的外部变量必须是产量final；在{}内部什么的变量不可以更改，且不能和外部变量名相同。

```java
// 1.不需要参数，没有返回值，输出 hello
()->System.out.pritnln("hello");

// 2.不需要参数，返回 hello
()->"hello";

// 3. 接受2个参数(数字),返回两数之和 
(x, y) -> x + y  

// 4. 接受2个数字参数,返回两数之和 
(int x, int y) -> x + y  

// 5. 两个数字参数，如果都大于10，返回和，如果都小于10，返回差
(int x,int y) ->{
  if( x > 10 && y > 10){
    return x + y;
  }
  if( x < 10 && y < 10){
    return Math.abs(x-y);
  }
};
```

**lambda遍历**

```java
ArrayList<String> list = Lists.newArrayList();
list.add("1");
list.add("2");
list.add("3");
list.forEach( (String s) -> { 
System.out.print(s);
});
//map遍历
HashMap<Object, Object> map = Maps.newHashMap();
map.forEach((k,v) -> {
    System.out.println("k是："+v);
    System.out.println("value是："+v);
});

```

**方法引用**

可以直接访问类或者对象的方法，**这个方法必须是函数式接口**，不需要new实例，再去调用，实例/类::方法，是简化的lambda。

**函数式接口**

即接口interface中只有一个抽象方法，那么这个接口叫做函数式接口，是为了lambda可以使用。 函数接口使用注解 `@FunctionalInterface` 进行声明（注解声明不是必须的，如果没有注解，也是只有一个抽象函数，依旧会被认为是函数接口） 。 函数式接口可以隐式的转换为 Lambda 表达式进行使用。

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}
```

```java
public static void lambda(){ 
    Runnable runnable = new Runnable() {   
        @Override        
        public void run() {       
            System.out.print("线程创建");       
        }  
    };    
    new Thread(runnable).start();   
    //该函数接口使用lambda 后  
    //Java 8 中的 Lambda 碰到了函数接口Runnable，自动推断了要运行的 run 方法
    new Thread( () -> {   
        System.out.print("lambda线程创建");  
    }).start();}
```

**stream和parallelStream流**

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649943028336image-20191114204500673.png" style="zoom:50%;" />

```java
stream：串行流 单管道;  parallelStream：并行流 多管道
并行流就是把一个内容分成多个数据块，并用不同的线程分成多个数据块，并用不同的线程分别处理每个数据块的流。
.stream()  创建流

Function<? super T, ? extends R>
代表一个方法对象，调用T对象，返回R对象

通配符： “？”就是一个占位符，它不表示任何具体的类型，而是表示符合定义规则的一个或者多个类型的一个占位标志边界：
<? extends T> 表示上界通配符 它表示T以及T的子类， 类型最高是T
<? super T> 表示下界通配符 它表示T以及T的超类，类型最高可到Object ，最低是T 

```

- 不关心执行顺序
- 没有并行处理的并发问题
- 处理事件涉及io阻塞操作，业务处理事件较长

```java
ArrayList<String> list = Lists.newArrayList();
list.add("1");
list.add("2");
list.add("3");
list.add("3");
//集合去重之后lambda遍历 .stream().distinct()  distinct：不同的
list.stream().distinct().forEach( (String s) -> {
	System.out.print(s);
});
//集合去重->1，2，3 ; 然后过滤filter()  ()中是这个集合留下的元素必须满足()中条件；过滤后只有 1 ;  st.equals("1") 相等 返回 true
list.stream().distinct().filter( (st -> st.equals("1")) ).forEach( (String s) -> {
            System.out.print(s);
});
//流式操作数据转换map 	.stream().map() 映射（即类型转换）
List<String> collect = list.stream().map( (st -> st + "1") ).collect(Collectors.toList());// collect 为  [11, 21, 31, 31]
//.flatMap  可以操作list中的list中的对象的属性
List<AppVersionEntity> userList = Lists.newArrayList();
AppVersionEntity中的getVersionPar依旧是一个list
//操作list中list的对象
List<String> collect = userList.stream().flatMap(appVersionEntity -> appVersionEntity.getVersionPar().stream()).map(AppVersionParEntity::getCode).collect(toList());
//操作list中对象
List<String> collect = userList.stream().map(AppVersionEntity::getDemoAddress).map(AppDemoAddrEntity::getId).collect(toList());

//集合分组
Map<String, List<WorkHourEntity>> collect = workHourList.stream().collect(Collectors.groupingBy(WorkHourEntity::getMissionId));
        collect.forEach( (k,v) -> {
          
});
//抽取集合中对象某个属性，然后组成新的集合
ArrayList<AppEntity> list2 = Lists.newArrayList();
AppEntity e1 = new AppEntity();
e1.setId("1");
e1.setName("11");
AppEntity e2 = new AppEntity();
e2.setId("2");
e2.setName("22");
AppEntity e3 = new AppEntity();
e3.setId("3");
e3.setName("33");
list2.add(e1);
list2.add(e2);
list2.add(e3);
//.map()  抽取AppEntity对象中id，返回 [1, 2, 3]
List<String> collect1 = list2.stream().map((AppEntity::getId
                        )).collect(Collectors.toList());
//.limit()  限制返回的数量，从前往后
List<String> collect1 = list2.stream().map((AppEntity::getId
        )).limit(1).collect(Collectors.toList()); // [1]
// .skip()  删除前n个元素
List<String> collect1 = list2.stream().map((AppEntity::getId
        )).skip(1).collect(Collectors.toList()); // [2, 3]
// .count() 计数；返回stream中元素的格式，返回类型long
long collect1 = list2.stream().map((AppEntity::getId
        )).count(); // 3
//集合操作
ArrayList<AppEntity> list3 = Lists.newArrayList();
list3.add(e1);
list3.add(e2);
// list2 和 list2 的交集， 返回e1和e2  
List<AppEntity> collect1 = list2.stream().filter(li2 -> list3.contains(li2)).collect(toList());
System.out.print(JSON.toJSON(collect1)); //list 转json  JSON.toJSON()
//list2 — list3  的差集 返回 e3
List<AppEntity> collect2 = list2.stream().filter(li2 -> !list3.contains(li2)).collect(toList());
//list2 + list3 并集 返回 e1、e2、e3、e1、e2；将list3加入到list2中
list2.addAll(list3);

//集合复制
CollectionUtils.addAll(newList, new Object[oldList.size()]);
Collections.copy(newList, oldList);

//2个集合中对象不相同，进行筛选；
//list2中存的是AppEntity对象，list4中存的是AppVersionEntity对象
ArrayList<AppVersionEntity> list4 = Lists.newArrayList();
AppVersionEntity v1 = new AppVersionEntity();
AppVersionEntity v2 = new AppVersionEntity();
v1.setId("v1");
v1.setAppId("1");
v2.setId("v2");
v2.setAppId("2");
list4.add(v1);
list4.add(v2);
//根据list2中AppEntity的id 和list4中AppVersionEntity的appId进行筛选，
//如果appEntity.id等于appVersionEntity.appId；那么从list2中去掉这条appEntity
//对list2中每一条记录进行判断；如果2个id相等，那么return返回的是1条数据.count()是1,.count()<=0返回false；filter即过滤这条id相等的数据
List<AppEntity> collect3 = list2.stream().filter(appEntity1 -> {
    return list4.stream().filter(appVersionEntity ->                               appEntity1.getId().equals(appVersionEntity.getAppId())).count()<=0;
}).collect(toList()); // 返回 e3
//.count()>0 ；取反  返回 e1、e2
List<AppEntity> collect3 = list2.stream().filter(appEntity1 -> {
    return list4.stream().filter(appVersionEntity ->                               appEntity1.getId().equals(appVersionEntity.getAppId())).count()>0;
```

###### 时间工具类

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649943055335image-20191116102223227.png" style="zoom:50%;" />

```java
//LocalDateTime
LocalDateTime now = LocalDateTime.now(); //2019-11-16T10:40:47.880
//获取当前时间的年、月、日、时、分、秒
System.out.print("年："+now.getYear()+" 月："+now.getMonthValue()
      		  +" 日："+now.getDayOfMonth() +" 时：" +now.getHour()
              +" 分："+now.getMinute()+" 秒:" +now.getSecond());
// LocalDate
LocalDate now1 = LocalDate.now();      // 2019-11-16 只到天
 System.out.print(now1.getYear()+"月："+now1.getMonthValue()+"日："+now1.getDayOfMonth());

//LocalTime 
LocalTime now2 = LocalTime.now(); // 10:46:01.375 只有当天的时、分、秒
System.out.print("时："+now2.getHour()+"分："+now2.getMinute()+"秒："+now2.getSecond());

//设置时区
ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
System.out.print(zonedDateTime); //2019-11-16T10:51:54.278+08:00[Asia/Shanghai]

//创建 固定时间
LocalDateTime of = LocalDateTime.of(2019, 10, 8, 8, 8, 8);//年月日 时分秒
LocalDate of1 = LocalDate.of(2019, 10, 10);//年月日
LocalTime of2 = LocalTime.of(10, 11,12);//时分秒
System.out.print(of);//2019-10-08T08:08:08
System.out.print(of1);//2019-10-10
System.out.print(of2);//10:11:12

//时间格式转换  Date  和  LocalDateTime
//Date  转换成 LocalDateTime
Date date =new Date();
LocalDateTime now3 = LocalDateTime.ofInstant(date.toInstant(),
                                             ZoneId.systemDefault());
//date: Sat Nov 16 11:24:25 CST 2019     LocalDateTime: 2019-11-16T11:24:25.962
System.out.print("date: "+date+" LocalDateTime: "+now3);
//LocalDateTime 转换成  Date
LocalDateTime now4 = LocalDateTime.now();
Date date1 = Date.from(now4.atZone(ZoneId.systemDefault()).toInstant());
//毫秒时间戳，与UTC相差8小时，时区为东8区  1573904637402
long milli = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
//秒级时间戳
long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
//时间戳转换成 LocalDateTime
LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.systemDefault());

//格式化时间 .format()  格式化  DateaTimeFormatter 日期化格式 ofPattern 模式
// yyyy和YYYY  y表示年；Y表示weak year 表示当天所在周的年份，一周从周日开始，周六结束，只要本周跨年，那么这周就算入下一年。
//MMM 		  必须大写；为了和 分mm区别开
//dd		  必须小写
//hh和HH		 hh：表示12小时制   HH：表示24小时制
//mm和m  ss和s   mm和ss 补0  m和s不补0；比如凌晨1点2分，HH:mm显示为01:02，H:m显示为1:2
综上：时间格式采用 yyyy-MM-dd HH:mm:ss  即 月MM和小时HH大写，其余小写
String now5 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));//2019-11-16 13:56:36
String now6 = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);//2019-11-16
//字符串解析成时间LocalDateTime  .parse() 解析
LocalDateTime parse = LocalDateTime.parse(now5,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //now5 时间字符串，它的格式 

//时间加减
LocalDateTime now7 = LocalDateTime.now();
LocalDateTime localDateTime1 = now7.plusMonths(2).plusDays(2).plusHours(2).plusMonths(2).plusSeconds(2);
//向后推，增加2月2天2小时2分钟2秒时间
LocalDateTime localDateTime2 = now7.minusMonths(2).minusDays(2).minusHours(2);
//向前推，减少2月2天2小时
//本月第一天  天 置 1 ； 时、分。秒、纳秒 置 0            2019-11-01T00:00
LocalDateTime localDateTime1 = now7.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
//本月第一天和最后一天
LocalDate first = localDate.with(TemporalAdjusters.firstDayOfMonth());
LocalDate last = localDate.with(TemporalAdjusters.lastDayOfMonth());
//今天是周几，中文 星期一
localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.CHINA)
```

###### **BeanUtils**

是一个复制实体类的工具，

```java
AppEntity entity = new AppEntity();
entity.setId("1");
entity.setName("11");
AppEntity target = new AppEntity();
// package org.springframework.beans  spring里的类，不是apache下的
BeanUtils.copyProperties(entity,target); //将entity的属性赋值给target，前附后
```

###### **IO流**

分为字节流和字符流，先有字节流，然后对字节流进行处理出来了 字符流；**只要是处理文本，那么就用字符流，其余用字节流**

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649943077335image-20191124141005853.png" style="zoom:50%;" />

**缓冲流**

提高对流的操作效率，是工具。

1. 字节流

   ```java
   //除文本文件之外必须使用字节流;文件比较大的必须使用缓冲区提升效率
   FileInputStream是一个一个字节从磁盘读取
   BufferedInputStream可以预先从磁盘中读取指定字节容量的字节数到内存
   //读取文件；字节流
   File file = new File("C:\\Users\\jbz\\Desktop\\HHTP\\statistics.json");
   //用来读文件file内容
   FileInputStream fileInputStream = new FileInputStream(file);
   //buffer缓冲流
   BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
   //将读取的内容写入到fileNew文件中
   File fileNew = new File("C:\\Users\\jbz\\Desktop\\HHTP\\test.txt");
   FileOutputStream fileOutputStream = new FileOutputStream(fileNew);
   byte[] by = new byte[1024];
   int len;
   //当读取到文件的末尾，即文件没有下一个字节供读取了，将返回值 -1
   while ( (len=fileInputStream.read(by)) != -1) {
       //写入,读取多少长度，写入多少长度，不然不足1024的长度，会写入null
       fileOutputStream.write(by,0,len);
   }
   //缓冲流
   while ( (len=fileOutputStream.read(by)) != -1) {
       //写入,读取多少长度，写入多少长度，不然不足1024的长度，会写入null
       fileOutputStream.write(by,0,len);
   }
   //所有流都需要关闭
   fileInputStream.close();
   bufferedInputStream.close();
   fileOutputStream.close();
   
   //查找文件夹下面有没有这个文件
   File dir = new File(path);
   File[] allFile = dir.listFiles();
       if (null != allFile) {
           for ( File f :  allFile) {
               if ( f.isFile() && 		 f.getName().equals(originalFilename)) {
                   path =  f.getAbsolutePath();
               }
           }
   }
   
   ```

2. 字符流

 字节流处理文件的时候是基于字节的，而字符流处理文件则是基于一个个字符为基本单元的， 但字符流操作的本质就是「字节流操作」+「编码」两个过程的封装 。

```java
java中默认字符编码是UTF-8，UTF-8是使用1-4个字节进行存储的，字节即byte，它是数据量存储的单位，一个字节由8个0 1组成一个英文字符需要1个字节，一个汉字存储需要3到4个字节;注意，char是2个字节，只能存一定范围的汉字。
File file = new File("C:\\Users\\jbz\\Desktop\\HHTP\\statistics.json");
//读取
FileReader fileReader = new FileReader(file);
// BufferedReader bufferedReader = new BufferedReader(fileReader);   //会乱码
//写入
FileWriter fileWriter = new FileWriter(new 	    		 	                                                   File("C:\\\\Users\\\\jbz\\\\Desktop\\\\HHTP\\\\test.txt"));
//指定的是读取的文件的编码，如果statistics.json文件不是UTF-8，依旧会乱码
InputStreamReader inputStreamReader = new InputStreamReader(new 	                                                           FileInputStream(file),"UTF-8"); 
BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
String w="";
//.readLine()是阻塞式的，如果达到文件末尾，就返回null，
while ( ((w=bufferedReader.readLine()) != null) ) {
    fileWriter.write(w+"\n");
}
//先关闭缓冲，再关闭文件
bufferedReader.close();
fileReader.close();
fileWriter.close();
//File.separator方法，该方法表示系统的分隔符(Linux下的/或者Windows下的\)
```

###### 反射-reflect

Java反射：在程序运行时，对于任意一个类，都能够知道这个类的所有属性和方法，对于任意一个对象，都能够调用它的任意一个方法和属性。这种 **动态的获取信息** 以及 **动态调用对象的方法** 的功能称为 **java 的反射机制**。 

**反射就是在运行时才知道要操作的类是什么，并且可以在运行时获取类的完整构造，并调用对应的方法。**

<img src="E:/develop/idea_work/develop-doc/Java/typora-user-images/image-20191224164941287.png" alt="image-20191224164941287" style="zoom:50%;" />

- 根据类名创建实例（类名可以从配置文件读取，不用new，达到解耦）
- 用Method.invoke执行方法

```java
  public static void main(String[] args) throws InstantiationException,
            IllegalAccessException, NoSuchMethodException,InvocationTargetException,
            NoSuchFieldException{
        //实例化class对象
        Class<App> appClass = App.class;
        //实例化对象-无参构造
        App app = appClass.newInstance();
        //实例化对象-有参构造
        Constructor<App> constructor = appClass.getConstructor(String.class);
        App newInstance = constructor.newInstance("小明");
        //调用类的普通方法，必须先实例化
        Method method = appClass.getMethod("PrintHello");
        //调用 PrintHello 方法
        method.invoke(app);
        //调用类的成员属性，类的属性必须要实例化之后才会分配内存
        Field name = appClass.getDeclaredField("name");
        name.setAccessible(true);
        //相当于app.set
        name.set(app,"哈哈");
        System.out.println(name.get(app));
        // System.out.println(newInstance.toString());
        //app.PrintHello();
    }
```

#### 常见工具类

```java
//文件复制 
package org.apache.commons.io;
FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
```

#### 并发

优点：提高执行效率，由于I/O等情况阻塞，单个任务并不能充分利用CPU时间。java中多线程是抢占式的，这意味着一个任务随时可能中断并切换到其它任务。通过并发编程的形式可以将多核CPU的计算能力发挥到极致，性能得到提升。

缺点：频繁的上下文切换；编程容易造成死锁，即线程安全问题，在多线程下代码执行的结果与预期正确的结果不一致，该代码就是线程不安全的，否则则是线程安全的。

1. 悲观锁

   假设最坏的情景，

#### 异常

异常机制是指**当程序出现错误**后，程序如何处理。具体来说，异常机制提供了程序退出的安全通道。当出现错误后，程序执行的流程发生改变，程序的控制权转移到异常处理器。

分为编译错误、运行错误、逻辑错误。

编译错误：代码编译不通过

运行错误：编译通过，代码运行出现错误。

逻辑错误：程序没有按照预定的预期的逻辑顺序执行。

##### 异常类

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649943125335image-20200203151301191.png" style="zoom:50%;" />


