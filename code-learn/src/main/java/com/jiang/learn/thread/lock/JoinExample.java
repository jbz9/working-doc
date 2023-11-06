/**
 * Project Name : working-doc
 * File Name    : JoinExample
 * Package Name : com.jiang.learn.thread.lock
 * Date         : 2023-11-06 21:53
 * Author       : jbz
 */
package com.jiang.learn.thread.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName : JoinExample
 * @author : jbz
 * @Date : 2023-11-06 21:53
 * @Description :   
 */
public class JoinExample {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new Worker("Worker 1"));
        Thread thread2 = new Thread(new Worker("Worker 2"));

        thread1.start();
        thread2.start();

        try {
            thread1.join(); // 等待thread1终止
            thread2.join(); // 等待thread2终止
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All workers have finished.");
    }

    static class Worker implements Runnable {
        private String name;

        public Worker(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(name + " is working...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + " has finished.");
        }

    }
}
