/**
 * Project Name : learn
 * File Name    : DataBase
 * Package Name : com.jiang.learn.db
 * Date         : 2022-05-09 15:26
 * Author       : jbz
 */
package com.jiang.learn.db;

/**
 * @ClassName : DataBase
 * @author : jbz
 * @Date : 2022-05-09 15:26
 * @Description : 数据库接口 通过配置开关决定 将数据存到哪个DB
 */
public interface DataBase {

    void add();

    void getOne();
}