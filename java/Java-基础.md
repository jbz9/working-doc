https://www.cnblogs.com/crazymakercircle/p/13904029.html

[疯狂创客圈 JAVA 高并发 总目录 - 疯狂创客圈 - 博客园 (cnblogs.com)](https://www.cnblogs.com/crazymakercircle/p/9904544.html)

[面试题内容聚合 (qq.com)](https://mp.weixin.qq.com/s?__biz=MzI4Njc5NjM1NQ==&mid=2247488811&idx=3&sn=b04d8fdacf575c7ec959de4107f31091&chksm=ebd62a07dca1a31186f7f62de60ba7c7a88db26233562ed7183c6f609c5e7f4ffb448f30375f&scene=21#wechat_redirect)

# 基础知识点

## 常见知识点

### 一. 多线程、锁

![16988420671731698842066988.png](https://fastly.jsdelivr.net/gh/jbz9/picture@main/image/16988420671731698842066988.png)

##### 1.  volatile关键字

答：(1)保证变量的可见性：一个线程修改变量之后，会把值刷新到主内存中 ，其它线程立刻就能看到（2）防止JVM指令对代码进行重排

它是为了解决多线程下数据不一致的问题，也就是保证多线程下，变量的一个可见性，让线程直接从共享内存里读写变量，而不是从线程的本地内存副本里面读写。
另外一个作用的话，是为了防止JVM指令重排，因为编译器在运行的时候会对代码进行优化，导致了代码的执行顺序和我们实际编写的顺序不一致。

##### 2. synchronized关键字

它是对悲观锁的实现，用来锁住共享资源，控制并发访问，本身也是排它锁和非公平锁，可以用在实例方法或者静态方法或者代码块上，让它在多线程下，同一时刻，只有一个线程能访问资源。

普通方法上：使用的锁是this对象

代码块上：使用的就是对象锁

```java
 public  void say(boolean isYou){
        synchronized (obj){
            System.out.println("Hello");
        }
    }
```

静态方法上：使用的锁是类对象，就是把当前类的Class对象当成锁了

```java
public static synchronized void work(){
        System.out.println("Work hard...");
    }
```

补充知识：锁又叫做对象监视器（Object Monitor），可以把任何一个非null对象当做锁，synchronized本身也是**可重入锁和非公平锁**，可以避免死锁出现，当它锁住代码块的时候，退出或异常时，释放锁。

###### 2.1 Synchronized 和 ReentrantLock 

Reentrant（re-en·trant /rēˈentrənt/）可重入的

**相同点**

都是为了控制共享资源的同步访问

**区别**

* `synchronized` 是内置锁，ReentrantLock使用需要用lock和unlock，用来获取和释放。

ReentrantLock 例子

```java
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        // 创建并启动两个线程
        Thread thread1 = new Thread(new Worker("Worker 1"));
        Thread thread2 = new Thread(new Worker("Worker 2"));
        thread1.start();
        thread2.start();
    }

    static class Worker implements Runnable {
        private String name;

        public Worker(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            // 尝试获取锁
            System.out.println(name + " is trying to acquire the lock.");
            lock.lock();
            try {
                // 第一次获取锁
                System.out.println(name + " has acquired the lock for the 1st time.");
                someMethod();
                // 第二次获取锁（可重入）
                System.out.println(name + " has acquired the lock for the 2nd time.");
            } finally {
                lock.unlock(); // 释放锁
                System.out.println(name + " has released the lock.");
            }
        }

        private void someMethod() {
            lock.lock(); // 可重入地再次获取锁
            try {
                System.out.println(name + " is executing someMethod.");
            } finally {
                lock.unlock();
            }
        }
    }
}

```

###### 2.2 synchronized原理

在Java里面，每个对象都有一个内置的对象锁，位于对象头， synchronized是基于对象锁实现的，当线程获取到synchronized修饰的代码后，也就是monitorEnter和monitorExit字节码修饰的，会去获取对象锁，如果对象锁被其它线程占用，就会进入阻塞，知道对象锁释放；同时线程释放对象锁的时候，会把修改的数据刷新共享内存中，避免数据不一致；

对象的结构：

- 对象头（Object Header）
  - 哈希码（HashCode）：用于快速查找对象，标识对象的唯一性
  - 锁状态（Lock State）：管理多线程对对象的访问，包括以下状态：
    * 无锁：对象没有被锁定，可以被任意线程访问
    *  轻量级锁 (Lightweight Lock)：用于短时间内的争用情况，通过CAS（比较并交换）实现
    *  重量级锁 (Heavyweight Lock)：用于长时间的争用情况，由操作系统级别的锁来实现
    * 偏向锁 (Biased Lock)：用于只有一个线程访问对象的情况，减少同步操作的开销。
  - 垃圾回收信息（GC Info）：包括分代信息、存活状态、年龄等，用于垃圾回收
- 实例数据（Instance Data）
  - 成员变量和数据
  - 对象的属性和字段
- 对齐填充（Padding）

![16991602442731699160243940.png](https://fastly.jsdelivr.net/gh/jbz9/picture@main/image/16991602442731699160243940.png)

例子

```java
/**
 * Project Name : working-doc
 * File Name    : SynchronizedExample
 * Package Name : com.jiang.learn.thread.lock
 * Date         : 2023-11-05 10:36
 * Author       : jbz
 */
package com.jiang.learn.thread.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : jbz
 * @ClassName : SynchronizedExample
 * @Date : 2023-11-05 10:36
 * @Description :
 */
public class SynchronizedExample {

    public void synchronizedMethod() {
        synchronized (this) {
            System.out.println("hello world");
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    SynchronizedExample synchronizedExample = new SynchronizedExample();
                    synchronizedExample.synchronizedMethod();
                }
            });
        }
    }
}
```

执行：

javac SynchronizedExample.java

javap -c SynchronizedExample

```java
Compiled from "SynchronizedExample.java"
public class com.jiang.learn.thread.lock.SynchronizedExample {
  public com.jiang.learn.thread.lock.SynchronizedExample();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public void synchronizedMethod();
    Code:
       0: aload_0
       1: dup
       2: astore_1
       3: monitorenter // 对象锁
       4: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
       7: ldc           #3                  // String hello world
       9: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      12: aload_1
      13: monitorexit
      14: goto          22
      17: astore_2
      18: aload_1
      19: monitorexit //对象锁
      20: aload_2
      21: athrow
      22: return
    Exception table:
       from    to  target type
           4    14    17   any
          17    20    17   any

  public static void main(java.lang.String[]);
    Code:
       0: bipush        10
       2: invokestatic  #5                  // Method java/util/concurrent/Executors.newFixedThreadPool:(I)Ljava/util/concurrent/ExecutorService;
       5: astore_1
       6: iconst_0
       7: istore_2
       8: iload_2
       9: bipush        100
      11: if_icmpge     34
      14: aload_1
      15: new           #6                  // class com/jiang/learn/thread/lock/SynchronizedExample$1
      18: dup
      19: invokespecial #7                  // Method com/jiang/learn/thread/lock/SynchronizedExample$1."<init>":()V
      22: invokeinterface #8,  2            // InterfaceMethod java/util/concurrent/ExecutorService.submit:(Ljava/lang/Runnable;)Ljava/util/concurrent/Future;
      27: pop
      28: iinc          2, 1
      31: goto          8
      34: return
}

```

`monitorEnter`：这个指令用于获取对象的监视器锁。当一个线程执行到一个同步块（synchronized 块）的入口时，它会尝试获取对象的监视器锁。如果锁已被其他线程占用，那么该线程将被阻塞，直到锁被释放。

`monitorExit`：这个指令用于释放对象的监视器锁。当一个线程执行完同步块的代码或者通过异常退出同步块时，它将释放对象的监视器锁，以允许其他线程获取这个锁。



##### **3. synchronized 和 volatile 关键字的区别**

答：① synchronized 主要可以保证代码的原子性和线程之间的可见性，它可以用在方法和代码块上，volatile主要是保证线程之间的一个可见性，而且只能用在变量上，不能提供原子性保证
② 另外的话，多线程下synchronized 可能导致阻塞，volatile不会

##### 4. 并发编程的三个特性

（1）**原子性**：一个或多个操作，那么都执行，要么都不执行。synchronized 可以保证代码的原子性
（2）**可见性**：一个线程修改了共享变量，那么对其它线程是可见的，volatile可以保证
（3）**有序性**：因为编译器在运行的时候会对代码进行优化，导致了代码的执行顺序和我们实际编写的顺序可能不一致，这个用volatile可以禁止JVM指令重排

##### 5. 公平锁和非公平锁

答：公平锁：**按照线程顺序去拿锁**，它是按照线程在队列里面的顺序，来决定谁先拿锁，谁在队列前面，谁先拿锁。实际上它是会拿锁之前，去判断队列是不是空的，或者自己是不是在队列的最前面。

非公平锁：**先去拿锁，拿不到再去排队**。这样的话减少了线程被挂起的概率，效率比公平锁要高。

实际的区别：公平锁，会在线程拿锁之前，去判断队列是不是空的或者自己是不是在队列的头部第一个。是的话，才去拿锁。

**非公平锁效率高于公平锁，因为非公平锁减少了线程被挂起的概率**

##### 6. 乐观锁和悲观锁

答：（1）悲观锁：每次拿数据的时候都会去加锁，这个话就是用synchronized实现，直接加在代码块或者方法上面就行了；

（2）乐观锁：通过CAS（Compare And Swap）无锁算法，实现线程的同步访问 ，java并发包（java.util.concurrent）中的原子类（AtomicInteger）就是通过CAS来实现的乐观锁。

CAS：

每次拿数据的时候都不会加锁（如果有冲突，就重试，直到成功），只有在修改数据的时候，才会去判断数据有没有被使用，这个的话可以用CAS原子指令去实现，比较并替换，基于它的3个操作数：旧的预期值、新的预期值、内存值，只有**当的旧预期=内存上的值**，才去操作。或者版本号机制，就是每次修改数据的时候版本号+1，读取修改数据前的版本号，和修改之后准备提交时候的版本号是否一致。

**一般的话，读取数据上可以用乐观锁，写入数据上可以用悲观锁**

补充：悲观锁，它是假设最坏的情况，每次拿数据的时候，都去认为别人会修改数据

乐观锁：它是假设最好的情况，每次拿数据的时候，都不会拿锁，只有修改数据的时候，才去会判断 别人有没有修改这个数据。

**CAS：Compare And Swap的缩写，比较并替换。**

例子：比如有一个变量a值是1，现在想把它数据加1，那么有个线程1去操作了，此时对于它，旧的预期值就是1，新的预期值是2，内存上的值是1，如果没有其它干涉的话，那么它就会提交成功了，但是如果在他提交之前，有个线程2把它的值改成了2，也就是内存值是2了，那么前一个线程就会提交失败。 那么它就会重新计算重试，也就是**自旋**。

**例子**

1. **初始状态**：
   - 内存中有一个共享的变量 `V`，并有两个线程 A 和 B，它们都想更新这个变量。
   - A 想将变量 `V` 的值从 10 更新为 20，B 想将变量 `V` 的值从 10 更新为 30。
2. **比较**：
   - 首先，线程 A 从内存中读取变量 `V` 的当前值，它的值是 10。
   - 线程 B 也从内存中读取变量 `V` 的当前值，它的值也是 10。
3. **更新**：
   - 线程 A 更新变量 `V` 的值，将其从 10 更改为 20。
   - **但在执行更新之前，线程 A 会再次检查变量 `V` 的当前值是否仍然是 10。如果是，线程 A 将其更新为 20。**
4. **返回结果**：
   - 线程 A 成功更新变量 `V` 的值，现在 `V` 的值是 20。
   - 线程 B 也尝试执行相同的操作，但由于线程 A 先更新了变量 `V`，因此线程 B 的比较操作失败。线程 B 的操作不会改变变量 `V` 的值。

这种 CAS 操作的优势在于它是无锁的，不需要使用传统的锁机制，可以有效地减少线程争用和提高并发性能。

**核心是更新共享内存之前，查看比较共享内存有没有被更改。**

版本号机制：
一般是在数据表中加上一个数据版本号version字段，表示数据被修改的次数，当数据被修改时，version值会加一。当线程A要更新数据值时，在读取数据的同时也会读取version值，在提交更新时，若刚才读取到的version值为当前数据库中的version值相等时才更新，否则重试更新操作，直到更新成功。

`UPDATE log set username='admin2' WHERE version=5`

##### 7. 独占锁和共享锁

答：是根据这个锁能不能多个线程占用来划分的，可以的话，它就是共享锁，可以被多线程读取，只能被一个线程修改（ReentrantReadWriteLock）。不可以的话它就是独占锁，比如synchronized和Reentrantlock

##### 8. 死锁

答：死锁的话是因为，多个线程去竞争同一个共享资源（**资源的互斥性**），线程之间又相互等待对方释放资源，但是自身资源又不主动释放，从而导致了线程被无限制的阻塞。

它的产生的条件有几个条件：

①资源的互斥性，也就是共享资源同一时刻只能被一个线程持有；

②持有和等待（Hold and Wait）：线程在请求另外一个资源B时，本身持有的资源A是不释放的；

③不可抢占：线程持有的资源，只能自己是否，其它线程不能抢占；

④ 循环等待资源释放，等待闭环。

![16991767732641699176772750.png](https://fastly.jsdelivr.net/gh/jbz9/picture@main/image/16991767732641699176772750.png)

如果要解决的话，可以针对它产生的条件去破坏。比如在线程请求不到资源的时候，去**主动释放自身拥有的资源**，或者一次性申请所有的资源，破坏循环等待条件等。

##### 7. Java四种引用：强、软、弱、虚

答：**强引用**：一般代码里面的对象就是强引用，比如new Object，强引用的话即使内存溢出，JVM是不会对它进行垃圾回收的

**软引用**：它是有用的对象，但不是必须，在JVM内存不足的时候，就会回收它，是可以放在防止内存溢出的，它的话用SoftReference来表示

```java
 SoftReference<Object> obj =new SoftReference<Object>(new Object());
```

**弱引用**：也是非必须的对象，只要JVM开始垃圾回收了，就会回收它，用WeakReference来表示

```java
 WeakReference<Object> weakObj =new WeakReference<Object>(new Object());
```

**虚引用:**随时可能被JVM回收，而且它也必须和引用队列一起使用

```java
PhantomReference<Object> phantomReference =new ReferenceQueue<Object>(new Object());
```

##### 8. sleep() ⽅法和 wait() ⽅法区别和共同点

答：

**相同点**

2个都可以暂停线程

**不同点**

sleep是暂停线程，一般定时任务用的比较多；

wait是Object类的方法，会释放它持有的锁，还有的就是，wait等待之后，需要另外的线程去notify通知唤醒它，一般用在线程之间的相互协作。

例子

```java
public class WaitExample {
    public static void main(String[] args) {
        final Object lock = new Object(); // 创建一个共享对象作为锁

        Thread waitingThread = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("等待线程X：等待消息...");
                    lock.wait(); // 进入等待状态
                    System.out.println("等待线程X：收到消息了！");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread notifyingThread = new Thread(() -> {
            synchronized (lock) {
                System.out.println("通知线程Y：发送通知...");
                lock.notify(); // 唤醒等待中的线程
                System.out.println("通知线程Y：通知已发送！");
            }
        });

        waitingThread.start();
        notifyingThread.start();
    }
}

打印输出：
等待线程X：等待消息...
通知线程Y：发送通知...
通知线程Y：通知已发送！
等待线程X：收到消息了！
```

##### 9. 为什么调用线程的start方法，而不是run方法

答：因为调用start方法之后，它就会启动一个线程，而且进入线程就绪状态，然后自动执行run方法里面业务逻辑，但是如果去调用run方法，它是不会去启动一个线程的，只会当中普通的方法在主线程执行

##### 10、线程的状态有哪些

答：当我们new了线程，这时候线程处于**新建状态**，当我们调用start方法的时候，线程就处于一个**可运行的状态**，当线程调用run方法的时候，它就处于**运行状态**，当我们调用sleep或者wait方法时候，线程就会由运行状态，变成**阻塞状态**，当run方法运行完成之后，线程被销毁，也就是**死亡状态**。

##### 11、如何创建线程

答：（1）继承Thred线程类，重写run方法；（2）实现Runnable接口，重写它的run方法；（3）实现Callable接口(重写call方法，有返回值)；（4）线程池创建线程

```java
public class ThreadExample {
    static class ThreadOne extends Thread{
        @Override
        public void run(){
            System.out.println("执行任务A");
        }
    }

    static class ThreadTwo implements Runnable{
        @Override
        public void run() {
            System.out.println("执行任务B");
        }
    }

    static class ThreadThree implements Callable {
        @Override
        public Object call() throws Exception {
            System.out.println("执行任务C");
            return true;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadOne threadOne = new ThreadOne();
        threadOne.start();

        Thread threadTwo = new Thread(new  ThreadTwo());
        threadTwo.start();


        FutureTask<Boolean> futureTask = new FutureTask<>(new ThreadThree());
        Thread thread = new Thread(futureTask);
        thread.start();
        Boolean result = futureTask.get();
        System.out.println(result);
    }
}
```

##### 12. synchronized和ReentrantLock

答：相同点：都是可重入锁，都是用来做多线程下同步机制的。不同点：synchronize是JVM实现的，ReentrantLock(re en tran lock)，而且ReentrantLock增加了一些其它功能，比如说可以指定锁是公平锁，可以响应中断和限时等待，而synchronized不可以。但是ReentrantLock使用的话需要自己加锁、解锁，synchronized是不需要的

补充：可重入锁，比如说2个方法A、B，它们都使用到同一个实例锁，那么一个线程在执行A方法，已经获取实例锁的情况，在A方法里面，再去调用方法B，它可以再次获取到这个实例锁的，只会把锁的计数器+1，而不会造成死锁的，如果是不可重入锁的话，就不可以了。

响应中断：即如果一个线程获取不到锁，就会一直等待，响应中断可以放弃等待

限时等待：获取锁的时候，传一个时间参数，指定等待的时间

synchronized和ReentrantLock也叫做阻塞同步，它们都是通过阻塞其它线程，来实现互斥，是一种悲观的配并发策略，无论共享数据是否真的会出现竞争，它都要进行加锁。还有一种同步方式，叫非阻塞同步

##### 13、join方法

答：join的话就是先把自己的线程挂起来，去执行另外一个线程，等另外一个线程执行完了，再执行自己的任务

##### 14、线程的优先级范围是什么

答：范围是1-10，默认是5。线程的优先级越高，被先执行的概率也就越高

##### 15、**日常工作中有用到线程池吗？什么是线程池？为什么要使用线程池？**

答：用到过；

线程池就是采用池化思想来管理线程的工具；

对于服务器来说，现在的服务器大多都是多核CPU，使用多线程的话，并行执行任务，能够提高系统响应速度和处理能力，但是服务器的资源是有限的，如果不用多线程的话，频繁的创建和销毁线程，也是对资源的一种消耗，使用线程池能够避免这些问题，降低资源的消耗。

##### 16、线程池的submit和execute方法的区别

答：submit是有返回值的，用的Callable；execute没有返回值,用的是Runnable

##### 17、线程池有哪几类？

答：5种。

① 定长线程池：核心线程数=最大线程数，一般用在需要控制并发数，放在过载

②定时线程池：用来执行一些定时任务

③缓存线程池：它的核心线程数是0，最大线程数不固定，最大值是int类型的最大值，一般用在短时间的大量任务

④单个线程池：核心线程数、最大线程数都是1，一般在顺序任务上，比如文件处理、数据同步

⑤工作窃取线程池：核心线程数和最大线程数都不固定，根据具体的服务器CPU核数来确定，而且每个线程有自己的工作队列，如果一个线程完成了自己队列的任务，会从其他线程的队列中"窃取"任务来执行，以提高并行性。适合大规模的数据处理

补充：

```java

        //定长线程池，可以控制线程数量，任务超过线程数，则会在队列中等待，核心线程 = 最大线程
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

        //定时线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        //缓存线程池，不定长的线程池，核心线程数为0，最大线程数不限定
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

       //单线程池，使用唯一的工作线程来执行工作，保证任务按照优先级来执行
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(
            () -> {
            // ... do something inside runnable task
        });
        singleThreadExecutor.shutdown(); 

       //工作窃取线程池
 ExecutorService executor = Executors.newWorkStealingPool();
```

###### 1.1创建线程池的参数

创建线程池使用**ThreadPoolExecutor**类，有7个参数，分为corePoolSize 核心线程数、最大线程数、存活时间、时间单位、缓冲队列（已提交但是没有执行的任务放在这里）、线程工厂（设置线程名称）和拒绝策略。

###### 1.2 线程池的状态

1. **Running（运行）**：线程池正常运行，可以接受新任务并处理已提交的任务。
2. **ShutDown（关闭）**：不再接受新任务，但会继续执行已提交的任务，直到所有任务完成。
3. **Stop（停止）**：不再接受新任务，不会执行已提交的任务，会中断正在执行的任务线程。
4. **Tidying（整理）**：所有任务都已终止，工作线程数量为0，线程池进入该状态进行资源清理。
5. **Terminated（终止）**：线程池彻底终止，不能再执行任何任务

![16995428014031699542800677.png](https://fastly.jsdelivr.net/gh/jbz9/picture@main/image/16995428014031699542800677.png)

https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html

###### 1.1阻塞队列

1. **LinkedBlockingQueue（无界队列）**：

   - 特点：无界队列，可以无限制地存储元素，但可能占用大量内存。
   - 使用场景：适用于需要存储大量数据的场景，但需要注意内存占用。
   - 代码示例：

   ```
   javaCopy codeBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
   queue.put(1);  // 阻塞等待，直到队列有空间
   int item = queue.take();  // 阻塞等待，直到队列有数据
   ```

2. **ArrayBlockingQueue（有界队列）**：

   - 特点：有界队列，需要指定最大容量，不会占用过多内存。
   - 使用场景：适用于需要限制队列大小的场景，可以控制资源消耗。
   - 代码示例：

   ```
   javaCopy codeBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);  // 指定队列容量
   queue.put(1);  // 阻塞等待，直到队列有空间
   int item = queue.take();  // 阻塞等待，直到队列有数据
   ```

3. **PriorityBlockingQueue（优先级队列）**：

   - 特点：基于元素的自然顺序或自定义比较器的顺序来执行出队操作，不会阻塞。
   - 使用场景：适用于需要按照优先级顺序处理任务的场景。
   - 代码示例：

   ```
   javaCopy codeBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();
   queue.offer(3);
   queue.offer(1);
   int item = queue.poll();  // 不会阻塞，按优先级出队
   ```

4. **SynchronousQueue（同步队列）**：

   - 特点：同步队列中的每个插入操作必须等待一个对应的删除操作，反之亦然。
   - 使用场景：适用于实现生产者-消费者模式，其中生产者和消费者需要精确匹配。
   - 代码示例：

   ```
   javaCopy codeBlockingQueue<Integer> queue = new SynchronousQueue<>();
   new Thread(() -> {
       try {
           queue.put(1);  // 阻塞等待，直到有消费者取走数据
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }).start();
   
   int item = queue.take();  // 阻塞等待，直到有生产者放入数据
   ```

这些是Java中常见的阻塞队列，它们可根据不同的需求在多线程编程中使用，以实现线程之间的数据交换、任务调度等功能。根据具体的应用场景，你可以选择合适的阻塞队列类型来满足需求。

###### 1.2 拒绝策略

用来处理线程池无法接受新任务时的采取的措施，4个拒绝策略：

①默认是拒绝执行新任务，丢出异常，用的最多的方式

②直接丢失新任务

③丢失最开始的任务，这2种都会导致任务丢失

④使用调用者的线程去执行新任务。这个策略如果过来的任务过多，调用者线程执行不过来，可能会导致同步阻塞任务。

```java
import java.util.concurrent.*;

public class RejectedExecutionHandlerExample {
    public static void main(String[] args) {
     //   testAbortPolicy();
        testCallerRunsPolicy();
       // testDiscardPolicy();
       // testDiscardOldestPolicy();
    }

    private static void testAbortPolicy() {
        // 创建一个线程池，最大线程数为1，队列容量为1，使用 AbortPolicy 拒绝策略
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        // 定义三个任务
        Runnable task1 = () -> System.out.println("Task 1 executed.");
        Runnable task2 = () -> System.out.println("Task 2 executed.");
        Runnable task3 = () -> System.out.println("Task 3 executed.");

        // 提交三个任务到线程池
        executor.execute(task1);  // Task 1将被执行
        executor.execute(task2);  // Task 2将被执行 队列中
        executor.execute(task3);  // Task 3会被拒绝执行
    }

    private static void testCallerRunsPolicy() {
        // 创建一个线程池，最大线程数为1，队列容量为1，使用 CallerRunsPolicy 拒绝策略
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 定义三个任务
        Runnable task1 = () -> System.out.println(Thread.currentThread().getName()+": Task 1 executed.");
        Runnable task2 = () -> System.out.println(Thread.currentThread().getName()+": Task 2 executed.");
        Runnable task3 = () -> System.out.println(Thread.currentThread().getName()+": Task 3 executed.");

        // 提交三个任务到线程池
        executor.execute(task1);  // Task 1将被执行
        executor.execute(task2);  // Task 2将被执行
        executor.execute(task3);  // Task 3会由当前线程执行
    }

    private static void testDiscardPolicy() {
        // 创建一个线程池，最大线程数为1，队列容量为1，使用 DiscardPolicy 拒绝策略
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

        // 定义三个任务
        Runnable task1 = () -> System.out.println("Task 1 executed.");
        Runnable task2 = () -> System.out.println("Task 2 executed.");
        Runnable task3 = () -> System.out.println("Task 3 executed.");

        // 提交三个任务到线程池
        executor.execute(task1);  // Task 1将被执行
        executor.execute(task2);  // Task 2将被执行
        executor.execute(task3);  // Task 3会被丢弃
    }

    private static void testDiscardOldestPolicy() {
        // 创建一个线程池，最大线程数为1，队列容量为1，使用 DiscardOldestPolicy 拒绝策略
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());

        // 定义三个任务
        Runnable task1 = () -> System.out.println("Task 1 executed.");
        Runnable task2 = () -> System.out.println("Task 2 executed.");
        Runnable task3 = () -> System.out.println("Task 3 executed.");

        // 提交三个任务到线程池
        executor.execute(task1);  // Task 1将被执行
        executor.execute(task2);  // Task 2会被丢弃
        executor.execute(task3);  // Task 3将被执行
    }
}

```

```java
public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             Executors.defaultThreadFactory(), handler);
    }    


public void creatThread() {
        /**
         * 手动创建 线程池
         *         public ThreadPoolExecutor(int corePoolSize, 线程池中的核心线程数
         *         int maximumPoolSize, 线程池中的最大线程数
         *         long keepAliveTime, 存活时间，当线程池数量超过核心线程数时，多余的空闲线程存活的时间，
         *                             即：这些线程多久被销毁。
         *         TimeUnit unit,      时间单位
         *         BlockingQueue<Runnable> workQueue,    等待队列，线程池中的线程数超过核心线程数时，
         *                                               任务将放在等待队列，它是一个BlockingQueue类型的对象
         *         ThreadFactory threadFactory,    线程工厂
         *         RejectedExecutionHandler handler) 拒绝策略，当线程池和等待队列都满了之后，需要通过该对象的回调
         *         函数进行回调处理
         */

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        singleThreadPool.execute(() -> System.out.println(Thread.currentThread().getName()));
        singleThreadPool.shutdown();
    }
```

###### 1.2 线程池的工作流程

![16995012593551699501258434.png](https://fastly.jsdelivr.net/gh/jbz9/picture@main/image/16995012593551699501258434.png)

###### 1.3线程池核心参数设置多少合适呢？

①需要结合具体的业务场景，可以通过不断的阶梯式压测，比如设置成服务器CPU核数，来观察服务器的CPU、内存、负载情况，找到一个比较合适的值；

②一般的话，如果是I/O密集型任务，比如说文件读取、网络请求，可以设置大一些，因为I/O的话会等待资源的时间偏多，I/O等待的过程中，会释放CPU资源，核心线程数更大的话，可以更好的利用CPU时间。

如果是CPU密集型任务，比如一些计算任务，线程会一直占用CPU资源，那么核心线程数可以设置相对小一些，防止频繁的CPU切换，导致资源消耗。

##### 17、垃圾回收机制介绍一下

答：

**①标记-清除算法 (Mark-Sweep Algorithm):**

**②标记-整理算法 (Mark-Compact Algorithm)**：

**③复制算法 (Copying Algorithm)**：

**标记-整理算法 (Mark-Compact Algorithm)**：

创建对象，分配到Eden区，当Eden区空间满了，就触发一次Young GC，将还在使用的对象复制到幸存区From,这样Eden被清空，以供继续存储对象，当Eden再次满了的时候，再触发一次Young GC，将Eden和幸存From区中还在被使用的对象复制到幸存区的to区，下一次，Young GC则是将Eden和To区中还在使用的对象放入到From区，这样，经过多次GC，有些对象会在From和To区经过多次复制，都没有被释放，那么到达一个阈值之后，这些对象就将放到老年代，如果老年代空间也用完，就会触发Full GC全量回收。

##### 19、CAP了解吗

答：是值分布式系统中，Consistency(一致性)、Availability（可用性）、Partition tolerance（分区容错性），三者不可得兼。

一致性（C）：所有节点的数据是不是一样的

> 在分布式系统中的所有数据备份，在同一时刻是否同样的值。（等同于所有节点访问同一份最新的数据副本）

可用性（A）：系统挂掉一部分节点之后，系统还能不能响应用户的读写

> 在集群中一部分节点故障后，集群整体是否还能响应客户端的读写请求。（对数据更新具备高可用性）
>
> 某个读写操作在出问题的机器上不能读写了，但是在其他机器可以完成

分区容错性（P）：

> 网络节点在无法通讯的情况下，节点被隔离了，产生了网络分区，但是整个系统还是可用的。以实际效果而言，分区相当于对通信的时限要求。系统如果不能在时限内达成数据一致性，就意味着发生了分区的情况，必须就当前操作在 C 和 A 之间做出选择。

在一个分布式系统中， 在出现节点之间无法通信（网络分区产生）， 你只能选择 可用性 或者 一致性， 没法同时选择他们。

补充：

[分布式事务，这一篇就够了 | 小米信息部技术团队 (xiaomi-info.github.io)](https://xiaomi-info.github.io/2020/01/02/distributed-transaction/)


##### 16、前后端分离项目中，接口安全性如何保证？

答：主要是用**用户身份验证**、访问控制（访问频率、ip访问次数）。

①做了token校验：用户登录之后，后台使用jwt生成token，并存到缓存里面，再把token返回给前端，之后接口访问的话，前台需要带token，后台会进行token校验

②https数据传输加密

###### 1.1Token原理

![16997640505081699764049756.png](https://fastly.jsdelivr.net/gh/jbz9/picture@main/image/16997640505081699764049756.png)

###### **1.2JWT**

![16997717993581699771799143.png](https://fastly.jsdelivr.net/gh/jbz9/picture@main/image/16997717993581699771799143.png)

JSON Web Token（JWT）是一种用于在不同实体之间安全传输信息的开放标准。它基于三部分构成：头部、载荷和签名。

**头部（Header）：**

JWT的头部包含了两部分信息：加密算法和令牌类型。它采用JSON格式，然后经过Base64编码。一个典型的头部可能是：

```
jsonCopy code{
  "alg": "HS256",
  "typ": "JWT"
}
```

- `alg`（algorithm）表示使用的签名算法，比如HS256代表HMAC SHA-256。
- `typ`（type）表示令牌的类型，通常为JWT。

**载荷（Payload）：**

载荷包含了JWT的声明信息。它采用JSON格式，同样经过Base64编码。一个典型的载荷可能是：

```
jsonCopy code{
  "sub": "1234567890",
  "name": "John Doe",
  "exp": 1516239022
}
```

- `sub`（subject）表示令牌的主题，即标识令牌所属的用户或实体。
- `name`表示令牌所属主体的姓名。
- `exp`（expiration）表示令牌的过期时间。

**签名（Signature）：**

签名是通过对头部和载荷进行编码（通常是Base64编码），然后使用指定的加密算法和密钥生成的。签名用于验证令牌的完整性和真实性。

签名的生成过程包括：

- 对头部和载荷进行Base64编码。
- 使用指定的签名算法和密钥对编码后的数据进行签名。
- 将签名添加到JWT的第三部分。

**验签**

服务器端，你可以使用相同的加密密钥对 JWT 进行验证。一般情况下，验证 JWT 包括以下步骤：

1. **接收 JWT：** 服务器接收到来自客户端的 JWT。
2. **解析 JWT：** 将 JWT 拆分为 Header、Payload 和 Signature 三个部分。
3. **验证签名：** 对接收到的 Header 和 Payload 进行相同的签名计算，使用相同的密钥。
4. **比较签名：** 将服务器端计算出的签名与接收到的 Signature 进行比较。
5. **验证有效期：** 验证 Payload 中的时间戳是否过期。

###### Oauth2

OAuth 2.0（Open Authorization 2.0）是一种授权框架，用在单点登录（SSO）、第三方登录、API访问控制等；JWT（JSON Web Token）通常被用作OAuth 2.0的访问令牌。

###### RBAC权限控制模型了解吗

答：它是通过角色关联来控制用户的权限，一个用户对应多个角色，一个角色对应多个菜单权限。

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1624267390395-1624267390385.png)

补充：

RBAC 即基于角色的权限访问控制（Role-Based Access Control）。通常 RBAC 下的权限设计相关的表有 5 张，其中有 2 张用于建立表之间的联系：用户表、角色表、菜单表、用户角色关系表、角色菜单关系表

### 五、spring

##### 1、springmvc工作流程

答：用户请求到前端处理器DispatchServlet——>前端处理器去查询HandlerMapper，找到对应处理请求的Controller——>Controller调用业务逻辑之后得到ModelAndView——>将得到ModelAndView发给视图解析器进行渲染——得到view——最后把渲染的view返回给用户

##### 2、了解IOC控制反转和DI吗

IoC（Inverse of Control:控制反转）

答：IOC的话是控制反转，它是一种设计理念，达到解耦的效果，DI是依赖注入，它是控制反转的一种的实现方式。在正常的代码里面，如果需要使用对象，需要应用程序自己去创建，这样的话容易造成代码的耦合，而且在内存里面会存在多个重复的对象。而IOC，它是把对象的创建和依赖关系的管理都交给了Spring容器，在我们需要是要使用的时候，只需要通过注解（Autowried）声明就可以了。

**依赖注入（DI）**

1. 构造函数注入（Constructor Injection）：

通过类的构造函数注入依赖。Spring容器在实例化Bean时，会调用具有相应参数的构造函数。

**使用方式：**

```
public class MyService {
    private MyRepository repository;

    // 构造函数注入
    public MyService(MyRepository repository) {
        this.repository = repository;
    }
}
```

2. Setter方法注入（Setter Injection）：

通过Setter方法为Bean的属性设置值。Spring容器调用Bean的Setter方法注入依赖。

**使用方式：**

```
public class MyService {
    private MyRepository repository;

    // Setter方法注入
    public void setRepository(MyRepository repository) {
        this.repository = repository;
    }
}
```

3. 注解方式注入：

通过注解标记实现依赖注入。常见的注解有 `@Autowired`, `@Resource`, `@Inject` 等。

**使用方式：**

```
public class MyService {
    @Autowired
    private MyRepository repository;
}
```

4. 接口注入（Interface Injection）：

通过接口实现依赖注入。使用接口定义依赖，实现类通过实现该接口完成依赖的注入。

**使用方式：**

```
public interface MyRepository {
    // method declarations
}

public class MyRepositoryImpl implements MyRepository {
    // method implementations
}

public class MyService implements MyRepository {
    // Implement methods from MyRepository interface
}
```

##### 3、AOP切面编程介绍一下

答：AOP就是面向切面编程，它的话也是一种设计思想。AOP底层的话是动态代理。是针对程序里面，有一些共同的操作比如说日志、权限管理，我们就可以把它抽离出来，看做一个切面，使用@Aspect做一个切面类，设置切入点和通知方式，切入到需要日志或者权限验证的业务方法，降低了代码的耦合度，提高了代码的**复用性**。

基于Java的主要AOP实现有：

1. AspectJ
2. Spring AOP
3. JBoss AOP

实现：aspects注解实现

项目中哪里用到了：

日志、用户权限验证

- **切面(Aspect)** – 一些横跨多个类的公共模块，如日志、安全、事务等。简单地说，日志模块就是一个切面。 使用@Aspect 注解的类就是切面.

- **连接点(Joint Point)** – 目标类中插入代码的地方。连接点可以是方法、异常、字段，连接点处的切面代码会在方法执行、异常抛出、字段修改时触发执行。

- **建议(Advice)** – 在连接点插入的实际代码(即切面的方法)，有5种不同类型（后面介绍）。

  ```java
   @Before("pointCut()")
      public void isSuperAdmin(JoinPoint joinPoint) {
          if (!UserUtils.isTenantManager()) {
              throw new GlobalException("您无权限访问该接口", 		         GlobalConst.Response.Code.NO_ACCESS);
          }
      }
  ```

- **切入点(Pointcut)** – 定义了连接点的条件，一般通过正则表达式。例如，可以定义所有以`loadUser`开头的方法作为连接点，插入日志代码。

  ```java
   @Pointcut("@annotation(com.gccloud.starter.core.annation.RequiresSuperAdmin)")
      public void pointCut() {
      }
  ```


通知范围

- **before** – 在方法之前运行建议（插入的代码）
- **after** – 不管方法是否成功执行，在方法之后运行插入建议（插入的代码）
- **after-returning** – 当方法执行成功，在方法之后运行建议（插入的代码）
- **after-throwing** – 仅在方法抛出异常后运行建议（插入的代码）
- **around** – 在方法被调用之前和之后运行建议（插入的代码）

##### 4、Bean的作用域

答：单例、原型、会话、全局会话、一个请求内

补充：

| 作用域                 | 描述                                                         |
| ---------------------- | ------------------------------------------------------------ |
| 单例singleton          | 在spring IoC容器仅存在一个Bean实例，Bean以单例方式存在，默认值 |
| 原型prototype          | 每次从容器中调用Bean时，都返回一个新的实例，即每次调用getBean()时，相当于执行newXxxBean() |
| 请求request            | 每次HTTP请求都会创建一个新的Bean，该作用域仅适用于WebApplicationContext环境 |
| 会话session            | 同一个HTTP Session共享一个Bean，不同Session使用不同的Bean，仅适用于WebApplicationContext环境 |
| 全局会话global-session | 一般用于Portlet应用环境，该运用域仅适用于WebApplicationContext环境 |

**生命周期**

定义——初始化——使用——销毁

**配置方式**

使用Scope

```java
@Bean
@Scope("singleton")
public MySingletonBean mySingletonBean() {
    return new MySingletonBean();
}
```

##### 5、springboot启动原理

答：主要是根据启动类的注解springbootApplication，它是由import、一个扫描包注解和一个自动配置类注解。springboot在启动的时候会通过自动配置类注解（EnableAutoConfiguration）找到所有jar下META-INF下的spring.factories文件，这个配置文件下记录了所有加载到IOC容器里面的配置装配类的全类名，最后是否加载的还需要Conditional条件注解来决定是否注入。

##### 6、springboot比springmvc有点在哪里

答：springboot是约定大于配置，通过自动配置解决了springmvc大量配置XML的问题

##### 7、springboot里面有哪些常用的starter

答：

* spring-boot-starter-web
* spring-boot-starter-test
* spring-boot-starter-data-jpa
* spring-boot-starter-data-redis

###### 怎么定义一个Starter

1、定义一个工程，父工程是springboot

2、写一个autoConfiguration自动配置项和一个配置类

3、在resource/META-INF下，创建spring.factories文件，写入一个spring EnableAutoConfiguration，把刚刚的EnableAutoConfiguration自动配置项的全类名写上。

##### 8、springboot核心配置文件有哪些

答：有application文件，有yml和properties两种格式（yml是key和value格式，properties用=号），还有bootstrap 文件

##### 9、Spring Boot 的核心注解是哪个？它主要由哪几个注解组成的？

答：是springbootApplication启动类注解，是有3个注解组成的，分为是@SpringBootConfiguration 配置类注解、@EnableAutoConfiguration开启自动配置和@ComponentScan组件扫描以及@SpringBootConfiguration

##### 10、怎么样把项目变成springboot

答：继承spring-boot-parent工程；或者直接依赖springboot-dependencies。如果要更改版本的话，那么在导入的时候就加入版本号

##### 11、自动装配原理

答：它是基于spring的自动装配机制，也就是EnableAutoConfiguration注解

①通过EnableAutoConfiguration去读取所有starter项目下的META-INF/spring.factories，读取里面所有配置类的全类名

②根据Configuration、Conditional注解，进行按需加载，注册bean

②spring.factories这里文件里面配置了所有加载到IOC容器里面的自动配置类的全路径，然后根据自动配置类的里面的Configuration、Conditional注解来查找注册bean

##### 12、如何指定用哪个配置文件

答：用spring:  profiles:    active: 指定。

##### 13、Spring事务

答：

补充：Spring事务的本质其实就是数据库对事务的支持，没有数据库的事务支持，spring是无法提供事务功能的。真正的数据库层的事务提交和回滚是通过binlog或者redo log实现的。

注解@transactional的底层实现是Spring AOP技术，而Spring AOP技术使用的是动态代理。这就意味着对于静态（static）方法和非public方法，注解@Transactional是失效的。

##### 14、RequestMapper

答：RequestMapper用在类上或者方法上，用来映射请求的URL

##### 15、介绍一下Spring Bean 的自动注入？

答：spring Bean自动注入有2种方式，一种是xml，基本不用，一种是注解，也就是Autowired和Resource，Autowired是按照类型在IOC容器里面查找bean对象，然后赋值给变量。`@Autowired` 默认按照类型进行自动装配，如果存在多个匹配类型的Bean，Spring会尝试按照属性名或者使用`@Qualifier`注解的Bean进行匹配

补充：

Autowired

如果查询结果刚好为一个，就将该bean装配给@Autowired指定的数据；
如果查询的结果不止一个，那么@Autowired会根据名称来查找；
如果上述查找的结果为空，那么会抛出异常。解决方法时，使用required=false。

注解装配在默认情况下是不开启的，为了使用注解装配，我们必须在Spring配置文件中配置 <context:annotation-config/>元素（spring）。使用@Qualifier 注解和 @Autowired 通过指定应该装配哪个确切的 bean 来消除歧义。

### 六、spring cloud 及微服务

##### 1、介绍一下微服务？

答：微服务是对之前的系统中模块进行拆分，让模块变成一个个独立的组件，然后各个组件之间通过网络进行通讯交互。springcloud是微服务的解决方案，整合了微服务的基础组件。

补充：

微服务的优点：

* 故障隔离：因为每个系统都是独立部署的，所以如果有某一个系统出现了故障，那么它也不会影响到其它系统，导致整个系统都不可用。
* 更小的缩放粒度：可以针对某个组件进行水平缩放，而不需要对整个系统进行缩放
* 整个系统都是由各个组件组成的，系统的耦合度很低，方便开发和之后的升级。

##### 2、微服务组件有哪些？

答：

* 注册中心：Eureka（尤里卡），基于Rest服务的分布式中间件，用于服务的注册和发现。
* 网关：zuul(z 哦)或者springcloud gateway，服务网关，为微服务集群提供代理、过滤、路由的功能
* 配置中心：springcloud config，管理集群中的配置文件
* 负载均衡：Ribbon(re 本)，在微服务集群中，为客户端通讯提供支持，实现中间层的负载均衡。
* 容错：Hystrix(黑死特瑞)，添加延迟容错和容错逻辑，控制各个服务之间的交互

springcloud是一个微服务框架集合，集成了一系列组件，用来解决使用微服务的所遇到的问题，比如注册中心、负载均衡、网关这些。

补充：

Ribbon:

Ribbon 是一个基于 HTTP 和 TCP 的客户端负载均衡器，当使用 Ribbon 对服务进行访问的时候，它会扩展 Eureka 客户端的服务发现功能，实现从 Eureka 注册中心中获取服务端列表，并通过 Eureka 客户端来确定服务端是否己经启动。 Ribbon 在 Eureka 客户端服务发现的基础上，实现了对服务实例的选择策略， 从而实现对服务的负载均衡消费。

消费者：消费者使用Eureka去发现服务，使用Ribbon去调用服务

服务需要发送心跳到注册中心，如果没有发送心跳到注册中心，注册中尤里卡就会注销服务

尤里卡的自我保护机制：为了防止因为网络问题，服务没有及时发送心跳，注册中心会发送告警，而不是去删除服务

硬件负载均衡：比如F5、深信服

软件负载均衡：nginx、LVS

Ribbon 是客户端的负载均衡，nginx是服务端负载均衡，Ribbon需要去注册中心获取注册地址，nginx是手动配置好服务地址。

硬件负载均衡或是软件负载均衡,他们都会维护一个可用的服务端清单,通过心跳检测来剔除故障的服务端节点以保证清单中都是可以正常访问的服务端节点。当客户端发送请求到负载均衡设备的时候,该设备按某种算法(比如轮询、权重、最小连接数等)从维护的可用服务端清单中取出一台服务端的地址,然后进行转发。

**Hystrix**

它是用来解决服务降级、服务熔断，防止因为某个服务不能用而导致整个服务不能用，导致服务雪崩

一般配置在消费者那边，解决调用服务异常或者并发量太大的情况

网关**gateway**

作为整个微服务API请求的入口，作用有：路由转发、权限验证、限流(流量控制)

Feign(非)

是对Hystrix和Ribbon的封装

**配置中心**

分布式配置中心，spring-cloud-config。搭建一个配置中心仓库（git仓库），然后客户端就不需要application配置文件了，只要去使用bootstrap去连接配置中心，指定需要的配置文件就可以了

##### 3、Springcloud和Dubbo的区别

答：Dubbo是用的RPC协议，Springcloud用的是Http协议；

补充：

- 服务调用方式
  - dubbo是RPC
  - SpringCloud采用Rest Api
- 注册中心
  - dubbo 是nacos、zookeeper
  - SpringCloud是eureka，也可以是nacos、zookeeper
- 服务网关
  - dubbo本身没有实现，只能通过其他第三方技术整合，
  - SpringCloud有Zuul、geteway路由网关，作为路由服务器，进行消费者的请求分发,springcloud支持断路器，与git完美集成配置文件支持版本控制，事物总线实现配置文件的更新与服务自动装配等等一系列的微服务架构要素。

##### 4、安全认证Oauth

答：实现是用的spring security oauth2。

登录token使用jwt

补充：

后端包含：资源服务器和授权服务器，授权服务器可以共用，其它服务器来请求资源服务；

Oauth2有4种角色：用户-前台-授权服务器-资源服务器

| 名称       | 英文名               |                             描述                             | web例子      |
| :--------- | :------------------- | :----------------------------------------------------------: | :----------- |
| 资源所有者 | resource owner       | 能够授予对受保护资源的访问权的实体。当资源所有者是一个人时，它就是用户。 | 用户         |
| 资源服务器 | resource server      | 承载受保护资源的服务器，能够使用访问令牌接受和响应受保护资源请求。 | 后端资源数据 |
| 客户端     | client               | 代表资源所有者及其授权发出受保护资源请求的应用程序。``客户端'' 并不意味着任何特定的实现特征(例如，应用程序是否在服务器、桌面或其他设备上执行)。 | 前端应用     |
| 授权服务器 | authorization server | 在成功认证资源所有者并获得授权后，**服务器向客户端发出访问令牌。** | 后端授权     |

流程：

     +--------+                               +-----------------+
     |        |--（A）------- 授权请求 -------->|                 |
     |        |                               | 资源所有者（用户） |
     |        |<-（B）------- 授权许可 ---------|                 |
     |        |                               +-----------------+
     |        |
     |        |                               +-----------------+
     |        |--（C）------- 授权许可 -------->|                 |
     | 客户端  |                               |  授权服务器（1    |
     |        |<-（D）----- Access Token ----）|                 |
     |        |                               +-----------------+
     |        |
     |        |                               +-----------------+
     |        |（-（E）---- Access Token ----->|                 |
     |        |                               |   资源服务器（2   |
     |        |<-（F）---- 获取受保护的资源 -----|                 |
     +--------+                               +-----------------+
**四种模式**

- 授权码模式（authorization code）
- 密码模式（resource owner password credentials）
- 简化模式（implicit）
- 客户端模式（client credentials）

##### 5、zookeeper和尤里卡Eureka当做注册中心的区别

答：因为分布式系统不可能同时满足CAP一致性（C）、可用性(A)、容错性(P)。但是在分布式系统里面，容错性是必须有，因此，zk选择了强一致性，保证了CP，而尤里卡选择了可用性，保证AP

补充：

zk有主节点和从节点，当主节点挂掉之后，其余节点会重新选举主节点，但是在选举的过程中，zk基本是不可用的。

尤里卡Eureka优先保证的是可用性，每个节点都是平等的，如果有一个节点挂掉，整个注册中心也是可用的，只不过查询到注册信息可能不是最新的，不保证数据的强一致性。

服务注册：将服务所在主机、端口、版本号、通信协议等信息登记到注册中心上；

服务发现：服务消费者向注册中心请求已经登记的服务列表，然后得到某个服务 的主机、端口、版本号、通信协议等信息，从而实现对具体服务的调用；

##### 6、eureka的保护机制是什么

答：是注册中心如果在短时间内因为网络问题没有收到服务心跳，它会进入保护模式，保护注册信息，不进行删除，故障恢复的时候，会退出保护模式。

##### RPC协议介绍一下？

答：远程过程调用协议。就是像调本地方法一样去调用远程服务

##### QPS、TPS了解吗

答：

* QPS：query每秒可以处理的请求数量
* TPS:transaction每秒可以处理的事务请求数量（更重要，代表着处理请求的能力）
* 吞吐量：单位时间能够处理的请求数量
* 并发量：同一时刻有多少请求可以访问服务器
* PV：当前页面被访问的次数
* UV:当前页面被多少用户访问，以用户的访问
* 日活：一天活跃用户

##### 令牌桶限流

1）所有的请求在处理之前都需要拿到一个可用的令牌才会被处理； 2）根据限流大小，设置按照一定的速率往桶里添加令牌； 3）桶设置最大的放置令牌限制，当桶满时、新添加的令牌就被丢弃或者拒绝； 4）请求达到后首先要获取令牌桶中的令牌，拿着令牌才可以进行其他的业务逻辑，处理完业务逻辑之后，将令牌直接删除； 5）令牌桶有最低限额，当桶中的令牌达到最低限额的时候，请求处理完之后将不会删除令牌，以此保证足够的限流

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1627436941302-1627436941287.png)

### 七、设计模式

##### 1、工厂模式介绍一下

答：工厂模式的话它有**3**种，简单工厂、工厂方法、抽象工厂（最后一个）。

（1）简单工厂：**它是有一个工厂类和抽象产品类，由工厂类去生产具体的实例产品。**

（2）工厂方法：**它是工厂方法的基础上，又抽象了一个总工厂（接口返回抽象的产品）**，因为在简单工厂的模式下，如果需要添加新的实例，那么就需要修改工厂类的代码了。而在工厂方法的模式下，如何我们新增一个产品，只需要新增一个子工厂，子工厂去生产具体的实例

（3）抽象工厂：**它是工厂方法的上基础上，又增加了一个抽象产品，**因为在抽象方法模式下，所有生产的产品都是同一个父类下的，也就是只能创建通一类的实例，抽象方法的话能够创建不同的产品类

补充：spring里面的bean就使用到了工厂模式，通过BeanFactory 和ApplicationContext 去创建bean

##### 2、单例模式介绍一下

答：单例模式的话有2种，有懒汉式和饿汉式。懒汉式也就是加载类的时候先不去实例化对象，等到需要的时候才去加载，**它在多线程下，可能导致多次实例化，是线程不安全**的；饿汉式也就是加载类的时候就直接去实例化对象，是线程安全的，同时可以用和voltile关键字双重加锁保证对象只实例化一次，对实例化以及Class对象进行加锁。

补充：spring的bean模式就是单例

##### 3、代理模式介绍一下

答：代理模式的话通过代理，来控制对对象的访问，它主要有2个角色，一个目标对象，也就是真实对象，它关注的是业务逻辑的实际实现。另外一个就是代理对象，它就是目标对象的代理，我们通过访问代理对象来达到我们的目的，而且代理对象也可以对目标对象进行扩展，添加它自己的逻辑。模式主要的有2种，一种的静态代理，也就是自己创建代理类，一种动态代理，由程序帮助我们创建代理类。

补充：spring中AOP切面编程就是使用了动态代理。

静态代理实现：代理类去集成目标对象，然后去重写方法，或者代理类和目标类实现同一个接口。

动态代理：动态代理就是交给程序去自动生成代理类（JVM根据反射等机制动态的生成）

2种，JDK动态代理和CGlib动态代理

区别：

 1.JDK动态代理是利用反射机制生成一个实现代理接口的匿名类，在调用具体方法前调用InvokeHandler来处理。

 2.CGLIB动态代理是利用asm开源包，对代理对象类的class文件加载进来，通过修改其字节码生成子类来处理。

##### 4、观察者模式

答：观察者模式的话类似发布-订阅模式（通知模式）。它的话是一种一对多的关系，当发布对象的状态发生改变，那么所有依赖它的订阅对象就会得到通知，并且更新自身的状态。

它主要有2个主体，观察者对象和被观察者对象，它的有优点是观察者和被观察者之间是动态联动的

### 八、消息队列

##### **1、为什么用kafka队列**

答：（1）kafka的吞吐量高、延迟低（2）数据被持久化到磁盘，能够保证消息不丢失（3）有副本集群机制，容错性好。（4）支持点对点模式和发布订阅模式

补充：

- **点对点模型**：也叫消息队列模型。如果拿上面那个“民间版”的定义来说，那么系统 A发送的消息只能被系统 B 接收，其他任何系统都不能读取 A 发送的消息。日常生活的例子比如电话客服就属于这种模型：同一个客户呼入电话只能被一位客服人员处理，第二个客服人员不能为该客户服务。**消息生产者向消息队列中发送了一个消息之后，只能被一个消费者消费一次。**
- **发布 / 订阅模型**：与上面不同的是，它有一个主题（Topic）的概念，你可以理解成逻辑语义相近的消息容器。该模型也有发送方和接收方，只不过提法不同。发送方也称为发布者（Publisher），接收方称为订阅者（Subscriber）。和点对点模型不同的是，这个模型可能存在多个发布者向相同的主题发送消息，而订阅者也可能存在多个，它们都能接收到相同主题的消息。生活中的报纸订阅就是一种典型的发布 / 订阅模型。**消息生产者向消息队列中发送了一个消息之后，只能被一个消费者消费一次。**

发布与订阅模式和观察者模式有以下不同： 

* 观察者模式中，观察者和主题都知道对方的存在；而在发布与订阅模式中，生产者与消费者不知道对方的存在它们之间通过频道进行通信。 

* 观察者模式是同步的，当事件触发时，主题会调用观察者的方法，然后等待方法返回；而发布与订阅模式是异步的，生产者向频道发送一个消息之后，就不需要关心消费者何时去订阅这个消息，可以立即返回。

##### 2、项目中哪里用到了消息队列

答：

##### 3、消息丢失的问题怎么解决？

答：用确认机制+补偿策略。消息丢失可能几种情况，一种是生产者发送消息到MQ的时候出错了；或者MQ本身出了问题，宕机了；还有可能是消费者消费出现了问题。

如果是发送环节：可以使用confirm（确认）回调确认，也就是MQ收到消息之后回一个确认消息（rocketmq有一个SendStatus来确认），失败的话重新发送；如果是MQ的话，它是有持久化功能；如果是消费的问题，那么可以使用ACK进行手动确认。

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1623917185810-1623917185780.png)

```java
public boolean sendMsg(String topicName, String key, String msg, String tag) {
        Message message = new Message(topicName, tag, key, msg.getBytes());
        boolean flag;
        if (producer == null) {
            log.warn("生产者对象还未创建, 请先创建对象");
            flag = false;
        } else {
            try {
                SendResult result = producer.send(message);
                if (result != null && result.getSendStatus() == SendStatus.SEND_OK) {
                    log.info("入队成功, 主题名: {}, tag名: {}", topicName, tag);
                    flag = true;
                } else {
                    log.info("入队失败, 主题名: {}, tag名: {}, msg: {}", topicName, tag, msg);
                    log.info("尝试重新入队, 主题名: {}", topicName);
                    Thread.sleep(2000);
                    result = producer.send(message);
                    if (result != null && result.getSendStatus() == SendStatus.SEND_OK) {
                        log.info("重新入队成功, 主题名: {}, tag名: {}", topicName, tag);
                        flag = true;
                    } else {
                        log.warn("重新入队失败, 主题名: {}, tag名: {}, msg: {}", topicName, tag , msg);
                        flag = false;
                    }
                }
            } catch (Exception e) {
                log.error("入队失败, 主题名: {} ,tag名：{}, value: {}", topicName, tag, msg, e);
                flag = false;
            }
        }
        return flag;
    }
```

```java
 public void run() {
        //创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        //设置NameServer地址
        consumer.setNamesrvAddr(namesrvAddr);
        //设置实例名称
        consumer.setInstanceName("consumer:" + groupName);
        //每次最大消费数
        consumer.setConsumeMessageBatchMaxSize(consumerMaxSize);
        // 从消息队列尾部开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        //集群模式，广播模式重复消费
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //订阅Topic
        log.info("实例名称是：" + groupName);
        log.info("订阅主题是：" + topic);
        try {
            if (StringUtils.isNotBlank(tag)) {
                consumer.subscribe(topic, tag);
            } else {
                consumer.subscribe(topic, "*");
            }
        } catch (MQClientException e) {
            log.error("订阅主题" + topic + "异常", e);
        }
        //监听消息，消费异常延迟重新消费
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            try {
                if (CollectionUtils.isEmpty(msgs)) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                for (MessageExt msg : msgs) {
                    String value = new String(msg.getBody());
                    String tags = msg.getTags();
                    String id = storeService.insertByTopic(topic, value, tags);
                    if (id != null) {
                        log.info("数据已入库, 主题名:{}", topic);
                    } else {
                        log.warn("数据入库失败，主题名:{}", topic);
                    }
                }
                //ack，只有等上面一系列逻辑都处理完后，到这步CONSUME_SUCCESS才会通知broker说消息消费完成，如果上面发生异常没有走到这步ack，
                // 则消息还是未消费状态
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Exception e) {
                log.error("队列消费失败, 队列名称={}", topic, e);
                //延迟重新消费
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        //启动消费者
        try {
            consumer.start();
        } catch (MQClientException e) {
            log.error("消费者启动异常", e);
        }
    }
```

##### 4、如何保证消息不重复消费

答：

* 对于kafka:它有offset偏移量

补充：

Consumer先pull 消息到本地，消费完成后，才向服务器返回ack。

通常消费消息的ack机制一般分为两种思路：

1、先提交后消费；

2、先消费，消费成功后再提交；

思路一可以解决重复消费的问题但是会丢失消息，

因此Rocketmq默认实现的是思路二，由各自consumer业务方保证幂等来解决重复消费问题。

幂等性：是指**多次操作，结果是一致的**。（多次操作数据库数据是一致的）

解决：

* 唯一索引；保证插入的数据只有一条
* 插入之前先查下数据
* 用悲观锁或者数据库加一个版本字段，做乐观锁
* 使用分布式锁

分布式锁：

- 因为是分布式情况，所以synchronized失效。
- 多线程同时访问同一个变量的时候，需要保持互斥，使用redis的setnx+过期时间来实现。

（1）用户通过浏览器发起请求，服务端会收集数据，并且生成订单号code作为唯一业务字段

（2）使用redis的setNX命令，将该订单code设置到redis中，同时设置超时时间

（3）判断是否设置成功，如果设置成功，说明是第一次请求，则进行数据操作

（4）如果设置失败，说明是重复请求，则直接返回成功

```shell
一、Redis 2.6版本之前
192.168.52.190:0>setnx lock 1
"1" 加锁成功
192.168.52.190:0>setnx lock 2
"0" 加锁失败

192.168.52.190:0>expire lock 10
"1" 加10s的过期时间

192.168.52.190:0>del lock
"1" 删锁成功
192.168.52.190:0>del lock
"0" 删锁失败
在的问题：2条命令可能只执行了第一条，造成死锁

二、2.6版本之后 2条命令合成一条，成为原子命令
192.168.52.190:0>set lock 1 ex 10 nx
"OK"
存在的问题：业务执行耗时比较长，导致锁过期；释放的是别人的锁
场景：
客户端 1 加锁成功，开始操作共享资源
客户端 1 操作共享资源的时间，「超过」了锁的过期时间，锁被「自动释放」
客户端 2 加锁成功，开始操作共享资源
客户端 1 操作共享资源完成，释放锁（但释放的是客户端 2 的锁）

三、将set的值设置成唯一的 比如线程ID或者随机UUID
192.168.52.190:0>SET lock uuid EX 20 NX
"OK" 锁的VALUE是唯一的
之后释放锁的时候，再去判断这把锁是不是自己的。
if redis.get("lock") == $uuid:
    redis.del("lock")
存在的问题：get del是2条命令，不是原子操作

四、使用Lua脚本，将2条命令写成一条
Redis 处理每一个请求是「单线程」执行的，在执行一个 Lua 脚本时，其它请求必须等待，直到这个 Lua 脚本处理完成，这样一来，GET + DEL 之间就不会插入其它命令了
// 判断锁是自己的，才释放
if redis.call("GET",KEYS[1]) == ARGV[1]
then
    return redis.call("DEL",KEYS[1])
else
    return 0
end
    
五、过期时间不容易设置
自动续期 使用Redisson开源库

六、以上解决的都是：锁在「单个」Redis 实例中可能产生的问题
场景：
客户端 1 在主库上执行 SET 命令，加锁成功
此时，主库异常宕机，SET 命令还未同步到从库上（主从复制是异步的）
从库被哨兵提升为新主库，这个锁在新的主库上，丢失了！

解决方案：redlock(红锁)
Redlock 的方案基于 2 个前提：
不再需要部署从库和哨兵实例，只部署主库
但主库要部署多个，官方推荐至少 5 个实例
```

##### 5、push（推送）和pull（拉取）的区别？

答：kafka中使用的是pull模式

pull模式：消费者主动拉取

* 可以根据消费者的消费能力有序拉取，能够控制速度
* 可以批量拉取，也可以单个去拉取

缺点：如果Kafka里面没有数据，消费者去拉取的话会造成空数据，造成资源消耗

解决：可以设置当消费者拉取数量为空的时候或者没有达到一定数量的时候，进行阻塞。

Push模式：

有服务端向客户端主动去推送消息，有消息就推送，没消息就不推送，这样不会造成消费者循环等待。

缺点：忽略了消费端的消费能力，消费端可能来不及处理消息，造成超时阻塞这些问题。

##### 6、kafka中zk的作用

答：kafka会将broker注册信息存到zk中；也会将分区partition和topic的对应关系存到zk;生产者、消费者的负载均衡

##### 7、kafka的读写性能高的原因是什么？

答：kafka使用的顺序读写磁盘，它的消息都是append操作，partition是有序的，节省了磁盘的寻道时间，顺序访问的数据比较快。还有kafka用到操作系统的零拷贝，直接将内存缓存区的数据发送到网卡。

补充：

kafka不基于内存，而是基于磁盘存储，因此它堆积消息的能力更强。（消息直接往文件里面写）

##### 8、集群模式和广播模式

答：集群模式：是一条消息只会被消费组中的一个消费者消费；广播模式：一条消息会被消费组里的所有消费者都消费一次

##### 线上消息堆积如何解决

### 九、基础知识

https://blog.csdn.net/weixin_43495390/article/details/86533482

github：

https://snailclimb.gitee.io/javaguide-interview/#/./docs/j-1java

https://github.com/Snailclimb/JavaGuide

https://github.com/Snailclimb/JavaGuide-Interview

https://github.com/CL0610/Interview-Notebook   

http://hollischuang.gitee.io/tobetopjavaer/#/basics/java-basic/ut-with-jmockit

https://redspider.gitbook.io/concurrent/

##### 1、String、StringBuilder、StringBuffer的区别   

String在拼接时，会开辟一个新的堆内存空间，在频繁更改字符串的值时，比较浪费内存空间，而StringBuilder和StringBuffer它们类的对象能够多次修改而不产生新的内存空间，StringBuffer和StringBuilder相比，StringBuffer是线程安全的（使用synchronized）。  

##### 2、synchronized锁  

使用锁是为了线程安全，可以保证在同一时刻，只有一个线程可以执行加锁的方法或者代码块。  
![image-20201008110954481](D:\软件\Markdown\typora-user-images\image-20201008110954481.png)

##### 3、==和equals的区别  

== 对于基本类型，比较的是值相等，对于引用类型，比较的是2个对象是否指向的是同一个引用，即内存地址是否相同;
equals方法是Object类的一个方法，它里面是使用==进行比较的，也就是equals默认比较的是引用，只是很多类重写了equals方法，比如String、Integer等把它变成了值比较  

##### 4、final 在 java 中有什么作用？  

final是最终的意思

- final 修饰的变量是常量，常量在初始的时候就必须给定一个值，**而且这个值之后也不能再被修改。** 
- final 也可以用来修饰类，**修饰完成后，那么这个类就不能被继承了。**
- final 还能够修饰方法，**修饰完成后，那么这个方法就不能被子类重写。**  

##### 5、普通类、抽象类、接口的区别   

1. 抽象类是被子类extends实现;接口是被implements实现  

* 抽象类不能直接实例化，普通类可以
* 抽象类中可以有抽象方法，普通类不可以
* 抽象类中不能被final修饰，普通类可以
* 抽象类里可以有成员变量和普通方法，接口只能定义常量   
* 抽象类可以做方法声明和实现，接口里只能做方法的申明，不能有方法体(JDK8中可以使用default修饰定义)
* 接口的成员（字段 + 方法）默认都是 public 的，并且不允许定义为 private 或者 protected

**共同点**   
抽象类和接口都能有抽象方法（ 例：public abstract void run();）  

##### 6、访问权限  

访问权限大小依次是：private<default<protectd<public； 
private只能是当前类具有权限，default在同一个包下具有访问权，
protected子类也具有访问权限，public在任何地方都具有访问权限。

| 访问权限  | 本类 | 同包 | 子类 | 不同包非子类 |
| --------- | ---- | ---- | ---- | ------------ |
| public    | 是   | 是   | 是   | 是           |
| protected | 是   | 是   | 是   | 否           |
| default   | 是   | 是   | 否   | 否           |
| private   | 是   | 否   | 否   | 否           |

##### 7、IO流

* 功能分：输入流（input）、输出流（output）
* 类型分：字节流、字符流，**除非是处理文本，使用字符流，其余都用字节流。**

<img src="C:\Users\jbz\AppData\Roaming\Typora\typora-user-images\image-20191124141005853.png" alt="image-20191124141005853" style="zoom: 67%;" />   

##### 8、序列化   

序列化过程是将“一个对象编码成一个字节流”，相反的处理过程被称之为“反序列化过程”  

##### 9、异常   

Throwable是所有异常类的父类，异常分为2种：**Error**和**Exception**,Error表示JVM无法处理的错误，Exception表示程序可以捕获并处理的异常。Exception分为2种：

* 运行时异常：编译器不会检查这种异常，编译是可以通过的，比如空指针异常、数组越界异常
* 非运行时异常：编译器在编译阶段就不通过，比如IO异常，必须要进行处理，可以使用try  catch 捕获或者throws抛出



![image-20201010143344052](D:\软件\Markdown\typora-user-images\image-20201010143344052.png)

##### 10、关键字 

###### **final**

* 修饰基本类型，数值就不能修改了
* 修饰方法，这个方法不能被子类重写
* 修饰类，不能被继承

###### **static**

* 修饰变量，那么这个变量就是静态变量：也就是类变量，也就是这个变量是属于类的，而不是实例的，类的所有实例都共享这一个变量，可以通过类名.的方式来访问。静态变量在内存中只存在一份。实例变量：每创建一个实例就会产生一个实例变量，它与该实例同生共死。

* 修饰方法，那么这个方法就是静态方法：静态方法在类加载时就会存在，不依赖任何实例，因此，在静态方法的方法体中，只能访问到所属类的静态变量和静态方法,方法中不能有this和super关键字，因为这2个关键字与具体对象关联。

* 修饰代码块，那么这个代码块就是静态语句块：只在类初始化的时候加载一次

* 静态内部类，不能访问外部类的非静态变量和方法

  ```java
  static class StaticInnerClass {
      }
  ```

* 初始化顺序：静态变量和静态代码块优先于实例变量和普通语句块，静态变量和静态语句块的初始化顺序取决于它们在代码中的顺序。

  ```java
  public static String staticField = "静态变量";
  static {
      System.out.println("静态语句块");
  }
  public String field = "实例变量";
  {
      System.out.println("普通语句块");
  }
  //最后才是构造函数的初始化
  public InitialOrderTest() {
      System.out.println("构造函数");
  }
  
  ```

  存在继承的情况下，初始化顺序为：

  - 父类（静态变量、静态语句块）
  - 子类（静态变量、静态语句块）
  - 父类（成员变量、普通语句块）
  - 父类（构造函数）
  - 子类（成员变量、普通语句块）
  - 子类（构造函数

###### **super**

* 访问父类的构造函数：可以使用 super() 函数访问父类的构造函数，从而委托父类完成一些初始化的工作。应该注意到，子类一定会调用父类的构造函数来完成初始化工作，一般是调用父类的默认构造函数，如果子类需要调用父类其它构造函数，那么就可以使用 super() 函数。
* 访问父类的成员：如果子类重写了父类的某个方法，可以通过使用 super 关键字来引用父类的方法实现。

##### 11、hashCode()

hashCode() 返回哈希值，而 equals() 是用来判断两个对象是否等价。等价的两个对象散列值一定相同，但是散列值相同的两个对象不一定等价，这是因为计算哈希值具有随机性，两个值不同的对象可能计算出相同的哈希值。在覆盖 equals() 方法时应当总是覆盖 hashCode() 方法，保证等价的两个对象哈希值也相等。

##### 12、重写（override）和重载

* 重写：子类重写父类的方法
  - 子类的**访问权限**必须大于等于父类方法；
  - 子类的返回类型必须是父类方法返回类型或为其子类型。
  - 子类方法抛出的异常类型必须是父类抛出异常类型或为其子类型

* 重载：是在一个类里面，同一个方法它们的参数类型、个数或者顺序不同，**只有返回值不同，其它都相同，不算重载，编译也会不通过**

##### 13、反射

**什么是反射**

在程序运行过程中，对于任何一个类，都能够获取它的属性及方法，这种动态获取就就叫做反射。

**如何使用**

首先获取Class对象：通过Class.forName()全类名获取；使用newInstance获取对象，使用getMethod获取方法，使用invoke去调用方法

##### 14、面向对象

##### 15、JVM和线程池   

##### 15、swtich

JDJ1.7支持判断类型为String，不支持Long类型

##### 16、通配符 T、E、K、V、？

这些通配符一般出现在接口文档中

* T：代表一个具体的Java类型
* E：代表element
* K、V：代表键值对，key和value
* ？：代表不确定的Java类型

**？无界通配符**

List<? extends Animal > animals

不管List放什么类型的元素，只要它是Animal的子类就可以

**上界通配符 < ? extends E>**

上界：用 extends 关键字声明，表示参数化的类型可能是所指定的类型，或者是此类型的子类。

**下界通配符 < ? super E>**

下界: 用 super 进行声明，表示参数化的类型可能是所指定的类型，或者是此类型的父类型，直至 Object

**？和 T 的区别**

？和 T 都表示不确定的类型，区别在于我们可以对 T 进行操作，但是对 ？不行，比如如下这种 ：

```java
// 可以
T t = operate();

// 不可以
？car = operate();
```

##### 16、类加载过程  

类的实例化顺序：

父类的静态变量

父类的静态代码块（里面可能有静态变量）

子类的静态变量

子类的静态代码块

父类的成员变量和构造函数

子类的成员变量和构造函数

首先是Java文件通过编译成为class二进制文件，通过类加载器将class文件加载到Java内存当中，生成对应的class对象的成功。类加载分为3个阶段：加载——连接——初始化，加载就是通过类加载器将class文件加载到Java内存的方法区，连接的化又分为验证——准备——解析 三个小阶段。

* 加载：通过类加载器将二进制文件加载到内存当中。主要有3个步骤
  * 通过全类名来获取二进制字节流
  * 将字节流转换储存到Java内存中的元空间MetaSpace
  * 在内存当中生成一个对应的java.lang.class对象，作为这个类的访问入口
* 连接：验证是连接的第一步，主要验证class二进制文件、字节码、元数据、符号引用这些是否规范；然后是准备阶段，准备阶段是为类变量分配内存并设置初始值；最后是解析，解析是将常量池内的符号引号替换成直接引用的过程。符号引用是class文件中的字符串，是一组符号，直接引用相当于一个指向目标的一个地址，字面量相当于值，`int i = 1;把整数1赋值给int型变量i，整数1就是Java字面量`
* 初始化：初始化是类加载的最后一个阶段，执行Java代码，完成对对象的初始化。

① 类加载器

如果 **JVM** 想要执行这个 **.class** 文件，我们需要将其装进一个 **类加载器** 中，它就像一个搬运工一样，会把所有的 **.class** 文件全部搬进JVM里面

② 方法区

**方法区** 是用于存放类似于元数据信息方面的数据的，比如类信息，常量，静态变量，编译后代码···等

类加载器将 .class 文件搬过来就是先丢到这一块上

③ 堆

**堆** 主要放了一些存储的数据，比如对象实例，数组···等，它和方法区都同属于 **线程共享区域** 。也就是说它们都是 **线程不安全** 的

④ 栈

**栈** 这是我们的代码运行空间。我们编写的每一个方法都会放到 **栈** 里面运行。

我们会听说过 本地方法栈 或者 本地方法接口 这两个名词，不过我们基本不会涉及这两块的内容，它俩底层是使用C来进行工作的，和Java没有太大的关系。**栈管运行，堆管存储**

⑤ 程序计数器

主要就是完成一个加载工作，类似于一个指针一样的，指向下一行我们需要执行的代码。和栈一样，都是 **线程独享** 的，就是说每一个线程都会有自己对应的一块区域而不会存在并发和多线程的问题。

##### 性能指标  

在设计软件架构时需要关注的几个常见指标：**响应时间、延迟时间、吞吐量、并发用户数和资源利用率**。

- 系统响应时间 响应时间是指**系统对用户请求做出响应的时间**，不同的功能的链路长短不同，并且同一功能在不同数据量等这些情况都会导致响应时间的不同。因此，在衡量系统响应时间时，通常会关注软件产品所有功能的**平均响应时间**以及**最大响应时间**。
- 延迟时间 在讨论系统响应时间时，更细粒度的划分可以划分为：

1. 1. 客户端在接受数据进行渲染的内容“呈现时间”；
   2. 服务端在接受用户请求发送至服务端以及服务端将数据返回到客户端这两个过程中涉及到的：**网络传输时间以及应用延迟时间**。应用延迟时间即是服务端在执行整个服务链路时所花费的时间，也是性能优化首要降低的就是这个时间。

- 吞吐量 吞吐量指的是**单位时间内能够处理请求的数量**，对于无并发的应用来说，吞吐量和请求响应时间成反比，服务延迟更长则系统吞吐量更低。
- 并发用户数 并发用户数指的是系统能够**同时承载正常使用系统功能的用户数**，相较于吞吐量，这个指标更为笼统但是对于非软件领域的人来说更容易理解。
- 资源利用率 资源利用率反映的是在一段时间内资源被占用的情况

##### 17、进程和线程的区别

进程是一个具体的执行程序，比如启动一个springboot项目，也就是启动了JVM的进程，这个我们在机器上都能看到它的进程ID,线程的话是程序的更小执行单位，一个进程可能是有由多个线程组成的。在Jvm里面，多个线程共享同一个进程的堆和方法区（元空间），但是每个线程有自己的程序计数器、虚拟机栈 和 本地方法栈。

他们两个本质的区别是是否单独占有内存地址空间及其它系统资源（比如I/O）。

另外一个重要区别是，**进程是操作系统进行资源分配的基本单位，而线程是操作系统进行调度的基本单位**，即CPU分配时间的单位 。

**如何判断是 CPU 密集任务还是 IO 密集任务？**

CPU 密集型简单理解就是利用 CPU 计算能力的任务比如你在内存中对大量数据进行排序。但凡涉及到网络读取，文件读取这类都是 IO 密集型，这类任务的特点是 CPU 计算耗费时间相比于等待 IO 操作完成的时间来说很少，大部分时间都花在了等待 IO 操作完成上。

##### 18、注解（Annotation）介绍一下

它是代码一种标记，用来简化代码，它可以在编译、类加载或者运行的时候被编译器读取。

那定义注解的话就需要使用元注解了，

① 使用target指定注解的作用域，类、字段、接口；定义在elementType 枚举类上

② 使用Retention，定义注解的生命周期，我们一般用的都是runtime级别的

项目上的使用：

还有一个缓存刷新的功能用到过，因为项目上用到缓存了，所以我们提供一个数据同步的功能，把Mysql的数据重新同步到redis，但是因为同步的数据类比较多，写在不同的方法里；而且我们希望知道缓存具体刷新了哪些内容；所以，我们就自定义了一个注解，定义了刷新方法的名称，刷新标识（是否成功），刷新缓存的描述这些字段；然后直接再缓存刷新方法上使用注解就可以了。同时我们使用了AOP切面，去做了日志记录，指定了注解类作为pointcut；把缓存刷新的过程日志记录下来，发给消息队列，然后消费端去做解析保存。

##### 19、泛型

是为了代码的一个通用性，只在创建对象或者调用方法的时候才确定具体的类型。

##### 20、动态代理

有2种，一个JDK动态代理，需要自己定义一个动态代理处理器，去实现`InvocationHandler`接口，然后重写invoke方法；JDK动态代理只能代理接口，因为JDK帮我们生存代理对象它继承了reflect包下`Proxy`类

还有一种CGlib，它是通过修改class文件来生成子类来实现的。

##### 21、CAS

CAS：Compare And Swap的缩写，**比较并替换。是数据更新的一种方式。

共享值V：主内存中的值

当前A：共享值的副本，或者叫预期值A

新值B：共享变量更新到的最新值

**更新共享变量：**

①从共享内存中读取V值到本地内存中，②然后更新为最新值B，③再把B写入到共享内存中

核心：在更新B的值到共享内存的时候，去比较A值和V值是不是一样，如果是一样的，那么直接更新就可以了，**如果不一样，就说明V值已经被其它线程更改了**，那么就需要重新计算

优点

- 可以保证变量操作的原子性；
- 并发量不是很高的情况下，使用CAS机制比使用锁机制效率更高；
- 在线程对共享资源占用时间较短的情况下，使用CAS机制效率也会较高。

缺点：

* 并发量的情况下，消耗更多的Cpu
* 会有ABA问题

ABA：

因为我们是使用当前值A==共享值V，这个来判断值有没有被更改；但是假设这段期间有更改一个线程去更新，将共享值从A更新B，再更新到A；那么这时候，CAS的判断条件就没办法观察到；这就是问题；解决的话需要加上一个版本号，去比较当前值、共享值和版本号。

例子：比如有一个变量a值是1，现在想把它数据加1，那么有个线程1去操作了，此时对于它，旧的预期值就是1，新的预期值是2，内存上的值是1，如果没有其它干涉的话，那么它就会提交成功了，但是如果在他提交之前，有个线程2把它的值改成了2，也就是内存值是2了，那么前一个线程就会提交失败。 那么它就会重新计算重试，也就是**自旋**

<img src="https://fastly.jsdelivr.net/gh/jbz9/picture@main/image/1653731201835java-%E9%94%81.drawio.png" style="zoom: 67%;" />

