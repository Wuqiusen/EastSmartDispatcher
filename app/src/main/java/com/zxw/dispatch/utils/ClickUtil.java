package com.zxw.dispatch.utils;

import android.util.Log;

/**
 * Created by Administrator on 2015/12/7.
 */
public class ClickUtil {

    private static long lastClickTime = 0;
    private static long lastClickBtnTime = 0;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        Log.i("log",""+timeD);
        return timeD <= 100;
    }



    public static boolean isFastDoubleClickStopBtn() {
        long time = System.currentTimeMillis();
        if (time - lastClickBtnTime < 500) {
            return true;
        }
        lastClickBtnTime = time;
        return false;
    }


}
