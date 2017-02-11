package com.zxw.dispatch.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.zxw.dispatch.Constants;

import java.io.File;

/**
 * 作者：${MXQ} on 2017/2/10 18:07
 * 邮箱：1299242483@qq.com
 */
public class SetUpAPK {

    public void setUpAPK(Activity activity){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" +
                Constants.Path.SECONDPATH, Constants.Path.APKNAME)), "application/vnd.android.package-archive");
        DebugLog.e("错误！！！！！");
        activity.startActivityForResult(intent, 0);
        activity.finish();
    }
}