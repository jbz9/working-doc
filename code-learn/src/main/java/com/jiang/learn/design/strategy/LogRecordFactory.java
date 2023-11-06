package com.jiang.learn.design.strategy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

/**
 * 记录检测日志
 *
 * @author jiangbaozi
 * @createTime 2022/10/11 17:27
 * 策略工厂
 **/
@Component
public class LogRecordFactory implements ApplicationContextAware {


    private final static Set<LogRecordService> logRecordList = new HashSet<>();

   /* static {
        initByServiceLoader();
    }*/

    /**
     * 加载日志记录实例
     * 需要配置META-INF/services
     */
    private static void initByServiceLoader() {
        ServiceLoader<LogRecordService> serviceLoader = ServiceLoader.load(LogRecordService.class);
        final Iterator<LogRecordService> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            final LogRecordService a = iterator.next();
            logRecordList.add(a);
        }
    }


    /**
     * spring bean 自动注入
     */

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, LogRecordService> tempMap = applicationContext.getBeansOfType(LogRecordService.class);
        logRecordList.addAll(tempMap.values());
    }


    /**
     * 判断文件是否需要记录日志
     *
     * @param file 文件
     * @return
     */
    public static boolean ifRecord(File file) {
        if (file == null) {
            return false;
        }
        //聚合
        return logRecordList.stream().map((a) -> a.accept(file)).reduce(false, (accumulator, result) -> accumulator || result);
    }

}
