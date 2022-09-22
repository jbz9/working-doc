package com.jiang.learn.utils.file;

import info.monitorenter.cpdetector.io.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 如果读写字符集不一致，那么会导致乱码文件
 * 场景1：
 * 文件字符集是Gbk;使用UTF-8读取，那么读取的内容就会出现乱码，重新以UTF-8写入文件，文件内容也会乱码
 * 第三方包判断字符集 http://cpdetector.sourceforge.net
 */
public class CharacterSet {

    /**
     * @param filePath 待检测文件
     * @return 字符集编码
     * @throws IOException
     */
    public static String getFileCharacterSet(String filePath) throws IOException {
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        detector.add(new ParsingDetector(false));
        detector.add(UnicodeDetector.getInstance());
        detector.add(JChardetFacade.getInstance());
        detector.add(ASCIIDetector.getInstance());
        File file = new File(filePath);
        Charset charset = detector.detectCodepage(file.toURI().toURL());
        System.out.println(charset.name());
        return charset.name();
    }


    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\jiangbaozi\\Desktop\\1\\pom.xml";
        getFileCharacterSet(path);
    }
}
