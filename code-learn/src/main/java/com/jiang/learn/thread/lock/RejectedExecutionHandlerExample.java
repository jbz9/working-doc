/**
 * Project Name : working-doc
 * File Name    : RejectedExecutionHandlerExample
 * Package Name : com.jiang.learn.thread.lock
 * Date         : 2023-11-07 11:47
 * Author       : jbz
 */
package com.jiang.learn.thread.lock;

/**
 * @ClassName : RejectedExecutionHandlerExample
 * @author : jbz
 * @Date : 2023-11-07 11:47
 * @Description :   
 */
import cn.hutool.core.thread.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class RejectedExecutionHandlerExample {
    public static void main(String[] args) {
     //   testAbortPolicy();
        testCallerRunsPolicy2();
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

    /**
     * 如何任务过多，调用者线程执行不过来，将会同步阻塞执行
     */
    private static void testCallerRunsPolicy2(){
        // 创建一个线程池，核心线程数为1，最大线程数为1，队列容量为1，使用CallerRunsPolicy拒绝策略
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, java.util.concurrent.TimeUnit.MILLISECONDS, new java.util.concurrent.LinkedBlockingQueue<>(1));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 提交多个任务
        for (int i = 1; i <= 10; i++) {
            final int taskNumber = i;
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName()+":Task " + taskNumber + " is running in thread " + Thread.currentThread().getName());
                try {
                    // 模拟任务执行时间
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName()+": Task " + taskNumber + " completed.");
            });
        }
        // 关闭线程池
        executor.shutdown();
    }
}
