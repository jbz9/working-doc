### 一. 集合框架

####  简介

什么是集合？

参考：https://diguage.github.io/jdk-source-analysis

存放对象的一个容器

#### 1. Collection 接口

|————list	接口

​					|——ArrayList 	接口实现类  数组 	查询快

​					|——LinkedList	 接口实现类	链表 	增删快

​					|——Vector	接口实现类        数组

​								|——Stack  Vector的接口实现类

|————Set	接口

​				 |——HashSet	使用hash表（数组）存储元素

​								|——LinkedHashSet  链表维护元素的插入次序

​				 |——TreeSet	底层实现为二叉树，元素排好序

##### List 接口

###### （1） ArrayList

底层数据结构是数组，支持随机访问，存储的元素是无序可重复的，类图：

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649941420332image-20201016141555812.png" style="zoom:50%;" />



`ArrayList`是`AbstractList`的子类，同时实现了`List`接口。除此之外，它还实现RandomAccess、Cloneable和Serializable三个标识型的接口。这几个接口都没有任何方法，仅作为标识表示实现类具备某项功能。`RandomAccess`表示实现类支持快速随机访问，`Cloneable`表示实现类支持克隆，具体表现为重写了`clone`方法，`java.io.Serializable`则表示支持序列化。

* add：当创建一个ArrayList没有指定它的长度，集合里面有一个elementData数组用来存储元素，此时它的容量默认是0，当我们第一次往这个集合添加元素时，它会创建一个长度为10的数组，然后将这个新数组通过Arrays.copyOf（）方法复制给elementData数组；当二次添加元素时，集合的长度2，小于10，所有不需要扩容；当第11次扩容的时候，elementData数组长度是不够了，所有它会进行扩容，它会创建一个1.5倍的新数组，并将原数组元素的引用复制给新数组。
* remove：先判断给定的索引，有没有超出size范围，超过了，直接报异常，接着通过 System.arraycopy 方法对数组进行自身拷贝，让索引后的元素整体位移1位。
* 修改：先判断给定的索引，然后进行替换
* 查找：先判断给定的索引，然后直接返回处于下标的数组元素

###### （2） LinkedList

底层是双向链接结构，元素是可重复的。LinkedList查找元素会麻烦一些，它需要从链表的头结点或者尾结点开始查找数据，

双向链表结构：

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649941746331image-20201016170254152.png" style="zoom:50%;" />

双向链表是由节点组成的，每个数据节点都有2个指针，一个指向前节点，一个指向后节点。

类图：

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649941841333image-20201016163347919.png" style="zoom:50%;" />

LinkedList是`AbstractSequentialList`的子类，实现了Cloneable、Serializable、List和Deque（队列）接口L。

###### （3） Vector

`Vector`是`AbstractList`的子类，同时实现了`List`接口。除此之外，它还实现RandomAccess、Cloneable和Serializable三个标识型的接口。

Vector也是基于数组实现的，功能上和ArrayList没有什么差别，只不过Vector的方法很多加了同步语句synchronized（see  k re na a zi），因此是线程安全的。

类图

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649941948333image-20201017165530086.png" style="zoom:50%;" />

##### Set 接口

###### （1） HashSet

HashSet是AbstractSet的子类，实现Set接口，同时也实现了Serializable、Cloneable标识接口。它的底层是由HashMap实现的，在创建HashSet对象时，它的构造函数实际是去new了一个HashMap,在添加、删除、查找数据时它都去调用HashMap的添加和删除方法。

类图

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942047334image-20201017174125159.png" style="zoom:50%;" />





**ArrayList和LinkedList区别**

ArrayList底层是数组结构，查找数据效率比较高，获取获取也不需要去遍历集合，LinkedList是双向链表结构，数据插入、删除效率更好，而ArrayList在指定位置插入时会重新复制数组，不指定则插入到最后、删除数据时需要重新复制数组，效率要偏低一些，LinkedList查找元素效率偏低一些

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

#### 2. Map 接口

###### （1） HashMap

HashMap是AbstractMap的子类，实现了Serializable、Cloneable和Map接口。它存储的数据结构是键值对的形式，它的键不能够重复，并且允许存储null值和null的key，内部存储的元素是无序的，它也是非线程安全的，即同一个时刻有多个线程同时写HashMap，会造成数据不一致。

HashMap它的数据结构是数组+链表+红黑树，当我们添加数据时，首先会根据计算的hash值，来确定这个元素插入到数组的哪个位置，这样，hash值相同的元素，就会放到数组的同一位置，形成链表，当链表长度过长，就会影响查询速度，所以，当HashMap中链表长度达到8时，链表就会转成红黑树，提高查询效率

当链表的长度大于等于8，链表改为红黑树。数据结构：

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942241333image-20201019145317759.png" style="zoom:50%;" />



HashMap添加数据的流程：

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942339346Java%E9%9B%86%E5%90%88%E6%A1%86%E6%9E%B6.png" style="zoom: 67%;" />

为什么使用红黑树？

红黑树的查找效率是非常的高，查找效率会从链表的o(n)降低为o(logn)。

为什么一开始不使用红黑树？

红黑树比较复杂，在链表节点不多的情况下，没有必要一开始就使用红黑树；HashMap 频繁的扩容，会造成底部红黑树不断的进行拆分和重组，这是非常耗时的。因此，也就是链表长度比较长的时候转变成红黑树才会显著提高效率。

HashMap中hash函数是如何实现的？

key的hash值 异或 hash值高位右移16位 

HashMap是如何扩容的？

第一次添加元素的时候，它会进行扩容，初始容量扩为16，负载因子是0.75，也就是扩容的阈值是12,；之后扩容的话，每次扩容的容量和阈值都是之前的2倍。创建一个容量是原来2倍的节点数组，然后把原来数组中的元素经过重新散列，再加入到新的数组中。

HashMap是如何get取值？

通过key取值，先计算key的hash值，然后通过hash值得到table数组下标，得到节点数据，再判断是不是红黑树，是的话，从根节点往下查找，如果是链表的话，直接循环查找值。

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942387335image-20201017174519189.png" style="zoom:50%;" />

**数组**

* 一组**连续的内存空间**，来存储一组具有**相同类型**的数据，通过下标可以快速查找数据
* 插入数据困难：数组结构在插入数据时，后面的元素都需要向后移动，才能进行操作，比较麻烦。

**单链表**

* 插入、删除数据较长：直接修改指针的指向
* 查询效率低（ 遍历节点，时间复杂度O(n) ）

**红黑树**

* 是一种自平衡的二叉树

###### （2）TreeMap

TreeMap是AbstractMap的子类，实现了序列接口、Cloneable、NavigableMap接口。底层数据结构是红黑树，储存的是有序的key-value集合。

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942464335image-20201020171338640.png" style="zoom:50%;" />

### 二. 数据结构

#### 时间、空间复杂度

#### 1.  二叉查找树

它有一个根节点，每个节点下面只能有2个节点，左节点的值要小于父节点，右节点的值要大于父节点。

* 插入节点：从根节点开始往下查找，如果元素大于比较的节点时，放到右侧，如果小于节点时，放到左侧，这样一直往下查找。 依次插入节点[100,50,200,80,300,10]

  <img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16499424963351601970-20190803091400407-1322764832.gif" style="zoom: 67%;" />

  * 查找节点：从根节点往下查找，当查找的元素大于比较的元素时，向右查找，当查找的元素小于比较的元素时，向左侧查找，这样依次往下查找，直到找到。

    <img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16499425363351601970-20190803110643308-509746032.gif" style="zoom: 67%;" />

* 删除节点：首次需要查找到节点，然后进行删除操作，删除的节点有几种情况：

  * 删除的节点有2个子节点

    在删除节点的右树上查找到最小节点，即从右子树一直往左查找，直到null，然后用这个节点去替换删除的节点。

    <img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16499425643381601970-20190803111731338-1224090242.gif" style="zoom: 67%;" />

  * 删除的节点有1个子节点

    将它的父节点指向它的子节点，然后删除它

    <img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16499426323391601970-20190803111834308-2070132039.gif" style="zoom:67%;" />

  * 删除的节点有没有子节点：直接删除这个节点

    <img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16499427203421601970-20190803111948176-1042337598.gif" style="zoom:50%;" />

* 缺点：二叉树不是平衡树，可能会导致数据偏向某一侧，变成类似链表的结构。这种不平衡导致树高增加，导致查找和插入的效率变慢。

  <img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16499427543351601970-20190803112059750-1915207427.gif" style="zoom:50%;" />

  

##### **红黑树（red black tree）**

它是一个平衡的二叉树，它遵守一系列规则，来保证每个节点下的左侧和右侧数据大致相同（最长路径也不会超过最短路径的2倍）。

红黑树的规则：

* 每个节点颜色不是红色就是黑色

* 根节点颜色是黑色

* 2个红节点不能相邻，即红节点的子节点一定是黑色，而不能是红色

* 每个叶子节点（null 节点，凑数的）一定是黑色

* 从任意节点 到 叶子节点，每条路径上 包含的 黑色节点 数量相同

  例如：红黑树 - 依次插入节点[100,200,300,400,500,600,700,800]

  <img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16499427963351601970-20190803113549962-1113334054.gif" style="zoom:50%;" />

旋转和变色

如果新插入、删除元素导致了红黑树规则被破坏，那么就需要调整红黑树来保持平衡

* 变色：改变节点颜色，红变黑，黑变红

* 旋转：改变节点位置，包含左旋和右旋

  * 左旋：向左旋转-使自己的右子节点成为自己的父节点，而自己成为自己孩子的左节点，然后把自己孩子的左节点作为自己的右节点。  把当前节点的父指向 指向该节点的子节点，然后把该子节点的  的子节点 的 父节点 指向 该节点

    <img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942837336image-20201019141218817.png" style="zoom:50%;" />

  * 右旋：向右旋转-使自己的左节点成为自己的父节点，自己成为自己孩子的右节点，然后将自己孩子的右节点变成自己左节点

    <img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942935336image-20201019142706092.png" style="zoom:50%;" />

节点的属性：

* 父节点
* 子节点
* 节点颜色

##### **哈希表**

哈希，一般翻译做“散列”，也有直接音译为“哈希”的，**就是把任意长度的输入，通过散列算法，变换成固定长度的输出，该输出就是散列值。**

哈希表是由一块地址连续的数组空间构成的，其中每个数组都是一个链表，数组的作用在于快速寻址查找，链表的作用在于快速插入和删除元素，因此，哈希表可以被认为就是链表的数组

##### 队列

先进先出

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1649942968335image-20201022171501496.png" style="zoom:50%;" />

### 三. 多线程  

##### 概念

**同步和异步**

同步和异步用于方法时，同步方法调用一旦开始，调用方必须等到方法返回后，才能进行下一步操作；异步方法，调用方调用方法后，会立即返回一个结果，调用方可以进行下一步操作。

```
关于异步目前比较经典以及常用的实现方式就是消息队列：在不使用消息队列服务器的时候，用户的请求数据直接写入数据库，在高并发的情况下数据库压力剧增，使得响应速度变慢。但是在使用消息队列之后，用户的请求数据发送给消息队列之后立即 返回，再由消息队列的消费者进程从消息队列中获取数据，异步写入数据库。由于消息队列服务器处理速度快于数据库（消息队列也比数据库有更好的伸缩性），因此响应速度得到大幅改善。
```

多个线程同时操作实例对象中的变量，会造成非线程安全。**非线程安全**问题存在于“实例变量”中，如果是方法内部的私有变量，则不存在**非线程安全**问题，所得结果也就是**线程安全**的了。

**线程阻塞**



##### 什么是线程

进程是系统**资源分配的最小单位**，线程**是系统进行运算调度的最小单位**，同一个进程里有多个线程，它们共享这个进程的资源

1. 线程是cpu调度的最小单位，一个进程可以有多个线程  
2. 线程和进程同样有5个阶段：新建、Runnable可运行状态、运行状态、阻塞、终止  
    * 新建：创建一个线程对象
    * 可运行状态：线程对象调用start()方法，进行可运行的就绪状态。等待JVM的调用，什么时候运行（run方法），是由系统决定的
    * 运行状态：就绪状态的线程，执行run()方法，获取cpu资源，此时线程就处于运行状态
    * 阻塞状态：线程暂时放弃了CPU的使用权，当线程执行睡眠（sleep）、挂起（suspend）这些方法之后，线程失去了所占用的资源，那么它就从运行状态进行了阻塞状态。它可以重新进入到就绪状态，阻塞的情况分为3种：
      * 等待阻塞：运行状态的线程，执行wait()方法，JVM会将该线程放到**等待池**中，进入等待状态，**当有其它线程以notify()、notifyAll()、interrupt()方法唤醒该线程或者wait时间到了**则进入就绪状态
      * 同步阻塞：线程在获取同步锁（synchronized）失败后，会进入同步阻塞的状态，**同步锁被其它线程占用**，JVM会把线程放到锁池中
      * 其它阻塞：线程调用睡眠、挂起后，就进入阻塞状态，当随眠时间结束，它就会从阻塞状态转入就绪状态。处于【执行中】状态的线程，若遇到阻塞I/O操作，也会停止等待I/O完成，然后回到【可执行状态】
    * 死亡状态：当run()方法执行完成之后，线程就会终结了。
3. 进程代表是程序，多进程代表系统可以同时运行多个任务（程序）；多线程代表同一个程序中有多个顺序流执行。

**方法**

- `obj.wait()`是把当前线程放到obj的wait set；
- `obj.notify()`是从obj的wait set里唤醒1个线程；
- `obj.notifyAll()`是唤醒所有在obj的wait set里的线程。

Thread和Runnable的区别？

Thread是继承，Runnable是实现，如果线程类实现Runnable，它还是可以继承其它类的，而且实现Runnable，那么多个线程就可以共享一个对象了，适合多个相同线程来处理同一份资源的情况。

Runnable和Callable的区别？

Runnable重写的是run方法，Callable重写的是call方法，Callable执行完有返回值，Runnable没有

##### 如何实现多线程

1.Thread（si red ）  :继承Thread 类，重写run()方法，调用start启动线程

```java
/**
 * Project Name : develop-doc
 * File Name    : OneThread
 * Package Name : springboot.example.demo.thread
 * Date         : 2020-05-01 15:38
 * Author       : jbz
 * Copyright (c) 2019, jiang.baozi@ustcinfo.com All Rights Reserved.
 */
package springboot.demo.thread;

import lombok.SneakyThrows;

/**
 * @ClassName : OneThread
 * @author : jbz
 * @Date : 2020-05-01 15:38
 * @Description :   线程
 */
public class OneThread extends Thread {

    private String name;

    public OneThread(){
    }

    public OneThread(String name){
        this.name=name;
    }

    @SneakyThrows
    @Override
    public void run(){
        for (int i = 0; i < 10; i++) {
            System.out.println(name+"执行线程"+i);
            Thread.sleep(1);
        }
    }

    public static void main(String[] args) {
        OneThread oneThread = new OneThread("第一个线程");
        OneThread oneThread2 = new OneThread("第二个线程");
        oneThread.start();
        oneThread2.start();
    }
}
```

2. Runnable：实现Runnable接口

```java
/**
 * Project Name : develop-doc
 * File Name    : OneRunnable
 * Package Name : springboot.example.demo.thread
 * Date         : 2020-05-01 16:32
 * Author       : jbz
 * Copyright (c) 2019, jiang.baozi@ustcinfo.com All Rights Reserved.
 */
package springboot.demo.thread;

/**
 * @ClassName : OneRunnable
 * @author : jbz
 * @Date : 2020-05-01 16:32
 * @Description :
 */
public class OneRunnable implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("线程运行"+i);
        }
    }

    public static void main(String[] args) {
        //创建线程，指定线程任务
        Thread thread = new Thread(new OneRunnable());
        thread.start();
    }
}
```

3.匿名类创建线程

```
/**
 * Project Name : develop-doc
 * File Name    : OneLambdaThread
 * Package Name : springboot.example.demo.thread
 * Date         : 2020-05-01 17:31
 * Author       : jbz
 * Copyright (c) 2019, jiang.baozi@ustcinfo.com All Rights Reserved.
 */
package springboot.example.demo.thread;

/**
 * @ClassName : OneLambdaThread
 * @author : jbz
 * @Date : 2020-05-01 17:31
 * @Description :   java8  Lambda写法
 */
public class OneLambdaThread {

    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名类创建线程");

            }
        });
        thread.start();

        Thread thread1 = new Thread( () -> {
            System.out.println("Lambda创建线程");
        });
        thread1.start();
    }

}
```
4.Callable：带返回值的线程

```java
/**
 * Project Name : develop-doc
 * File Name    : OneCallable
 * Package Name : springboot.example.demo.thread
 * Date         : 2020-05-01 20:40
 * Author       : jbz
 * Copyright (c) 2019, jiang.baozi@ustcinfo.com All Rights Reserved.
 */
package springboot.demo.thread;

import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @ClassName : OneCallable
 * @author : jbz
 * @Date : 2020-05-01 20:40
 * @Description :   
 */
public class OneCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("执行线程");
        return "执行结果：执行成功";
    }

    @SneakyThrows
    public static void main(String[] args) {
        OneCallable oneCallable = new OneCallable();
        FutureTask<String> futureTask = new FutureTask(oneCallable);
        Thread thread = new Thread(futureTask);
        thread.start();
        //获取线程执行结果
        String result = futureTask.get();
        System.out.println(result);

    }
}
```

##### 线程优先级

优先级是1-10，最低是1，最高是10，优先级越高，线程获取运行的机会就越多，默认是5。

##### Java锁

https://xiaomi-info.github.io/2020/03/24/synchronized/

有哪些锁：

* 公平锁/非公平锁

  公平锁指多个线程按照申请锁的顺序来依次获取到锁

  非公平锁即多个线程获取到锁的顺序，并不是安装它们申请锁的顺序

* 可重入锁

  又名递归锁

  ```javascript
  synchronized void setA() throws Exception{
      Thread.sleep(1000);
      setB();
  }
  
  synchronized void setB() throws Exception{
      Thread.sleep(1000);
  }
  ```

* 独享锁/共享锁

  独享锁：该锁一次只能被一个线程所持有

  共享锁：该锁可以被多个线程所持有

* 互斥锁/读写锁

  上面讲的独享锁/共享锁就是一种广义的说法，互斥锁/读写锁就是具体的实现。

* 乐观锁/悲观锁

* 分段锁

  是一种锁的设计，不是具体的锁

* 偏向锁/重量级锁/轻量级锁

  指的是锁的状态。

  偏向锁是指的是一段同步代码一直被一个线程所访问，那么该线程会自动获取锁。降低获取锁的代价。

  轻量级锁是指当锁是偏向锁的时候，被另一个线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不阻塞，提高性能。

  重量级锁是指当锁为轻量级锁的时候，另一个线程虽然是自旋，但自旋不会一直持续下去，当自旋一定次数的时候，还没有获取到锁，就会进入阻塞，该锁膨胀为重量级锁。重量级锁会让其他申请的线程进入阻塞，性能降低。

* 自旋锁

  当线程没有获得锁时，不是直接进入同步阻塞的状态，而是执行一个空循环，默认10次，这样是为了减少是为了减少线程被挂起的机率。

**死锁**

线程之间相互等着对方释放资源，而自己的资源又不释放给别人，这种情况就是死锁。

产生死锁的四个条件：

* 互斥：资源被一个线程使用后，其它线程就不能再使用了
* 不能抢占：一个线程不能抢占另一个线程的资源，只能等这个线程自己放开资源
* 请求和保持：线程在请求其它资源时，对自身占用的资源也不放开
* 循环等待：比如，A抢占了B的资源，B抢占了C的资源，C又抢占了A的资源，形成了闭环。

解决死锁问题的方法是：一种是用synchronized，一种是用Lock显式锁

在Java里任何一个对象都可以作为锁，当一个线程访问同步代码块时，它首先要得到锁，才能执行同步代码块，当退出时，必须需要释放锁。

##### 线程池

1. 什么是线程池     

   管理线程的池子，当有任务需要处理的时候，可以从线程池中取出线程来处理，处理完成后，线程也不会销毁，降低频繁创建创建、销毁线程的消耗，提高资源使用率和响应速度。

* **帮助我们管理线程资源：**线程实际也是一个对象，创建一个对象，需要经过类加载过程，销毁一个对象，需要走GC垃圾回收流程，都是需要资源开销的。 
* **提高响应速度**：执行任务时，可以直接去线程池中拿线程，而不需要重新去创建一条线程执行，提高了响应速度。
* **重复利用**：线程使用完毕，不进行销毁，而是放入线程池中，减少了线程创建和销毁的次数，达到重复利用的效果，节省资源。

2. 使用

* **核心线程**：线程池新建线程的时候，`当前线程总数< corePoolSize`，新建的线程即为核心线程，没有任务的线程
* **非核心线程**：线程池新建线程的时候，`当前线程总数< corePoolSize`，新建的线程即为核心线程。
* **最大线程数**：核心线程数+非核心线程数，**corePoolSize** 
* **存活时间**：非核心线程，空闲时存活的时间
* 阻塞队列：等待执行的任务
* **workQueue**：

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

### 缓存

```tex
程序将经常使用到的对象存入到内存中，使用的时候可以快速调用，不必要再去创建新的重复的实例。
```

分类：

**文件缓存**

把数据存到磁盘上，不管你是以XML格式，序列化文件DAT格式还是其它文件格式。

**内存缓存**

创建一个静态内存区域，将数据存储进去

##### 本地缓存

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

#### maven

##### 常见配置

```java
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

#### JSON

JSON是一种简单数据格式，它有三种数据结构：

* 键值对—（key-value）
* 对象—object
* 数组—arrays


