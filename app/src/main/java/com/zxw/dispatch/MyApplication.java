package com.zxw.dispatch;

import android.app.Application;
import android.content.Context;

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
    }
}
