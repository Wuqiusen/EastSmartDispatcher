package com.zxw.dispatch;

/**
 * author：CangJie on 2016/9/21 11:50
 * email：cangjie2016@gmail.com
 */
public class Constants {
    public static boolean DEBUG_LOG = true;

    // 新增:
    public static final class NET {
        public static final int SUCCESS = 500;
        public static final int FAILED = 505;
        public static final int LOGIN_OVER = 510;
    }

    public class Path{
        public static final String ERRORPATH = "DriverPhone/error";
        public static final String SECONDPATH = "DriverPhone/download";
        public static final String APKNAME = "DriverPhone.apk";
    }




    public static final int AUTO_TYPE = 1; // 自动发车
    public static final int MANUAL_TYPE = 2; // 手动发车
}