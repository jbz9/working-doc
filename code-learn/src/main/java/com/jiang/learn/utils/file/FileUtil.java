package com.jiang.learn.utils.file;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

public class FileUtil {

    private static final int BUFFER_SIZE = 2 * 1024;


    public static void unZip(File srcFile, String destDirPath) {
        // 开始解压
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(srcFile);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                System.out.println("解压" + entry.getName());
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    String dirPath = destDirPath + "/" + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDirPath + "/" + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[BUFFER_SIZE];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
                    fos.close();
                    is.close();
                }
            }
            long end = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        FileUtil fileUtil = new FileUtil();
        String src = "D:\\develop\\workSpace\\sca\\sca-binary-scan\\extract\\bin\\genymotion-3.2.1-linux_x64\\genymotion-3.2.1-linux_x64_bin";
        //src = "D:\\develop\\workSpace\\sca\\sca-binary-scan\\extract\\bin\\Install_TW6.1.5.8_Enterprise_Linux\\good.zip";
        src = "D:\\develop\\workSpace\\sca\\sca-binary-scan\\extract\\bin\\Install_TW6.1.5.8_Enterprise_Linux\\Install_TW6.1.5.8_Enterprise_Linux_bin";
        src = "D:\\develop\\workSpace\\sca\\sca-binary-scan\\extract\\bin\\jre-6u45-linux-x64\\jre-6u45-linux-x64_bin";
        File sourceFileTmp = new File(src);
        File targetDir = new File("C:\\Users\\jiangbaozi\\AppData\\Local\\Temp\\dctemp3b9dda60-f13d-4cb0-8222-f9addd0cb502");
        FileUtil.unZip(sourceFileTmp, targetDir.getAbsolutePath());
        //    FileUtil.decompressZip(sourceFileTmp.getAbsolutePath(), targetDir.getAbsolutePath());
        //fileUtil.repairZipFile(src);
    }
}
