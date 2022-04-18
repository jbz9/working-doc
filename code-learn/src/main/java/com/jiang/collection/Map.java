/**
 * Project Name : working-doc
 * File Name    : Map
 * Package Name : com.jiang.learn.collection
 * Date         : 2022-04-13 22:38
 * Author       : jbz
 */
package com.jiang.collection;

import java.util.HashMap;

/**
 * @author : jbz
 * @ClassName : Map
 * @Date : 2022-04-13 22:38
 * @Description :Map 源码分析
 */
public class Map {

    public Map() {
    }

    /**
     * Map
     */
    public static void mapOper() {
        HashMap<String, String> map1 = new HashMap<String, String>(16);
        int hashCode = "admin".hashCode();
        System.out.println(hashCode);
        System.out.println(Math.floorMod(hashCode,2));
        System.out.println(hashCode % 2);
        System.out.println(Math.floorMod(10, -3));
        System.out.println(2 >>> 1);
        System.out.println(2 >>> 2);
        System.out.println(2 >>> 3);
        map1.put("a", "1");
    }

    public static void main(String[] args) {
        mapOper();
    }
}