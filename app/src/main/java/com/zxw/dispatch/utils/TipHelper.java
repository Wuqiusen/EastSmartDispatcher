package com.zxw.dispatch.utils;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.zxw.dispatch.MyApplication;

/**
 * author：CangJie on 2016/9/28 16:05
 * email：cangjie2016@gmail.com
 */
public class TipHelper {
    public static void vibrate() {
        vibrate(MyApplication.mContext);
    }
    public static void vibrate(final Context context) {
        vibrate(context, 200);
    }
    public static void vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
    public static void vibrate(final Context context, long[] pattern,boolean isRepeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
}