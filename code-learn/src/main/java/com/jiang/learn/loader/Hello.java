/**
 * Project Name : learn
 * File Name    : Hello
 * Package Name : com.jiang.loader
 * Date         : 2022-04-19 16:40
 * Author       : jbz
 */
package com.jiang.learn.loader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author : jbz
 * @ClassName : Hello
 * @Date : 2022-04-19 16:40
 * @Description :
 */
public class Hello {

    public static void main(String[] args) {
        Hello hello = new Hello();
        //app loader
        System.out.println(hello.getClass().getClassLoader());

        // bootstrap loader
        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (URL url : urls) {
            System.out.println(url);
        }

        //extension loader
        URL[] extensions = ((URLClassLoader) ClassLoader.getSystemClassLoader().getParent()).getURLs();
        for (URL url : extensions) {
            System.out.println(url);
        }
    }

}