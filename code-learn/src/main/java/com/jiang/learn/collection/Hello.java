/**
 * Project Name : learn
 * File Name    : Hello
 * Package Name : com.jiang.collection
 * Date         : 2022-04-18 23:14
 * Author       : jbz
 * Copyright (c) 2019, jiang.baozi@ustcinfo.com All Rights Reserved.
 */
package com.jiang.learn.collection;

import com.jiang.learn.api.AbstractHello;

/**
 * @author : jbz
 * @ClassName : Hello
 * @Date : 2022-04-18 23:14
 * @Description :
 */
public class Hello implements AbstractHello {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }


    public Hello() {
    }

    public Hello(String name) {
        this.name = name;
    }

    @Override
    public String hello(String name) {
        String r = "001: 你好 " + name;
        System.out.println(r);
        return r;
    }
}