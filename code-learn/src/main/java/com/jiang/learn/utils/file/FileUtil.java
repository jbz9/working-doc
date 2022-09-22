package com.jiang.learn.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;

@Slf4j
public class FileUtil {

    /**
     * 方法说明：加载resources/lib下的dll或so文件
     *
     * @param libName 文件名 例如：Fileinfo   linux下文件为：libFileinfo.so  windows下文件为：fileinfo.dll
     * @return void
     * @throws
     * @Author jiangbaozi
     * @Date 2022/9/21 18:19
     **/

    public static void loadLib(String libName) throws IOException {
        String systemType = System.getProperty("os.name");
        String libExtension = (systemType.toLowerCase().contains("win")) ? ".dll" : ".so";
        String libFullName = libName + libExtension;
        String nativeTempDir = System.getProperty("user.dir") + File.separator + "lib";
        if (!new File(nativeTempDir).exists()) {
            new File(nativeTempDir).mkdir();
        }
        File extractedLibFile = new File(nativeTempDir + File.separator + libFullName);
        if (!extractedLibFile.exists()) {
            //将dll从jar复制到当前目录
            InputStream ddlStream = FileUtils.class.getClassLoader().getResourceAsStream("lib" + File.separator + libFullName);
            try (FileOutputStream fos = new FileOutputStream(extractedLibFile.getAbsolutePath());) {
                byte[] buf = new byte[2048];
                int r;
                while (-1 != (r = ddlStream.read(buf))) {
                    fos.write(buf, 0, r);
                }
            }
        }
        System.load(extractedLibFile.getAbsolutePath());
    }


    /**
     * 方法说明：复制resources/binary下的dll或so文件 到临时目录
     *
     * @param
     * @return void
     * @throws
     * @Author jiangbaozi
     * @Date 2022/9/22 16:00
     **/


    public void loadAllLibs() {
        ClassLoader cl = FileUtil.class.getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources;
        String nativeTempDir = System.getProperty("java.io.tmpdir") + File.separator + "binary";
        File tmpFile = new File(nativeTempDir);
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        try {
            resources = resolver.getResources("binary/*.*");
            for (Resource r : resources) {
                File libFile = r.getFile();
                File tmpLib = new File(nativeTempDir + File.separator + libFile.getName());
                if (!tmpLib.exists()) {
                    //不存在复制一份
                    Files.copy(libFile.toPath(), tmpLib.toPath());
                }
            }
        } catch (IOException e) {
            log.warn("加载二进制插件dll/so文件失败", e);
        }

    }

    /**
     * 方法说明：重新设置 重新设置java.library.path目录，添加一个新的目录
     *
     * @param
     * @return void
     * @throws
     * @Author jiangbaozi
     * @Date 2022/9/22 16:05
     **/

    public void resetLibDir() {
        //重新设置java.library.path属性
        String soPath = File.separator + "opt" + File.separator + "dev";
        String libPath = System.getProperty("java.library.path");
        libPath = soPath + File.pathSeparator + libPath;
        try {
            final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch (Exception e) {
            log.warn("java.library.path 设置失败，error：{}", e.getStackTrace());
        }
        System.setProperty("java.library.path", libPath);
        log.info("java.library.path 路径：{}", System.getProperty("java.library.path"));
    }


}
