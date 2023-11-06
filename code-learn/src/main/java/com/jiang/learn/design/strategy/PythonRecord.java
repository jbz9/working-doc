package com.jiang.learn.design.strategy;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 业务实现类
 */
public class PythonRecord implements LogRecordService {

    /**
     * Python
     */
    public static final List<String> PYTHON_SIGN_FILE = Arrays.asList("requirements.txt");


    @Override
    public boolean accept(File file) {
        if (PYTHON_SIGN_FILE.contains(file.getName())) {
            return true;
        }
        return false;
    }

    @Override
    public String getType() {
        return null;
    }
}
