package com.zxw.data.utils;

import android.os.Environment;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author：CangJie on 2017/3/10 14:36
 * email：cangjie2016@gmail.com
 */
public class LogUtil {


    public static void loadRemoteError(String str){
        String fileName = "loadRemoteError.txt";
        log(fileName, str);
    }

    private static void log(String fileName, String str){
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd_");
        String day = format2.format(new Date());
        fileName = day + fileName;

        String filePath = Environment.getExternalStorageDirectory() + "/BdYy/";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = format.format(new Date());

        str = "\r\n" + time +"\r\n" + str +"\r\n";
        writeTxtToFile(str, filePath, fileName);
    }


    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String strcontent, String filePath, String fileName) {
        makeFilePath(filePath, fileName);
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
        }
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
        }

    }

}
