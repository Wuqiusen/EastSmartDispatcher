package com.zxw.dispatch.utils;

/**
 * author：CangJie on 2016/10/9 17:14
 * email：cangjie2016@gmail.com
 */
public class DisplayTimeUtil {
    public static String substring(String originString){
        try{
            return originString.substring(0,2) + ":" + originString.substring(2,4);
        }catch (Exception e){
            return "";
        }
    }
}
