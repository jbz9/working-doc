/**
 * Project Name : working-doc
 * File Name    : ThreadExample
 * Package Name : com.jiang.learn.thread.lock
 * Date         : 2023-11-06 17:31
 * Author       : jbz
 */
package com.jiang.learn.thread.lock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @ClassName : ThreadExample
 * @author : jbz
 * @Date : 2023-11-06 17:31
 * @Description :   
 */
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