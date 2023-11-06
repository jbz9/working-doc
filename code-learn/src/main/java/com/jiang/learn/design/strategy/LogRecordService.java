package com.jiang.learn.design.strategy;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 业务接口
 */
@Component
public interface LogRecordService {

    /**
     * 业务接口
     * @param file
     * @return
     */
    boolean accept(File file);

    /**
     * 标记接口，判断使用什么策略
     * @return
     */
    String getType();

}
