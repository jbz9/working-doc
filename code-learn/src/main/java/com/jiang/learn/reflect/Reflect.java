/**
 * Project Name : learn
 * File Name    : reflect
 * Package Name : com.jiang.learn.reflect
 * Date         : 2022-05-01 18:45
 * Author       : jbz
 */
package com.jiang.learn.reflect;

import com.jiang.learn.api.AbstractHello;
import com.jiang.learn.collection.Hello;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;

/**
 * @author : jbz
 * @ClassName : reflect
 * @Date : 2022-05-01 18:45
 * @Description :   类本身也是一个对象，任何一个类都是Class类 的对象 反射
 */
public class Reflect {

    /**
     * 通过包名+类名获取Class对象
     * 最常使用
     */
    @SneakyThrows
    public void one() {
        String classPath = "com.jiang.learn.collection.Hello";
        Class<?> helloClass = Class.forName(classPath);
        //实例化得到对象,调用无参构造
        AbstractHello hello = (AbstractHello) helloClass.newInstance();
        hello.hello("小明");
        //有参构造
        Class[] classes = new Class[]{String.class};
        Constructor<?> constructor = helloClass.getDeclaredConstructor(classes);
        Hello instance = (Hello) constructor.newInstance("小李");
        System.out.println(instance.getName());
    }

    /**
     * 通过已经实例化后的对象，去获取它的class,提前条件：有对象
     */
    @SneakyThrows
    public void two() {
        Hello hello = new Hello();
        Class<? extends Hello> helloClass = hello.getClass();
        helloClass.newInstance().hello("小明");
    }

    /**
     * 直接 类.class 获取
     */
    @SneakyThrows
    public void three() {
        Class<Hello> helloClass = Hello.class;
        helloClass.newInstance().hello("小明");

    }

    public static void main(String[] args) {
        Reflect reflect = new Reflect();
        reflect.one();
        reflect.two();
        reflect.three();
    }
}