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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * @author : jbz
 * @ClassName : List
 * @Date : 2022-04-16 10:53
 * @Description :
 */
public class List {

    public static void arraylistAdd() {
        ArrayList<String> list = new ArrayList<>(10);
        list.add("1");
        list.add("3");
        list.remove("1");
        list.set(0, "2");
        String v = list.get(0);
        String[] array2 = {"1", "2"};
        array2 = Arrays.copyOf(array2, 10);
        System.out.println(array2.length);
        System.out.println(v);
    }

    public void linken() {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("1");
        linkedList.add("2");
        linkedList.add("1");
        linkedList.add("2");
        linkedList.add(1,"2");
        linkedList.remove("1");
        String s = linkedList.get(0);
        System.out.println(s);
        Vector vector = new Vector();
        vector.add(1);
    }

    public static void main(String[] args) {
        arraylistAdd();
    }


}