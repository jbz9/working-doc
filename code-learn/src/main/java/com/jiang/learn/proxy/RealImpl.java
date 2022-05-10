/**
 * Project Name : learn
 * File Name    : Realmplate
 * Package Name : com.jiang.learn.proxy
 * Date         : 2022-05-10 16:43
 * Author       : jbz
 */
package com.jiang.learn.proxy;

/**
 * @author : jbz
 * @ClassName : Realmplate
 * @Date : 2022-05-10 16:43
 * @Description :   真实对象
 */
public class RealImpl implements Real {

    @Override
    public void add() {
        System.out.println("hello,真实对象方法执行");
    }
}