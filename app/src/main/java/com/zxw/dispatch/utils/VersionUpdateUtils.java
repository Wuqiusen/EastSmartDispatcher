package com.zxw.dispatch.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;


public class VersionUpdateUtils {
    public static boolean getSDCardMemory(Context context){
        File path2 = Environment.getDataDirectory();
        StatFs stat2 = new StatFs(path2.getPath());
        long blockSize2 = stat2.getBlockSize();
        long availableBlocks2 = stat2.getAvailableBlocks();
        long availSize2 = availableBlocks2 * blockSize2;
        availSize2 = availSize2/1024/1024;
        DebugLog.i(availSize2 +"");
        if((int)availSize2 >= 10){
            return true;
        }else{
            ToastHelper.showToast("您的内存空间不足10M，下载失败", context);
            return false;
        }
    }
}
