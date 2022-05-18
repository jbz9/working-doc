## 多线程  

### 一. 线程概念

* 进程：**操作系统进行资源分配的最小单位**
* 线程：**CPU进行运算调度的最小单位，一个进程可以有很多个线程，这些线程共享这个进程的资源**
* 协程：

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

#### 线程5个状态

<img src="https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16503725179881650372517519.png" style="zoom:67%;" />

### 二. 同步和异步

同步和异步用于方法时，同步方法调用一旦开始，调用方必须等到方法返回后，才能进行下一步操作；异步方法，调用方调用方法后，会立即返回一个结果，调用方可以进行下一步操作。

```
关于异步目前比较经典以及常用的实现方式就是消息队列：在不使用消息队列服务器的时候，用户的请求数据直接写入数据库，在高并发的情况下数据库压力剧增，使得响应速度变慢。但是在使用消息队列之后，用户的请求数据发送给消息队列之后立即 返回，再由消息队列的消费者进程从消息队列中获取数据，异步写入数据库。由于消息队列服务器处理速度快于数据库（消息队列也比数据库有更好的伸缩性），因此响应速度得到大幅改善。
```

多个线程同时操作实例对象中的变量，会造成非线程安全。**非线程安全**问题存在于“实例变量”中，如果是方法内部的私有变量，则不存在**非线程安全**问题，所得结果也就是**线程安全**的了。

#### 线程阻塞

**方法**

- `obj.wait()`是把当前线程放到obj的wait set；
- `obj.notify()`是从obj的wait set里唤醒1个线程；
- `obj.notifyAll()`是唤醒所有在obj的wait set里的线程。

### 三. 实现多线程

#### 1. Thread

继承Thread 类，重写run()方法，调用start启动线程

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

#### 2. Runnable

实现Runnable接口

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

#### 3. Thread和Runnable区别

Thread是继承，Runnable是实现，如果线程类实现Runnable，它还是可以继承其它类的，而且实现Runnable，那么多个线程就可以共享一个对象了，适合多个相同线程来处理同一份资源的情况。

Runnable和Callable的区别？

Runnable重写的是run方法，Callable重写的是call方法，Callable执行完有返回值，Runnable没有

#### 4. 线程优先级

优先级是1-10，最低是1，最高是10，优先级越高，线程获取运行的机会就越多，默认是5。

### 四. Java锁

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

### 五. 线程池

#### 1. 线程池概念    

管理线程的池子，当有任务需要处理的时候，可以从线程池中取出线程来处理，处理完成后，线程也不会销毁，降低频繁创建创建、销毁线程的消耗，提高资源使用率和响应速度。

* **帮助我们管理线程资源：**线程实际也是一个对象，创建一个对象，需要经过类加载过程，销毁一个对象，需要走GC垃圾回收流程，都是需要资源开销的。 
* **提高响应速度**：执行任务时，可以直接去线程池中拿线程，而不需要重新去创建一条线程执行，提高了响应速度。
* **重复利用**：线程使用完毕，不进行销毁，而是放入线程池中，减少了线程创建和销毁的次数，达到重复利用的效果，节省资源。

#### 2. 线程池工作流程

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/1650373495002Java%E7%BA%BF%E7%A8%8B-%E7%BA%BF%E7%A8%8B%E6%B1%A0.drawio.png)



#### 3.线程池使用

创建线程池使用**ThreadPoolExecutor**类，有6个参数，分为corePoolSize 核心线程数、最大线程数、存活时间、时间单位、缓冲队列（已提交但是没有执行的任务放在这里）和拒绝策略。

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

##### 1.1 核心参数

7个，核心线程数、最大线程数、线程存活时间、时间单位、阻塞队列、拒绝策略

##### 1.2 拒绝策略

* CallRunsPolicy:
* AbortPolicy:
* DiscardPolicy:
* DiscardOldestPolicy:

##### 1.3 怎么使用线程池

使用 ThreadPoolExecutor 自定义线程

##### 1.4 阻塞队列

队列是空：取不到会被阻塞

队列是满：set不进去也会被阻塞

![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16526924655691652692464959.png)



![](https://cdn.jsdelivr.net/gh/jbz9/picture@main/image/16526924365681652692435745.png)

#### 1.4 线程池参数怎么配置

 需要具体看线程池执行的任务是CPU密集还是IO密集的

* CPU密集：一般线程数可以设为服务器的cpu核数+1，减少cpu上下文的切换
* IO密集：任务消耗时间主要在等待IO返回上，cpu的压力不大，那么核心线程可以多一些，可以设为cpu核数的2倍。