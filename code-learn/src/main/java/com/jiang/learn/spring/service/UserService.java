/**
 * Project Name : learn
 * File Name    : User
 * Package Name : com.jiang.spring.service
 * Date         : 2022-04-25 21:54
 * Author       : jbz
 */
package com.jiang.learn.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : jbz
 * @ClassName : User
 * @Date : 2022-04-25 21:54
 * @Description :
 */
@Component
public class UserService {


    public void getUser() {
        System.out.println("小明");
    }

    //去bean 池去找 RoleService bean 先类型RoleService 后名称 roleService
    @Autowired
    public void getRole(RoleService roleService) {

        System.out.println("get role");
    }
}