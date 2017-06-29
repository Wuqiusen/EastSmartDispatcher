
package com.zxw.dispatch.utils;

/**
 * Created by Administrator on 2017/6/17 0017.
 */

public class TimeUtil {

    public static String[] shiftMilliSecond(Long ms) {
        String[] str = new String[10];
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;


        long hour = ms / hh;
        long minute = (ms - hour * hh) / mi;
        long second = (ms - hour * hh - minute * mi) / ss;


        str[0] = hour < 10 ? "0" + hour : "" + hour;//小时
        str[1] = minute < 10 ? "0" + minute : "" + minute;//分钟
        str[2] = second < 10 ? "0" + second : "" + second;//秒

        return str;
    }
}