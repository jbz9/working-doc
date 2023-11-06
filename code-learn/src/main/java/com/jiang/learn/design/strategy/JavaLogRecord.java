package com.jiang.learn.design.strategy;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 记录JAVA特征文件的内容
 *
 * @author jiangbaozi
 * @createTime 2022/10/13 17:27
 * 业务实现类
 **/
@Component
public class JavaLogRecord implements LogRecordService {


    /**
     * Java
     */

    public static final List<String> JAVA_SIGN_FILE = Arrays.asList("pom.xml", "build.gradle");


    @Override
    public boolean accept(File file) {
        if (JAVA_SIGN_FILE.contains(file.getName())) {
            return true;
        }
        return false;
    }

    @Override
    public String getType() {
        return null;
    }
}
