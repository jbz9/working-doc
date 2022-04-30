/**
 * Project Name : learn
 * File Name    : Hello
 * Package Name : com.jiang.learn.spring
 * Date         : 2022-04-25 22:16
 * Author       : jbz
 */
package com.jiang.learn.spring;

import javafx.application.Application;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author : jbz
 * @ClassName : Hello
 * @Date : 2022-04-25 22:16
 * @Description :
 */
public class Hello {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        Object roleService = applicationContext.getBean("roleService");
    }
}