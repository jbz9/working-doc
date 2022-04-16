/**
 * Project Name : learn
 * File Name    : List
 * Package Name : com.jiang.learn.collection
 * Date         : 2022-04-16 10:53
 * Author       : jbz
 * Copyright (c) 2019, jiang.baozi@ustcinfo.com All Rights Reserved.
 */
package com.jiang.collection;

import java.util.ArrayList;

/**
 * @author : jbz
 * @ClassName : List
 * @Date : 2022-04-16 10:53
 * @Description :
 */
public class List {

    public static void add() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("3");
        list.remove("1");
        list.set(0, "2");
        String v = list.get(0);
        System.out.println(v);
    }

    public static void main(String[] args) {
        add();
    }


}