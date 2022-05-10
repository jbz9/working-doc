/**
 * Project Name : learn
 * File Name    : JDK
 * Package Name : com.jiang.learn.proxy
 * Date         : 2022-05-10 14:58
 * Author       : jbz
 */
package com.jiang.learn.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author : jbz
 * @ClassName : JDK
 * @Date : 2022-05-10 14:58
 * @Description :  JDK 动态代理 处理器
 */
public class JDKProxy implements InvocationHandler {

    /**
     * 真实对象
     */
    private Object real;

    /**
     * 真实对象
     */
    public JDKProxy(Object real) {
        this.real = real;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理处理器执行逻辑");
        Object rs = method.invoke(real, args);
        return rs;
    }

}