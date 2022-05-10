/**
 * Project Name : learn
 * File Name    : Test
 * Package Name : com.jiang.learn.proxy
 * Date         : 2022-05-10 15:03
 * Author       : jbz
 */
package com.jiang.learn.proxy;

import jdk.nashorn.internal.runtime.regexp.JdkRegExp;

import java.lang.reflect.Proxy;

/**
 * @ClassName : Test
 * @author : jbz
 * @Date : 2022-05-10 15:03
 * @Description :   
 */
public class Test {


    public static void main(String[] args) {
        Real real = new RealImpl();
        // 1.得到目标对象的类加载器
        ClassLoader classLoader = real.getClass().getClassLoader();
        // 2.得到目标对象的实现接口
        Class<?>[] interfaces = real.getClass().getInterfaces();
        // 3.第三个参数需要一个实现invocationHandler接口的对象
        JDKProxy proxy = new JDKProxy(real);
       // System.getProperties().setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Real proxyInstance = (Real)Proxy.newProxyInstance(classLoader, interfaces,proxy);
        proxyInstance.add();
    }
}