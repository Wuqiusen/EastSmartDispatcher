package com.zxw.dispatch.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.zxw.dispatch.Constants;
import com.zxw.dispatch.MyApplication;

import java.io.File;

/**
 * 作者：${MXQ} on 2017/2/10 18:07
 * 邮箱：1299242483@qq.com
 */
public class SetUpAPK {

    public void setUpAPK(Activity activity){
        File file = new File(Environment.getExternalStorageDirectory() + "/" +
                Constants.Path.SECONDPATH, Constants.Path.APKNAME);
        DebugLog.w("length" +file.getAbsolutePath());
        Intent intent =new Intent(Intent.ACTION_VIEW);

//判断是否是AndroidN以及更高的版本
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(MyApplication.mContext,"com.zxw.dispatch.fileprovider",file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        }
        activity.startActivityForResult(intent, 0);
        activity.finish();
    }


}
