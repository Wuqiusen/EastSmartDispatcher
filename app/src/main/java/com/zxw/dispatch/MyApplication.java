package com.zxw.dispatch;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.facebook.stetho.Stetho;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.lang.reflect.Field;

/**
 * author：CangJie on 2016/9/28 14:45
 * email：cangjie2016@gmail.com
 */
public class MyApplication extends Application {

    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;
        Thread.currentThread().setUncaughtExceptionHandler(
                new MyUncaughtExceptionHandler());
        Stetho.initializeWithDefaults(this);
    }
    private class MyUncaughtExceptionHandler implements
            Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            // TODO Auto-generated method stub
            // 获取异常信息 （补救）
            // 把异常写到文件里 放到sd卡
            // 服务器上传
            try {
                // byte[] file String

                StringWriter sw = new StringWriter();
                PrintWriter err = new PrintWriter(sw);

                Field[] fields = Build.class.getFields();
                for (Field f : fields) {
                    sw.write(f.getName() + ":" + f.get(null) + "\n");// 静态属性
                    // 不需要对象
                }

                ex.printStackTrace(err);
                String errorLog = sw.toString();
                SpUtils.cacheErrorLog(MyApplication.this, errorLog, SpUtils.getCache(MyApplication.this, SpUtils.NAME));

                //保存到本地
                String filePath = Environment.getExternalStorageDirectory()+"/"+Constants.Path.ERRORPATH;
                String fileName = "log.txt";
                writeTxtToFile(errorLog, filePath, fileName);

                sw.close();
                err.close();
                PackageManager pm = getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage("com.zxw.dispatch");
                startActivity(intent);
//                 自杀 重生
                android.os.Process.killProcess(android.os.Process.myPid());

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

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
                DebugLog.d("Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            DebugLog.e("Error on write File:" + e);
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
            DebugLog.i(e + "");
        }

    }


}
