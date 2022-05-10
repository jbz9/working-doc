/**
 * Project Name : learn
 * File Name    : Hello
 * Package Name : com.jiang.learn.spring.ioc
 * Date         : 2022-05-01 17:38
 * Author       : jbz
 */
package com.jiang.learn.spring.ioc;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author : jbz
 * @ClassName : Hello
 * @Date : 2022-05-01 17:38
 * @Description :
 */
public class Hello {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public static void main(String[] args) {
        //

    }
}