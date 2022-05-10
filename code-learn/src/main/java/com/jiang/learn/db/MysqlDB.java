/**
 * Project Name : learn
 * File Name    : MysqlDB
 * Package Name : com.jiang.learn.db
 * Date         : 2022-05-09 15:27
 * Author       : jbz
 */
package com.jiang.learn.db;

import org.springframework.stereotype.Component;

/**
 * @ClassName : MysqlDB
 * @author : jbz
 * @Date : 2022-05-09 15:27
 * @Description :   
 */
@Component
public class MysqlDB implements DataBase {

    @Override
    public void add() {
        System.out.println("数据存入Mysql");
    }

    @Override
    public void getOne() {
        System.out.println("查询Mysql");
    }
}