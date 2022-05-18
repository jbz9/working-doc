/**
 * Project Name : learn
 * File Name    : Hello
 * Package Name : com.jiang.learn.spring
 * Date         : 2022-04-25 22:16
 * Author       : jbz
 */
package com.jiang.learn.spring;

import com.jiang.learn.spring.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;

/**
 * @author : jbz
 * @ClassName : Hello
 * @Date : 2022-04-25 22:16
 * @Description :
 */
public class Hello {


    @Resource
    private UserService userService;


    public static void main(String[] args) {
     /*   AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        Object roleService = applicationContext.getBean("roleService");*/


        ApplicationContext con = new ClassPathXmlApplicationContext("bean.xml");
        con.getBean("");
    }
}