/**
 * Project Name : learn
 * File Name    : UserController
 * Package Name : com.jiang.learn.controller
 * Date         : 2022-05-09 14:06
 * Author       : jbz
 */
package com.jiang.learn.controller;

import com.jiang.learn.db.DBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : jbz
 * @ClassName : UserController
 * @Date : 2022-05-09 14:06
 * @Description :
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private DBConfig dbConfig;


}