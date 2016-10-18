package com.zxw.dispatch;


import com.zxw.dispatch.utils.Base64;
import com.zxw.dispatch.utils.DESPlus;
import com.zxw.dispatch.utils.SpUtils;

import junit.framework.Assert;

import org.junit.Test;

/**
 * author：CangJie on 2016/9/6 11:49
 * email：cangjie2016@gmail.com
 */
public class MainActivityTest{
    private static final String TAG = "MainActivityTest";
    @Test
    public void test_rxjava(){
        String code = SpUtils.getCache(MyApplication.mContext, SpUtils.CODE);
        String keyCode = SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE);


        Assert.assertEquals(code, keyCode);
    }

    @Test
    public void test_des() throws Exception {
        DESPlus des = new DESPlus();

        //50584a2c4b88d536
        //f7adeedbfb8e2114
//        Assert.assertEquals(Base64.encode("徐".getBytes("utf-8")), des.encrypt(Base64.encode("徐".getBytes("utf-8")));
//        System.out.println(des.encrypt(Base64.encode("徐".getBytes("utf-8"))));
        Assert.assertEquals("", des.encrypt(Base64.encode("徐".getBytes("utf-8"))));
    }
}