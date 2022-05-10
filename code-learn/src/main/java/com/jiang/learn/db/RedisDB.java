/**
 * Project Name : learn
 * File Name    : Redis
 * Package Name : com.jiang.learn.db
 * Date         : 2022-05-09 15:26
 * Author       : jbz
 */
package com.jiang.learn.db;

import org.springframework.stereotype.Component;

/**
 * @ClassName : Redis
 * @author : jbz
 * @Date : 2022-05-09 15:26
 * @Description :   
 */
@Component
public class RedisDB implements DataBase {

    @Override
    public void add() {
        System.out.println("数据存入Redis");
    }

    @Override
    public void getOne() {
        System.out.println("数据查询Redis");
    }
}