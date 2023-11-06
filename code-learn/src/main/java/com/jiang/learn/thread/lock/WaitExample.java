/**
 * Project Name : working-doc
 * File Name    : WaitExample
 * Package Name : com.jiang.learn.thread.lock
 * Date         : 2023-11-06 16:37
 * Author       : jbz
 */
package com.jiang.learn.thread.lock;

import java.util.concurrent.Callable;

/**
 * @ClassName : WaitExample
 * @author : jbz
 * @Date : 2023-11-06 16:37
 * @Description :   
 */
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
