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