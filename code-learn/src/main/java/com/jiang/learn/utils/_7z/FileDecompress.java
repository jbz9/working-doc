package com.jiang.learn.utils._7z;


import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 解压工具
 *
 * @author jiangbaozi
 * @createTime 2022/9/13 10:22
 **/

public class FileDecompress {


    private static final Logger LOGGER = LoggerFactory.getLogger(FileDecompress.class);

    /**
     * 获取文件真实类型
     *
     * @param file 要获取类型的文件。
     * @return 文件类型枚举。
     */
    public static FileType getFileType(File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] byteData = new byte[4];
            if (-1 == inputStream.read(byteData)) {
                return FileType.UNKNOWN;
            }
            int headHex = 0;
            for (byte b : byteData) {
                headHex <<= 8;
                headHex |= b;
            }
            switch (headHex) {
                case 0x504B0304:
                    return FileType.ZIP;
                case -0x51:
                    return FileType._7Z;
                case 0x52617221:
                    return FileType.RAR;
                case 0x425a6839:
                    return FileType.BZ2;
                case -0x74f7f8:
                    return FileType.GZ;
                case 0x776f7264:
                    return FileType.TAR;
                default:
                    return FileType.UNKNOWN;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FileType.UNKNOWN;
    }


    /**
     * 方法说明：解压zip文件
     *
     * @param file
     * @param targetPath
     * @return void
     * @throws
     * @Author jiangbaozi
     * @Date 2022/9/13 11:12
     **/

    public static void decompressZIP(File file, String targetPath) {
        int BUFFER_SIZE = 2 * 1024;
        // 开始解压
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    String dirPath = targetPath + "/" + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(targetPath + "/" + entry.getName());
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
        } catch (Exception e) {
            LOGGER.warn("解压ZIP文件 {}失败：", file.getAbsolutePath(), e);
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

    /**
     * @param file       待解压文件
     * @param targetPath 解压后文件存放路径 - 支持压缩格式：7z, zip, tar, rar, lzma, iso, gzip, bzip2,
     *                   cpio, z, arj, lzh, cab, chm, nsis, deb, rpm, udf, wim
     */
    public static void extract(File file, String targetPath) {
        IInArchive inArchive = null;
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
            inArchive.extract(null, false, new ExtractCallback(inArchive, targetPath));
        } catch (Exception e) {
            LOGGER.warn("解压文件{} 异常", file.getAbsolutePath(), e);
        } finally {
            try {
                if (inArchive != null) {
                    inArchive.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
