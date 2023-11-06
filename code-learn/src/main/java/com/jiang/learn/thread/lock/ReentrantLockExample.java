/**
 * Project Name : working-doc
 * File Name    : ReentrantLockExample
 * Package Name : com.jiang.learn.thread
 * Date         : 2023-11-02 16:49
 * Author       : jbz
 */
package com.jiang.learn.thread.lock;

/**
 * @ClassName : ReentrantLockExample
 * @author : jbz
 * @Date : 2023-11-02 16:49
 * @Description :   悲观锁
 */

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    private static ReentrantLock lock = new ReentrantLock();
    AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        // 创建并启动两个线程
        /*Thread thread1 = new Thread(new Worker("Worker 1"));
        Thread thread2 = new Thread(new Worker("Worker 2"));
        thread1.start();
        thread2.start();*/


        Thread thread1 = new Thread(new Worker.WorkerTwo("Worker 1"));
        Thread thread2 = new Thread(new Worker.WorkerTwo("Worker 2"));

        thread1.start();
        thread2.start();

        // 让线程2等待一段时间后中断
        try {
            Thread.sleep(2*1000);
            thread2.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

        //响应中断、限时等待
        static class WorkerTwo implements Runnable {
            private String name;

            public WorkerTwo(String name) {
                this.name = name;
            }

            @Override
            public void run() {
                try {
                    if (lock.tryLock(2, TimeUnit.SECONDS)) {
                        try {
                            System.out.println(name + " has acquired the lock.");
                            // 模拟执行一些工作
                            Thread.sleep(5000);
                        } finally {
                            lock.unlock();
                            System.out.println(name + " has released the lock.");
                        }
                    } else {
                        System.out.println(name + " couldn't acquire the lock.");
                    }
                } catch (InterruptedException e) {
                    System.out.println(name + " was interrupted.");
                }
            }
        }
    }
}
