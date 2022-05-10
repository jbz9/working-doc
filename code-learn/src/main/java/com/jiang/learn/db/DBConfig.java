/**
 * Project Name : learn
 * File Name    : DBConfiguration
 * Package Name : com.jiang.learn.db
 * Date         : 2022-05-09 15:35
 * Author       : jbz
 */
package com.jiang.learn.db;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author : jbz
 * @ClassName : DBConfiguration
 * @Date : 2022-05-09 15:35
 * @Description :
 */
@Component
@ConfigurationProperties(prefix = "database")
@Data
public class DBConfig {

    private Redis redis = new Redis();

    private Mysql mysql = new Mysql();

    @Data
    public static class Redis {
        private boolean enable;
    }

    @Data
    public static class Mysql {
        private boolean enable;
    }

}